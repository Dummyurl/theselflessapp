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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.theselflessapp.R;
import com.theselflessapp.activity.HomeActivity;
import com.theselflessapp.interfaces.Constant;
import com.theselflessapp.interfaces.RestClient;
import com.theselflessapp.modal.LogInPOJO;
import com.theselflessapp.modal.SnapAMemoryPOJO;
import com.theselflessapp.prefrence.Preference;
import com.theselflessapp.utility.Utilities;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SnapAMemoryFragment extends Fragment implements Constant, View.OnClickListener,
        Callback<SnapAMemoryPOJO> {
    private ProgressBar progressBar;
    private SimpleDraweeView img_snap_memory;
    private Button btn_snap_memory;
    private Uri selectedImageUri;
    private String selectedPath = "";
    private TextView text_snap_message;
    private Boolean status = false;

    public SnapAMemoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_camera, container, false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        img_snap_memory = (SimpleDraweeView) rootView.findViewById(R.id.img_snap_memory);
        img_snap_memory.setVisibility(View.GONE);

        btn_snap_memory = (Button) rootView.findViewById(R.id.btn_snap_memory);
        btn_snap_memory.setOnClickListener(this);
        Utilities.setTypeFace(getActivity(), btn_snap_memory);

        text_snap_message = (TextView) rootView.findViewById(R.id.text_snap_message);
        text_snap_message.setOnClickListener(this);
        text_snap_message.setVisibility(View.VISIBLE);
        Utilities.setTypeFace(getActivity(), text_snap_message);


         /* Get LogIn User Details */
        if (Preference.getUser(getActivity()) != null) {
            LogInPOJO logInPojo = Preference.getUser(getActivity());
            if (logInPojo.getData().get(0).getStatus().equals(ONE)) {
                status = true;
            } else {
                status = false;
            }
        }

        if (!status) {
            Utilities.showSnackBar(getActivity(),
                    HomeActivity.getInstance().getCoordinateLayout()
                    , getString(R.string.checkIn_first));

        } else {
            showCameraGalleryDialog();
        }

        // Inflate the layout for this fragment
        return rootView;
    }

    /* Show Camera Gallery Dialog */
    private void showCameraGalleryDialog() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {

                    cameraIntent();
                    dialog.dismiss();

                } else if (items[item].equals("Choose from Library")) {

                    galleryIntent();
                    dialog.dismiss();

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

            if (requestCode == REQUEST_CODE_CAMERA && null != data) {

                text_snap_message.setVisibility(View.GONE);
                btn_snap_memory.setEnabled(true);
                btn_snap_memory.setBackgroundColor(Color.parseColor("#19A3FF"));
                img_snap_memory.setVisibility(View.VISIBLE);

                Bitmap photo = (Bitmap) data.getExtras().get("data");
                selectedImageUri = Utilities.getImageUri(getActivity(), photo);
                selectedPath = Utilities.getRealPathFromURI(getActivity(), selectedImageUri);
                img_snap_memory.setImageURI(selectedImageUri);
                Log.e("Path", "Camera??" + selectedPath);

            } else {
//                text_snap_message.setVisibility(View.VISIBLE);
//                btn_snap_memory.setEnabled(false);
//                btn_snap_memory.setBackgroundColor(Color.parseColor("#CCCCCC"));
//                img_snap_memory.setVisibility(View.GONE);
            }

            if (requestCode == REQUEST_CODE_GALLERY && null != data)

            {
                text_snap_message.setVisibility(View.GONE);
                btn_snap_memory.setEnabled(true);
                btn_snap_memory.setBackgroundColor(Color.parseColor("#19A3FF"));
                img_snap_memory.setVisibility(View.VISIBLE);

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
                img_snap_memory.setImageURI(selectedImageUri);

                Log.e("Path", "Gallery??" + selectedPath);

            } else {
//                text_snap_message.setVisibility(View.VISIBLE);
//                btn_snap_memory.setEnabled(false);
//                btn_snap_memory.setBackgroundColor(Color.parseColor("#CCCCCC"));
//                img_snap_memory.setVisibility(View.GONE);
            }

        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_snap_memory:
                if (Utilities.isNetworkAvailable(getActivity())) {

                    if (Utilities.isImagePath(selectedPath)) {
                        Utilities.showSnackBar(getActivity(),
                                HomeActivity.getInstance().getCoordinateLayout()
                                , getString(R.string.snap_a_memory));
                        return;
                    }

                    if (!status) {
                        Utilities.showSnackBar(getActivity(),
                                HomeActivity.getInstance().getCoordinateLayout()
                                , getString(R.string.checkIn_first));
                        return;
                    }

                    /* Snap A Memory Call Request */
                    snapAMemoryRequestCall(selectedPath);

                } else {
                    Utilities.showSnackBarInternet(getActivity(),
                            HomeActivity.getInstance().getCoordinateLayout(),
                            getString(R.string.no_internet_connection));
                }
                break;

            case R.id.text_snap_message:
                if (Utilities.isNetworkAvailable(getActivity())) {

                    if (!status) {
                        Utilities.showSnackBar(getActivity(),
                                HomeActivity.getInstance().getCoordinateLayout()
                                , getString(R.string.checkIn_first));
                        return;
                    }

                    showCameraGalleryDialog();

                } else {
                    Utilities.showSnackBarInternet(getActivity(),
                            HomeActivity.getInstance().getCoordinateLayout(),
                            getString(R.string.no_internet_connection));
                }
                break;
            default:
                break;
        }
    }


    /* Make Snap A Memory Request Call */
    private void snapAMemoryRequestCall(String image) {
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

        File file = new File(image);

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), reqFile);

        Call<SnapAMemoryPOJO> call = restClientAPI.snapAMemoryRequest(
                Preference.getUserId(getActivity()), body);

        //asynchronous call
        call.enqueue(this);
    }

    /* Get Response From LogIn */
    @Override
    public void onResponse(Call<SnapAMemoryPOJO> call, Response<SnapAMemoryPOJO> response) {

        SnapAMemoryPOJO snapAMemoryPOJO = response.body();

        int code = response.code();
        if (code == RESPONSE_CODE) {

            Utilities.dismissProgressDialog(getActivity(), progressBar);

            if (snapAMemoryPOJO.getSuccess() == SUCCESS) {

                 /* Set SnapAMemoryPOJO User Details */
                LogInPOJO logInPOJO = Preference.getUser(getActivity());
                List list = snapAMemoryPOJO.getData();
                logInPOJO.setData(list);
                Preference.setUser(getActivity(), logInPOJO);

                if (snapAMemoryPOJO.getData().get(0).getId() != null && !snapAMemoryPOJO.getData().get(0).getId().equals("")) {
                    Preference.setUserId(getActivity(), String.valueOf(snapAMemoryPOJO.getData().get(0).getId()));
                }

                /* Refresh Load Left Fragment Drawer */
                HomeActivity.getInstance().setUpFragmentDrawer(Preference.getUser(getActivity()));

                Utilities.showSnackBarWithCallBackLoadFragment(getActivity(),
                        HomeActivity.getInstance().getCoordinateLayout(),
                        String.valueOf(getString(R.string.snap_your_memory)), 2);

            } else {
                Utilities.showSnackBar(getActivity(), HomeActivity.getInstance().getCoordinateLayout(),
                        String.valueOf(snapAMemoryPOJO.getMessage()));

            }

        } else {

            Utilities.dismissProgressDialog(getActivity(), progressBar);
        }
    }

    @Override
    public void onFailure(Call<SnapAMemoryPOJO> call, Throwable t) {
        Utilities.dismissProgressDialog(getActivity(), progressBar);
        Log.e("onFailure", "onFailure?? " + t.getMessage());
        Utilities.showSnackBar(getActivity(), HomeActivity.getInstance().getCoordinateLayout(),
                String.valueOf(getString(R.string.failed_to_connect_with_server)));

    }

}
