package com.mohi.in.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;


import com.mohi.in.R;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by ADMIN14 on 7/7/2017.
 */

public class Image_Picker {

    public static final int CAMERA_REQUEST = 100;
    public static final int GALLERY_REQUEST = 130;
    public static final int REQUEST_ACESS_STORAGE = 3;
    public static final int REQUEST_ACESS_CAMERA = 2;
    public static String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    public static File IMAGE_PATH = null;


    private Uri uri;
    private Context mContext;
    public static final int PERMISSION_ALL = 134;

    public Image_Picker(Context context) {
        mContext = context;
    }


    public void startDialog() {
        AlertDialog.Builder myAlertDilog = new AlertDialog.Builder(mContext);
        myAlertDilog.setTitle("Alert");
        myAlertDilog.setMessage("Allow asked permissions to make this work. Click begin to proceed.");
        myAlertDilog.setPositiveButton("Begin", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkPermission();
            }
        });

        myAlertDilog.show();
    }


    public boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    public void checkPermission() {
        if (!hasPermissions(mContext, PERMISSIONS)) {
            ActivityCompat.requestPermissions((Activity) mContext, PERMISSIONS, PERMISSION_ALL);
        }

    }

    private void openCameraApp() {
        Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


        String file_path = Environment.getExternalStorageDirectory().toString() +
                "/" + mContext.getResources().getString(R.string.app_name);

        File dir = new File(file_path);
        if (!dir.exists())
            dir.mkdirs();
        IMAGE_PATH = new File(dir, mContext.getResources().getString(R.string.app_name) + System.currentTimeMillis() + ".png");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            picIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(mContext, "com.mohi.fileprovider", IMAGE_PATH));
        } else {
            picIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(IMAGE_PATH));
        }

        ((Activity) mContext).startActivityForResult(picIntent, CAMERA_REQUEST);

    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        ((Activity) mContext).startActivityForResult(intent, GALLERY_REQUEST);
    }

    public void imageOptions() {

        final CharSequence[] opsChars = {"Take Picture", "Gallery"};
        new AlertDialog.Builder(mContext)
                .setTitle("Select:")
                .setSingleChoiceItems(opsChars, 0, null)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        // Do something useful withe the position of the selected radio button

                        if (selectedPosition == 0) {
                            openCameraApp();
                        } else if (selectedPosition == 1) {
                            openGallery();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public String getRealPathFromURI(Uri contentURI) {

        String result;
        Cursor cursor = mContext.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }


    public boolean checkURI(Uri contentURI) {

        String result;
        Cursor cursor = mContext.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            return false;
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();

            if (result == null) {
                return false;
            }

            if (new File(result).exists()) {
                // do something if it exists
                return true;
            } else {
                return false;
            }
        }

    }


    public Bitmap getBitmapFromUri(Context context, Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                context.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }


    public static Bitmap decodeBitmapURI(Context context, Uri uri, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        return null;
    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }


    public Uri getImageUrlWithAuthority(Context context, Uri uri) {
        InputStream is = null;
        if (uri.getAuthority() != null) {
            try {
                is = context.getContentResolver().openInputStream(uri);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                return writeToTempImageAndGetPathUri(context, bmp);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public Uri writeToTempImageAndGetPathUri(Context inContext, Bitmap inImage) {
      /*  ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 70, bytes);*/
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }



     /*  onClick of Button Call This


     if (image_picker.hasPermissions(this, Image_Picker.PERMISSIONS)) {
        image_picker.imageOptions();
    } else {
        image_picker.startDialog();
    }*/



/*    onActivity Result and Permission Module



   @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Image_Picker.CAMERA_REQUEST) {

            switch (resultCode) {
                case Activity.RESULT_OK:

                    cropImage(Uri.fromFile(Image_Picker.IMAGE_PATH), imagePickerRequest);

                    break;
                case Activity.RESULT_CANCELED:

                    break;
            }


        } else {

            if (requestCode == Image_Picker.GALLERY_REQUEST) {

                switch (resultCode) {

                    case Activity.RESULT_OK:


                        Uri selectedImageURI = data.getData();
                        try {
                            if (image_picker.checkURI(selectedImageURI)) {
                                cropImage(selectedImageURI, imagePickerRequest);
                            } else {
                                cropImage(image_picker.getImageUrlWithAuthority(this, selectedImageURI), imagePickerRequest);

                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        break;

                    case Activity.RESULT_CANCELED:
                        //         Log.e("Get Data", ""+data.getData());

                        break;
                }

            } else {

                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (resultCode == RESULT_OK) {
                        Uri resultUri = result.getUri();
                        final String path = image_picker.getRealPathFromURI(resultUri);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                            Glide.with(this).load(path).diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(false).placeholder(R.drawable.main_user).error(getResources().getDrawable(R.drawable.main_user, getTheme())).dontAnimate().into(globalImageObj);
                        } else {
                            Glide.with(this).load(path).diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(false).placeholder(R.drawable.main_user).error(getResources().getDrawable(R.drawable.main_user)).dontAnimate().into(globalImageObj);
                        }

                        if (imagePickerRequest == 0) {
                            profileImagePath = path;
                        } else {
                            siaBadgeImagePath = path;
                            sia_image_frame.setVisibility(View.VISIBLE);
                        }

                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Exception error = result.getError();
                    }
                } else {
                    super.onActivityResult(requestCode, resultCode, data);
                }

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Image_Picker.PERMISSION_ALL && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            if (image_picker.hasPermissions(ProfileActivity.this, Image_Picker.PERMISSIONS)) {
                image_picker.imageOptions();
            }

        }


    }*/


   /*

   // Add xml folder in the resource folder
   and create file_paths.xml and paste below code in the same

   <?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <external-path name="images" path="."/>
</paths>




   // For Image Cropper Add this to gradle, Manifest and activity

    compile 'com.theartofdev.edmodo:android-image-cropper:2.3.+'

     <activity android:name="com.theartofdev.edmodo.cropper.CropImageActivity"
    android:theme="@style/Base.Theme.AppCompat"
    android:screenOrientation="portrait"/>

       <provider
            android:name="android.support.v4.content.FileProvider"
            android:authorities="Your Pakage Name Here.fileprovider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_paths" />
        </provider>

// Change the file provider autrority name  in below line of openCamera Method of this file

 picIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(mContext, "Your Pakage Name Here.fileprovider", IMAGE_PATH));


    private void cropImage(Uri imageUri, int shape) {

        CropImage.ActivityBuilder activityBuilder = CropImage.activity(imageUri)
                .setGuidelines(CropImageView.Guidelines.ON);
        if (shape == 1) {
            activityBuilder.setCropShape(CropImageView.CropShape.RECTANGLE);
        } else {
            activityBuilder.setCropShape(CropImageView.CropShape.OVAL);
        }
        Intent intent = activityBuilder
                .getIntent(this);

        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
    }*/



}
