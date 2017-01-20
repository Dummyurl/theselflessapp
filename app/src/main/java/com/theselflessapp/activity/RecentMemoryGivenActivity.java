package com.theselflessapp.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.theselflessapp.R;
import com.theselflessapp.interfaces.Constant;
import com.theselflessapp.interfaces.RestClient;
import com.theselflessapp.modal.GetUserDataPOJO;
import com.theselflessapp.utility.Utilities;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ourdesignz on 13/10/16.
 */

public class RecentMemoryGivenActivity extends AppCompatActivity implements Constant,
        View.OnClickListener, Callback<GetUserDataPOJO> {

    private CoordinatorLayout coordinatorLayout;
    private ImageButton btn_menu_icon;
    private ImageButton btn_back_icon;
    private ImageButton btn_download;
    private TextView home_title;
    private TextView tv_snap_memory_by;
    private TextView tv_profile_name;
    private TextView tv_profile_des;
    private TextView tv_memory_taken_on;
    private SimpleDraweeView profile_pic;
    private SimpleDraweeView img_pic_large;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_memory_given);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        btn_menu_icon = (ImageButton) findViewById(R.id.btn_menu_icon);
        btn_menu_icon.setVisibility(View.GONE);

        btn_back_icon = (ImageButton) findViewById(R.id.btn_back_icon);
        btn_back_icon.setOnClickListener(this);
        btn_back_icon.setVisibility(View.VISIBLE);

        btn_download = (ImageButton) findViewById(R.id.btn_download);
        btn_download.setOnClickListener(this);

        home_title = (TextView) findViewById(R.id.home_title);
        Utilities.setTypeFace(this, home_title);
        home_title.setText(getString(R.string.recent_memory_given));

        tv_snap_memory_by = (TextView) findViewById(R.id.tv_snap_memory_by);
        Utilities.setTypeFace(this, tv_snap_memory_by);
        tv_profile_name = (TextView) findViewById(R.id.tv_profile_name);
        Utilities.setTypeFace(this, tv_profile_name);
        tv_profile_des = (TextView) findViewById(R.id.tv_profile_des);
        Utilities.setTypeFace(this, tv_profile_des);
        tv_memory_taken_on = (TextView) findViewById(R.id.tv_memory_taken_on);
        Utilities.setTypeFace(this, tv_memory_taken_on);

        profile_pic = (SimpleDraweeView) findViewById(R.id.profile_pic);
        img_pic_large = (SimpleDraweeView) findViewById(R.id.img_pic_large);

        if (getIntent().getStringExtra(SNAP_PIC) != null &&
                !getIntent().getStringExtra(SNAP_PIC).equals("")) {

            Uri uri = Uri.parse(getIntent().getStringExtra(SNAP_PIC));
            img_pic_large.setImageURI(uri);
        }


        if (getIntent().getStringExtra(KEY_USER_ID) != null &&
                !getIntent().getStringExtra(KEY_USER_ID).equals("")) {

            /* Set Up User Details */
            getUserDetails(getIntent().getStringExtra(KEY_USER_ID));
        }

        if (getIntent().getStringExtra(KEY_IMAGE_TIME) != null &&
                !getIntent().getStringExtra(KEY_IMAGE_TIME).equals("")) {

            tv_memory_taken_on.setText(getString(R.string.memory_taken_ons) + " " +
                    Utilities.formatDateFromString2(getIntent().getStringExtra(KEY_IMAGE_TIME))
                    + " | " + getString(R.string.memory_expire_on) + " " +
                    Utilities.formatDateFromString4(getIntent().getStringExtra(KEY_IMAGE_TIME)));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back_icon:
                finish();
                break;

            case R.id.btn_download:
                if (getIntent().getStringExtra(SNAP_PIC) != null &&
                        !getIntent().getStringExtra(SNAP_PIC).equals("")) {
                    Utilities.showSnackBar(RecentMemoryGivenActivity.this, coordinatorLayout, getString(R.string.downloading));
                    Utilities.downloadFile2(this, getIntent().getStringExtra(SNAP_PIC));
                } else {
                    Utilities.showSnackBar(RecentMemoryGivenActivity.this, coordinatorLayout, getString(R.string.snap_memory_not_given));
                }

                break;

            default:
                break;
        }
    }

    /* Get user data for edit */
    private void getUserDetails(String id) {
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

        Call<GetUserDataPOJO> call = restClientAPI.getUserDataRequest(id);

        //asynchronous call
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<GetUserDataPOJO> call, Response<GetUserDataPOJO> response) {
        GetUserDataPOJO getData = response.body();

        int code = response.code();
        if (code == RESPONSE_CODE) {

            Utilities.dismissProgressDialog(RecentMemoryGivenActivity.this, progressBar);

            if (getData.getSuccess() == SUCCESS) {
                Log.e("Response", "Response?? " + getData.getSuccess());

                if (getData.getMessage().get(0).getUsername() != null &&
                        !getData.getMessage().get(0).getUsername().equals("")) {

                    tv_profile_name.setText(getData.getMessage().get(0).getUsername());
                } else {
                    tv_profile_name.setText(getString(R.string.user_name));
                }

                if (getData.getMessage().get(0).getGender() != null &&
                        !getData.getMessage().get(0).getGender().equals("") ||
                        getData.getMessage().get(0).getDateCreated() != null &&
                                !getData.getMessage().get(0).getDateCreated().equals("")) {

                    tv_profile_des.setText(getData.getMessage().get(0).getGender() + " "
                            + getString(R.string.from) + " " +
                            getData.getMessage().get(0).getCountry() + "\n"
                            + getString(R.string.active_member) + " " +
                            Utilities.formatDateFromString(getData.getMessage()
                                    .get(0).getDateCreated()) + "\n"
                            + getData.getMessage().get(0).getTotalPhotos() + " "
                            + getString(R.string.memory_snaped) + " | "
                            + getData.getMessage().get(0).getMemoryGiven()
                            + " " + getString(R.string.memory_given));

                }

                if (getData.getMessage().get(0).getImage() != null &&
                        !getData.getMessage().get(0).getImage().equals("")) {


                    Uri uri = Uri.parse(getData.getMessage().get(0).getImage());
                    profile_pic.setImageURI(uri);

                }


            } else {

                Utilities.dismissProgressDialog(RecentMemoryGivenActivity.this, progressBar);
                Log.e("Response", "Response?? " + getData.getSuccess());
            }

        } else {

            Utilities.dismissProgressDialog(RecentMemoryGivenActivity.this, progressBar);

        }
    }

    @Override
    public void onFailure(Call<GetUserDataPOJO> call, Throwable t) {
        Utilities.dismissProgressDialog(RecentMemoryGivenActivity.this, progressBar);
        Log.e("onFailure", "onFailure?? " + t.getMessage());
        Utilities.showSnackBar(RecentMemoryGivenActivity.this, HomeActivity.getInstance().getCoordinateLayout(),
                String.valueOf(getString(R.string.failed_to_connect_with_server)));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}