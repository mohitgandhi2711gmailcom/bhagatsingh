package com.mohi.in.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

@SuppressLint("NewApi")
public class UbntuRegularRadioButton extends android.support.v7.widget.AppCompatRadioButton {

	 public UbntuRegularRadioButton(Context context, AttributeSet attrs, int defStyle) {
	        super(context, attrs, defStyle);
	        init();
	    }

	    public UbntuRegularRadioButton(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        init();
	    }

	    public UbntuRegularRadioButton(Context context, boolean flag) {
	        super(context);
	        	init();
	    }

	    private void init() {
	        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"font/Ubuntu-R.ttf");
	        setTypeface(tf);
	    }
	    

}
