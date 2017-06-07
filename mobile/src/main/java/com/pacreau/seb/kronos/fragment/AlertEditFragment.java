package com.pacreau.seb.kronos.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.pacreau.seb.kronos.R;
import com.pacreau.seb.kronos.alert.Alert;
import com.pacreau.seb.kronos.alert.AlertDao;
import com.pacreau.seb.kronos.databinding.AlertEditBinding;
import com.pacreau.seb.kronos.service.RingtoneService;

/**
 * Kronos
 * com.pacreau.seb.kronos
 *
 * @author spacreau
 * @since 07/03/2017
 */

public class AlertEditFragment extends Fragment implements View.OnClickListener {
	private Alert oAlert;

	private EditText infoEditText;
	private EditText totalDurationEditText;
	private EditText maxCountEditText;
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

		Log.d(TAG, "onClick  " + view);
		try {
			this.bind();
			switch (oAlert.validate()) {
				case TOTAL_DURATION_INVALID:
					totalDurationEditText.setError(getString(R.string.error_invalid_duration));
				break;
				case MAXCOUNT_INVALID:
					maxCountEditText.setError(getString(R.string.error_invalid_maxcount));
					break;
				default:
					Log.d(TAG, "save  alert" + oAlert);
					AlertDao.getInstance().save(oAlert);
					this.getActivity().finish();
					break;
			}
		} catch (NumberFormatException exception) {
			oAlert.setMaxCount(1);
		}
	}

	private void bind() throws  NumberFormatException{
		if (infoEditText == null) {
			infoEditText = (EditText) this.getView().findViewById(R.id.alert_edit_description);
		}
		oAlert.setInfo(infoEditText.getText().toString());
		if (totalDurationEditText == null) {
			totalDurationEditText = (EditText) this.getView().findViewById(R.id.alert_edit_total_duration);
		}
		oAlert.setTotalDuration(Integer.parseInt(totalDurationEditText.getText().toString()));

		if (maxCountEditText == null) {
			maxCountEditText = (EditText) this.getView().findViewById(R.id.alert_edit_alarm_count);
		}
		oAlert.setMaxCount(Integer.parseInt(maxCountEditText.getText().toString()));

	}

	@Override
	public void onDetach() {
		super.onDetach();
		RingtoneService.getInstance().releaseRingtone();
	}
}