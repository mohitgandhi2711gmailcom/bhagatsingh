package com.mohi.in.activities;

import android.support.v7.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity /*implements View.OnClickListener, ServerCallBack {
    private static final int REQUEST_READ_CONTACTS = 0;


    private CenturyGothicRegularTextView tv_email;
    private CenturyGothicRegularEditText et_userName, et_phoneNo;
    private UbuntuRegularButton but_save;
    private FrameLayout fl_userImage;
    private CircularImageView iv_image;
    private Uri mUri = null;
    private Bitmap bitMap = null;

    private String TAG = "EditProfileActivity";
    private Image_Picker image_picker = new Image_Picker(EditProfileActivity.this);


    // Header
    private ImageView iv_menu;
    private ImageView iv_search, iv_filter;
    private ImageView iv_menuIcon;
    private LinearLayout ll_titellogo;
    private MaterialRippleLayout mrl_search, mrl_filter, mrl_menu;
    private UbuntuLightTextView tv_headerTilel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        init();
    }

    private void init() {

        tv_email = (CenturyGothicRegularTextView) findViewById(R.id.EditProfile_EmailId);

        et_userName = (CenturyGothicRegularEditText) findViewById(R.id.EditProfile_UserName);
        et_phoneNo = (CenturyGothicRegularEditText) findViewById(R.id.EditProfile_PhoneNo);

        but_save = (UbuntuRegularButton) findViewById(R.id.EditProfile_Save);

        fl_userImage = (FrameLayout) findViewById(R.id.EditProfile_UserImage);
        iv_image = (CircularImageView) findViewById(R.id.EditProfile_Image);


        //Header
        mrl_menu = (MaterialRippleLayout) findViewById(R.id.Header_MenuLayOut);
        mrl_search = (MaterialRippleLayout) findViewById(R.id.Header_SearchLayOut);
        mrl_filter = (MaterialRippleLayout) findViewById(R.id.Header_FilterLayOut);

        iv_menu = (ImageView) findViewById(R.id.Header_Menu);
        iv_search = (ImageView) findViewById(R.id.Header_Search);
        iv_filter = (ImageView) findViewById(R.id.Header_Filter);

        tv_headerTilel = (UbuntuLightTextView) findViewById(R.id.Header_Title);

        ll_titellogo = (LinearLayout) findViewById(R.id.Header_Titel_Logo);


        setValue();

    }


    private void setValue() {
        iv_menu.setImageResource(R.drawable.back_arrow);
        ll_titellogo.setVisibility(View.GONE);

        iv_search.setVisibility(View.INVISIBLE);
        iv_filter.setVisibility(View.INVISIBLE);
        tv_headerTilel.setVisibility(View.VISIBLE);

        mrl_search.setRippleDuration(0);
        mrl_search.setRippleFadeDuration(0);

        mrl_filter.setRippleDuration(0);
        mrl_filter.setRippleFadeDuration(0);

        tv_headerTilel.setText(R.string.edit_profile);


        iv_menu.setOnClickListener(this);
        but_save.setOnClickListener(this);
        iv_image.setOnClickListener(this);

        setUsetInfo();

    }


    private void setUsetInfo() {
        try {

            Glide.with(EditProfileActivity.this)
                    .load(SessionStore.getUserDetails(EditProfileActivity.this, Common.USER_PREFS_NAME).get(SessionStore.PROFILEPICTURE))
                    .into(iv_image);

            String username=SessionStore.getUserDetails(EditProfileActivity.this, Common.USER_PREFS_NAME).get(SessionStore.USER_FIRST_NAME)+SessionStore.getUserDetails(EditProfileActivity.this, Common.USER_PREFS_NAME).get(SessionStore.USER_LAST_NAME);
            et_userName.setText(username);
            et_userName.setSelection(username.length());

            et_phoneNo.setText(SessionStore.getUserDetails(EditProfileActivity.this, Common.USER_PREFS_NAME).get(SessionStore.USER_MOBILENO));
            et_phoneNo.setSelection(SessionStore.getUserDetails(EditProfileActivity.this, Common.USER_PREFS_NAME).get(SessionStore.USER_MOBILENO).length());

            tv_email.setText(SessionStore.getUserDetails(EditProfileActivity.this, Common.USER_PREFS_NAME).get(SessionStore.USER_EMAIL));


            Ion.with(EditProfileActivity.this)
                    .load(SessionStore.getUserDetails(EditProfileActivity.this, Common.USER_PREFS_NAME).get(SessionStore.PROFILEPICTURE))
                    .asBitmap()
                    .setCallback(new FutureCallback<Bitmap>() {
                        @Override
                        public void onCompleted(Exception e, Bitmap result) {

                            if (e != null) {


                            } else {

                                bitMap = result;

                            }

                        }
                    });


        } catch (Exception e) {


        }


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.Header_Menu:

                onBackPressed();

                break;

            case R.id.EditProfile_Save:

                attemptUpdateProfile();

                break;

            case R.id.EditProfile_Image:

                if (image_picker.hasPermissions(this, Image_Picker.PERMISSIONS)) {
                    image_picker.imageOptions();
                } else {
                    image_picker.startDialog();
                }
                break;
        }
    }


    private void attemptUpdateProfile() {
        try {


            et_userName.setError(null);
            et_phoneNo.setError(null);


            String userName = et_userName.getText().toString();
            String mobileNo = et_phoneNo.getText().toString();

            Log.e("sdfdsfdsf","ffffff: "+userName+"  ::: "+mobileNo.length()+"   :::: "+Methods.isPhoneNoValid(mobileNo.trim()));

            boolean cancel = false;
            View focusView = null;


            if (TextUtils.isEmpty(userName.trim())) {
                et_userName.setError(getString(R.string.error_field_required));
                focusView = et_userName;
                cancel = true;
            }

            if (TextUtils.isEmpty(mobileNo.trim())) {
                et_phoneNo.setError(getString(R.string.error_field_required));
                focusView = et_phoneNo;
                cancel = true;
            }else   if (!Methods.isPhoneNoValid(mobileNo.trim())) {

                et_phoneNo.setError(getString(R.string.error_invalid_phoneno));
                focusView = et_phoneNo;
                cancel = true;
            }




            if (cancel) {
                // There was an error; don't attempt login and focus the first
                // form field with an error.
                focusView.requestFocus();
            } else {
                // Show a progress spinner, and kick off a background task to
                // perform the user login attempt.

                WaitDialog.showDialog(this);

                File file = null;
                if (mUri != null) {

                    file = new File(mUri.getPath());
                } *//*else {

                    file = new File(getCacheDir(), "editProfile.png");
                    file.createNewFile();

//Convert bitmap to byte array
//                Bitmap bitmap = your bitmap;
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitMap.compress(Bitmap.CompressFormat.PNG, 0 *//**//*ignored for PNG*//**//*, bos);
                    byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();
                }*//*


                ServerCalling.ServerCallingUserApiImagePost(EditProfileActivity.this, "updateProfile", this, SessionStore.getUserDetails(EditProfileActivity.this, Common.USER_PREFS_NAME).get(SessionStore.USER_ID)
                        , SessionStore.getUserDetails(EditProfileActivity.this, Common.USER_PREFS_NAME).get(SessionStore.USER_TOKEN), userName, mobileNo, file);


            }
        } catch (Exception e) {

        }

    }

    @Override
    public void ServerCallBackSuccess(JSONObject result, String strfFrom) {
        WaitDialog.hideDialog();
        try {
            if (strfFrom.trim().equalsIgnoreCase("updateProfile")) {
                if (result.getString("status").trim().equalsIgnoreCase("1")) {

                    Methods.showToast(EditProfileActivity.this, result.getString("msg"));


                    JSONObject data = result.getJSONObject("data");

                    String strAddress = "", strAddressName = "", strAddresId = "";


                    if (!data.isNull("address")) {
                        JSONObject addressData = data.getJSONObject("address");

                        String str = addressData.getString("flat_no") + ", " + addressData.getString("street");

                        if (!addressData.getString("landmark").equalsIgnoreCase("")) {
                            str = str + ", " + addressData.getString("landmark") + ", " + addressData.getString("city") + ", " + addressData.getString("state") + ", " +
                                    addressData.getString("postcode");

                        } else {

                            str = str + ", " + addressData.getString("city") + ", " + addressData.getString("state") + ", " + addressData.getString("postcode");

                        }


                        strAddressName = addressData.getString("name");
                        strAddresId = addressData.getString("address_id");
                        strAddress = str;
                    }


                    Log.e("dfds", "AAAAAA: " + strAddressName + ", " + strAddresId + ",  " + strAddress);

                    //Country Code may recive during login
                    String countryCode="";
                    SessionStore.saveUserDetails(EditProfileActivity.this, Common.USER_PREFS_NAME, data.getString("user_id"), data.getString("token"),
                            data.getString("email"), data.getString("mobile_number"), data.getString("name"), data.getString("user_image"), strAddresId, strAddressName, strAddress,
                            data.getString("currency"),countryCode);


                    onBackPressed();

                } else {

                    Methods.showToast(EditProfileActivity.this, result.getString("msg"));

                    Log.e(TAG, "Signup User log: " + result.getString("msg"));
                }
            }
        } catch (Exception ee) {

            Log.e(TAG, "Signup User Exception: " + ee.getMessage());
        }
    }


    // Permissions
    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        *//*getLoaderManager().initLoader(0, null, this);*//*
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
           *//* Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });*//*

            Methods.showToast(EditProfileActivity.this, getResources().getString(R.string.permission_rationale));

        } else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE, CAMERA, ACCESS_NETWORK_STATE}, REQUEST_READ_CONTACTS);
        }
        return false;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Image_Picker.CAMERA_REQUEST) {

            switch (resultCode) {
                case Activity.RESULT_OK:

                    cropImage(Uri.fromFile(Image_Picker.IMAGE_PATH), 0);

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
                                cropImage(selectedImageURI, 0);
                            } else {
                                cropImage(image_picker.getImageUrlWithAuthority(this, selectedImageURI), 0);

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
                        mUri = resultUri;
                        final String path = image_picker.getRealPathFromURI(resultUri);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                            Glide.with(this).load(path).diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(false).dontAnimate().into(iv_image);
                        } else {
                            Glide.with(this).load(path).diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(false).dontAnimate().into(iv_image);
                        }

                      *//*  if (imagePickerRequest == 0) {
                            profileImagePath = path;
                        } else {
                            siaBadgeImagePath = path;
                            sia_image_frame.setVisibility(View.VISIBLE);
                        }*//*

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

            if (image_picker.hasPermissions(EditProfileActivity.this, Image_Picker.PERMISSIONS)) {
                image_picker.imageOptions();
            }

        }


    }

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
    }*/
{
}
