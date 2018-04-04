package com.mohi.in.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

@SuppressLint("NewApi")
public class UbuntuMediumTextView extends android.support.v7.widget.AppCompatTextView{

	 public UbuntuMediumTextView(Context context, AttributeSet attrs, int defStyle) {
	        super(context, attrs, defStyle);
	        init();
	    }

	    public UbuntuMediumTextView(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        init();
	    }

	    public UbuntuMediumTextView(Context context, boolean flag) {
	        super(context);
	        	init();
	    }

	    private void init() {
	        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),"font/Ubuntu-M.ttf");
	        setTypeface(tf);
	    }
	    

}
