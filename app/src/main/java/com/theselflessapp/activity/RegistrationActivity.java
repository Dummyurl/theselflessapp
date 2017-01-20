package com.theselflessapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gabrielsamojlo.keyboarddismisser.KeyboardDismisser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.theselflessapp.R;
import com.theselflessapp.interfaces.Constant;
import com.theselflessapp.interfaces.RestClient;
import com.theselflessapp.modal.SignUpPOJO;
import com.theselflessapp.prefrence.Preference;
import com.theselflessapp.utility.Utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RegistrationActivity extends AppCompatActivity implements Constant, View.OnClickListener,
        TextWatcher, Callback<SignUpPOJO> {
    private EditText edt_user_name;
    private EditText edt_password;
    private EditText edt_email;

    private TextView tv_logo;

    private Spinner spnr_country;
    private Spinner spnr_gender;

    private Button btn_register;

    private SimpleDraweeView profile_pic;

    private List<String> countryList;
    private List<String> genderList;

    private ArrayAdapter<String> adapter_countryList;
    private ArrayAdapter<String> adapter_genderList;

    private Uri selectedImageUri;
    private String selectedPath = "";

    private CoordinatorLayout coordinatorLayout;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        /* Hide KeyBoard When Touch Outside */
        KeyboardDismisser.useWith(this);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        tv_logo = (TextView) findViewById(R.id.tv_logo);
        Utilities.setTypeFace3(this, tv_logo);

        edt_user_name = (EditText) findViewById(R.id.edt_user_name);
        Utilities.setTypeFace(this, edt_user_name);
        edt_password = (EditText) findViewById(R.id.edt_password);
        Utilities.setTypeFace(this, edt_password);
        edt_email = (EditText) findViewById(R.id.edt_email);
        Utilities.setTypeFace(this, edt_email);
        edt_email.addTextChangedListener(this);
        spnr_country = (Spinner) findViewById(R.id.spnr_country);
        spnr_gender = (Spinner) findViewById(R.id.spnr_gender);

        btn_register = (Button) findViewById(R.id.btn_register);
        Utilities.setTypeFace(this, btn_register);
        btn_register.setOnClickListener(this);

        profile_pic = (SimpleDraweeView) findViewById(R.id.profile_pic);
        profile_pic.setOnClickListener(this);

        /* Load Spinner Items Method */
        loadSpinnerItems();
    }

    //Load Spinner Items
    private void loadSpinnerItems() {

        countryList = new ArrayList<>(Arrays.asList(
                getResources().getStringArray(R.array.country_arrays)));

        genderList = new ArrayList<>(Arrays.asList(
                getResources().getStringArray(R.array.gender_array)));


        // Initializing an ArrayAdapter For Country
        adapter_countryList = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, countryList) {

            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the second item from Spinner
                    return false;

                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {

                    // Set the disable item text color
                    tv.setTextColor(Color.GRAY);

                } else {

                    tv.setTextColor(Color.parseColor("#000000"));

                }
                return view;
            }
        };

        adapter_countryList.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spnr_country.setAdapter(adapter_countryList);

        // Initializing an ArrayAdapter For Gender
        adapter_genderList = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, genderList) {

            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the second item from Spinner
                    return false;

                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {

                    // Set the disable item text color
                    tv.setTextColor(Color.GRAY);

                } else {

                    tv.setTextColor(Color.parseColor("#000000"));

                }
                return view;
            }
        };

        adapter_genderList.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spnr_gender.setAdapter(adapter_genderList);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_register:


                if (Utilities.isNetworkAvailable(getApplicationContext())) {

                    if (Utilities.isEmpty(edt_user_name)) {
                        Utilities.showSnackBar(this, coordinatorLayout, getString(R.string.enter_user_name));
                        return;
                    }

                    if (Utilities.isEmpty(edt_password)) {
                        Utilities.showSnackBar(this, coordinatorLayout, getString(R.string.enter_password));
                        return;
                    }

                    if (Utilities.isEmpty(edt_email)) {
                        Utilities.showSnackBar(this, coordinatorLayout, getString(R.string.enter_email));
                        return;
                    }

                    if (!Utilities.isValidEmail(edt_email.getText().toString())) {
                        Utilities.showSnackBar(this, coordinatorLayout, getString(R.string.enter_valid_email));
                        return;
                    }

                    if (Utilities.isSpinnerItemValid(this, spnr_country)) {
                        Utilities.showSnackBar(this, coordinatorLayout, getString(R.string.enter_valid_country));
                        return;
                    }

                    if (Utilities.isSpinnerItemValid(this, spnr_gender)) {
                        Utilities.showSnackBar(this, coordinatorLayout, getString(R.string.enter_valid_gender));
                        return;
                    }

                    if (Utilities.isImagePath(selectedPath)) {
                        Utilities.showSnackBar(this, coordinatorLayout, getString(R.string.select_profile_pic));
                        return;
                    }

                    makeRestClientCallForSignUp();


                } else {
                    Utilities.showSnackBarInternet(this, coordinatorLayout,
                            getString(R.string.no_internet_connection));
                }
                break;

            case R.id.profile_pic:

                selectImage();

                break;

            default:
                break;
        }
    }

    /* Select Image Form Camera And Gallery */
    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {

                    cameraIntent();

                } else if (items[item].equals("Choose from Library")) {

                    galleryIntent();

                } else if (items[item].equals("Cancel")) {

                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }

    /* Camera Intent */
    private void cameraIntent() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_CODE_CAMERA);
    }

    /* Gallery Intent */
    private void galleryIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
    }

    /* onActivityResult */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            try {
                // When an Image is picked
                if (requestCode == REQUEST_CODE_GALLERY && null != data) {
                    // Get the Image from data
                    selectedImageUri = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    // Get the cursor
                    Cursor cursor = this.getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    selectedPath = cursor.getString(columnIndex);
                    cursor.close();
                    Bitmap photo = BitmapFactory.decodeFile(selectedPath);
                    profile_pic.setImageURI(selectedImageUri);

                } else if (requestCode == REQUEST_CODE_CAMERA && null != data) {

                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    selectedImageUri = Utilities.getImageUri(this, photo);
                    selectedPath = Utilities.getRealPathFromURI(this, selectedImageUri);
                    profile_pic.setImageURI(selectedImageUri);

                } else {
                    Utilities.showSnackBar(this, coordinatorLayout,
                            getString(R.string.not_image_select));
                }
            } catch (Exception e) {

                Log.e("Exception", ">>" + e.toString());

            }
        }

    }

    /* Text Watcher */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (edt_email.getText().length() > 0 && edt_user_name.getText().length() > 0
                && edt_password.getText().length() > 0) {
            btn_register.setBackgroundColor(Color.parseColor("#19A3FF"));
            btn_register.setEnabled(true);
        } else {
            btn_register.setEnabled(false);
            btn_register.setBackgroundColor(Color.parseColor("#CCCCCC"));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /* Make Rest Client Call For SignUp */
    private void makeRestClientCallForSignUp() {
        Utilities.showProgressDialog(this, progressBar);

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.ROOT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // prepare call in Retrofit 2.0
        RestClient restClientAPI = retrofit.create(RestClient.class);

        File file = new File(selectedPath);

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);

        Call<SignUpPOJO> call = restClientAPI.signUpRequest(edt_user_name.getText().toString(),
                edt_email.getText().toString(), edt_password.getText().toString(),
                spnr_country.getSelectedItem().toString(),
                spnr_gender.getSelectedItem().toString(), body);

        //asynchronous call
        call.enqueue(this);
    }

    /* Get Response From SignUp */
    @Override
    public void onResponse(Call<SignUpPOJO> call, Response<SignUpPOJO> response) {

        SignUpPOJO logIn = response.body();

        int code = response.code();
        if (code == RESPONSE_CODE) {

            Utilities.dismissProgressDialog(this, progressBar);

            if (logIn.getSuccess() == SUCCESS) {

                if (logIn.getUserid() != null && !logIn.getUserid().equals("")) {
                    Preference.setUserId(RegistrationActivity.this, String.valueOf(logIn.getUserid()));
                }

                Utilities.showSnackBarWithCallBack(RegistrationActivity.this, coordinatorLayout,
                        String.valueOf(logIn.getValue()), LogInActivity.class);


            } else {
                Utilities.showSnackBar(RegistrationActivity.this, coordinatorLayout,
                        String.valueOf(logIn.getValue()));
            }

        } else {

            Utilities.dismissProgressDialog(this, progressBar);

        }
    }

    @Override
    public void onFailure(Call<SignUpPOJO> call, Throwable t) {
        Utilities.dismissProgressDialog(this, progressBar);
        Utilities.showSnackBar(RegistrationActivity.this, coordinatorLayout,
                String.valueOf(getString(R.string.failed_to_connect_with_server)));

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
}
