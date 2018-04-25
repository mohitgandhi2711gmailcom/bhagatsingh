package com.mohi.in.fragments;

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
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.mohi.in.R;
import com.mohi.in.activities.LoginActivityNew;
import com.mohi.in.activities.SignupActivityNew;
import com.mohi.in.common.Common;
import com.mohi.in.dialog.WaitDialog;
import com.mohi.in.ui.adapter.TimelineProfileAddressPasswordAdapter;
import com.mohi.in.utils.Methods;
import com.mohi.in.utils.ServerCalling;
import com.mohi.in.utils.SessionStore;
import com.mohi.in.utils.listeners.ServerCallBack;
import com.mohi.in.widgets.UbuntuMediumTextView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class TimelinePasswordProfileAddressFragment extends Fragment implements View.OnClickListener, ServerCallBack {

    private TimelineProfileAddressPasswordAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private CircularImageView editProfileImageView;
    private TextView userNameTextView;
    private Context mContext;
    private View loginSignUpView;
    private UbuntuMediumTextView loginTextView;
    private UbuntuMediumTextView signupTextView;
    private static final int CAMERA_REQUEST = 101;
    private static final int GALLERY_REQUEST = 130;
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private File imagePath = null;
    public static final int PERMISSION_ALL = 134;
    private ProgressBar profile_image_pb;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_timeline_profile_address_password, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        mContext = getActivity();
        editProfileImageView = view.findViewById(R.id.EditProfile_Image);
        userNameTextView = view.findViewById(R.id.user_name);
        viewPager = view.findViewById(R.id.pager_timleline);
        adapter = new TimelineProfileAddressPasswordAdapter(getChildFragmentManager());
        tabLayout = view.findViewById(R.id.tabs);
        loginSignUpView = view.findViewById(R.id.MyAccount_LoginSingup);
        loginTextView = view.findViewById(R.id.SigninSignup_popup_Login);
        signupTextView = view.findViewById(R.id.SigninSignup_popup_Signup);
        profile_image_pb=view.findViewById(R.id.profile_image_pb);
        setValue();
    }

    private void setValue() {
        adapter.addFragment(new TimelineFragment(), "TIMELINE");
        adapter.addFragment(new ProfileFragment(), "PROFILE");
        adapter.addFragment(new FragmentEditAddress(), "SHIPPING ADDRESS");
        adapter.addFragment(new ChangePasswordFragment(), "CHANGE PASSWORD");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        editProfileImageView.setOnClickListener(this);
        loginTextView.setOnClickListener(this);
        signupTextView.setOnClickListener(this);
        setUserData();
    }

    private void setUserData() {
        HashMap<String, String> map = SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME);
        if (map.get(SessionStore.USER_ID) == null || map.get(SessionStore.USER_ID).isEmpty()) {
            Methods.showToast(mContext, "User is not logged in");
        } else {
            String username = map.get(SessionStore.USER_FIRST_NAME) + " " + map.get(SessionStore.USER_LAST_NAME);
            userNameTextView.setText(username);
            Glide.with(mContext).load(map.get(SessionStore.PROFILEPICTURE)).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    profile_image_pb.setVisibility(View.GONE);
                    return false;
                }
            }).into(editProfileImageView);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!checkLogin()) {
            loginSignUpView.setVisibility(View.VISIBLE);
        } else {
            loginSignUpView.setVisibility(View.GONE);
        }
    }

    private boolean checkLogin() {
        HashMap<String, String> map = SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME);
        return !(map.get(SessionStore.USER_ID) == null || map.get(SessionStore.USER_ID).isEmpty());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.EditProfile_Image:
                if (checkLogin()) {
                    if (hasPermissions(mContext, permissions)) {
                        imageOptions();
                    } else {
                        startDialog();
                    }
                }
                break;

            case R.id.SigninSignup_popup_Login:
                loginSignUpView.setVisibility(View.GONE);
                Intent logInIntent = new Intent(mContext, LoginActivityNew.class);
                startActivity(logInIntent);
                break;

            case R.id.SigninSignup_popup_Signup:
                loginSignUpView.setVisibility(View.GONE);
                Intent signUpIntent = new Intent(mContext, SignupActivityNew.class);
                startActivity(signUpIntent);
                break;

            default:
                Methods.showToast(mContext, "Error");

        }
    }

    /*
     * Camera Functionality Below
     * */

    private void startDialog() {
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

    private boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void checkPermission() {
        if (!hasPermissions(mContext, permissions)) {
            ActivityCompat.requestPermissions((Activity) mContext, permissions, PERMISSION_ALL);
        }

    }

    private void openCameraApp() {
        Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        String file_path = Environment.getExternalStorageDirectory().toString() + "/" + mContext.getResources().getString(R.string.app_name);

        File dir = new File(file_path);
        if (!dir.exists())
            dir.mkdirs();
        imagePath = new File(dir, mContext.getResources().getString(R.string.app_name) + System.currentTimeMillis() + ".png");


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            picIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(mContext, "com.mohi.fileprovider", imagePath));
        } else {
            picIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imagePath));
        }

        startActivityForResult(picIntent, CAMERA_REQUEST);

    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        startActivityForResult(intent, GALLERY_REQUEST);
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
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == CAMERA_REQUEST) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    cropImage(Uri.fromFile(imagePath), 0);

                    break;
                case Activity.RESULT_CANCELED:
                    break;
            }
        } else {
            if (requestCode == GALLERY_REQUEST) {
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Uri selectedImageURI = data.getData();
                        try {
                            if (checkURI(selectedImageURI)) {
                                cropImage(selectedImageURI, 0);
                            } else {
                                cropImage(getImageUrlWithAuthority(mContext, selectedImageURI), 0);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        break;

                    case Activity.RESULT_CANCELED:
                        break;
                }
            } else {
                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                    CropImage.ActivityResult result = CropImage.getActivityResult(data);
                    if (resultCode == Activity.RESULT_OK) {
                        Uri resultUri = result.getUri();
                        final String path = getRealPathFromURI(resultUri);
                        Glide.with(mContext).load(path).diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(false).dontAnimate().into(editProfileImageView);
                        attemptUploadImage(resultUri);
                    } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                        Exception error = result.getError();
                    }
                } else {
                    super.onActivityResult(requestCode, resultCode, data);
                }

            }
        }
    }

    private void attemptUploadImage(Uri resultUri) {
        try {
            WaitDialog.showDialog(mContext);
            File file = new File(resultUri.getPath());
            String name = SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_ID);
            String token = SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME).get(SessionStore.USER_TOKEN);
            ServerCalling.ServerCallingUserApiImagePost(mContext, "profileimage", this, name, token, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cropImage(Uri imageUri, int shape) {

        CropImage.ActivityBuilder activityBuilder = CropImage.activity(imageUri).setGuidelines(CropImageView.Guidelines.ON);
        if (shape == 1) {
            activityBuilder.setCropShape(CropImageView.CropShape.RECTANGLE);
        } else {
            activityBuilder.setCropShape(CropImageView.CropShape.OVAL);
        }
        Intent intent = activityBuilder.getIntent(mContext);
        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void ServerCallBackSuccess(JSONObject jobj, String strfFrom) {
        WaitDialog.hideDialog();
        if (strfFrom.trim().equalsIgnoreCase("profileimage")) {
            if (jobj.optString("status").trim().equalsIgnoreCase("success")) {
                JSONObject data = jobj.optJSONObject("data");
                String image = data.optString("image");
                Glide.with(mContext).load(image).listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        profile_image_pb.setVisibility(View.GONE);
                        return false;
                    }
                }).into(editProfileImageView);
                HashMap<String, String> map = SessionStore.getUserDetails(mContext, Common.USER_PREFS_NAME);
                SessionStore.saveUserDetails(mContext, Common.USER_PREFS_NAME, map.get(SessionStore.USER_ID), map.get(SessionStore.USER_TOKEN), map.get(SessionStore.USER_EMAIL), map.get(SessionStore.USER_MOBILENO), map.get(SessionStore.USER_FIRST_NAME), map.get(SessionStore.USER_LAST_NAME), image, map.get(SessionStore.USER_CURRENCYTYPE), map.get(SessionStore.COUNTRY_CODE));
            } else {
                Methods.showToast(mContext, "Error");
            }
        }
    }
}
