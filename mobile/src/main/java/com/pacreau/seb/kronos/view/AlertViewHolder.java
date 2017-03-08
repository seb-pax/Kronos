package com.pacreau.seb.kronos.view;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/**
 * Kronos
 * com.pacreau.seb.kronos
 *
 * @author spacreau
 * @since 02/03/2017
 */
public class AlertViewHolder extends RecyclerView.ViewHolder {

	private ViewDataBinding mViewDataBinding;


	public AlertViewHolder(ViewDataBinding viewDataBinding) {
		super(viewDataBinding.getRoot());

		mViewDataBinding = viewDataBinding;
		mViewDataBinding.executePendingBindings();
	}

	public ViewDataBinding getViewDataBinding() {
		return mViewDataBinding;
	}
}
