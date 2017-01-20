package com.theselflessapp.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.theselflessapp.activity.HomeActivity;
import com.theselflessapp.interfaces.Constant;
import com.theselflessapp.interfaces.RestClient;
import com.theselflessapp.modal.EditProfilePOJO;
import com.theselflessapp.modal.GetUserDataPOJO;
import com.theselflessapp.modal.LogInPOJO;
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


/**
 * Created by ourdesignz on 20/10/16.
 */

public class EditProfileFragment extends Fragment implements Constant, View.OnClickListener,
        TextWatcher, Callback<EditProfilePOJO> {

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

    private ProgressBar progressBar;

    private String password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_register, container, false);
         /* Hide KeyBoard When Touch Outside */
        KeyboardDismisser.useWith(this);

        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        tv_logo = (TextView) rootView.findViewById(R.id.tv_logo);
        Utilities.setTypeFace3(getActivity(), tv_logo);

        edt_user_name = (EditText) rootView.findViewById(R.id.edt_user_name);
        Utilities.setTypeFace(getActivity(), edt_user_name);
        edt_password = (EditText) rootView.findViewById(R.id.edt_password);
        Utilities.setTypeFace(getActivity(), edt_password);
        edt_email = (EditText) rootView.findViewById(R.id.edt_email);
        Utilities.setTypeFace(getActivity(), edt_email);
        edt_email.addTextChangedListener(this);
        spnr_country = (Spinner) rootView.findViewById(R.id.spnr_country);
        spnr_gender = (Spinner) rootView.findViewById(R.id.spnr_gender);

        btn_register = (Button) rootView.findViewById(R.id.btn_register);
        btn_register.setText(getString(R.string.edit));
        Utilities.setTypeFace(getActivity(), btn_register);
        btn_register.setOnClickListener(this);

        profile_pic = (SimpleDraweeView) rootView.findViewById(R.id.profile_pic);
        profile_pic.setOnClickListener(this);

        Log.e("User", "Id??" + Preference.getUserId(getActivity()));

        /* Load Spinner Items Method */
        loadSpinnerItems();

        /* Get User data */
        getUserData();

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    //Load Spinner Items
    private void loadSpinnerItems() {

        countryList = new ArrayList<>(Arrays.asList(
                getResources().getStringArray(R.array.country_arrays)));

        genderList = new ArrayList<>(Arrays.asList(
                getResources().getStringArray(R.array.gender_array)));


        // Initializing an ArrayAdapter For Country
        adapter_countryList = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, countryList) {

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
                getActivity(), android.R.layout.simple_spinner_item, genderList) {

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


                if (Utilities.isNetworkAvailable(getActivity())) {

                    if (Utilities.isEmpty(edt_user_name)) {
                        Utilities.showSnackBar(getActivity(),
                                HomeActivity.getInstance().getCoordinateLayout(),
                                getString(R.string.enter_user_name));
                        return;
                    }

                    if (Utilities.isEmpty(edt_password)) {
                        Utilities.showSnackBar(getActivity(),
                                HomeActivity.getInstance().getCoordinateLayout(),
                                getString(R.string.enter_password));
                        return;
                    }

                    if (Utilities.isEmpty(edt_email)) {
                        Utilities.showSnackBar(getActivity(),
                                HomeActivity.getInstance().getCoordinateLayout(),
                                getString(R.string.enter_email));
                        return;
                    }

                    if (!Utilities.isValidEmail(edt_email.getText().toString())) {
                        Utilities.showSnackBar(getActivity(),
                                HomeActivity.getInstance().getCoordinateLayout(),
                                getString(R.string.enter_valid_email));
                        return;
                    }

                    if (Utilities.isSpinnerItemValid(getActivity(), spnr_country)) {
                        Utilities.showSnackBar(getActivity(),
                                HomeActivity.getInstance().getCoordinateLayout(),
                                getString(R.string.enter_valid_country));
                        return;
                    }

                    if (Utilities.isSpinnerItemValid(getActivity(), spnr_gender)) {
                        Utilities.showSnackBar(getActivity(),
                                HomeActivity.getInstance().getCoordinateLayout(),
                                getString(R.string.enter_valid_gender));
                        return;
                    }

                    if (Utilities.isImagePath(selectedPath)) {
                        Utilities.showSnackBar(getActivity(),
                                HomeActivity.getInstance().getCoordinateLayout(),
                                getString(R.string.select_profile_pic));
                        return;
                    }

                    makeRestClientCallForEditProfile();


                } else {
                    Utilities.showSnackBarInternet(getActivity(),
                            HomeActivity.getInstance().getCoordinateLayout(),
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

        if (resultCode == getActivity().RESULT_OK) {
            try {
                // When an Image is picked
                if (requestCode == REQUEST_CODE_GALLERY && null != data) {
                    // Get the Image from data
                    selectedImageUri = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    // Get the cursor
                    Cursor cursor = getActivity().getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    selectedPath = cursor.getString(columnIndex);
                    cursor.close();
                    Bitmap photo = BitmapFactory.decodeFile(selectedPath);
                    profile_pic.setImageURI(selectedImageUri);
                    Log.e("Path", "Gallery??" + selectedPath);

                } else if (requestCode == REQUEST_CODE_CAMERA && null != data) {

                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    selectedImageUri = Utilities.getImageUri(getActivity(), photo);
                    selectedPath = Utilities.getRealPathFromURI(getActivity(), selectedImageUri);
                    profile_pic.setImageURI(selectedImageUri);
                    Log.e("Path", "Camera??" + selectedPath);

                } else {
                    Utilities.showSnackBar(getActivity(), HomeActivity.getInstance().getCoordinateLayout(),
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
        if (edt_email.getText().length() > 0 || edt_user_name.getText().length() > 0
                || edt_password.getText().length() > 0) {
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

    /* Get user data for edit */
    private void getUserData() {
        Utilities.showProgressDialog(getActivity(), progressBar);

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.ROOT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // prepare call in Retrofit 2.0
        RestClient restClientAPI = retrofit.create(RestClient.class);

        Call<GetUserDataPOJO> call = restClientAPI.getUserDataRequest(Preference.getUserId(getActivity()));

        Callback<GetUserDataPOJO> callBack = new Callback<GetUserDataPOJO>() {
            @Override
            public void onResponse(Call<GetUserDataPOJO> call, Response<GetUserDataPOJO> response) {
                GetUserDataPOJO getData = response.body();

                int code = response.code();
                if (code == RESPONSE_CODE) {

                    Utilities.dismissProgressDialog(getActivity(), progressBar);

                    if (getData.getSuccess() == SUCCESS) {
                        Log.e("Response", "Response?? " + getData.getSuccess());

                        if (getData.getMessage().get(0).getUsername() != null &&
                                !getData.getMessage().get(0).getUsername().equals("")) {
                            edt_user_name.setText(getData.getMessage().get(0).getUsername());
                            edt_user_name.setSelection(edt_user_name.getText().length());
                        }

                        if (getData.getMessage().get(0).getEmail() != null &&
                                !getData.getMessage().get(0).getEmail().equals("")) {
                            edt_email.setText(getData.getMessage().get(0).getEmail());
                            edt_email.setSelection(edt_email.getText().length());
                        }

                        if (getData.getMessage().get(0).getImage() != null &&
                                !getData.getMessage().get(0).getImage().equals("")) {
                            selectedPath = getData.getMessage().get(0).getImage();

                            Uri uri = Uri.parse(getData.getMessage().get(0).getImage());
                            profile_pic.setImageURI(uri);

                        }

                        if (getData.getMessage().get(0).getCountry() != null &&
                                !getData.getMessage().get(0).getCountry().equals("")) {

                            for (int i = 0; i < countryList.size(); i++) {
                                if (getData.getMessage().get(0).getCountry().equals(countryList.get(i))) {
                                    spnr_country.setSelection(i);
                                }
                            }
                        }

                        if (getData.getMessage().get(0).getGender() != null &&
                                !getData.getMessage().get(0).getGender().equals("")) {

                            for (int i = 0; i < genderList.size(); i++) {
                                if (getData.getMessage().get(0).getGender().equals(genderList.get(i))) {
                                    spnr_gender.setSelection(i);
                                }
                            }
                        }

                    } else {

                        Utilities.dismissProgressDialog(getActivity(), progressBar);
                        Log.e("Response", "Response?? " + getData.getSuccess());
                    }

                } else {

                    Utilities.dismissProgressDialog(getActivity(), progressBar);

                }
            }

            @Override
            public void onFailure(Call<GetUserDataPOJO> call, Throwable t) {
                Utilities.dismissProgressDialog(getActivity(), progressBar);
                Log.e("onFailure", "onFailure?? " + t.getMessage());
                Utilities.showSnackBar(getActivity(), HomeActivity.getInstance().getCoordinateLayout(),
                        String.valueOf(getString(R.string.failed_to_connect_with_server)));

            }
        };

        //asynchronous call
        call.enqueue(callBack);
    }

    /* Make Rest Client Call For SignUp */
    private void makeRestClientCallForEditProfile() {
        Utilities.showProgressDialog(getActivity(), progressBar);

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.ROOT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // prepare call in Retrofit 2.0
        RestClient restClientAPI = retrofit.create(RestClient.class);
        Call<EditProfilePOJO> call = null;
        if (selectedPath.startsWith("http://") || selectedPath.startsWith("https://")) {

            if (!edt_password.getText().toString().equals("")) {
                password = edt_password.getText().toString();
            } else {
                password = "";
            }

            call = restClientAPI.editProfileRequest2(Preference.getUserId(getActivity()),
                    edt_user_name.getText().toString(),
                    edt_email.getText().toString(), password,
                    spnr_country.getSelectedItem().toString(),
                    spnr_gender.getSelectedItem().toString(), "");


        } else {

            if (!edt_password.getText().toString().equals("")) {
                password = edt_password.getText().toString();
            } else {
                password = "";
            }

            File file = new File(selectedPath);

            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);
            call = restClientAPI.editProfileRequest(Preference.getUserId(getActivity()),
                    edt_user_name.getText().toString(),
                    edt_email.getText().toString(), password,
                    spnr_country.getSelectedItem().toString(),
                    spnr_gender.getSelectedItem().toString(), body);

        }

        //asynchronous call
        call.enqueue(this);
    }

    /* Get Response From SignUp */
    @Override
    public void onResponse(Call<EditProfilePOJO> call, Response<EditProfilePOJO> response) {

        EditProfilePOJO editProfilePOJO = response.body();

        int code = response.code();
        if (code == RESPONSE_CODE) {

            Utilities.dismissProgressDialog(getActivity(), progressBar);

            if (editProfilePOJO.getSuccess() == SUCCESS) {

                /* Set Edit Profile User Details */
                LogInPOJO logInPOJO = Preference.getUser(getActivity());
                List list = editProfilePOJO.getData();
                logInPOJO.setData(list);
                Preference.setUser(getActivity(), logInPOJO);

                if (editProfilePOJO.getData().get(0).getId() != null && !editProfilePOJO.getData().get(0).getId().equals("")) {
                    Preference.setUserId(getActivity(), String.valueOf(editProfilePOJO.getData().get(0).getId()));
                }

                /* Refresh Load Left Fragment Drawer When Edit Profile */
                HomeActivity.getInstance().setUpFragmentDrawer(Preference.getUser(getActivity()));

                Utilities.showSnackBar(getActivity(),
                        HomeActivity.getInstance().getCoordinateLayout(),
                        String.valueOf(getString(R.string.save_profile)));

                Log.e("Response", "Response?? " + editProfilePOJO.getMessage());

            } else {
                Utilities.showSnackBar(getActivity(),
                        HomeActivity.getInstance().getCoordinateLayout(),
                        String.valueOf(editProfilePOJO.getMessage()));
                Log.e("Response", "Response?? " + editProfilePOJO.getMessage());
            }

        } else {

            Utilities.dismissProgressDialog(getActivity(), progressBar);
            Log.e("ResponseD", "ResponseD?? " + editProfilePOJO.getMessage());
        }
    }

    @Override
    public void onFailure(Call<EditProfilePOJO> call, Throwable t) {
        Utilities.dismissProgressDialog(getActivity(), progressBar);
        Log.e("onFailure", "onFailure?? " + t.getMessage());
        Utilities.showSnackBar(getActivity(), HomeActivity.getInstance().getCoordinateLayout(),
                String.valueOf(getString(R.string.failed_to_connect_with_server)));

    }
}
