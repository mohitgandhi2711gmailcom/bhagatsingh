package com.mohi.in.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.bumptech.glide.Glide;
import com.koushikdutta.ion.Ion;
import com.mohi.in.R;
import com.mohi.in.model.ProductImgModel;
import com.mohi.in.widgets.TouchImageView;

/**
 * Created by etiloxadmin on 4/9/15.
 */
public class FullImageScreenviewPager_Adapter extends PagerAdapter {
	Matrix matrix = new Matrix();
	Matrix savedMatrix = new Matrix();
	PointF startPoint = new PointF();
	PointF midPoint = new PointF();
	float oldDist = 1f;
	static final int NONE = 0;
	static final int DRAG = 1;
	static final int ZOOM = 2;
	int mode = NONE;


	Context context;

	private List<ProductImgModel> imageList = new ArrayList<ProductImgModel>();


	public FullImageScreenviewPager_Adapter(Context act,  List<ProductImgModel> imageList) {
		this.imageList = imageList;
		context = act;

	}

	public int getCount() {
		return imageList.size();
	}

	public Object instantiateItem(ViewGroup collection, final int position) {
		ProductImgModel modelObject = imageList.get(position);
		TouchImageView img = new TouchImageView(collection.getContext());



		Glide.with(context)
				.load(modelObject.image)
				.into(img);
        
		/* BitmapFactory.Options options = new BitmapFactory.Options();
		 options.inSampleSize = 2;

		 Log.e("sdsadsa", "Path image: "+imageList.get(position).getUrl());
		 
		 Bitmap bmp = BitmapFactory.decodeFile(imageList.get(position).getUrl(),options);
		 img.setImageBitmap(bmp);*/

		collection.addView(img, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		return img;


	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == ((View) arg1);
	}

	@Override
	public Parcelable saveState() {
		return null;
	}
}
