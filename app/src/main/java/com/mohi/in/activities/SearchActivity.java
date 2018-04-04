package com.mohi.in.activities;

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

    private UbuntuLightEditText et_search;
    private ImageView iv_back;
    Intent intent;
    private String strsearch = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        intent = getIntent();

        if(intent.getStringExtra("Search")!=null){

            strsearch = intent.getStringExtra("Search");

        }



        init();
    }


    private void init(){

        et_search = (UbuntuLightEditText)findViewById(R.id.Search_Search);

        iv_back = (ImageView)findViewById(R.id.Search_Back);

        setValue();
    }


    private void setValue(){

        et_search.setText(strsearch.trim());
        et_search.setSelection(strsearch.trim().length());


        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.search || id == EditorInfo.IME_ACTION_SEARCH) {

                    attemptSearchProduct();
                    return true;
                }
                return false;
            }
        });


        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

    }

    private void attemptSearchProduct(){

        Log.e("sdfdsfds","test");

        String searchValue = et_search.getText().toString();
        boolean cancel = false;
        if (TextUtils.isEmpty(searchValue)) {

            cancel = true;
        }


        if(cancel){


            Methods.showToast(SearchActivity.this, "Please enter value");

        }else {

            Intent intent = new Intent(SearchActivity.this, AllProductsListActivity.class);
            intent.putExtra("Type", "Search");
            intent.putExtra("Value", searchValue);
            startActivity(intent);
            if (!strsearch.trim().equalsIgnoreCase(""))
            finish();


        }


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

}
