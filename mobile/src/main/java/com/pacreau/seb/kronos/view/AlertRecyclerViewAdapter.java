package com.pacreau.seb.kronos.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pacreau.seb.kronos.BR;
import com.pacreau.seb.kronos.R;
import com.pacreau.seb.kronos.activity.AlertDetailActivity;
import com.pacreau.seb.kronos.alert.Alert;
import com.pacreau.seb.kronos.alert.AlertDao;
import com.pacreau.seb.kronos.fragment.AlertDetailFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Kronos
 * com.pacreau.seb.kronos
 *
 * @author spacreau
 * @since 02/03/2017
 */

public class AlertRecyclerViewAdapter
		extends RecyclerView.Adapter<AlertViewHolder> {

	private final List<Alert> mValues;
	private boolean mTwoPane = false;

	public AlertRecyclerViewAdapter(boolean pTwoPane) {
		mValues = new ArrayList<Alert>();
	}

	public void addItem(Alert oAlert) {
		mValues.add(oAlert);
		this.notifyItemInserted(mValues.size()-1);
	}

	public void deleteItem(Alert p_oAlert) {
		for ( int indice = 0; indice< mValues.size() ; indice++ ) {
			if (p_oAlert.getId().equals(mValues.get(indice).getId())) {
				mValues.remove(indice);
				this.notifyItemRemoved(indice);
				break;
			}
		}

	}

	@Override
	public AlertViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		//View view = LayoutInflater.from(parent.getContext())
		//		.inflate(R.layout.alert_list_content, parent, false);
		//return new ViewHolder(view);
		ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
				R.layout.alert_list_content, parent, false);

		return new AlertViewHolder(binding);
	}

	@Override
	public void onBindViewHolder(final AlertViewHolder holder, final int p_position) {
		ViewDataBinding viewDataBinding = holder.getViewDataBinding();
		viewDataBinding.setVariable(BR.alert, mValues.get(p_position));
		final int pos = p_position;

		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mTwoPane) {
					Bundle arguments = new Bundle();
					arguments.putParcelable(AlertDetailFragment.ARG_DETAIL_ALERT_ID, mValues.get(pos));
					AlertDetailFragment fragment = new AlertDetailFragment();
					fragment.setArguments(arguments);
					((AppCompatActivity) v.getContext()).getSupportFragmentManager().beginTransaction()
							.replace(R.id.alert_detail_container, fragment)
							.commit();
				} else {
					Context context = v.getContext();
					Intent intent = new Intent(context, AlertDetailActivity.class);
					intent.putExtra(AlertDetailFragment.ARG_DETAIL_ALERT_ID, mValues.get(pos));
					context.startActivity(intent);
				}
			}
		});
		holder.itemView.setOnLongClickListener((new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				AlertDialog dialog = new AlertDialog.Builder(v.getContext()).setTitle(R.string.title_alert_delete).setMessage(
						R.string.title_alert_delete).setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
									AlertDao.getInstance().deleteAlert(mValues.get(p_position).getId());
							}
						}).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				}).show();
				return true;
			}
		}));
	}

	@Override
	public int getItemCount() {
		return mValues.size();
	}

}