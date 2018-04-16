package com.mohi.in.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import com.mohi.in.R;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.utils.listeners.imgSelectListener;
import com.mohi.in.model.ProductDetailModel;
import com.mohi.in.ui.adapter.FeaturedProductsAdapter;
import com.mohi.in.ui.adapter.ProductsLeftImgAdapter;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.listeners.ServerCallBack;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;
import com.mohi.in.utils.Urls;
import com.mohi.in.widgets.ArialUnicodeMSTextView;
import com.mohi.in.widgets.TextAwesome;
import com.mohi.in.widgets.UbuntuLightTextView;
import com.mohi.in.widgets.UbuntuMediumTextView;
import com.mohi.in.widgets.UbuntuRegularButton;
import com.mohi.in.widgets.UbuntuRegularEditText;
import com.mohi.in.widgets.UbuntuRegularTextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 16/10/17.
 */

public class ProductDetailActivity extends AppCompatActivity implements imgSelectListener, ServerCallBack, View.OnClickListener {


    private RecyclerView mProductsLeftRv, mProductSuggestionRV;
    private LinearLayoutManager mLayoutManagerProductSuggestion;

    private LinearLayoutManager mLayoutManager;
    private Context mContext;
    private ProductDetailModel mProductDetailModel;

    private ProductsLeftImgAdapter mProductsLeftImgAdapter;
    private ImageView mProductImgView, iv_wishList;

    private UbuntuRegularTextView tv_titel, tv_price;
    private ArialUnicodeMSTextView tv_estimatTime, tv_WriteReview, tv_sareeType, tv_topFabric, tv_bottom, tv_dupattaFabric, tv_inner, tv_washCare;
    private UbuntuRegularButton but_sizeCart, but_addToCart, but_buyNow;
    private RatingBar rb_rating;
    private WebView wv_description;
    private UbuntuRegularTextView tv_currencyType;

    private Intent intent;
    private String strProductId = "";
    private boolean buynow = false;

    FeaturedProductsAdapter mProductSuggestionAdapter;


    // Header
    private ImageView iv_menu;
    private ImageView iv_search, iv_filter;
    private ImageView iv_menuIcon;
    private LinearLayout ll_titellogo;
    private MaterialRippleLayout mrl_search, mrl_filter, mrl_menu;
    private UbuntuLightTextView tv_headerTilel;

    private LinearLayout ll_DeliveryPinLayout, ll_deliveryDetaillayout;
    private UbuntuRegularTextView tv_DeliveryPinText, tv_DeliveryDetail, tv_DrliveryPinChange;
    TextAwesome tv_DeliveryLeftPin, tv_DeliveryRightPin;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        intent = getIntent();
        if (intent.getStringExtra("ProductId") != null) {

            strProductId = intent.getStringExtra("ProductId");

        }


