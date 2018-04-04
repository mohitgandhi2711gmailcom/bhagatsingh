package com.mohi.in.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

@SuppressLint("NewApi")
public class CenturyGothicRegularTextView extends android.support.v7.widget.AppCompatTextView {

	 public CenturyGothicRegularTextView(Context context, AttributeSet attrs, int defStyle) {
	        super(context, attrs, defStyle);
	        init();
	    }

	    public CenturyGothicRegularTextView(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        init();
	    }

	    public CenturyGothicRegularTextView(Context context, boolean flag) {
	        super(context);
	        	init();
	    }

	    private void init() {
	        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"font/century_gothic_regular.ttf");
	        setTypeface(tf);
	    }
	    

}
