package com.pacreau.seb.kronos.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pacreau.seb.kronos.R;
import com.pacreau.seb.kronos.alert.Alert;
import com.pacreau.seb.kronos.alert.AlertDao;
import com.pacreau.seb.kronos.databinding.AlertEditBinding;

/**
 * Kronos
 * com.pacreau.seb.kronos
 *
 * @author spacreau
 * @since 07/03/2017
 */

public class AlertEditFragment extends Fragment implements View.OnClickListener {
	private Alert oAlert;
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String TAG = AlertEditFragment.class.getName();


	public AlertEditFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//if (getArguments().containsKey(ARG_EDIT_ALERT_ID)) {
		// Load the dummy content specified by the fragment
		// arguments. In a real-world scenario, use a Loader
		// to load content from a content provider.
		oAlert = AlertDao.getInstance().createEmptyAlert();
			/*CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
			if (appBarLayout != null) {
				appBarLayout.setTitle(String.valueOf(mItem.getTotalDuration()));
			}*/
		//}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		AlertEditBinding oAlertBinding = DataBindingUtil.inflate(inflater, R.layout.alert_edit, container, false);
		oAlertBinding.setAlert(oAlert);
		FloatingActionButton oButton = (FloatingActionButton) oAlertBinding.getRoot().findViewById(R.id.alert_edit_save_button);
		oButton.setOnClickListener(this);
		return oAlertBinding.getRoot();
	}

	public void onClick(View view) {
		Log.d(TAG, "save  " + view);
		Log.d(TAG, "save  " + oAlert);
		AlertEditBinding oAlertEditBinding = DataBindingUtil.bind(this.getView());
		oAlert = oAlertEditBinding.getAlert();
		Log.d(TAG, "save  " + oAlert);
		AlertDao.getInstance().save(oAlert);
	}
}