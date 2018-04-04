package com.mohi.in.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

@SuppressLint("NewApi")
public class CenturyGothicBoldTextView extends android.support.v7.widget.AppCompatTextView {

	 public CenturyGothicBoldTextView(Context context, AttributeSet attrs, int defStyle) {
	        super(context, attrs, defStyle);
	        init();
	    }

	    public CenturyGothicBoldTextView(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        init();
	    }

	    public CenturyGothicBoldTextView(Context context, boolean flag) {
	        super(context);
	        	init();
	    }

	    private void init() {
	        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"font/century_gothic_bold.ttf");
	        setTypeface(tf);
	    }
	    

}
