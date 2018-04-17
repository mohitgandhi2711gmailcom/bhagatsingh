package com.mohi.in.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.mohi.in.R;
import com.mohi.in.utils.Methods;

public class SplashScreen extends AppCompatActivity {

    private Boolean appOpenedFirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splesh_screen);
        System.setProperty("javax.net.debug", "all");
        System.setProperty("https.protocols", "SSLv3");
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        appOpenedFirst = sharedPref.getBoolean("appOpenedFirst", true);
        if (appOpenedFirst) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("appOpenedFirst", false);
            editor.apply();
            callNextActivity(SliderHomeActivity.class);
        } else {
            callNextActivity(HomeActivity.class);
        }
    }

    private void callNextActivity(final Class name) {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(SplashScreen.this, name);
                startActivity(intent);
                overridePendingTransition(R.anim.move_in_left, R.anim.move_out_left);
                finish();
            }
        }, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            Methods.trimCache(this);
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
    }
}
