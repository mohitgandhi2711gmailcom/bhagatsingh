package com.mohi.in.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.gson.JsonObject;
import com.mohi.in.R;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.model.FeaturedProductsModel;
import com.mohi.in.ui.adapter.DealsAdapter;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.ServerCallBack;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DealsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DealsFragment extends Fragment  implements  ServerCallBack{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private View root;
    private RecyclerView mCategoryRv;
    private LinearLayoutManager mLayoutManager;
    private Context mContext;
    private DealsAdapter mCategoryAdapter ;
    private ArrayList<FeaturedProductsModel> mCategoryList = new ArrayList<>();

    private MaterialRippleLayout mFilterLayout;




    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //private OnFragmentInteractionListener mListener;

    public DealsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DealsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DealsFragment newInstance(String param1, String param2) {
        DealsFragment fragment = new DealsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_deals, container, false);
        getControls();
        return root ;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            Methods.trimCache(getActivity());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onResume() {
        super.onResume();
        setValue();
    }

    private void getControls()
    {
        mContext = getActivity();
        mCategoryRv = (RecyclerView) root.findViewById(R.id.Deals_rv);

    }

    private void setValue(){

        mCategoryAdapter =new DealsAdapter(getActivity());


        mLayoutManager = new LinearLayoutManager(getActivity() , LinearLayoutManager.VERTICAL , false);
        mCategoryRv.setLayoutManager(mLayoutManager);
        mCategoryRv.setAdapter(mCategoryAdapter);

        attemptGetFeaturedCategories();

    }



    /*
  * set dummy featured category data
  * */
    private void setFeaturedCategories(JSONArray dataArray ) {

        try {

            mCategoryList.clear();

            mCategoryList.addAll(FeaturedProductsModel.getList(dataArray));

            mCategoryAdapter.setList(mCategoryList);

        }catch (Exception e){


            Log.e("AllProductsListActivity", "Exception attemptTOGetUserInfo: "+e.getMessage());
        }
    }



    // Get user information
    private void attemptGetFeaturedCategories(){
        try {
            WaitDialog.showDialog(getActivity());
            JsonObject json = new JsonObject();
            json.addProperty("limit", "");
            json.addProperty("page", "");
            json.addProperty("user_id", SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_ID));
            json.addProperty("token", SessionStore.getUserDetails(getActivity(), Common.userPrefName).get(SessionStore.USER_TOKEN));
            json.addProperty("width", getResources().getDimension(R.dimen.home_allproduct_row_width));
            json.addProperty("height", getResources().getDimension(R.dimen.home_allproduct_row_image_height));

            ServerCalling.ServerCallingProductsApiPost(getActivity(),  "deals", json, this);


        }catch (Exception e){


            Log.e("AllProductsListActivity", "Exception attemptTOGetUserInfo: "+e.getMessage());
        }

    }




    // ServerCallBackSuccess
    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        try {


            if(strfFrom.trim().equalsIgnoreCase("deals")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {

                    JSONArray dataArray = result.getJSONArray("data");

                    setFeaturedCategories(dataArray);
                    WaitDialog.hideDialog();

                } else {

                    Methods.showToast(getActivity(), result.getString("msg"));

                    Log.e("AllProductsListActivity", "ServerCallBackSuccess attemptTOGetUserInfo log: " + result.getString("msg"));
                }
            }

        }catch (Exception e){


            Log.e("AllProductsListActivity", "Exception attemptTOGetUserInfo ServerCallBackSuccess: "+e.getMessage());
        }

    }
}
