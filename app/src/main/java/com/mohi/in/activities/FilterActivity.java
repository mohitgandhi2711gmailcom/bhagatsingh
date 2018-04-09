package com.mohi.in.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.mohi.in.model.SelectListModel;
import com.mohi.in.ui.adapter.SelectListAdapter;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.listeners.ServerCallBack;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;
import com.mohi.in.widgets.ArialUnicodeMSRadioButton;
import com.mohi.in.widgets.UbuntuBoldTextView;
import com.mohi.in.widgets.UbuntuRegularEditText;
import com.mohi.in.widgets.UbuntuRegularTextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class FilterActivity extends AppCompatActivity implements View.OnClickListener, ServerCallBack {


    private UbuntuRegularTextView tv_minPrice, tv_maxPrice, tv_shopByBrand, tv_colours, tv_catalog, tv_title, tv_apply, tv_maxPriceCurrencyType, tv_minPriceCurrencyType;
    private CrystalRangeSeekbar sb_priceRange;
    private LinearLayout ll_shopByBrand, ll_colours, ll_catalog;

    private SelectListAdapter adapter;
    private ArialUnicodeMSRadioButton rb_inStock, rb_outOfStock;
    private ImageView iv_back;


    private ArrayList<SelectListModel> shopByBrandList = new ArrayList<>();
    private ArrayList<SelectListModel> coloursList = new ArrayList<>();
    private ArrayList<SelectListModel> catalogList = new ArrayList<>();

    private String strType = "", strCateId = "";
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        intent = getIntent();

        if (intent.getStringExtra("Type") != null) {

            strType = intent.getStringExtra("Type");
            if (intent.getStringExtra("CatId") != null) {
                strCateId = intent.getStringExtra("CatId");
            }

        }
        init();

    }


    private void init() {

        iv_back = (ImageView) findViewById(R.id.Filter_Back);

        tv_minPrice = (UbuntuRegularTextView) findViewById(R.id.Filter_MinPrice);
        tv_maxPrice = (UbuntuRegularTextView) findViewById(R.id.Filter_MaxPrice);
        tv_shopByBrand = (UbuntuRegularTextView) findViewById(R.id.Filter_ShopByBrandText);
        tv_colours = (UbuntuRegularTextView) findViewById(R.id.Filter_ColorsText);
        tv_catalog = (UbuntuRegularTextView) findViewById(R.id.Filter_CatalogText);
        tv_title = (UbuntuRegularTextView) findViewById(R.id.Filter_Title);
        tv_apply = (UbuntuRegularTextView) findViewById(R.id.Filter_Apply);

        tv_maxPriceCurrencyType = (UbuntuRegularTextView) findViewById(R.id.Filter_MaxPriceCurrencyType);
        tv_minPriceCurrencyType = (UbuntuRegularTextView) findViewById(R.id.Filter_MinPriceCurrencyType);

        ll_shopByBrand = (LinearLayout) findViewById(R.id.Filter_ShopByBrand);
        ll_colours = (LinearLayout) findViewById(R.id.Filter_Colors);
        ll_catalog = (LinearLayout) findViewById(R.id.Filter_Catalog);

        rb_inStock = (ArialUnicodeMSRadioButton) findViewById(R.id.Filter_InStock);
        rb_outOfStock = (ArialUnicodeMSRadioButton) findViewById(R.id.Filter_OutOfStock);


        sb_priceRange = (CrystalRangeSeekbar) findViewById(R.id.Filter_rangeSeekbar);

        setValue();
    }


    private void setValue() {

        tv_maxPriceCurrencyType.setText(" " + SessionStore.getUserDetails(FilterActivity.this, Common.userPrefName).get(SessionStore.USER_CURRENCYTYPE));
        tv_minPriceCurrencyType.setText(" " + SessionStore.getUserDetails(FilterActivity.this, Common.userPrefName).get(SessionStore.USER_CURRENCYTYPE));


        adapter = new SelectListAdapter(FilterActivity.this);


        ll_shopByBrand.setOnClickListener(this);
        ll_colours.setOnClickListener(this);
        ll_catalog.setOnClickListener(this);

        tv_apply.setOnClickListener(this);


        sb_priceRange.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tv_minPrice.setText(Methods.getTwoDecimalVAlue("" + minValue));
                tv_maxPrice.setText(Methods.getTwoDecimalVAlue("" + maxValue));
            }
        });

        attamTOGetList();

        iv_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.Filter_Back:

                onBackPressed();


                break;
            case R.id.Filter_ShopByBrand:

                showPopup("Brand", shopByBrandList, tv_shopByBrand);


                break;

            case R.id.Filter_Colors:

                showPopup("Color", coloursList, tv_colours);

                break;

            case R.id.Filter_Catalog:

                showPopup("Categories", catalogList, tv_catalog);


                break;

            case R.id.Filter_Apply:

                int availabilty = 2;

                if (rb_inStock.isChecked()) {
                    availabilty = 1;
                }


                String brand = "", categories = "", colors = "";

                int size = shopByBrandList.size();

                for (int i = 0; i < size; i++) {

                    if (shopByBrandList.get(i).isChecked) {
                        if (brand.trim().equalsIgnoreCase("")) {

                            brand = shopByBrandList.get(i).cat_id;

                        } else {

                            brand = brand + "," + shopByBrandList.get(i).cat_id;


                        }
                    }
                }

                size = coloursList.size();

                for (int i = 0; i < size; i++) {
                    if (coloursList.get(i).isChecked) {
                        if (colors.trim().equalsIgnoreCase("")) {

                            colors = coloursList.get(i).cat_id;

                        } else {

                            colors = colors + "," + coloursList.get(i).cat_id;


                        }
                    }
                }

                size = catalogList.size();

                for (int i = 0; i < size; i++) {
                    if (catalogList.get(i).isChecked) {
                        if (categories.trim().equalsIgnoreCase("")) {


                            categories = catalogList.get(i).cat_id;

                        } else {

                            categories = categories + "," + catalogList.get(i).cat_id;


                        }
                    }
                }


                Intent intent = new Intent(FilterActivity.this, AllProductsListActivity.class);
                intent.putExtra("Type", "Filter");
                intent.putExtra("MinPrice", tv_minPrice.getText().toString());
                intent.putExtra("MaxPrice", tv_maxPrice.getText().toString());
                intent.putExtra("Colors", colors);
                intent.putExtra("Availabilty", availabilty);
                intent.putExtra("Categories", categories);
                intent.putExtra("Brand", brand);


                startActivity(intent);
                if (!strType.trim().equalsIgnoreCase(""))
                    // finish();

                    break;


        }


    }


    private void setListItem(JSONObject jdata) {

        try {


            shopByBrandList.clear();
            coloursList.clear();
            catalogList.clear();


            JSONArray jArray = jdata.getJSONArray("color");
            int size = jArray.length();

            for (int i = 0; i < size; i++) {

                JSONObject data = jArray.getJSONObject(i);
                coloursList.add(new SelectListModel(data.getString("id"), data.getString("name"), data.getString("code"), false));

            }

            jArray = jdata.getJSONArray("brands");
            size = jArray.length();

            for (int i = 0; i < size; i++) {

                JSONObject data = jArray.getJSONObject(i);
                shopByBrandList.add(new SelectListModel(data.getString("id"), data.getString("name"), "", false));

            }

            jArray = jdata.getJSONArray("category");
            size = jArray.length();

            for (int i = 0; i < size; i++) {

                JSONObject data = jArray.getJSONObject(i);

                if (data.getString("id").trim().equalsIgnoreCase(strCateId.trim())) {
                    catalogList.add(new SelectListModel(data.getString("id"), data.getString("name"), data.getString("image"), true));
                    tv_catalog.setText(data.getString("name").trim());

                } else {
                    catalogList.add(new SelectListModel(data.getString("id"), data.getString("name"), data.getString("image"), false));

                }


            }

            JSONObject jPrice = jdata.getJSONObject("price");

            int maxPrice = 0, minPrice = 0;
            minPrice = Integer.parseInt(jPrice.getString("low"));
            maxPrice = Integer.parseInt(jPrice.getString("high"));


            Log.e("fgdfgdfg", "fgfgfgL: " + minPrice + " :: " + maxPrice);
            sb_priceRange
                    .setCornerRadius(10f)
                    .setBarColor(getResources().getColor(R.color.color_d7d7d7))
                    .setBarHighlightColor(getResources().getColor(R.color.termsandcondition_color))
                    .setMaxValue(maxPrice)
                    .setMinValue(minPrice)
                    .setMaxStartValue(0)
                    .setLeftThumbColor(getResources().getColor(R.color.termsandcondition_color))
                    .setRightThumbColor(getResources().getColor(R.color.termsandcondition_color))
                    //.setLeftThumbDrawable(R.drawable.thumb_android)
                    //  .setLeftThumbHighlightDrawable(R.drawable.thumb_android_pressed)
                    //  .setRightThumbDrawable(R.drawable.thumb_android)
                    //  .setRightThumbHighlightDrawable(R.drawable.thumb_android_pressed)
                    .setDataType(CrystalRangeSeekbar.DataType.INTEGER)
                    .apply();


        } catch (Exception e) {


            Log.e("HomeActivity Exception", "Exception attemptTOGetUserInfo: " + e.getMessage());
        }
    }


    private void attamTOGetList() {


        try {
            WaitDialog.showDialog(this);
            JsonObject json = new JsonObject();

            ServerCalling.ServerCallingProductsApiPost(FilterActivity.this, "getFilterListItems", json, this);


        } catch (Exception e) {


            Log.e("HomeActivity Exception", "Exception attemptTOGetUserInfo: " + e.getMessage());
        }
    }


    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        try {

            WaitDialog.hideDialog();
            if (strfFrom.trim().equalsIgnoreCase("getFilterListItems")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {


                    JSONObject jdata = result.getJSONObject("data");
                    setListItem(jdata);


                } else {

                    Methods.showToast(FilterActivity.this, result.getString("msg"));

                    Log.e("HomeActivity", "ServerCallBackSuccess attemptTOGetUserInfo log: " + result.getString("msg"));
                }
            }


        } catch (Exception e) {


            Log.e("HomeActivity Exception", "Exception attemptTOGetUserInfo ServerCallBackSuccess: " + e.getMessage());
        }
    }


    public void showPopup(String strTitle, final ArrayList<SelectListModel> list, final UbuntuRegularTextView tv_labale) {

        LinearLayoutManager mLayoutManager;
        adapter.setList(list, strTitle);


        ArrayList<SelectListModel> listItem = new ArrayList<>();

        final UbuntuBoldTextView tv_title;
        ImageView iv_back;
        UbuntuRegularTextView tv_Done;

        final RecyclerView rv_list;
        UbuntuRegularEditText et_searchText;

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.select_row, null);

        tv_title = (UbuntuBoldTextView) view.findViewById(R.id.Select_Row_Title);

        rv_list = (RecyclerView) view.findViewById(R.id.Select_Row_List);

        iv_back = (ImageView) view.findViewById(R.id.Select_Row_Back);

        tv_Done = (UbuntuRegularTextView) view.findViewById(R.id.Select_Row_Done);

        et_searchText = (UbuntuRegularEditText) view.findViewById(R.id.Select_Row_Search);


        mLayoutManager = new LinearLayoutManager(FilterActivity.this, LinearLayoutManager.VERTICAL, false);
        rv_list.setLayoutManager(mLayoutManager);

        rv_list.setAdapter(adapter);

        final Dialog dialog = new Dialog(FilterActivity.this, R.style.full_screen_dialog_withanimation);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);


        tv_title.setText(strTitle);


        et_searchText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub


               /* if((""+s).equalsIgnoreCase("")){
                    bloodTypeBeenList1.clear();

                    bloodTypeBeenList1.addAll(bloodTypeBeenList);

                }else{
                    bloodTypeBeenList1.clear();
                    for (int i = 0; i < bloodTypeBeenList.size(); i++) {

                        if(bloodTypeBeenList.get(i).getName().toLowerCase().trim().toLowerCase().contains((""+s).toLowerCase())){

                            bloodTypeBeenList1.add(new OtherFrequency_Been(bloodTypeBeenList.get(i).getName(), false));
                        }

                    }



                }

                lv_blood_type.setAdapter(new DosageUnit_Adapter(MyProfilePersonalScreen_Activity.this, bloodTypeBeenList1,txt_BloodGroup,dialog));*/

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        tv_Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int size = list.size();

                String str = "";

                for (int i = 0; i < size; i++) {

                    if (list.get(i).isChecked) {


                        Log.e("sdfdsfd", "fffff: " + str + " ::: " + i);

                        if (str.trim().equalsIgnoreCase("")) {
                            str = list.get(i).name;
                        } else {
                            str = str + ", " + list.get(i).name;
                        }
                    }

                }

                tv_labale.setText(Methods.capitalizeWord(str.trim()));

                dialog.dismiss();
            }
        });

        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = Common.getDeviceHeightWidth(FilterActivity.this).widthPixels;
        lp.height = Common.getDeviceHeightWidth(FilterActivity.this).heightPixels;
        lp.gravity = Gravity.TOP;

        dialog.getWindow().setAttributes(lp);

        dialog.show();


    }
}
