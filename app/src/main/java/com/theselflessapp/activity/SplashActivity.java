package com.theselflessapp.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.theselflessapp.R;
import com.theselflessapp.gcm.QuickstartPreferences;
import com.theselflessapp.gcm.RegistrationIntentService;
import com.theselflessapp.interfaces.Constant;
import com.theselflessapp.interfaces.RestClient;
import com.theselflessapp.modal.RemoveSnapMemoryPOJO;
import com.theselflessapp.prefrence.Preference;
import com.theselflessapp.services.MyService;
import com.theselflessapp.utility.Utilities;


import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;
import permission.auron.com.marshmallowpermissionhelper.PermissionResult;
import permission.auron.com.marshmallowpermissionhelper.PermissionUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SplashActivity extends ActivityManagePermission implements Constant,
        Callback<RemoveSnapMemoryPOJO> {
    private CoordinatorLayout coordinatorLayout;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    private boolean isReceiverRegistered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Transparent Status Bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_splash);
        TextView tv = (TextView) findViewById(R.id.tv_logo);
        Utilities.setTypeFace3(this, tv);
        TextView tv_loading = (TextView) findViewById(R.id.tv_loading);
        Utilities.setTypeFace(this, tv_loading);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        if (Utilities.isNetworkAvailable(getApplicationContext())) {

            makeRestClientCallForRemoveSnapMemory();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                askMultiplePermission();

            } else {

                permissionAccessDone();
            }

        } else {
            Utilities.showSnackBarInternet(this, coordinatorLayout,
                    getString(R.string.no_internet_connection));
        }

    }

    /* Permission Access When Done */
    private void permissionAccessDone() {
        if (checkPlayServices()) {

            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    SharedPreferences sharedPreferences =
                            PreferenceManager.getDefaultSharedPreferences(context);
                    boolean sentToken = sharedPreferences
                            .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                }
            };

            // Registering BroadcastReceiver
            registerReceiver();

            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);

            // Start Services For Getting Latitude and Longitude With Google Fused API.
            Intent intent2 = new Intent(this, MyService.class);
            startService(intent2);

            initializeSplash();

        }
    }

    /* Ask For Multiple Permission For Marshmallow */
    private void askMultiplePermission() {
        String permissionAsk[] = {PermissionUtils.Manifest_CAMERA,
                PermissionUtils.Manifest_ACCESS_FINE_LOCATION,
                PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE,
                PermissionUtils.Manifest_READ_EXTERNAL_STORAGE,
                PermissionUtils.Manifest_ACCESS_COARSE_LOCATION};

        askCompactPermissions(permissionAsk, new PermissionResult() {

            @Override
            public void permissionGranted() {
                //permission granted
                //replace with your action
                permissionAccessDone();
            }

            @Override
            public void permissionDenied() {
                //permission denied
                //replace with your action
                finish();
            }

            @Override
            public void permissionForeverDenied() {
                showDialog();
            }
        });

    }

    /* Initialize Splash Screen */
    private void initializeSplash() {
        new Handler().postDelayed(new Runnable() {

			/*
             * Showing splash screen with a timer. This will be useful when you
			 * want to show case your app logo / company
			 */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                if (Preference.getLogOut(SplashActivity.this)) {

                    Utilities.passActivityIntent(SplashActivity.this, LogInJoinActivity.class);

                } else {

                    Utilities.passActivityIntent(SplashActivity.this, HomeActivity.class);
                }
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    /* Make Rest Client Call For RemoveSnapMemory */
    private void makeRestClientCallForRemoveSnapMemory() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.ROOT)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // prepare call in Retrofit 2.0
        RestClient restClientAPI = retrofit.create(RestClient.class);

        Call<RemoveSnapMemoryPOJO> call = restClientAPI.removeSnapMemoryRequest();

        //asynchronous call
        call.enqueue(this);
    }

    /* Get Response From LogIn */
    @Override
    public void onResponse(Call<RemoveSnapMemoryPOJO> call, Response<RemoveSnapMemoryPOJO> response) {

        RemoveSnapMemoryPOJO remove = response.body();

        int code = response.code();
        if (code == RESPONSE_CODE) {
            if (remove.getSuccess() == SUCCESS) {
                Log.i("Success", "true??" + remove.getSuccess());
            } else {
                Log.i("Success", "false??" + remove.getSuccess());
            }
        }
    }

    @Override
    public void onFailure(Call<RemoveSnapMemoryPOJO> call, Throwable t) {
        Utilities.showSnackBar(this, coordinatorLayout,
                String.valueOf(getString(R.string.failed_to_connect_with_server)));

    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;
        super.onPause();
    }

    private void registerReceiver() {
        if (!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    /* Check Google Play Service is Install Or Not */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("Splash", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /* Attention Dialog Showing */
    public void showDialog() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(SplashActivity.this, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(R.string.attention);
        builder.setMessage(R.string.messageperm);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                openSettingsApp(SplashActivity.this);
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.show();
    }

}
