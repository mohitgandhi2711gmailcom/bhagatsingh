package com.mohi.in.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.mohi.in.R;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.model.MediaModel;
import com.mohi.in.model.RelatedProductModel;
import com.mohi.in.ui.adapter.ItemDetailsAdapter;
import com.mohi.in.ui.adapter.ItemDetailsSimilarItemAdapter;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;
import com.mohi.in.utils.listeners.ServerCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityItemDetails extends AppCompatActivity implements ServerCallBack, View.OnClickListener {
    private String productId;
    private Context mContext;
    private ItemDetailsAdapter imagesAdapter;
    private TextView itemTitleTextView;
    private TextView oldPriceTextView;
    private TextView skuNumberTextView;
    private TextView newPriceTextView;
    private TextView modelNumberTextView;
    private TextView description;
    private TextView moreExtraInfoTextView;
    private TextView deliveryStatusTextView;
    private EditText pinCodeEditText;
    private ImageView favoriteImageView;
    private ItemDetailsSimilarItemAdapter viewMoreAdapter;
    private ScrollView parentScrollView;
    private EditText selectCountryEditText;
    private LinearLayout parentLinearLayout;
    private String isWishList;
    private static final String PRODUCT_ID = "product_id";
    private static final String USER_ID = "user_id";
    private static final String TOKEN = "token";
    private static final String STATUS = "status";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        init();
    }

    private void init() {
        mContext = this;
        WaitDialog.showDialog(this);
        itemTitleTextView = findViewById(R.id.item_title);
        oldPriceTextView = findViewById(R.id.old_price_tv);
        skuNumberTextView = findViewById(R.id.sku_number_tv);
        newPriceTextView = findViewById(R.id.new_price_tv);
        modelNumberTextView = findViewById(R.id.model_no_tv);
        description = findViewById(R.id.description);
        TextView moreInfoTextView = findViewById(R.id.more_info_tv);
        moreExtraInfoTextView = findViewById(R.id.more_extra_info_tv);
        deliveryStatusTextView = findViewById(R.id.delivery_status_tv);
        pinCodeEditText = findViewById(R.id.pincode_et);
        favoriteImageView = findViewById(R.id.favorite_iv);
        parentScrollView = findViewById(R.id.parent_sv);
        selectCountryEditText = findViewById(R.id.select_country_et);
        RecyclerView imagesRecyclerView = findViewById(R.id.images_rv);
        Button cartButton = findViewById(R.id.cart_btn);
        parentLinearLayout = findViewById(R.id.parent_ll);
        cartButton.setOnClickListener(this);
        pinCodeEditText.setOnClickListener(this);
        pinCodeEditText.clearFocus();
        selectCountryEditText.setOnClickListener(this);
        favoriteImageView.setOnClickListener(this);
        selectCountryEditText.clearFocus();
        RecyclerView viewSimilarRecyclerView = findViewById(R.id.view_similar_rv);
        moreInfoTextView.setOnClickListener(this);
        if (getIntent() != null) {
            productId = getIntent().getStringExtra("productId");
            isWishList = getIntent().getStringExtra("isWishList");
        }

        imagesAdapter = new ItemDetailsAdapter(mContext);
        imagesRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        imagesRecyclerView.setAdapter(imagesAdapter);

        viewMoreAdapter = new ItemDetailsSimilarItemAdapter(mContext);
        viewSimilarRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        viewSimilarRecyclerView.setAdapter(viewMoreAdapter);

        attemptGetProductDetail();
    }

    private void attemptGetProductDetail() {
        JsonObject json = new JsonObject();
        json.addProperty(PRODUCT_ID, productId);
        json.addProperty(USER_ID, SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_ID));
        json.addProperty(TOKEN, SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_TOKEN));
        ServerCalling.ServerCallingProductsApiPost(mContext, "getProductDetail", json, this);
    }

    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        try {
            WaitDialog.hideDialog();
            if (strfFrom.trim().equalsIgnoreCase("getProductDetail")) {
                if (result.getString(STATUS).trim().equalsIgnoreCase("success")) {
                    JSONObject dataObj = result.getJSONObject("data");
                    setData(dataObj);
                } else {
                    Methods.showToast(mContext, result.getString("msg"));
                }
            } else if (strfFrom.trim().equalsIgnoreCase("addToCart")) {
                if (result.getString(STATUS).trim().equalsIgnoreCase("1")) {
                    Methods.showToast(mContext, result.getString("msg"));
                    /*WishListModel model = mList.get(pos);
                    mList.set(pos, new WishListModel(model.product_id, model.product_name, model.image, model.qty, model.category, 1, model.rating, model.price));
                    notifyDataSetChanged();*/
                }
            } else if (strfFrom.trim().equalsIgnoreCase("addToWishlist")) {
                if (result.getString(STATUS).trim().equalsIgnoreCase("1")) {
                    isWishList = "1";
                    favoriteImageView.setImageResource(R.drawable.ic_love_fill);
                } else {
                    Methods.showToast(mContext, result.getString("msg"));
                }
            } else if (strfFrom.trim().equalsIgnoreCase("removeItemWishlist")) {
                if (result.getString(STATUS).trim().equalsIgnoreCase("1")) {
                    Methods.showToast(mContext, result.getString("msg"));
                    isWishList = "0";
                    favoriteImageView.setImageResource(R.drawable.ic_love_like);
                } else {
                    Methods.showToast(mContext, result.getString("msg"));
                }
            }
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
    }

    private void setData(JSONObject dataObj) {
        String productId = dataObj.optString("product_id");
        String productName = dataObj.optString("product_name");
        String productPrice = dataObj.optString("price");
        String productSpecialPrice = dataObj.optString("special_price");
        String productDescription = dataObj.optString("description");
        String stock = dataObj.optString("stock");
        String isWishlist = dataObj.optString("is_wishlist");
        String isAddToCart = dataObj.optString("is_add_to_cart");
        String brand = dataObj.optString("brand");
        JSONObject deliveryObject = dataObj.optJSONObject("delivery");
        String pincode = deliveryObject.optString("pincode");
        String country = deliveryObject.optString("country");
        String info = deliveryObject.optString("info");

        if (isWishlist.trim().equalsIgnoreCase("0")) {
            favoriteImageView.setImageResource(R.drawable.ic_love_like);
        } else {
            favoriteImageView.setImageResource(R.drawable.ic_love_fill);
        }
        itemTitleTextView.setText(productName);
        oldPriceTextView.setVisibility(View.INVISIBLE);
        skuNumberTextView.setText(productId);
        newPriceTextView.setText(Methods.getTwoDecimalVAlue(productPrice));
        modelNumberTextView.setText(productId);
        description.setText(productDescription);
        pinCodeEditText.setText(pincode);
        //deliveryStatusTextView.setText(pincode_address);

        JSONArray mediaJsonArray = dataObj.optJSONArray("media");
        ArrayList<MediaModel> mediaList = convertMediaJsonToList(mediaJsonArray);
        imagesAdapter.addData(mediaList);

        JSONArray relatedProductJsonArray = dataObj.optJSONArray("related_products");
        ArrayList<RelatedProductModel> relatedProductModelArrayList = convertRealtedProductJsonToList(relatedProductJsonArray);
        viewMoreAdapter.addData(relatedProductModelArrayList);

        parentLinearLayout.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
    }

    private ArrayList<RelatedProductModel> convertRealtedProductJsonToList(JSONArray jArray) {
        ArrayList<RelatedProductModel> list = new ArrayList<>();
        if (jArray != null) {
            for (int i = 0; i < jArray.length(); i++) {
                try {
                    RelatedProductModel model = new RelatedProductModel();
                    JSONObject obj = jArray.getJSONObject(i);
                    String product_id = obj.optString("product_id");
                    String product_name = obj.optString("product_name");
                    String image = obj.optString("image");
                    String product_price = obj.optString("product_price");
                    String new_price = obj.optString("new_price");
                    String date_from = obj.optString("date_from");
                    String date_to = obj.optString("date_to");
                    String is_wishlist = obj.optString("is_wishlist");
                    String rating = obj.optString("rating");
                    String is_add_to_cart = obj.optString("is_add_to_cart");
                    String stock = obj.optString("stock");
                    String is_product = obj.optString("is_product");
                    String type = obj.optString("type");
                    model.setProduct_id(product_id);
                    model.setProduct_name(product_name);
                    model.setImage_url(image);
                    model.setProduct_price(product_price);
                    model.setNew_price(new_price);
                    model.setDate_from(date_from);
                    model.setDate_to(date_to);
                    model.setIs_wishlist(is_wishlist);
                    model.setRating(rating);
                    model.setIs_add_to_cart(is_add_to_cart);
                    model.setStock(stock);
                    model.setIs_product(is_product);
                    model.setType(type);
                    list.add(model);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    private ArrayList<MediaModel> convertMediaJsonToList(JSONArray jArray) {
        ArrayList<MediaModel> list = new ArrayList<>();
        if (jArray != null) {
            for (int i = 0; i < jArray.length(); i++) {
                try {
                    MediaModel model = new MediaModel();
                    JSONObject obj = jArray.getJSONObject(i);
                    String media_id = obj.optString("media_id");
                    String thumbnail_url = obj.optString("thumbnail");
                    String base_url = obj.optString("base");
                    model.setMedia_id(media_id);
                    model.setThumbnail_url(thumbnail_url);
                    model.setBase_url(base_url);
                    list.add(model);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_info_tv:
                focusOnView(moreExtraInfoTextView);
                break;
            case R.id.cart_btn:
                attemptAddToCart();
                break;
            case R.id.select_country_et:
                selectCountryEditText.requestFocus();
                break;
            case R.id.pincode_et:
                selectCountryEditText.requestFocus();
                break;
            case R.id.favorite_iv:
                attemptAddToWishlist();
                break;
        }
    }

    private void attemptAddToWishlist() {
        JsonObject json = new JsonObject();
        json.addProperty("user_id", SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_ID));
        json.addProperty("token", SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_TOKEN));
        json.addProperty("product_id", productId);
        WaitDialog.showDialog(this);
        if (isWishList.equalsIgnoreCase("0")) {
            ServerCalling.ServerCallingProductsApiPost(mContext, "addToWishlist", json, ActivityItemDetails.this);
        } else {
            ServerCalling.ServerCallingProductsApiPost(mContext, "removeItemWishlist", json, ActivityItemDetails.this);
        }
    }

    private void focusOnView(final View view) {
        parentScrollView.post(new Runnable() {
            @Override
            public void run() {
                parentScrollView.scrollTo(0, view.getTop());
            }
        });
    }

    private void attemptAddToCart() {
        JsonObject json = new JsonObject();
        json.addProperty("user_id", SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_ID));
        json.addProperty("token", SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_TOKEN));
        json.addProperty("product_id", productId);
        json.addProperty("qty", 1);
        json.addProperty("quote_id", "");
        WaitDialog.showDialog(mContext);
        ServerCalling.ServerCallingProductsApiPost(mContext, "addToCart", json, ActivityItemDetails.this);
    }

}