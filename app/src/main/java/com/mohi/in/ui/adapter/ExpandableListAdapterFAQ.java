package com.mohi.in.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.mohi.in.R;
import com.mohi.in.model.FaqModel;
import java.util.ArrayList;

public class ExpandableListAdapterFAQ extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<FaqModel> list;

    public ExpandableListAdapterFAQ(Context context, ArrayList<FaqModel> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getGroupCount() {
        return list.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return list.get(groupPosition).getQuestion();
    }

    @Override
    public Object getChild(int group, int child) {
        return list.get(child).getDesc();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        convertView = inflater.inflate(R.layout.expandable_list_view_title, parent, false);
        TextView parentHeader = convertView.findViewById(R.id.title_tv);
        parentHeader.setText(list.get(groupPosition).getQuestion());
        if (isExpanded) {
            parentHeader.setBackground(ContextCompat.getDrawable(context, R.drawable.faq_top_round_corner_white_shape));
        } else {
            parentHeader.setBackground(ContextCompat.getDrawable(context, R.drawable.faq_round_corner_white_shape));
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        convertView = inflater.inflate(R.layout.expandable_list_view_desc, parent, false);
        TextView descHeader = convertView.findViewById(R.id.desc_tv);
        descHeader.setText(list.get(groupPosition).getDesc());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

