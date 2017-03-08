package com.pacreau.seb.kronos.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.pacreau.seb.kronos.R;
import com.pacreau.seb.kronos.alert.Alert;
import com.pacreau.seb.kronos.alert.AlertDao;
import com.pacreau.seb.kronos.databinding.ActivityAlertDetailBinding;
import com.pacreau.seb.kronos.fragment.AlertDetailFragment;
import com.pacreau.seb.kronos.fragment.AlertEditFragment;

/**
 * An activity representing a single Alert detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link AlertListActivity}.
 */
public class AlertDetailActivity extends AppCompatActivity {

	public enum AlertState {
		PAUSED, STOPPED, ACTIVATED, INCREATION;
	}

	private AlertState currentAlertState;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActivityAlertDetailBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_alert_detail);

		Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
		setSupportActionBar(toolbar);


		// Show the Up button in the action bar.
		ActionBar actionBar = getSupportActionBar();
		if (actionBar != null) {
			actionBar.setDisplayHomeAsUpEnabled(true);
			actionBar.setDisplayUseLogoEnabled(true);
		}

		// savedInstanceState is non-null when there is fragment state
		// saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added
		// to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at:
		//
		// http://developer.android.com/guide/components/fragments.html
		//
		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			if (getIntent().hasExtra(AlertDetailFragment.ARG_DETAIL_ALERT_ID)) {
				Alert oAlert = AlertDao.getInstance().getItem(getIntent().getLongExtra(AlertDetailFragment.ARG_DETAIL_ALERT_ID, -1));
				binding.setAlert(oAlert);
				this.setCurrentAlertState(AlertState.STOPPED);

				Bundle arguments = new Bundle();
				arguments.putParcelable(AlertDetailFragment.ARG_DETAIL_ALERT_ID, oAlert);
				AlertDetailFragment fragment = new AlertDetailFragment();
				fragment.setArguments(arguments);
				getSupportFragmentManager().beginTransaction().add(R.id.alert_detail_container, fragment).commit();
			} else {
				AlertEditFragment fragment = new AlertEditFragment();
				getSupportFragmentManager().beginTransaction().add(R.id.alert_detail_container, fragment).commit();
				this.setCurrentAlertState(AlertState.INCREATION);
			}
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			if (this.getCurrentAlertState() == AlertState.STOPPED) {
				navigateToList();
			} else if (this.getCurrentAlertState() == AlertState.INCREATION) {
				displayDialog(getString(R.string.alert_edit_lose_modifications));
			} else {
				displaySnackMessage(getString(R.string.alert_detail_stop_before_quit));
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void navigateToList() {
		navigateUpTo(new Intent(this, AlertListActivity.class));
	}

	@Override
	public void onBackPressed() {
		if (this.getCurrentAlertState() == AlertState.STOPPED) {
			super.onBackPressed();
		} else {
			displaySnackMessage(getString(R.string.alert_detail_stop_before_quit));
		}
	}

	private void displayDialog(String p_oMessage) {
		AlertDialog dialog = new AlertDialog.Builder(this).setTitle(p_oMessage).setMessage(
				p_oMessage).setPositiveButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						navigateToList();
					}
				}).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		}).show();
	}

	private void displaySnackMessage(String p_oMessage) {
		Snackbar.make(getWindow().getDecorView().getRootView(), p_oMessage, Snackbar.LENGTH_LONG)
				.setAction("Action", null).show();
	}

	public void setCurrentAlertState(AlertState currentAlertState) {
		this.currentAlertState = currentAlertState;
	}

	public AlertState getCurrentAlertState() {
		return currentAlertState;
	}
}
