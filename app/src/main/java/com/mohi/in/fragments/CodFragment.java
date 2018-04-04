package com.mohi.in.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.mohi.in.R;
import com.mohi.in.model.CODModel;
import com.mohi.in.ui.adapter.CODAdapter;

import java.util.ArrayList;

public class CodFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_cod,container,false);
        RecyclerView cod_rv=view.findViewById(R.id.cod_rv);
        ArrayList<CODModel> list=getList();
        CODAdapter adapter=new CODAdapter(getActivity(),list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        cod_rv.setLayoutManager(mLayoutManager);
        cod_rv.setAdapter(adapter);
        return view;
    }

    private ArrayList<CODModel> getList() {
        ArrayList<CODModel> list=new ArrayList<>();
        CODModel obj1=new CODModel();
        obj1.setDesc("Description1");
        obj1.setTitlename("Title1");

        CODModel obj2=new CODModel();
        obj2.setDesc("Description2");
        obj2.setTitlename("Title2");

        list.add(obj1);
        list.add(obj2);
        return list;
    }
}
