package com.theselflessapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.theselflessapp.R;
import com.theselflessapp.interfaces.Constant;
import com.theselflessapp.services.MyService;
import com.theselflessapp.utility.Utilities;


public class LogInJoinActivity extends AppCompatActivity implements Constant, View.OnClickListener {
    private Button btn_register;
    private Button btn_sign_up_email;
    private Button btn_about_app;
    private TextView tv_snap_memory_logo;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_join);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        tv_snap_memory_logo = (TextView) findViewById(R.id.tv_snap_memory_logo);
        Utilities.setTypeFace(this, tv_snap_memory_logo);

        btn_register = (Button) findViewById(R.id.btn_register);
        Utilities.setTypeFace(this, btn_register);
        btn_register.setOnClickListener(this);

        btn_sign_up_email = (Button) findViewById(R.id.btn_sign_up_email);
        Utilities.setTypeFace(this, btn_sign_up_email);
        btn_sign_up_email.setOnClickListener(this);

        btn_about_app = (Button) findViewById(R.id.btn_about_app);
        Utilities.setTypeFace(this, btn_about_app);
        btn_about_app.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:

                if (Utilities.isNetworkAvailable(getApplicationContext())) {

                    Utilities.passActivityIntent(LogInJoinActivity.this, LogInActivity.class);
                    //finish();

                } else {
                    Utilities.showSnackBarInternet(this, coordinatorLayout,
                            getString(R.string.no_internet_connection));
                }

                break;

            case R.id.btn_sign_up_email:

                if (Utilities.isNetworkAvailable(getApplicationContext())) {

                    Utilities.passActivityIntent(LogInJoinActivity.this, RegistrationActivity.class);
                    //finish();

                } else {
                    Utilities.showSnackBarInternet(this, coordinatorLayout,
                            getString(R.string.no_internet_connection));
                }

                break;

            case R.id.btn_about_app:

                if (Utilities.isNetworkAvailable(getApplicationContext())) {

                    Utilities.passActivityIntent(LogInJoinActivity.this, AboutAppActivity.class);
                    //finish();

                } else {
                    Utilities.showSnackBarInternet(this, coordinatorLayout,
                            getString(R.string.no_internet_connection));
                }

                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(MyService.getInstance()!= null){
            MyService.getInstance().stopFusedLocation();
        }
        finish();
    }
}
