package com.pacreau.seb.kronos.alert;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

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
	private static final String ALERT_FIELD_TOTALDURATION = "totalDuration";
	private static AlertDao instance;
	private DatabaseReference fireDatabase;
	private Query query;
	public static AlertDao getInstance() {
		if (instance == null) {
			instance = new AlertDao();
		}
		return instance;
	}


	/**
	 * An array of sample (dummy) items.
	 */
	final List<Alert> alerts = new ArrayList<Alert>();
	/**
	 * A map of sample (dummy) items, by ID.
	 */
	public static final Map<String, Alert> ITEM_MAP = new HashMap<String, Alert>();

	private AlertDao() {
		// Add some sample items.
		fireDatabase = FirebaseDatabase.getInstance().getReference();
		query = fireDatabase.child(ALERT_TABLE).orderByChild(ALERT_FIELD_TOTALDURATION);
		query.addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String s) {
				Alert oAlert = dataSnapshot.getValue(Alert.class) ;
				alerts.add(oAlert);
				Log.d("onChildAdded" , ""+ oAlert);
			}

			@Override
			public void onChildRemoved(DataSnapshot dataSnapshot) {

			}

			@Override
			public void onCancelled(DatabaseError databaseError) {

			}

			@Override
			public void onChildChanged(DataSnapshot dataSnapshot, String s) {

			}

			@Override
			public void onChildMoved(DataSnapshot dataSnapshot, String s) {

			}
		});
		save(new Alert(24 / 3 * 60, -1, null));
		save(new Alert(25 / 2 * 60, -1, null));
		save(new Alert(25 / 4 * 60, -1, null));
		save(new Alert(12 * 60, -1, null));
		save(new Alert(10 * 60, -1, null));

		save(new Alert(10, -1, null));
		save(new Alert(20, -1, null));
	}

	public Alert getItem(long longExtra) {
		return ITEM_MAP.get(Long.valueOf(longExtra));
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
		Alert oAlert = new Alert(0, -1, null);
		return oAlert;
	}

	public void save(Alert p_oAlert) {
		String sKey = p_oAlert.getId();
		if (sKey == null) {
			sKey = fireDatabase.child(ALERT_TABLE).push().getKey();
		}
		fireDatabase.child(ALERT_TABLE).child(sKey).setValue(p_oAlert);
	}

	public void saveL(List<Alert> p_oListAlert) {
		if (p_oListAlert != null) {
			for (Alert oAlert : p_oListAlert) {
				save(oAlert);
			}
		}
	}

	public List<Alert> getAlerts() {
		return alerts;
	}
}
