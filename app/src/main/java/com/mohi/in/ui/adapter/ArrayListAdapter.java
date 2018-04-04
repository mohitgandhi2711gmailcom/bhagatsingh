package com.mohi.in.ui.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SpinnerAdapter;


import com.mohi.in.ui.viewbinders.ViewBinder;

import java.util.ArrayList;


/**
 *
 * @param <T>
 *            The entity that will be passed into this adapter
 */

public class ArrayListAdapter<T> extends BaseAdapter implements SpinnerAdapter, Filterable {

	protected Activity mActivity;
	protected ArrayList<T> arrayList;
	protected ViewBinder<T> viewBinder;


	public ArrayListAdapter(Activity activity, ArrayList<T> arrayList, ViewBinder<T> viewBinder) {
		this.mActivity = activity;
		this.arrayList = arrayList;
		this.viewBinder = viewBinder;
	}

	public ArrayListAdapter(Activity activity, ViewBinder<T> viewBinder) {
		this(activity, new ArrayList<T>(), viewBinder);
	}

	public void setArrayList(ArrayList<T> arrayList) {
		this.arrayList = arrayList;
	}

	public void setViewBinder(ViewBinder<T> binder) {
		viewBinder = binder;
		notifyDataSetChanged();
	}

	/**
	 * Clears the internal list
	 */
	public void clearList() {
		arrayList.clear();
		notifyDataSetChanged();
	}

	public void add(T entity) {
		arrayList.add(entity);
		notifyDataSetChanged();
	}

	public void addAll(ArrayList<T> entityList) {
		arrayList.addAll(entityList);
		notifyDataSetChanged();
	}

	public void insert(int position, T item) {
		arrayList.add(position, item);
		notifyDataSetChanged();
	}

	public void remove(int position) {
		if (position < 0 || position >= arrayList.size())
			return;
		arrayList.remove(position);
		notifyDataSetChanged();
	}

	public void remove(T item) {
		for (int i = 0; i < arrayList.size(); i++) {
			if (arrayList.get(i).equals(item)) {
				arrayList.remove(i);
				return;
			}
		}
	}

	public void removeAll() {
		if (arrayList.isEmpty())
			return;

		arrayList.clear();
		notifyDataSetChanged();
	}

	public void clear() {
		removeAll();
	}

	@Override
	public int getCount() {
		return arrayList.size();
	}

	@Override
	public T getItem(int position) {
		return (T)arrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			convertView = viewBinder.createView(mActivity);
		}

		viewBinder.bindView(arrayList.get(position), position, 0, convertView, mActivity);

		return convertView;
	}
	
	public T getItemFromList(int index) {
		return arrayList.get(index);
	}
	
	public ArrayList<T> getList() {
		return arrayList;
	}

	@Override
	public Filter getFilter() {
		Filter filer = new Filter() {
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				@SuppressWarnings("unchecked")
                ArrayList<T> list = (ArrayList<T>) results.values;
				arrayList = list;
				notifyDataSetChanged();
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();
				ArrayList<T> FilteredNames = new ArrayList<T>();
				constraint = constraint.toString().toLowerCase();
				if (constraint.length() > 0) {
					for (int i = 0; i < arrayList.size(); i++) {
						T data = arrayList.get(i);
						FilteredNames.add(data);
					}
					results.values = FilteredNames;
					results.count = FilteredNames.size();
					return results;
				} else {
					results.values = arrayList;
					results.count = arrayList.size();
					return results;
				}

			}
		};
		return filer;
	}
}
