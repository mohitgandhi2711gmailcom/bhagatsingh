package com.mohi.in.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

@SuppressLint("NewApi")
public class CenturyGothicRegularEditText extends android.support.v7.widget.AppCompatEditText {

	 public CenturyGothicRegularEditText(Context context, AttributeSet attrs, int defStyle) {
	        super(context, attrs, defStyle);
	        init();
	    }

	    public CenturyGothicRegularEditText(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        init();
	    }

	    public CenturyGothicRegularEditText(Context context, boolean flag) {
	        super(context);
	        	init();
	    }

	    private void init() {
	        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"font/century_gothic_regular.ttf");
	        setTypeface(tf);
	    }
	    

}
