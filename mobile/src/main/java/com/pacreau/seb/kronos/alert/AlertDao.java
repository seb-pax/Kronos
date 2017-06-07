package com.pacreau.seb.kronos.alert;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */

public class AlertDao {

	public static final String KRONOS_ALERT_DAO = AlertDao.class.getName();
	private static final String ALERT_TABLE = "alerts";
	private static final String ALERT_FIELD_ID = "id";
	private static final String ALERT_FIELD_TOTALDURATION = "totalDuration";
	private static AlertDao instance;
	private DatabaseReference firebaseAlerts;
	private Query querySelectAll;
	private Query querySelectById;
	public static AlertDao getInstance() {
		if (instance == null) {
			instance = new AlertDao();
		}
		return instance;
	}

	private List<AlertListener> alertListeners = new ArrayList<AlertListener>();

	private AlertDao() {
		alertListeners = new ArrayList<AlertListener>();

		firebaseAlerts = FirebaseDatabase.getInstance().getReference(ALERT_TABLE);
		querySelectById = firebaseAlerts.child(ALERT_FIELD_ID);

		querySelectAll = firebaseAlerts.orderByChild(ALERT_FIELD_TOTALDURATION);
		querySelectAll.addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String s) {
				Alert oAlert = dataSnapshot.getValue(Alert.class) ;
				Log.d("onChildAdded" , ""+ oAlert);
				for ( AlertListener alertListener : alertListeners) {
					alertListener.onDataAdded(oAlert);
				}
			}

			@Override
			public void onChildRemoved(DataSnapshot dataSnapshot) {
				Alert oAlert = dataSnapshot.getValue(Alert.class) ;
				for ( AlertListener alertListener : alertListeners) {
					alertListener.onDataRemoved(oAlert);
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}

			@Override
			public void onChildChanged(DataSnapshot dataSnapshot, String s) {
				Alert oAlert = dataSnapshot.getValue(Alert.class) ;
				for ( AlertListener alertListener : alertListeners) {
					alertListener.onDataAdded(oAlert);
				}
			}

			@Override
			public void onChildMoved(DataSnapshot dataSnapshot, String s) {
				Alert oAlert = dataSnapshot.getValue(Alert.class) ;
			}
		});
	}

	public static String formatLongInTime(long p_millisUntilFinished) {
		return String.format(Locale.FRANCE, "%1$02d:%2$02d",
				//TimeUnit.MILLISECONDS.toHours(p_millisUntilFinished),
				TimeUnit.MILLISECONDS.toMinutes(p_millisUntilFinished) - TimeUnit.HOURS.toMinutes(
						TimeUnit.MILLISECONDS.toHours(p_millisUntilFinished)),
				TimeUnit.MILLISECONDS.toSeconds(p_millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
						TimeUnit.MILLISECONDS.toMinutes(p_millisUntilFinished)));
	}

	public Alert createEmptyAlert() {
		return new Alert(0, 1, "");
	}

	public void save(Alert p_oAlert) {
		String sKey = p_oAlert.getId();
		if (sKey == null) {
			sKey = firebaseAlerts.push().getKey();
		}
		p_oAlert.setId(sKey);
		firebaseAlerts.child(sKey).setValue(p_oAlert);
	}

	public void deleteAlert(String p_sId) {
		firebaseAlerts.child(p_sId).removeValue();
	}

	public void addAlertListener(AlertListener pAlertListener) {
		alertListeners.add(pAlertListener);
	}

	public void removeAlertListener(AlertListener pAlertListener) {
		alertListeners.remove(pAlertListener);
	}

	public void  getAsyncAlerts() {
		querySelectAll = firebaseAlerts.orderByChild(ALERT_FIELD_TOTALDURATION);
	}

	public void getAsyncAlert(String p_sId) {
		querySelectById.equalTo(p_sId).addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				for ( AlertListener alertListener : alertListeners) {
					alertListener.onDataAdded(dataSnapshot.getValue(Alert.class));
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
			}
		});
	}

	public interface AlertListener {

		public void onDataAdded(Alert p_oAlert);
		public void onDataRemoved(Alert p_oAlert);
	}
}
