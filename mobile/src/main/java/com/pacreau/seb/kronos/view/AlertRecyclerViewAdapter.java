package com.pacreau.seb.kronos.view;

import android.content.Context;
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
import com.pacreau.seb.kronos.fragment.AlertDetailFragment;

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
	private boolean mTwoPane;

	public AlertRecyclerViewAdapter(List<Alert> items, boolean pTwoPane) {
		mValues = items;
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
	public void onBindViewHolder(final AlertViewHolder holder, int position) {
		ViewDataBinding viewDataBinding = holder.getViewDataBinding();
		viewDataBinding.setVariable(BR.alert, mValues.get(position));
		final int pos = position;

		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mTwoPane) {
					Bundle arguments = new Bundle();
					arguments.putString(AlertDetailFragment.ARG_DETAIL_ALERT_ID, String.valueOf(mValues.get(pos).getId()));
					AlertDetailFragment fragment = new AlertDetailFragment();
					fragment.setArguments(arguments);
					((AppCompatActivity) v.getContext()).getSupportFragmentManager().beginTransaction()
							.replace(R.id.alert_detail_container, fragment)
							.commit();
				} else {
					Context context = v.getContext();
					Intent intent = new Intent(context, AlertDetailActivity.class);
					intent.putExtra(AlertDetailFragment.ARG_DETAIL_ALERT_ID, mValues.get(pos).getId());
					context.startActivity(intent);
				}
			}
		});
	}

	@Override
	public int getItemCount() {
		return mValues.size();
	}
/*
		public class ViewHolder extends RecyclerView.ViewHolder {
			public final View mView;
			public final TextView mIdView;
			public final TextView mContentView;
			public Alert mItem;

			public ViewHolder(View view) {
				super(view);
				mView = view;
				mIdView = (TextView) view.findViewById(R.id.id);
				mContentView = (TextView) view.findViewById(R.id.intervalInSeconds);
			}

			@Override
			public String toString() {
				return super.toString() + " '" + mContentView.getText() + "'";
			}
		}*/
}