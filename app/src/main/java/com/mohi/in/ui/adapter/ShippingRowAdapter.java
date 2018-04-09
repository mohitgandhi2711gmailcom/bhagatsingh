package com.mohi.in.ui.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.mohi.in.R;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.fragments.CartFragment;
import com.mohi.in.model.CartModel;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.listeners.OnValueChangeListner;
import com.mohi.in.utils.listeners.RefreshList;
import com.mohi.in.utils.listeners.ServerCallBack;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;
import com.mohi.in.widgets.ArialUnicodeMSTextView;
import com.mohi.in.widgets.UbuntuMediumTextView;
import com.mohi.in.widgets.UbuntuRegularTextView;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 11/10/17.
 */

public class ShippingRowAdapter extends BaseAdapter implements  ServerCallBack{
    // ServerCallBackSuccess

    private Context mContext;
    private ArrayList<CartModel> mList =new ArrayList<>();
    private int pos=0, oldQty=1, itemPos=0;
    ArrayAdapter<String> adapter;
    ArrayList<String> item =new ArrayList<>();
    CartFragment fragment;
    private OnValueChangeListner mCallback;
    RefreshList mRefreshList;


    public ShippingRowAdapter(Context context, OnValueChangeListner mCallback, RefreshList mRefreshList)
    {
        this.mContext =  context;
        this.fragment =  fragment;
        this.mRefreshList =  mRefreshList;

        item.add("Qty: 1");
        item.add("Qty: 2");
        item.add("Qty: 3");
        item.add("Qty: 4");
        item.add("Qty: 5");


        adapter = new ArrayAdapter<String>(context,
                R.layout.spinner_item, item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        this.mCallback = mCallback;;

    }

    public void setList(ArrayList<CartModel> list)
    {

        this.mList = list;
        notifyDataSetChanged();
    }

    private class ViewHolder {
        private UbuntuMediumTextView tv_productPrice;
        UbuntuRegularTextView tv_productName;
        private ImageView iv_image;
        private Spinner spn_qty;


    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        ViewHolder holder;

        if (view == null) {

            holder = new ViewHolder();

            LayoutInflater inflater = ( LayoutInflater )mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.shipping_row, null);

            holder.tv_productPrice = (UbuntuMediumTextView) view.findViewById(R.id.ShippingRow_ProductPrice);
            holder.tv_productName = (UbuntuRegularTextView) view.findViewById(R.id.ShippingRow_ProductName);
            holder.iv_image = (ImageView) view.findViewById(R.id.ShippingRow_Image);

            holder.spn_qty = (Spinner) view.findViewById(R.id.ShippingRow_Qty);

            view.setTag(holder);
        }else{

            holder = (ViewHolder) view.getTag();
        }



        final CartModel model = mList.get(position);
//        holder.tv_productName.setText(model.product_name);
//        holder.tv_productPrice.setText(Methods.getTwoDecimalVAlue(model.product_price)+" "+ SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_CURRENCYTYPE));

        holder.spn_qty.setAdapter(adapter);

//        holder.spn_qty.setSelection((Integer.parseInt(model.qty)-1));



//        Glide.with(mContext)
//                .load(model.image)
//                .into(holder.iv_image);

        holder.spn_qty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                CartModel model = mList.get(position);
                itemPos = position;
                mCallback.onValueChange(mList.size());

//                Log.e("dsfdsfds","11111111: "+Integer.parseInt(model.qty)+" :: "+Integer.parseInt(item.get(i).trim().replaceAll("Qty: ", "").trim()));

//                if (Integer.parseInt(model.qty) != Integer.parseInt(item.get(i).trim().replaceAll("Qty: ", "").trim())) {
//
//                    oldQty =Integer.parseInt(model.qty);
//                    model.qty = ""+Integer.parseInt(item.get(i).trim().replaceAll("Qty: ", "").trim());
//                    mCallback.onValueChange(mList.size());
//
//                    model.qty = ""+Integer.parseInt(item.get(i).trim().replaceAll("Qty: ","").trim());
//                    WaitDialog.showDialog(mContext);
//                    JsonObject json = new JsonObject();
//                    json.addProperty("user_id", SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_ID));
//                    json.addProperty("token", SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_TOKEN));
//                    json.addProperty("product_id", model.product_id);
//                    json.addProperty("qty", model.qty);
//                    json.addProperty("quote_id", model.quote_id);
//
//                    pos = position;
//
//                    ServerCalling.ServerCallingProductsApiPost(mContext, "updateCartQty", json, ShippingRowAdapter.this);
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        return view;
    }


    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        try {

            WaitDialog.hideDialog();
            if(strfFrom.trim().equalsIgnoreCase("updateCartQty")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {

                    Methods.showToast(mContext, result.getString("msg"));

                } else {

                    CartModel model = mList.get(itemPos);
                    //model.qty  =""+oldQty;

                    mRefreshList.refreshListSuccess();
                    Methods.showToast(mContext, result.getString("msg"));
                    Log.e("AllProductListAdapter", "error AllProductListAdapter log: " + result.getString("msg"));
                }
            }else if(strfFrom.trim().equalsIgnoreCase("addToWishlist")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {

                    Methods.showToast(mContext, result.getString("msg"));
                    CartModel model = mList.get(pos);

                   // mList.set(pos, new CartModel(model.product_id, model.product_name, model.image, model.qty, model.category,1, model.rating, model.price ));
                    notifyDataSetChanged();



                } else {

                    Methods.showToast(mContext, result.getString("msg"));

                    Log.e("AllProductListAdapter", "error AllProductListAdapter log: " + result.getString("msg"));
                }
            }


        }catch (Exception e){


            Log.e("AllProductListAdapter", "Exception AllProductListAdapter ServerCallBackSuccess: "+e.getMessage());
        }

    }

    private void showQtyDialog(final ArialUnicodeMSTextView tv_qty){

        AlertDialog.Builder b = new AlertDialog.Builder(mContext);

        String[] types = {"1", "2", "3", "4", "5"};
        b.setItems(types, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                switch(which){
                    case 0:
                        tv_qty.setText("Qty: 1");
                        break;
                    case 1:
                        tv_qty.setText("Qty: 2");
                        break;

                    case 2:
                        tv_qty.setText("Qty: 3");
                        break;

                    case 3:
                        tv_qty.setText("Qty: 4");
                        break;

                    case 4:
                        tv_qty.setText("Qty: 5");
                        break;



                    }
            }

        });


      /*  WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(b.getWindow().getAttributes());
        lp.width = LayoutParams.WRAP_CONTENT;
        lp.height =  LayoutParams.WRAP_CONTENT;
        lp.gravity =Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);*/

        b.show();

    }


}
/*extends RecyclerView.Adapter<ShippingRowAdapter.ViewHolder> implements ServerCallBack {


    private Context mContext;
    private ArrayList<CartModel> mList;
    private int pos=0;
    ArrayAdapter<String> adapter;
    ArrayList<String> item =new ArrayList<>();
    CartFragment fragment;
    private OnValueChangeListner mCallback;


    public ShippingRowAdapter(Context context, OnValueChangeListner mCallback)
    {
        this.mContext =  context;
        this.fragment =  fragment;
        this.fragment =  fragment;

        item.add("Qty: 1");
        item.add("Qty: 2");
        item.add("Qty: 3");
        item.add("Qty: 4");
        item.add("Qty: 5");


        adapter = new ArrayAdapter<String>(context,
                R.layout.spinner_item, item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        this.mCallback = mCallback;;

    }

    public void setList(ArrayList<CartModel> list)
    {
        this.mList = new ArrayList<>();
        this.mList = list;
        notifyDataSetChanged();
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.shipping_row , parent ,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {




        final CartModel model = mList.get(position);
        holder.tv_productName.setText(model.product_name);
        holder.tv_productPrice.setText(Methods.getTwoDecimalVAlue(model.product_price));

        holder.spn_qty.setAdapter(adapter);

        holder.spn_qty.setSelection((Integer.parseInt(model.qty)-1));

        Ion.with(mContext)
                .load(model.image)
                .withBitmap()
                .placeholder(R.drawable.small_logo)
                .error(R.drawable.small_logo)
                .intoImageView(holder.iv_image);



        holder.spn_qty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                CartModel model = mList.get(position);

                mCallback.onValueChange(mList.size());

        Log.e("dsfdsfds","11111111: "+Integer.parseInt(model.qty)+" :: "+Integer.parseInt(item.get(i).trim().replaceAll("Qty: ", "").trim()));

if (Integer.parseInt(model.qty) != Integer.parseInt(item.get(i).trim().replaceAll("Qty: ", "").trim())) {


                    model.qty = ""+Integer.parseInt(item.get(i).trim().replaceAll("Qty: ", "").trim());
                    mCallback.onValueChange(mList.size());

    model.qty = ""+Integer.parseInt(item.get(i).trim().replaceAll("Qty: ","").trim());
                    WaitDialog.showDialog(mContext);
                    JsonObject json = new JsonObject();
                    json.addProperty("user_id", SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_ID));
                    json.addProperty("token", SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_TOKEN));
                    json.addProperty("product_id", model.product_id);
                    json.addProperty("qty", model.qty);
                    json.addProperty("quote_id", model.quote_id);

                    pos = position;

                    ServerCalling.ServerCallingProductsApiPost(mContext, "updateCartQty", json, ShippingRowAdapter.this);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }



    class ViewHolder extends RecyclerView.ViewHolder
    {



        private UbuntuMediumTextView tv_productPrice;
        UbuntuRegularTextView tv_productName;
        private ImageView iv_image;
        private Spinner spn_qty;


        public ViewHolder(View itemView) {
            super(itemView);

            tv_productPrice = (UbuntuMediumTextView) itemView.findViewById(R.id.ShippingRow_ProductPrice);
            tv_productName = (UbuntuRegularTextView) itemView.findViewById(R.id.ShippingRow_ProductName);
            iv_image = (ImageView) itemView.findViewById(R.id.ShippingRow_Image);

            spn_qty = (Spinner) itemView.findViewById(R.id.ShippingRow_Qty);


        }
    }
*/