package com.mohi.in.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.google.gson.JsonObject;
import com.mohi.in.R;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.fragments.ProductListFragment;
import com.mohi.in.model.SelectListModel;
import com.mohi.in.ui.adapter.SelectListAdapter;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;
import com.mohi.in.utils.listeners.ServerCallBack;
import com.mohi.in.widgets.ArialUnicodeMSRadioButton;
import com.mohi.in.widgets.UbuntuBoldTextView;
import com.mohi.in.widgets.UbuntuRegularTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends AppCompatActivity implements View.OnClickListener, ServerCallBack {

    private UbuntuRegularTextView minPriceTextView;
    private UbuntuRegularTextView maxPriceTextView;
    private UbuntuRegularTextView shopByBrandTextView;
    private UbuntuRegularTextView coloursTextView;
    private UbuntuRegularTextView catalogTextView;
    private UbuntuRegularTextView applyTextView;
    private UbuntuRegularTextView maxPriceCurrencyType;
    private UbuntuRegularTextView minPriceCurrencyType;
    private CrystalRangeSeekbar priceRangeSeekBar;
    private LinearLayout shopByBrandLinearLayout;
    private LinearLayout coloursLinearLayout;
    private LinearLayout catalogLinearLayout;
    private SelectListAdapter mAdapter;
    private ArialUnicodeMSRadioButton inStockRadioButton;
    private ImageView backImageView;
    private ArrayList<SelectListModel> shopByBrandList;
    private ArrayList<SelectListModel> coloursList;
    private ArrayList<SelectListModel> catalogList;
    private ArrayList<SelectListModel> availabilityList;
    private Context mContext;
    private String error = "Error";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        init();
    }

    private void init() {
        mContext = this;
        shopByBrandList = new ArrayList<>();
        coloursList = new ArrayList<>();
        catalogList = new ArrayList<>();
        availabilityList = new ArrayList<>();
        backImageView = findViewById(R.id.Filter_Back);
        minPriceTextView = findViewById(R.id.Filter_MinPrice);
        maxPriceTextView = findViewById(R.id.Filter_MaxPrice);
        shopByBrandTextView = findViewById(R.id.Filter_ShopByBrandText);
        coloursTextView = findViewById(R.id.Filter_ColorsText);
        catalogTextView = findViewById(R.id.Filter_CatalogText);
        applyTextView = findViewById(R.id.Filter_Apply);
        maxPriceCurrencyType = findViewById(R.id.Filter_MaxPriceCurrencyType);
        minPriceCurrencyType = findViewById(R.id.Filter_MinPriceCurrencyType);
        shopByBrandLinearLayout = findViewById(R.id.Filter_ShopByBrand);
        coloursLinearLayout = findViewById(R.id.Filter_Colors);
        catalogLinearLayout = findViewById(R.id.Filter_Catalog);
        inStockRadioButton = findViewById(R.id.Filter_InStock);
        priceRangeSeekBar = findViewById(R.id.Filter_rangeSeekbar);
        setValue();
    }

    private void setValue() {
        String currencyType = " " + SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_CURRENCYTYPE);
        maxPriceCurrencyType.setText(currencyType);
        minPriceCurrencyType.setText(currencyType);
        mAdapter = new SelectListAdapter(mContext);
        shopByBrandLinearLayout.setOnClickListener(this);
        coloursLinearLayout.setOnClickListener(this);
        catalogLinearLayout.setOnClickListener(this);
        applyTextView.setOnClickListener(this);
        priceRangeSeekBar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                minPriceTextView.setText(Methods.getTwoDecimalVAlue("" + minValue));
                maxPriceTextView.setText(Methods.getTwoDecimalVAlue("" + maxValue));
            }
        });
        backImageView.setOnClickListener(this);
        attemptToGetList();
    }

    private void attemptToGetList() {
        try {
            WaitDialog.showDialog(this);
            JsonObject json = new JsonObject();
            ServerCalling.ServerCallingProductsApiPost(mContext, "getFilterListItems", json, this);
        } catch (Exception e) {
            Log.e(error, e.toString());
        }
    }

    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        WaitDialog.hideDialog();
        if (strfFrom.trim().equalsIgnoreCase("getFilterListItems")) {
            if (result.optString("status").trim().equalsIgnoreCase("1")) {
                JSONObject jdata = result.optJSONObject("data");
                setListItem(jdata);
            } else {
                Methods.showToast(mContext, result.optString("msg"));
            }
        }
    }

    private void setListItem(JSONObject jdata) {
        try {
            clearPreviousList();

            //Inserting Latest Values
            JSONArray jArray = jdata.getJSONArray("color");
            coloursList = setJsonDataToList(jArray);
            jArray = jdata.getJSONArray("brands");
            shopByBrandList = setJsonDataToList(jArray);
            jArray = jdata.getJSONArray("category");
            catalogList = setJsonDataToList(jArray);
            jArray = jdata.getJSONArray("availabilty");
            availabilityList = setJsonDataToList(jArray);
            JSONObject jPrice = jdata.getJSONObject("price");
            int minPrice = Integer.parseInt(jPrice.getString("low"));
            int maxPrice = Integer.parseInt(jPrice.getString("high"));
            setSeekBar(minPrice, maxPrice);
        } catch (Exception e) {
            Log.e(error, e.toString());
        }
    }

    private void clearPreviousList() {
        coloursList.clear();
        shopByBrandList.clear();
        catalogList.clear();
        availabilityList.clear();
    }

    private ArrayList<SelectListModel> setJsonDataToList(JSONArray array) {
        ArrayList<SelectListModel> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject data = array.optJSONObject(i);
            SelectListModel model = new SelectListModel();
            model.setCatId(data.optString("id"));
            model.setName(data.optString("name"));
            model.setColorCode(data.optString("code"));
            model.setImageUrl(data.optString("image"));
            model.setValue(data.optString("value"));
            model.setChecked(false);
            list.add(model);
        }
        return list;
    }

    private void setSeekBar(int minPrice, int maxPrice) {
        priceRangeSeekBar
                .setCornerRadius(10f)
                .setBarColor(getResources().getColor(R.color.color_d7d7d7))
                .setBarHighlightColor(getResources().getColor(R.color.termsandcondition_color))
                .setMaxValue(maxPrice)
                .setMinValue(minPrice)
                .setMaxStartValue(0)
                .setLeftThumbColor(getResources().getColor(R.color.termsandcondition_color))
                .setRightThumbColor(getResources().getColor(R.color.termsandcondition_color))
                .setDataType(CrystalRangeSeekbar.DataType.INTEGER)
                .apply();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            //Back Button
            case R.id.Filter_Back:
                onBackPressed();
                break;

            //Shop By Brand
            case R.id.Filter_ShopByBrand:
                showPopup("Brand", shopByBrandList, shopByBrandTextView);
                break;

            //Colors
            case R.id.Filter_Colors:
                showPopup("Color", coloursList, coloursTextView);
                break;

            //CataLog
            case R.id.Filter_Catalog:
                showPopup("Categories", catalogList, catalogTextView);
                break;

            //Apply
            case R.id.Filter_Apply:
                int availabilty = 2;
                if (inStockRadioButton.isChecked()) {
                    availabilty = 1;
                }
                String brand = "";
                String categories = "";
                String colors = "";

                for (int i = 0; i < coloursList.size(); i++) {
                    if (coloursList.get(i).isChecked()) {
                        if (colors.trim().equalsIgnoreCase("")) {
                            colors = coloursList.get(i).getCatId();
                        } else {
                            colors = colors + "," + coloursList.get(i).getCatId();
                        }
                    }
                }

                for (int i = 0; i < shopByBrandList.size(); i++) {
                    if (shopByBrandList.get(i).isChecked()) {
                        if (brand.trim().equalsIgnoreCase("")) {
                            brand = shopByBrandList.get(i).getCatId();
                        } else {
                            brand = brand + "," + shopByBrandList.get(i).getCatId();
                        }
                    }
                }


                for (int i = 0; i < catalogList.size(); i++) {
                    if (catalogList.get(i).isChecked()) {
                        if (categories.trim().equalsIgnoreCase("")) {
                            categories = catalogList.get(i).getCatId();
                        } else {
                            categories = categories + "," + catalogList.get(i).getCatId();
                        }
                    }
                }

                Intent intent = new Intent(mContext, HomeActivity.class);
                intent.putExtra("Type", "Filter");
                intent.putExtra("MinPrice", minPriceTextView.getText().toString());
                intent.putExtra("MaxPrice", maxPriceTextView.getText().toString());
                intent.putExtra("Colors", colors);
                intent.putExtra("Availabilty", availabilty);
                intent.putExtra("Categories", categories);
                intent.putExtra("Brand", brand);
                setResult(7,intent);
                finish();
        }
    }

    public void showPopup(String title, final List<SelectListModel> list, final UbuntuRegularTextView labelTextView) {

        LinearLayoutManager mLayoutManager;
        mAdapter.setList(list, title);
        final UbuntuBoldTextView titleTextView;
        ImageView backImageview;
        UbuntuRegularTextView doneTextView;
        final RecyclerView listRecyclerView;
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(layoutInflater!=null)
        {
            View view = layoutInflater.inflate(R.layout.filter_dialog, null);
            titleTextView = view.findViewById(R.id.Select_Row_Title);
            listRecyclerView = view.findViewById(R.id.Select_Row_List);
            backImageview = view.findViewById(R.id.Select_Row_Back);
            doneTextView = view.findViewById(R.id.Select_Row_Done);
            mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            listRecyclerView.setLayoutManager(mLayoutManager);
            listRecyclerView.setAdapter(mAdapter);
            final Dialog dialog = new Dialog(mContext, R.style.full_screen_dialog_withanimation);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(view);
            titleTextView.setText(title);
            doneTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String str = "";
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).isChecked()) {
                            if (str.trim().equalsIgnoreCase("")) {
                                str = list.get(i).getName();
                            } else {
                                str = str.concat(", "+list.get(i).getName());
                            }
                        }
                    }
                    labelTextView.setText(Methods.capitalizeWord(str.trim()));
                    dialog.dismiss();
                }
            });

            backImageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = Common.getDeviceHeightWidth(mContext).widthPixels;
            lp.height = Common.getDeviceHeightWidth(mContext).heightPixels;
            lp.gravity = Gravity.TOP;

            dialog.getWindow().setAttributes(lp);

            dialog.show();
        }
        else
        {
            Log.e(error,"Inflater of Dialog is Null");
        }
    }
}
