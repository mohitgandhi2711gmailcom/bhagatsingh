package com.mohi.in.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.mohi.in.R;
import com.mohi.in.utils.Methods;
import com.mohi.in.widgets.UbuntuLightEditText;

public class SearchActivity extends AppCompatActivity {

    private UbuntuLightEditText searchEditText;
    private ImageView backBtnImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        init();
    }

    private void init(){
        searchEditText = findViewById(R.id.Search_Search);
        backBtnImageView = findViewById(R.id.Search_Back);
        setValue();
    }

    private void setValue(){
        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.search || id == EditorInfo.IME_ACTION_SEARCH) {
                    attemptSearchProduct();
                    return true;
                }
                return false;
            }
        });
        backBtnImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void attemptSearchProduct(){
        String searchValue = searchEditText.getText().toString();
        if (TextUtils.isEmpty(searchValue)) {
            Methods.showToast(SearchActivity.this, "Please enter value");
        } else {
            /*Intent searchIntent = new Intent("com.android.settings.TTS_SETTINGS");
            searchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(searchIntent);*/

            Intent searchIntent=new Intent(this,AllProductsListActivity.class);
            searchIntent.putExtra("action",searchValue);
            startActivity(searchIntent);
            finish();
        }
    }
}
