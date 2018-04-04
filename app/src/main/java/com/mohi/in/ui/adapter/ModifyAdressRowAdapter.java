package com.mohi.in.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.google.gson.JsonObject;

import com.mohi.in.R;
import com.mohi.in.activities.EditAddressActivity;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.model.ModifyAddressModel;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.OnValueChangeListner;
import com.mohi.in.utils.RefreshList;
import com.mohi.in.utils.ServerCallBack;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;
import com.mohi.in.widgets.UbuntuBoldTextView;
import com.mohi.in.widgets.UbuntuMediumTextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admin on 11/10/17.
 */

public class ModifyAdressRowAdapter extends RecyclerView.Adapter<ModifyAdressRowAdapter.ViewHolder> implements ServerCallBack {


    private Context mContext;
    private OnValueChangeListner onValueChangeListner;
    private ArrayList<ModifyAddressModel> mList = new ArrayList<>();
    private int size = 0, pos = -1;//, previousPosition = 0, currentPosition = 0, isOpenPrepos = 0, isOpenCurrPos = 0;
    private boolean flage = false, status;
    String removeStatus;
    private boolean onBind;
    RefreshList mRefreshList;

    public ModifyAdressRowAdapter(Context context, boolean status,OnValueChangeListner onValueChangeListner, String removeStatus, RefreshList mRefreshList ) {
        this.mContext = context;
        this.status = status;
        this.removeStatus = removeStatus;
        this.onValueChangeListner = onValueChangeListner;
        this.mRefreshList = mRefreshList;


    }

