package com.pacreau.seb.kronos.fragment;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pacreau.seb.kronos.R;
import com.pacreau.seb.kronos.activity.AlertDetailActivity;
import com.pacreau.seb.kronos.activity.AlertListActivity;
import com.pacreau.seb.kronos.alert.Alert;
import com.pacreau.seb.kronos.alert.AlertDao;
import com.pacreau.seb.kronos.databinding.AlertDetailBinding;
import com.pacreau.seb.kronos.service.NotificationService;
import com.pacreau.seb.kronos.service.RingtoneService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * A fragment representing a single Alert detail screen.
 * This fragment is either contained in a {@link AlertListActivity}
 * in two-pane mode (on tablets) or a {@link AlertDetailActivity}
 * on handsets.
 */
public class AlertDetailFragment extends Fragment implements View.OnClickListener {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_DETAIL_ALERT_ID = "detail_id";
	public static final String TAG = "AlertDetailFragment";
	private static final long INTERMEDIAIRE_ALARM_DURATION = 3;
	private static final long SNACK_DISPLAY_DURATION = 5;

	private FloatingActionButton oPauseButton;
	private FloatingActionButton oStopButton;
	private FloatingActionButton oLauncherButton;

	private TextView oDisplayedDurationRestView;
	private long lDurationInMillisUntilFinished;
	private long lNextAlarmTimeInMillis;
	private long lDurationBetweenAlarm;


	private CountDownTimer oCountDownTimer;

