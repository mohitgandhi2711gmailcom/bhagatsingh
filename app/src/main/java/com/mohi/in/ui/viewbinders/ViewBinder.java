package com.mohi.in.ui.viewbinders;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.mohi.in.ui.viewholders.BaseViewHolder;


public abstract class ViewBinder<T> {

	private int mLayoutResId;

	public ViewBinder(int layoutResId) {
		mLayoutResId = layoutResId;
	}

	public View createView (Activity activity) {
		LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View view = inflater.inflate( mLayoutResId , null );
		view.setTag( createViewHolder( view ) );
		return view;
	}

	public void setLayoutResId(int layoutResId) {
		mLayoutResId = layoutResId;
	}

	public abstract BaseViewHolder createViewHolder(View view);

	/**
	 * @param entity
	 * @param position
	 * @param grpPosition In cases applicable, for e.g in expandable listview
	 * @param view
	 * @param activity
	 */
	public abstract void bindView(T entity, int position, int grpPosition , View view, Activity activity);
}