    public void setList(ArrayList<ModifyAddressModel> list) {

        this.mList = list;
        pos = -1;
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.modify_address_row, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ModifyAddressModel model = mList.get(position);

        String str = model.flat_no+", "+model.street;

        if(!model.landmark.trim().equalsIgnoreCase("")){
            str = str+", "+model.landmark+", "+ model.city + ", " + model.state + ", " + model.postcode;

        }else {

            str = str+", "+ model.city + ", " + model.state + ", " + model.postcode;

        }


        holder.tv_name.setText(model.name);
        holder.tv_Address.setText(str);
        holder.tv_phoneNo.setText(model.mobile);


        if (status) {
            holder.rb_radiobutton.setVisibility(View.INVISIBLE);

        }





        if (model.isSelected) {
            onBind = true;
            holder.rb_radiobutton.setChecked(true);
            onBind = false;

        } else {
            onBind = true;
            holder.rb_radiobutton.setChecked(false);
            onBind = false;

        }



        if (pos == position) {

            holder.ll_editDelete.setVisibility(View.VISIBLE);


        }else {
            holder.ll_editDelete.setVisibility(View.GONE);
        }


        if(removeStatus.trim().equalsIgnoreCase("")){

            holder.tv_delete.setVisibility(View.VISIBLE);


        }else {

            holder.tv_delete.setVisibility(View.GONE);

        }

        holder.iv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                flage = true;
                size = mList.size();
                pos = position;
/*


                isOpenPrepos = isOpenCurrPos;
                isOpenCurrPos = position;
*/
/*

                ModifyAddressModel model = mList.get(isOpenPrepos);

                model.isOpen = false;

//                model = mList.get(isOpenCurrPos);
                model.isOpen = true;
*/

                notifyDataSetChanged();
            }
        });


        holder.ll_inner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!onBind) {
                    pos = -1;

                    closeEditDeleteDialog(position);

                }

            }
        });
        holder.rb_radiobutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if(!onBind) {
                    pos = -1;

                    closeEditDeleteDialog(position);
                }

            }
        });


        holder.tv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!onBind) {
//                    closeEditDeleteDialog(position);
                    Intent intent = new Intent(mContext, EditAddressActivity.class);
                    intent.putExtra("AddressId", model.address_id);
                    intent.putExtra("Flat", model.flat_no);
                    intent.putExtra("Street", model.street);
                    intent.putExtra("Landmark", model.landmark);
                    intent.putExtra("Name", model.name);
                    intent.putExtra("Mobile", model.mobile);
                    intent.putExtra("PinCode", model.postcode);
                    intent.putExtra("City", model.city);
                    intent.putExtra("State", model.state);
                    intent.putExtra("Contry", model.country);
                    ((Activity)mContext).startActivity(intent);


                }



            }
        });


        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!onBind) {


                    try {

                        WaitDialog.showDialog(mContext);

                        JsonObject json = new JsonObject();
                        json.addProperty("user_id", SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_ID));
                        json.addProperty("token", SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_TOKEN));
                        json.addProperty("address_id", model.address_id);

                        pos = position;



                        ServerCalling.ServerCallingUserApiPost(mContext, "deleteShippingAddress", json, ModifyAdressRowAdapter.this);



                    }catch (Exception e){


                        Log.e("AllProductListAdapter", "Exception AllProductListAdapter: "+e.getMessage());
                    }
                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_more;
        private UbuntuBoldTextView tv_name;
        private UbuntuMediumTextView tv_Address, tv_phoneNo;
        private RadioButton rb_radiobutton;
        private FrameLayout fl_row;
        private LinearLayout ll_inner, ll_editDelete;
        private UbuntuMediumTextView tv_edit, tv_delete;


        public ViewHolder(View itemView) {
            super(itemView);
            iv_more = (ImageView) itemView.findViewById(R.id.Modify_Address_Row_EditDelete);

            tv_name = (UbuntuBoldTextView) itemView.findViewById(R.id.Modify_Address_Row_Name);
            tv_phoneNo = (UbuntuMediumTextView) itemView.findViewById(R.id.Modify_Address_Row_PhoneNo);
            tv_Address = (UbuntuMediumTextView) itemView.findViewById(R.id.Modify_Address_Row_Address);

            tv_edit = (UbuntuMediumTextView) itemView.findViewById(R.id.Modify_Address_Row_Edit);
            tv_delete = (UbuntuMediumTextView) itemView.findViewById(R.id.Modify_Address_Row_Delete);

            rb_radiobutton = (RadioButton) itemView.findViewById(R.id.Modify_Address_Row_Radiobutton);

            fl_row = (FrameLayout) itemView.findViewById(R.id.Modify_Address_Row);
            ll_inner = (LinearLayout) itemView.findViewById(R.id.Modify_Address_Row_Inner);
            ll_editDelete = (LinearLayout) itemView.findViewById(R.id.Modify_Address_Row_EditDeleteLayout);

        }
    }

    // ServerCallBackSuccess
    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        try {
            WaitDialog.hideDialog();

            if (strfFrom.trim().equalsIgnoreCase("deleteShippingAddress")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {

                    Methods.showToast(mContext, result.getString("msg"));
                    mList.remove(pos);

                    HashMap<String, String> hash = new HashMap<>();
                    hash = SessionStore.getUserDetails(mContext, Common.userPrefName);

                    if(mList.size()==0){
                        //Country Code may recive in future
                        String countryCode="";

                        SessionStore.save(mContext, Common.userPrefName, hash.get(SessionStore.USER_ID), hash.get(SessionStore.USER_TOKEN), hash.get(SessionStore.USER_EMAIL), hash.get(SessionStore.USER_MOBILENO)
                        , hash.get(SessionStore.USER_FIRST_NAME),hash.get(SessionStore.USER_LAST_NAME), hash.get(SessionStore.PROFILEPICTURE), "","","", hash.get(SessionStore.USER_CURRENCYTYPE),hash.get(SessionStore.COUNTRY_CODE));
                    }else {

                        JSONObject data = result.getJSONObject("data");

                        String strAddress= "", strAddressName="", strAddresId="";


                        if(!data.isNull( "address" )){
                            JSONObject addressData = data.getJSONObject("address");

                            String str = addressData.getString("flat_no")+", "+addressData.getString("street");

                            if(!addressData.getString("landmark").equalsIgnoreCase("")){
                                str = str+", "+addressData.getString("landmark")+", "+ addressData.getString("city") + ", " + addressData.getString("state") + ", " +
                                        addressData.getString("postcode");

                            }else {

                                str = str+", "+ addressData.getString("city") + ", " + addressData.getString("state") + ", " + addressData.getString("postcode");

                            }


                            strAddressName = addressData.getString("name");
                            strAddresId = addressData.getString("address_id");
                            strAddress = str;
                        }


                        SessionStore.save(mContext, Common.userPrefName, hash.get(SessionStore.USER_ID), hash.get(SessionStore.USER_TOKEN), hash.get(SessionStore.USER_EMAIL), hash.get(SessionStore.USER_MOBILENO)
                                , hash.get(SessionStore.USER_FIRST_NAME),hash.get(SessionStore.USER_LAST_NAME), hash.get(SessionStore.PROFILEPICTURE), strAddresId,strAddressName,strAddress, hash.get(SessionStore.USER_CURRENCYTYPE),hash.get(SessionStore.COUNTRY_CODE));

                    }
                   // {"status":1,"msg":"Shipping address deleted successfully","data":{"address_id":"209","name":"Pankaj Saad","postcode":"54545454",
                    // "flat_no":"5454545","street":"Hijkkjkkjjk","landmark":"Jkjkhjkhkh","city":"Indore","state":"MP","country":"IN","mobile":"4545454545"}}



                    mRefreshList.refreshListSuccess();

                } else {

                    Methods.showToast(mContext, result.getString("msg"));

                    Log.e("AllProductListAdapter", "error AllProductListAdapter log: " + result.getString("msg"));
                }
            }


        } catch (Exception e) {


            Log.e("AllProductListAdapter", "Exception AllProductListAdapter ServerCallBackSuccess: " + e.getMessage());
        }

    }


    private void closeEditDeleteDialog(final int pos) {

        ((Activity) mContext).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //Code for the UiThread
                if (flage) {
                    flage = false;
                    size = mList.size();
                   /* isOpenPrepos = isOpenCurrPos;
                    isOpenCurrPos = pos;

                    ModifyAddressModel model = mList.get(isOpenPrepos);
                    model.isOpen = false;*/
                    notifyDataSetChanged();

                } else {

                   /* previousPosition = currentPosition;
                    currentPosition = pos;
                    ModifyAddressModel model = mList.get(previousPosition);
                    model.isSelected = false;
                    model = mList.get(currentPosition);
                    model.isSelected = true;*/
                    notifyDataSetChanged();
                    onValueChangeListner.onValueChange(pos);
                }
            }
        });




    }


}