	/**
	 * The dummy content this fragment is presenting.
	 */
	private Alert alert;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public AlertDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments().containsKey(ARG_DETAIL_ALERT_ID)) {
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
			alert = getArguments().getParcelable(ARG_DETAIL_ALERT_ID);
			/*CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
			if (appBarLayout != null) {
				appBarLayout.setTitle(String.valueOf(alert.getTotalDuration()));
			}*/
			RingtoneService.getInstance().prepare(this.getActivity());
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		AlertDetailBinding oAlertBinding = DataBindingUtil.inflate(inflater, R.layout.alert_detail, container, false);
		oAlertBinding.setAlert(alert);
		oLauncherButton = (FloatingActionButton) oAlertBinding.getRoot().findViewById(R.id.alert_detail_launch_button);
		oStopButton = (FloatingActionButton) oAlertBinding.getRoot().findViewById(R.id.alert_detail_stop_button);
		oPauseButton = (FloatingActionButton) oAlertBinding.getRoot().findViewById(R.id.alert_detail_pause_button);

		oLauncherButton.setOnClickListener(this);

		return oAlertBinding.getRoot();
	}

	@Override
	public void onStart() {
		super.onStart();

		this.displayDurationInView(alert.getTotalDuration() * TimeUnit.SECONDS.toMillis(1));
	}

	private AlertDetailActivity.AlertState getCurrentAlertState() {
		return ((AlertDetailActivity) this.getActivity()).getCurrentAlertState();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.alert_detail_pause_button:
				if (this.getCurrentAlertState() == AlertDetailActivity.AlertState.ACTIVATED) {
					setCurrentAlertState(AlertDetailActivity.AlertState.PAUSED);
					Snackbar.make(view, "Chrono en pause", Snackbar.LENGTH_LONG)
							.setAction("Action", null).show();
					oCountDownTimer.cancel();
				} else if (this.getCurrentAlertState() == AlertDetailActivity.AlertState.PAUSED) {
					setCurrentAlertState(AlertDetailActivity.AlertState.ACTIVATED);
					Snackbar.make(view, "Chrono relancé", Snackbar.LENGTH_LONG)
							.setAction("Action", null).show();
					launchCountDownTimer();
				}
				RingtoneService.getInstance().stopRingtone();

				break;
			case R.id.alert_detail_stop_button:
				setCurrentAlertState(AlertDetailActivity.AlertState.STOPPED);
				oCountDownTimer.cancel();
				lDurationInMillisUntilFinished = alert.getTotalDuration();
				displayDurationInView(lDurationInMillisUntilFinished);
				Snackbar.make(view, "Chrono stoppé", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
				RingtoneService.getInstance().stopRingtone();

				break;
			case R.id.alert_detail_launch_button:
				if (this.getCurrentAlertState() == AlertDetailActivity.AlertState.STOPPED) {
					lDurationInMillisUntilFinished =  TimeUnit.SECONDS.toMillis(alert.getTotalDuration());
				}
				lDurationBetweenAlarm = TimeUnit.SECONDS.toMillis(alert.getTotalDuration() / alert.getMaxCount() );
				lNextAlarmTimeInMillis = lDurationInMillisUntilFinished - lDurationBetweenAlarm;
				setCurrentAlertState(AlertDetailActivity.AlertState.ACTIVATED);
				Snackbar.make(view, "Chrono lancé", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();

				launchCountDownTimer();
				break;
		}
	}

	private void launchCountDownTimer() {
		final View thisview = this.getView();
		final Activity thisactivity = this.getActivity();
		oCountDownTimer = new CountDownTimer(lDurationInMillisUntilFinished, 1000) { // adjust the milli seconds here
			public void onTick(long millisUntilFinished) {
				lDurationInMillisUntilFinished = millisUntilFinished;
				displayDurationInView(lDurationInMillisUntilFinished);
				Log.d(TAG,"lDurationInMillisUntilFinished"+ TimeUnit.MILLISECONDS.toSeconds(lDurationInMillisUntilFinished));
				Log.d(TAG,"millisUntilFinished"+ TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished));
				Log.d(TAG,"lDurationBetweenAlarm"+ TimeUnit.MILLISECONDS.toSeconds(lDurationBetweenAlarm));
				long timeInSeconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished);
				Log.d(TAG,"previous alarm"+ (TimeUnit.MILLISECONDS.toSeconds(lNextAlarmTimeInMillis - lDurationBetweenAlarm) - INTERMEDIAIRE_ALARM_DURATION));
				if ( timeInSeconds == TimeUnit.MILLISECONDS.toSeconds(lNextAlarmTimeInMillis)) {
					lNextAlarmTimeInMillis = lNextAlarmTimeInMillis + lDurationBetweenAlarm;
					RingtoneService.getInstance().startRingtone(false);
					//NotificationService.getInstance().sendLocalNotification(getContext(), "Chrono fini ",
					//		"Chrono intermé à ", "");

				} else if (timeInSeconds ==
						(TimeUnit.MILLISECONDS.toSeconds(lNextAlarmTimeInMillis - lDurationBetweenAlarm)
								- INTERMEDIAIRE_ALARM_DURATION)) {
					RingtoneService.getInstance().stopRingtone();

				}
			}

			public void onFinish() {
				SimpleDateFormat oSimpleDateFormat = new SimpleDateFormat("HH:MM");
				String sDate = oSimpleDateFormat.format(new Date(System.currentTimeMillis()));

				Snackbar.make(thisview, "Chrono fini à " + sDate, (int) TimeUnit.MINUTES.toMillis(SNACK_DISPLAY_DURATION))
						.setAction("Action", null).show();

				//NotificationService.getInstance().sendLocalNotification(getContext(), "Chrono fini ",
				//		"Chrono fini à " + sDate, sDate);
				RingtoneService.getInstance().startRingtone(true);
				lDurationInMillisUntilFinished = 0;
				displayDurationInView(lDurationInMillisUntilFinished);

				setCurrentAlertState(AlertDetailActivity.AlertState.STOPPED);
			}
		}.start();

	}

	public void displayDurationInView(long p_millisUntilFinished) {
		if (oDisplayedDurationRestView == null) {
			oDisplayedDurationRestView = (TextView) getView().findViewById(R.id.alert_chrono);
		}
		oDisplayedDurationRestView.setText(AlertDao.formatLongInTime(p_millisUntilFinished));
	}

	private void setCurrentAlertState(AlertDetailActivity.AlertState currentAlertState) {
		((AlertDetailActivity) this.getActivity()).setCurrentAlertState(currentAlertState);
		switch (currentAlertState) {
			case PAUSED:
				enableButton(oPauseButton);
				enableButton(oStopButton);
				enableButton(oLauncherButton);
				break;
			case ACTIVATED:
				enableButton(oPauseButton);
				enableButton(oStopButton);
				disableButton(oLauncherButton);
				break;
			case STOPPED:
				disableButton(oPauseButton);
				disableButton(oStopButton);
				enableButton(oLauncherButton);
			default:
		}
	}

	private void enableButton(FloatingActionButton p_oButton) {
		p_oButton.setVisibility(View.VISIBLE);
		p_oButton.setClickable(true);
		p_oButton.setOnClickListener(this);
	}

	private void disableButton(FloatingActionButton p_oButton) {
		p_oButton.setVisibility(View.GONE);
		p_oButton.setClickable(false);
		p_oButton.setOnClickListener(null);
	}
}
