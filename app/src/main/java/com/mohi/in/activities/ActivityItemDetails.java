package com.mohi.in.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
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
import com.mohi.in.model.AvailableOptions;
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
    private TextView descriptionTitle;
    private TextView description;
    private TextView moreExtraInfoTextView;
    private TextView deliveryStatusTextView;

    private EditText pinCodeEditText;
    private ImageView favoriteImageView;
    private ItemDetailsSimilarItemAdapter viewMoreAdapter;
    private ScrollView parentScrollView;
    private EditText selectCountryEditText;
    private LinearLayout parentLinearLayout;
    private LinearLayout llPrice;
    private String isWishList;
    private static final String PRODUCT_ID = "product_id";
    private static final String USER_ID = "user_id";
    private static final String TOKEN = "token";
    private static final String STATUS = "status";
    private String isAddToCart;
    private ArrayList<TextView> sizeList;
    SpannableString link_less, link_more;


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
        descriptionTitle = findViewById(R.id.description_title);
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
        llPrice = findViewById(R.id.ll_price);
        cartButton.setOnClickListener(this);
        pinCodeEditText.setOnClickListener(this);
        pinCodeEditText.clearFocus();
        selectCountryEditText.setOnClickListener(this);
        favoriteImageView.setOnClickListener(this);
        selectCountryEditText.clearFocus();
        RecyclerView viewSimilarRecyclerView = findViewById(R.id.view_similar_rv);
        moreInfoTextView.setOnClickListener(this);
        if (getIntent() != null) {
            productId = getIntent().getStringExtra("ProductId");
            isWishList = getIntent().getStringExtra("isWishList");
        }

        imagesAdapter = new ItemDetailsAdapter(mContext);
        imagesRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        imagesRecyclerView.setAdapter(imagesAdapter);

        viewMoreAdapter = new ItemDetailsSimilarItemAdapter(mContext);
        viewSimilarRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        viewSimilarRecyclerView.setAdapter(viewMoreAdapter);

        attemptGetProductDetail();
        Log.d("BeforeAPISize", llPrice.getHeight()+"");

    }

    private SpannableString makeLinkSpan(CharSequence text, View.OnClickListener listener) {
        SpannableString link = new SpannableString(text);
        link.setSpan(new ClickableString(listener), 0, text.length(),
                SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        return link;
    }

    private void attemptGetProductDetail() {
        JsonObject json = new JsonObject();
        json.addProperty(USER_ID, SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_ID));
        json.addProperty(PRODUCT_ID, productId);
        json.addProperty("pincode", SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_POSTCODE));
        json.addProperty("country", SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.COUNTRY_CODE));
        //json.addProperty(TOKEN, SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_TOKEN));
        ServerCalling.ServerCallingProductsApiPost(mContext, "getProductDetail", json, this);
    }

    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        try {
            Log.d("BeforeAPISize", llPrice.getHeight()+"");
            WaitDialog.hideDialog();
            if (strfFrom.trim().equalsIgnoreCase("getProductDetail")) {
                if (result.getString(STATUS).trim().equalsIgnoreCase("success")) {
                    JSONObject dataObj = result.getJSONObject("data");
                    setData(dataObj);
                } else {
                    Methods.showToast(mContext, result.getString("msg"));
                }
            } else if (strfFrom.trim().equalsIgnoreCase("addToCart")) {
//                if (result.getString(STATUS).trim().equalsIgnoreCase("1")) {
                    Methods.showToast(mContext, result.getString("msg"));
                    /*WishListModel model = mList.get(pos);
                    mList.set(pos, new WishListModel(model.product_id, model.product_name, model.image, model.qty, model.category, 1, model.rating, model.price));
                    notifyDataSetChanged();*/
//                }
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
            Log.d("BeforeAPISize", llPrice.getHeight()+"");
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
    }

    private void setData(JSONObject dataObj) {

        productId = dataObj.optString("product_id");
        String productName = dataObj.optString("product_name");
        String productPrice = dataObj.optString("price");
        String productSpecialPrice = dataObj.optString("special_price");
        String sku = dataObj.optString("sku");
        final String productDescription = dataObj.optString("description");
        String stock = dataObj.optString("stock");
        String isWishlist = dataObj.optString("is_wishlist");
        isWishList = isWishlist;
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
        oldPriceTextView.setText(Methods.getTwoDecimalVAlue(productPrice));
        skuNumberTextView.setText(sku);
        newPriceTextView.setText(Methods.getTwoDecimalVAlue(productSpecialPrice));
//        descriptionTitle.setText(productId);
        link_more = makeLinkSpan("More...", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetMoreClickableText(productDescription, link_less, description);
                // respond to click
            }
        });
        link_less = makeLinkSpan("Less...", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetMoreClickableText(productDescription.substring(0, 195), link_more, description);
                // respond to click
            }
        });
        description.setText(productDescription);
        if (productDescription.length() > 200) {
            SetMoreClickableText(productDescription.substring(0, 195), link_more, description);
        } else {
            description.setText(productDescription);
        }
        pinCodeEditText.setText(pincode);
        selectCountryEditText.setText(country);
        deliveryStatusTextView.setText(info.trim());

        JSONArray mediaJsonArray = dataObj.optJSONArray("images");
        ArrayList<MediaModel> mediaList = convertMediaJsonToList(mediaJsonArray);
        imagesAdapter.addData(mediaList, llPrice.getHeight()+getSoftButtonsBarHeight());

        JSONArray relatedProductJsonArray = dataObj.optJSONArray("related");
        ArrayList<RelatedProductModel> relatedProductModelArrayList = convertRealtedProductJsonToList(relatedProductJsonArray);
        viewMoreAdapter.addData(relatedProductModelArrayList);
        parentLinearLayout.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        if (dataObj.has("options")){
            JSONArray options = dataObj.optJSONArray("options");
            ArrayList<AvailableOptions> options1 = getOptions(options);
        }
    }

    @SuppressLint("NewApi")
    private int getSoftButtonsBarHeight() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }
        return 0;
    }

    private ArrayList<RelatedProductModel> convertRealtedProductJsonToList(JSONArray jArray) {
        ArrayList<RelatedProductModel> list = new ArrayList<>();
        if (jArray != null) {
            for (int i = 0; i < jArray.length(); i++) {
                try {
                    RelatedProductModel model = new RelatedProductModel();
                    JSONObject obj = jArray.getJSONObject(i);
                    productId = obj.optString("product_id");
                    String productName = obj.optString("product_name");
                    String image = obj.optString("image");
                    String productPrice = obj.optString("product_price");
                    String specialPrice = obj.optString("product_special_price");
                    String isWishlist = obj.optString("is_wishlist");
                    isAddToCart = obj.optString("is_add_to_cart");
                    model.setProduct_id(productId);
                    model.setProduct_name(productName);
                    model.setImage_url(image);
                    model.setProduct_price(productPrice);
                    model.setSpecialPrice(specialPrice);
                    model.setIs_wishlist(isWishlist);
                    model.setIs_add_to_cart(isAddToCart);
                    list.add(model);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    private ArrayList<AvailableOptions> getOptions(JSONArray jsonArray){
        ArrayList<AvailableOptions> list = new ArrayList<>();
        if (jsonArray!=null){
            TextView tvSize = findViewById(R.id.tv_size);
            tvSize.setVisibility(View.VISIBLE);
            LinearLayout sizeChart = findViewById(R.id.size_chart);
            sizeList = new ArrayList<>();
            for (int i = 0; i< jsonArray.length(); i++){
                try {
                    JSONObject object = jsonArray.getJSONObject(i);
                    AvailableOptions model = new AvailableOptions();
                    model.setAttributeCode(object.optString("attribute_code"));
                    model.setOptionId(object.optInt("options_id"));
                    model.setOptionLabel(object.optString("option_label"));
                    model.setOptionValue(object.optString("option_value"));
                    TextView tv = new TextView(this);
                    tv.setId(model.getOptionId());
                    tv.setBackground(getResources().getDrawable(R.drawable.circle_shape));
                    tv.setText(model.getOptionLabel());
                    tv.setGravity(Gravity.CENTER);
                    tv.setTag(model);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(10,0,10,0);
                    params.gravity = Gravity.CENTER;
                    tv.setLayoutParams(params);
                    sizeChart.addView(tv);
                    sizeList.add(tv);

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
                MediaModel model = new MediaModel();
                model.setImageUrl(jArray.optString(i));
                list.add(model);
            }
        }
        return list;
    }

    public void SetMoreClickableText(String strContent, SpannableString linkedString, TextView tv) {
        tv.setText(strContent);
        tv.append("  ");
        tv.append(linkedString);
        makeLinksFocusable(tv);
    }

    private void makeLinksFocusable(TextView tv) {
        MovementMethod m = tv.getMovementMethod();
        if ((m == null) || !(m instanceof LinkMovementMethod)) {
            if (tv.getLinksClickable()) {
                tv.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
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
            default:
                Methods.showToast(mContext, "Error..");
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
        json.addProperty("sku", skuNumberTextView.getText().toString().trim());
        if (isAddToCart.equalsIgnoreCase("0")) {
            json.addProperty("qty", 1);
        } else {
            json.addProperty("qty", Integer.parseInt(isAddToCart) + 1);
        }
        WaitDialog.showDialog(mContext);
        ServerCalling.ServerCallingProductsApiPost(mContext, "addToCart", json, ActivityItemDetails.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("BeforeAPISize Resume", llPrice.getHeight()+"");
    }

    /*
     * ClickableString class
     */

    private static class ClickableString extends ClickableSpan {
        private View.OnClickListener mListener;

        public ClickableString(View.OnClickListener listener) {
            mListener = listener;
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }
    }
}