package com.pacreau.seb.kronos.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.pacreau.seb.kronos.R;
import com.pacreau.seb.kronos.alert.Alert;
import com.pacreau.seb.kronos.alert.AlertDao;
import com.pacreau.seb.kronos.view.AlertRecyclerViewAdapter;

/**
 * An activity representing a list of Alerts. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link AlertDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class AlertListActivity extends AppCompatActivity implements AlertDao.AlertListener {

	/**
	 * Whether or not the activity is in two-pane mode, i.e. running on a tablet
	 * device.
	 */
	private boolean mTwoPane;
	private static final String TAG = "AlertListActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//DataBindingUtil.setContentView(this, R.layout.activity_alert_list);
		setContentView(R.layout.activity_alert_list);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		toolbar.setTitle(getTitle());

		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.create_alert);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Snackbar.make(view, "Create new alert template", Snackbar.LENGTH_LONG)
						.setAction("Action", null).show();
				Context context = view.getContext();
				Intent intent = new Intent(context, AlertDetailActivity.class);
				context.startActivity(intent);

			}
		});


		//assert recyclerView != null;
		setupRecyclerView();

		if (findViewById(R.id.alert_detail_container) != null) {
			// The detail container view will be present only in the
			// large-screen layouts (res/values-w900dp).
			// If this view is present, then the
			// activity should be in two-pane mode.
			mTwoPane = true;
		}
	}

	private void setupRecyclerView() {
		AlertDao.getInstance().addAlertListenerthis);
		AlertDao.getInstance().getAsyncAlerts();
	}

	private AlertRecyclerViewAdapter adapter;

	@Override
	public void onDataAdded(Alert p_oAlert) {
		Log.d(TAG, "new data " + p_oAlert);
		RecyclerView recyclerView = (RecyclerView)findViewById(R.id.alert_list);
		if (adapter == null) {
			adapter = new AlertRecyclerViewAdapter(mTwoPane);
			recyclerView.setAdapter(adapter);
		}
		adapter.addItem(p_oAlert);
	}

	@Override
	public void onDataRemoved(Alert p_oAlert) {
		Log.d(TAG, "delete data " + p_oAlert);
		adapter.deleteItem(p_oAlert);
	}
}