        getControls();

    }

    private void getControls() {

        ll_DeliveryPinLayout = (LinearLayout) findViewById(R.id.ProductDetail_DelieveryPinLayout);
        ll_deliveryDetaillayout = (LinearLayout) findViewById(R.id.ProductDetail_DelieveryDetailLayout);


        tv_DeliveryPinText = (UbuntuRegularTextView) findViewById(R.id.ProductDetail_DelieveryMarkText);
        tv_DeliveryDetail = (UbuntuRegularTextView) findViewById(R.id.ProductDetail_DelieveryDetail);

        tv_DrliveryPinChange = (UbuntuRegularTextView) findViewById(R.id.ProductDetail_DelieveryChangeMark);

        tv_DeliveryLeftPin = (TextAwesome) findViewById(R.id.ProductDetail_DelieveryLeftPin);
        tv_DeliveryRightPin = (TextAwesome) findViewById(R.id.ProductDetail_DelieveryRightPin);


        mContext = ProductDetailActivity.this;

        mProductSuggestionRV = (RecyclerView) findViewById(R.id.ProductDetail_ProductSuggestion);

        mProductsLeftRv = (RecyclerView) findViewById(R.id.ProductDetail_listView);

        mProductImgView = (ImageView) findViewById(R.id.ProductDetail_Image);
        iv_wishList = (ImageView) findViewById(R.id.ProductDetail_favourite);





        tv_titel = (UbuntuRegularTextView) findViewById(R.id.ProductDetail_Name);
        tv_price = (UbuntuRegularTextView) findViewById(R.id.ProductDetail_Price);

        tv_estimatTime = (ArialUnicodeMSTextView) findViewById(R.id.ProductDetail_EstimatTime);
        tv_WriteReview = (ArialUnicodeMSTextView) findViewById(R.id.ProductDetail_WriteReview);
        tv_sareeType = (ArialUnicodeMSTextView) findViewById(R.id.ProductDetail_SareeType);
        tv_topFabric = (ArialUnicodeMSTextView) findViewById(R.id.ProductDetail_TopFabric);
        tv_bottom = (ArialUnicodeMSTextView) findViewById(R.id.ProductDetail_Bottom);
        tv_dupattaFabric = (ArialUnicodeMSTextView) findViewById(R.id.ProductDetail_DupattaFabric);
        tv_inner = (ArialUnicodeMSTextView) findViewById(R.id.ProductDetail_Inner);
        tv_washCare = (ArialUnicodeMSTextView) findViewById(R.id.ProductDetail_WashCare);

        but_sizeCart = (UbuntuRegularButton) findViewById(R.id.ProductDetail_SizeChart);
        but_addToCart = (UbuntuRegularButton) findViewById(R.id.ProductDetail_AddToCart);
        but_buyNow = (UbuntuRegularButton) findViewById(R.id.ProductDetail_BuyNow);

        rb_rating = (RatingBar) findViewById(R.id.ProductDetail_Rating);

        wv_description = (WebView) findViewById(R.id.ProductDetail_Description);

        tv_currencyType = (UbuntuRegularTextView) findViewById(R.id.ProductDetail_CurrencyType);

        //Header
        mrl_menu = (MaterialRippleLayout) findViewById(R.id.Header_MenuLayOut);
        mrl_search = (MaterialRippleLayout) findViewById(R.id.Header_SearchLayOut);
        mrl_filter = (MaterialRippleLayout) findViewById(R.id.Header_FilterLayOut);

        iv_menu = (ImageView) findViewById(R.id.Header_Menu);
        iv_search = (ImageView) findViewById(R.id.Header_Search);
        iv_filter = (ImageView) findViewById(R.id.Header_Filter);

        tv_headerTilel = (UbuntuLightTextView) findViewById(R.id.Header_Title);

        ll_titellogo = (LinearLayout) findViewById(R.id.Header_Titel_Logo);

        setValue();
    }


    private void setValue() {


        iv_menu.setImageResource(R.drawable.back_arrow);
        ll_titellogo.setVisibility(View.GONE);

        iv_search.setVisibility(View.INVISIBLE);
        iv_filter.setVisibility(View.INVISIBLE);
        tv_headerTilel.setVisibility(View.VISIBLE);

        mrl_search.setRippleDuration(0);
        mrl_search.setRippleFadeDuration(0);

        mrl_filter.setRippleDuration(0);
        mrl_filter.setRippleFadeDuration(0);


        // tv_WriteReview.setOnClickListener(this);

        mLayoutManagerProductSuggestion =  new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mProductSuggestionRV.setLayoutManager(mLayoutManagerProductSuggestion);
        mProductSuggestionAdapter = new FeaturedProductsAdapter(mContext, null, null);
        mProductSuggestionRV.setAdapter(mProductSuggestionAdapter);

        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        mProductsLeftRv.setLayoutManager(mLayoutManager);
        mProductsLeftImgAdapter = new ProductsLeftImgAdapter(mContext, this);


        iv_menu.setOnClickListener(this);
        iv_wishList.setOnClickListener(this);
        but_addToCart.setOnClickListener(this);
        but_buyNow.setOnClickListener(this);
        but_sizeCart.setOnClickListener(this);
        mProductImgView.setOnClickListener(this);
        ll_DeliveryPinLayout.setOnClickListener(this);
        tv_DrliveryPinChange.setOnClickListener(this);


        tv_currencyType.setText(" " + SessionStore.getUserDetails(ProductDetailActivity.this, Common.USER_PREFS_NAME).get(SessionStore.USER_CURRENCYTYPE));



        attemptGetProductDetail();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            Methods.trimCache(this);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    private void setData(JSONObject dataObj) {

        try {



            mProductDetailModel = new ProductDetailModel(dataObj.getString("product_id"), dataObj.getString("product_name"), dataObj.getString("image"), dataObj.getString("description"),
                    dataObj.getString("product_price"), dataObj.getString("is_wishlist"), dataObj.getString("rating"), dataObj.getString("is_add_to_cart"), dataObj.getString("pincode"), dataObj.getString("pincode_address"), dataObj.getJSONArray("media"), dataObj.getJSONArray("related_products"));

            tv_titel.setText(Methods.capitalizeWord(mProductDetailModel.product_name));
            tv_headerTilel.setText(Methods.capitalizeWord(mProductDetailModel.product_name));

            tv_price.setText(" " + Methods.getTwoDecimalVAlue(mProductDetailModel.product_price));

            rb_rating.setRating(Float.parseFloat(mProductDetailModel.rating));

            if (mProductDetailModel.is_wishlist.equalsIgnoreCase("0")) {

                iv_wishList.setImageResource(R.drawable.like_inactive_large);


            } else {

                iv_wishList.setImageResource(R.drawable.like_inactive_large);

            }

            Log.e("dfgdfg","Pin Code: "+mProductDetailModel.pincode);
            Log.e("dfgdfg","Pin Code: "+mProductDetailModel.pincodeaddress);



            if(mProductDetailModel.pincode.trim().equalsIgnoreCase("")){

                tv_DeliveryLeftPin.setVisibility(View.GONE);
                tv_DeliveryRightPin.setVisibility(View.VISIBLE);
                tv_DrliveryPinChange.setVisibility(View.GONE);
                ll_deliveryDetaillayout.setVisibility(View.GONE);
                tv_DeliveryPinText.setText(getResources().getString(R.string.provide_your_pincode_for_details));

            }else {


                tv_DeliveryLeftPin.setVisibility(View.VISIBLE);
                tv_DeliveryRightPin.setVisibility(View.VISIBLE);
                tv_DrliveryPinChange.setVisibility(View.GONE);
                ll_deliveryDetaillayout.setVisibility(View.GONE);
                tv_DeliveryRightPin.setText(getResources().getString(R.string.fa_angle_down));
                String ss = getResources().getString(R.string.delivery_option_for)+" "+mProductDetailModel.pincode.trim();
                SpannableString s= new SpannableString(ss);
                s.setSpan(new ForegroundColorSpan((getResources().getColor(R.color.termsandcondition_color))), getResources().getString(R.string.delivery_option_for).length(), ss.trim().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tv_DeliveryPinText.setText(s);
                tv_DeliveryDetail.setText(mProductDetailModel.pincodeaddress);
            }


            String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/font/arialun.ttf\")}body {font-family: MyFont"
                    + ";text-align: justify;  margin:0;}</style></head><body>";
            String pas = "</body></html>";
            String myHtmlString = pish + mProductDetailModel.description + pas;


            mProductSuggestionAdapter.setList(mProductDetailModel.relatedProductsList, false);


            if (mProductDetailModel.mediaList.size() > 0) {

                Glide.with(mContext)
                        .load(mProductDetailModel.mediaList.get(0).image)
                        .into(mProductImgView);
                mProductDetailModel.mediaList.get(0).isSelected = true;
                mProductsLeftImgAdapter.setList(mProductDetailModel.mediaList);
                mProductsLeftRv.setAdapter(mProductsLeftImgAdapter);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Override
    public void selectImgPosition(int position) {
        int length = mProductDetailModel.mediaList.size();
        for (int i = 0; i < length; i++) {
            mProductDetailModel.mediaList.get(i).isSelected = false;
            if (i == position) {
                mProductDetailModel.mediaList.get(i).isSelected = true;
                Glide.with(mContext)
                        .load(mProductDetailModel.mediaList.get(i).image)
                        .into(mProductImgView);
            }
        }


    }

    private void attemptGetProductDetail() {
        //WaitDialog.showDialog(this);


        wv_description.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        wv_description.getSettings().setLoadWithOverviewMode(true);
        wv_description.getSettings().setUseWideViewPort(true);
        wv_description.getSettings().setMinimumFontSize(40);
        wv_description.loadUrl(Urls.BASEURL+"productDetail.php?product_id=" + strProductId);
        Log.e("dsfdsf","fffff: "+Urls.BASEURL+"productDetail.php?product_id=" + strProductId);

        JsonObject json = new JsonObject();
        json.addProperty("product_id", strProductId);
        json.addProperty("user_id", SessionStore.getUserDetails(ProductDetailActivity.this, Common.USER_PREFS_NAME).get(SessionStore.USER_ID));
        json.addProperty("token", SessionStore.getUserDetails(ProductDetailActivity.this, Common.USER_PREFS_NAME).get(SessionStore.USER_TOKEN));

//        json.addProperty("width", getResources().getDimension(R.dimen.home_allproduct_row_width));
//        json.addProperty("height", getResources().getDimension(R.dimen.home_allproduct_row_image_height));


        ServerCalling.ServerCallingProductsApiPost(ProductDetailActivity.this, "getProductDetail", json, this);
    }


    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {

        try {

            WaitDialog.hideDialog();

            if (strfFrom.trim().equalsIgnoreCase("getProductDetail")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {


                    JSONObject dataObj = result.getJSONObject("data");

                    setData(dataObj);

                } else {

                    Methods.showToast(ProductDetailActivity.this, result.getString("msg"));

                    Log.e("HomeFragment", "ServerCallBackSuccess attemptTOGetUserInfo log: " + result.getString("msg"));
                }
            } else if (strfFrom.trim().equalsIgnoreCase("addToWishlist")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {

                    Methods.showToast(mContext, result.getString("msg"));
                    mProductDetailModel.is_wishlist = "1";
                    iv_wishList.setImageResource(R.drawable.like_active_large);

                } else {

                    Methods.showToast(mContext, result.getString("msg"));
                    Log.e("AllProductListAdapter", "error AllProductListAdapter log: " + result.getString("msg"));
                }
            } else if (strfFrom.trim().equalsIgnoreCase("removeItemWishlist")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {

                    Methods.showToast(mContext, result.getString("msg"));
                    mProductDetailModel.is_wishlist = "0";
                    iv_wishList.setImageResource(R.drawable.like_inactive_large);

                } else {

                    Methods.showToast(mContext, result.getString("msg"));
                    Log.e("AllProductListAdapter", "error AllProductListAdapter log: " + result.getString("msg"));
                }
            } else if (strfFrom.trim().equalsIgnoreCase("checkServiceAvailability")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {

                    // SetPin callback
                    JSONObject jData = result.getJSONObject("data");
                    setPinCode(jData.getString("pincode").trim(), jData.getString("pincode_address"));

                } else {

                    Methods.showToast(mContext, result.getString("msg"));
                    Log.e("AllProductListAdapter", "error AllProductListAdapter log: " + result.getString("msg"));
                }
            } else if (strfFrom.trim().equalsIgnoreCase("addToCart")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {
                    Log.e("AllProductListAdapter", "error Address log: " + SessionStore.getUserDetails(ProductDetailActivity.this, Common.USER_PREFS_NAME).get(SessionStore.USER_ADDRESS_ID));
                    if (buynow) {
                        buynow = false;
                        JSONObject quoteId = result.getJSONObject("data");

                      /*  if (SessionStore.getUserDetails(ProductDetailActivity.this, Common.USER_PREFS_NAME).get(SessionStore.USER_ADDRESSID) == null || SessionStore.getUserDetails(ProductDetailActivity.this, Common.USER_PREFS_NAME).get(SessionStore.USER_ADDRESSID).equalsIgnoreCase("")) {

                            intent = new Intent(ProductDetailActivity.this, AddAddressActivity.class);


                        } else*/

                        {
                            String address=SessionStore.getUserDetails(this, Common.USER_PREFS_NAME).get(SessionStore.USER_STREET_ONE)+" "+SessionStore.getUserDetails(this, Common.USER_PREFS_NAME).get(SessionStore.USER_STREET_TWO)+" "+SessionStore.getUserDetails(this, Common.USER_PREFS_NAME).get(SessionStore.USER_CITY)+" "+SessionStore.getUserDetails(this, Common.USER_PREFS_NAME).get(SessionStore.USER_REGION)+" "+SessionStore.getUserDetails(this, Common.USER_PREFS_NAME).get(SessionStore.USER_POSTCODE);
                            intent = new Intent(ProductDetailActivity.this, ShippingAddressActivity.class);
                            intent.putExtra("AddressName", SessionStore.getUserDetails(ProductDetailActivity.this, Common.USER_PREFS_NAME).get(SessionStore.USER_STREET_ONE)+SessionStore.getUserDetails(ProductDetailActivity.this, Common.USER_PREFS_NAME).get(SessionStore.USER_STREET_TWO));
                            intent.putExtra("AddressId", SessionStore.getUserDetails(ProductDetailActivity.this, Common.USER_PREFS_NAME).get(SessionStore.USER_ADDRESS_ID));
                            intent.putExtra("Address", address);


//                            Log.e("AllProductListAdapter", "Address " + SessionStore.getUserDetails(ProductDetailActivity.this, Common.USER_PREFS_NAME).get(SessionStore.USER_ADDRESS));
//                            Log.e("AllProductListAdapter", "Address " + SessionStore.getUserDetails(ProductDetailActivity.this, Common.USER_PREFS_NAME).get(SessionStore.USER_ADDRESSID));
//                            Log.e("AllProductListAdapter", "Address " + SessionStore.getUserDetails(ProductDetailActivity.this, Common.USER_PREFS_NAME).get(SessionStore.USER_ADDRESSNAME));

                        }
                        intent.putExtra("ProductId", mProductDetailModel.product_id);
                        intent.putExtra("From", "Detail");
                        intent.putExtra("ProductName", mProductDetailModel.product_name);

                        ShippingAddressActivity.itemList.clear();

                        /*ShippingAddressActivity.itemList.add(new CartModel(mProductDetailModel.product_id, "1", mProductDetailModel.product_name, mProductDetailModel.product_price,
                                mProductDetailModel.image, "", "1", quoteId.getString("quote_id"),
                                "1", mProductDetailModel.is_wishlist));*/


                        startActivity(intent);


                    } else {

                        Methods.showToast(mContext, result.getString("msg"));
                        mProductDetailModel.is_add_to_cart = "1";

                    }

                } else {

                    Methods.showToast(mContext, result.getString("msg"));
                    Log.e("AllProductListAdapter", "error AllProductListAdapter log: " + result.getString("msg"));
                }
            }

        } catch (Exception ee) {

            Log.e("Login", "Login User Exception: " + ee.getMessage());
        }

    }


    //********************** onClick **************************
    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {

            case R.id.ProductDetail_WriteReview:

                intent = new Intent(ProductDetailActivity.this, WriteReviewActivity.class);
                intent.putExtra("ProductId", strProductId);
                intent.putExtra("ProductName", mProductDetailModel.product_name);
                intent.putExtra("ProductImage", mProductDetailModel.image);
                startActivity(intent);


                break;

            case R.id.Header_Menu:

                onBackPressed();

                break;

            case R.id.ProductDetail_DelieveryPinLayout:


                if(mProductDetailModel.pincode.trim().equalsIgnoreCase("")){


                    pinPopupShow();

                }else {


                    tv_DeliveryLeftPin.setVisibility(View.VISIBLE);
                    tv_DeliveryRightPin.setVisibility(View.GONE);
                    tv_DrliveryPinChange.setVisibility(View.VISIBLE);
                    ll_deliveryDetaillayout.setVisibility(View.VISIBLE);


                    String ss = getResources().getString(R.string.delivery_option_for)+" "+mProductDetailModel.pincode.trim();

                    SpannableString s= new SpannableString(ss);
                    s.setSpan(new ForegroundColorSpan((getResources().getColor(R.color.termsandcondition_color))), getResources().getString(R.string.delivery_option_for).length(), ss.trim().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    tv_DeliveryPinText.setText(R.string.delivery_option_for);

                    tv_DeliveryDetail.setText(mProductDetailModel.pincodeaddress);
                }

                break;


            case R.id.ProductDetail_DelieveryChangeMark:
                pinPopupShow();

                break;

            case R.id.ProductDetail_favourite:

                JsonObject json = new JsonObject();
                json.addProperty("user_id", SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_ID));
                json.addProperty("token", SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_TOKEN));
                json.addProperty("product_id", mProductDetailModel.product_id);

                WaitDialog.showDialog(this);

                if (mProductDetailModel.is_wishlist.equalsIgnoreCase("0")) {

                    ServerCalling.ServerCallingProductsApiPost(mContext, "addToWishlist", json, ProductDetailActivity.this);

                } else {

                    ServerCalling.ServerCallingProductsApiPost(mContext, "removeItemWishlist", json, ProductDetailActivity.this);

                }

                break;

            case R.id.ProductDetail_AddToCart:

                try {




                    WaitDialog.showDialog(this);
                    JsonObject json1 = new JsonObject();
                    json1.addProperty("user_id", SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_ID));
                    json1.addProperty("token", SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_TOKEN));
                    json1.addProperty("product_id", mProductDetailModel.product_id);
                    json1.addProperty("qty", 1);
                    json1.addProperty("quote_id", "");
                    ServerCalling.ServerCallingProductsApiPost(mContext, "addToCart", json1, ProductDetailActivity.this);




                } catch (Exception e) {


                    Log.e("AllProductListAdapter", "Exception AllProductListAdapter: " + e.getMessage());
                }

                break;

            case R.id.ProductDetail_BuyNow:


                try {


                    if (SessionStore.getUserDetails(ProductDetailActivity.this, Common.USER_PREFS_NAME).get(SessionStore.USER_ID)==null ||
                            SessionStore.getUserDetails(ProductDetailActivity.this, Common.USER_PREFS_NAME).get(SessionStore.USER_ID).isEmpty() ){
                        intent = new Intent(ProductDetailActivity.this, LoginActivityNew.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);

                    }else {
                        buynow = true;
                        WaitDialog.showDialog(this);
                        JsonObject json1 = new JsonObject();
                        json1.addProperty("user_id", SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_ID));
                        json1.addProperty("token", SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_TOKEN));
                        json1.addProperty("product_id", mProductDetailModel.product_id);
                        json1.addProperty("qty", 1);
                        json1.addProperty("quote_id", "");
                        json1.addProperty("buy_status", 1);
                        ServerCalling.ServerCallingProductsApiPost(ProductDetailActivity.this, "addToCart", json1, ProductDetailActivity.this);
                    }

                } catch (Exception e) {


                    Log.e("AllProductListAdapter", "Exception AllProductListAdapter: " + e.getMessage());
                }


                break;

            case R.id.ProductDetail_Image:

                FullImageScreen_Activity.mediaList = mProductDetailModel.mediaList;
                intent = new Intent(ProductDetailActivity.this, FullImageScreen_Activity.class);
                startActivity(intent);

                break;


        }


    }


    private void pinPopupShow() {

        UbuntuMediumTextView tv_Cancel, tv_Done;
        final UbuntuRegularEditText et_pin;

        LayoutInflater layoutInflater = (LayoutInflater) ProductDetailActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.pincode_popup, null);

        tv_Cancel = (UbuntuMediumTextView) view.findViewById(R.id.Pincode_Popup_Cancel);
        tv_Done = (UbuntuMediumTextView) view.findViewById(R.id.Pincode_Popup_Done);

        et_pin = (UbuntuRegularEditText) view.findViewById(R.id.Pincode_Popup_Pin);


        final Dialog dialog = new Dialog(ProductDetailActivity.this, R.style.PauseDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(view);
        dialog.setCancelable(false);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;

        dialog.getWindow().setAttributes(lp);


        tv_Done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if(et_pin.getText().toString().trim().equalsIgnoreCase("")){

                    Methods.showToast(ProductDetailActivity.this, getString(R.string.please_enter_pincode));
                    return;
                }


                attemptSetPinToServer(et_pin.getText().toString().trim());
                dialog.dismiss();
            }
        });


        tv_Cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                dialog.dismiss();
            }
        });

        dialog.show();

    }


    private void setPinCode(String status, String message) {
        try {





              /*  if(mProductDetailModel.pincode.trim().equalsIgnoreCase("")){

                    tv_DeliveryLeftPin.setVisibility(View.GONE);
                    tv_DeliveryRightPin.setVisibility(View.VISIBLE);
                    tv_DrliveryPinChange.setVisibility(View.GONE);
                    ll_deliveryDetaillayout.setVisibility(View.GONE);
                    tv_DeliveryPinText.setText(getResources().getString(R.string.provide_your_pincode_for_details));

                }else*/ {


                mProductDetailModel.pincode = status;
                mProductDetailModel.pincodeaddress = message;


                tv_DeliveryLeftPin.setVisibility(View.VISIBLE);
                tv_DeliveryRightPin.setVisibility(View.GONE);
                tv_DrliveryPinChange.setVisibility(View.VISIBLE);
                ll_deliveryDetaillayout.setVisibility(View.VISIBLE);


                String ss = getResources().getString(R.string.delivery_option_for)+" "+status;

                SpannableString s= new SpannableString(ss);
                s.setSpan(new ForegroundColorSpan((getResources().getColor(R.color.termsandcondition_color))), getResources().getString(R.string.delivery_option_for).length(), ss.trim().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                tv_DeliveryPinText.setText(s);

                tv_DeliveryDetail.setText(message);
            }






        }catch (Exception e){

        }
    }


    private void attemptSetPinToServer(String pinCode) {
        try {

            WaitDialog.showDialog(this);


            JsonObject json = new JsonObject();
            json.addProperty("pincode", pinCode);
            json.addProperty("user_id", SessionStore.getUserDetails(ProductDetailActivity.this, Common.USER_PREFS_NAME).get(SessionStore.USER_ID));
            json.addProperty("token", SessionStore.getUserDetails(ProductDetailActivity.this, Common.USER_PREFS_NAME).get(SessionStore.USER_TOKEN));

//            { "pincode":"452003", "user_id" : "", "token" : ""}

            ServerCalling.ServerCallingProductsApiPost(ProductDetailActivity.this, "checkServiceAvailability", json, this);


        }catch (Exception e){

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.move_in_right, R.anim.move_out_right);
        finish();
    }
}
