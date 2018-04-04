package com.mohi.in.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.mohi.in.utils.ServerCallBack;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityItemDetails extends AppCompatActivity implements ServerCallBack, View.OnClickListener {
    String ProductId;
    private Context mContext;
    private ItemDetailsAdapter imagesAdapter;
    private TextView item_title;
    private TextView old_price_tv;
    private TextView sku_number_tv;
    private TextView new_price_tv;
    private TextView model_no_tv;
    private TextView description;
    private TextView more_extra_info_tv;
    private TextView delivery_status_tv;
    private EditText pincode_et;
    private ImageView favorite_iv;
    private ItemDetailsSimilarItemAdapter viewMoreAdapter;
    private ScrollView parent_sv;
    private EditText select_country_et;
    private LinearLayout parent_ll;
    private String isWishlist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        init();
    }

    private void init() {
        mContext = this;
        WaitDialog.showDialog(this);
        item_title = findViewById(R.id.item_title);
        old_price_tv = findViewById(R.id.old_price_tv);
        sku_number_tv = findViewById(R.id.sku_number_tv);
        new_price_tv = findViewById(R.id.new_price_tv);
        model_no_tv = findViewById(R.id.model_no_tv);
        description = findViewById(R.id.description);
        TextView more_info_tv = findViewById(R.id.more_info_tv);
        more_extra_info_tv = findViewById(R.id.more_extra_info_tv);
        delivery_status_tv = findViewById(R.id.delivery_status_tv);
        pincode_et = findViewById(R.id.pincode_et);
        favorite_iv = findViewById(R.id.favorite_iv);
        parent_sv = findViewById(R.id.parent_sv);
        select_country_et = findViewById(R.id.select_country_et);
        RecyclerView images_rv = findViewById(R.id.related_images_rv);
        Button cart_btn = findViewById(R.id.cart_btn);
        parent_ll = findViewById(R.id.parent_ll);
        cart_btn.setOnClickListener(this);
        pincode_et.setOnClickListener(this);
        pincode_et.clearFocus();
        select_country_et.setOnClickListener(this);
        favorite_iv.setOnClickListener(this);
        select_country_et.clearFocus();
        RecyclerView view_similar_rv = findViewById(R.id.view_similar_rv);
        more_info_tv.setOnClickListener(this);
        if (getIntent() != null) {
            ProductId = getIntent().getStringExtra("ProductId");
            isWishlist = getIntent().getStringExtra("isWishlist");
        }

        imagesAdapter = new ItemDetailsAdapter(mContext);
        images_rv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        images_rv.setAdapter(imagesAdapter);

        viewMoreAdapter = new ItemDetailsSimilarItemAdapter(mContext);
        view_similar_rv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        view_similar_rv.setAdapter(viewMoreAdapter);

        attemptGetProductDetail();
    }

    private void attemptGetProductDetail() {
        JsonObject json = new JsonObject();
        json.addProperty("product_id", ProductId);
        json.addProperty("user_id", SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_ID));
        json.addProperty("token", SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_TOKEN));
        ServerCalling.ServerCallingProductsApiPost(mContext, "getProductDetail", json, this);
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
                    Methods.showToast(mContext, result.getString("msg"));
                }
            } else if (strfFrom.trim().equalsIgnoreCase("addToCart")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {
                    Methods.showToast(mContext, result.getString("msg"));
                    /*WishListModel model = mList.get(pos);
                    mList.set(pos, new WishListModel(model.product_id, model.product_name, model.image, model.qty, model.category, 1, model.rating, model.price));
                    notifyDataSetChanged();*/
                }
            } else if (strfFrom.trim().equalsIgnoreCase("addToWishlist")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {
                    isWishlist = "1";
                    favorite_iv.setImageResource(R.drawable.ic_love_fill);
                } else {
                    Methods.showToast(mContext, result.getString("msg"));
                }
            } else if (strfFrom.trim().equalsIgnoreCase("removeItemWishlist")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {
                    Methods.showToast(mContext, result.getString("msg"));
                    isWishlist = "0";
                    favorite_iv.setImageResource(R.drawable.ic_love_like);
                } else {
                    Methods.showToast(mContext, result.getString("msg"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setData(JSONObject dataObj) {
        String product_id = dataObj.optString("product_id");
        String product_name = dataObj.optString("product_name");
        String image_url = dataObj.optString("image");
        String product_description = dataObj.optString("description").trim();
        String product_price = dataObj.optString("product_price");
        String is_wishlist = dataObj.optString("is_wishlist");
        String rating = dataObj.optString("rating");
        String is_add_to_cart = dataObj.optString("is_add_to_cart");
        String pincode = dataObj.optString("pincode");
        String pincode_address = dataObj.optString("pincode_address");

        if (is_wishlist.trim().equalsIgnoreCase("0")) {
            favorite_iv.setImageResource(R.drawable.ic_love_like);
        } else {
            favorite_iv.setImageResource(R.drawable.ic_love_fill);
        }
        item_title.setText(product_name);
        old_price_tv.setVisibility(View.INVISIBLE);
        sku_number_tv.setText(product_id);
        new_price_tv.setText(Methods.getTwoDecimalVAlue(product_price));
        model_no_tv.setText(product_id);
        description.setText(product_description);
        pincode_et.setText(pincode);
        delivery_status_tv.setText(pincode_address);

        JSONArray mediaJsonArray = dataObj.optJSONArray("media");
        ArrayList<MediaModel> mediaList = convertMediaJsonToList(mediaJsonArray);
        imagesAdapter.addData(mediaList);

        JSONArray relatedProductJsonArray = dataObj.optJSONArray("related_products");
        ArrayList<RelatedProductModel> relatedProductModelArrayList = convertRealtedProductJsonToList(relatedProductJsonArray);
        viewMoreAdapter.addData(relatedProductModelArrayList);

        parent_ll.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
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
                focusOnView(more_extra_info_tv);
                break;
            case R.id.cart_btn:
                attemptAddToCart();
                break;
            case R.id.select_country_et:
                select_country_et.requestFocus();
                break;
            case R.id.pincode_et:
                select_country_et.requestFocus();
                break;
            case R.id.favorite_iv:
                attemptAddToWishlist();
                break;
        }
    }

    private void attemptAddToWishlist() {
        JsonObject json = new JsonObject();
        json.addProperty("user_id", SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_ID));
        json.addProperty("token", SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_TOKEN));
        json.addProperty("product_id", ProductId);
        WaitDialog.showDialog(this);
        if (isWishlist.equalsIgnoreCase("0")) {
            ServerCalling.ServerCallingProductsApiPost(mContext, "addToWishlist", json, ActivityItemDetails.this);
        } else {
            ServerCalling.ServerCallingProductsApiPost(mContext, "removeItemWishlist", json, ActivityItemDetails.this);
        }
    }

    private void focusOnView(final View view) {
        parent_sv.post(new Runnable() {
            @Override
            public void run() {
                parent_sv.scrollTo(0, view.getTop());
            }
        });
    }

    private void attemptAddToCart() {
        JsonObject json = new JsonObject();
        json.addProperty("user_id", SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_ID));
        json.addProperty("token", SessionStore.getUserDetails(mContext, Common.userPrefName).get(SessionStore.USER_TOKEN));
        json.addProperty("product_id", ProductId);
        json.addProperty("qty", 1);
        json.addProperty("quote_id", "");
        WaitDialog.showDialog(mContext);
        ServerCalling.ServerCallingProductsApiPost(mContext, "addToCart", json, ActivityItemDetails.this);
    }

}