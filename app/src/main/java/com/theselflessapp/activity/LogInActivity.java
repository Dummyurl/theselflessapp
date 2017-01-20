package com.theselflessapp.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gabrielsamojlo.keyboarddismisser.KeyboardDismisser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.theselflessapp.R;
import com.theselflessapp.interfaces.Constant;
import com.theselflessapp.interfaces.RestClient;
import com.theselflessapp.modal.LogInPOJO;
import com.theselflessapp.prefrence.Preference;
import com.theselflessapp.utility.Utilities;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LogInActivity extends AppCompatActivity implements Constant, View.OnClickListener,
        TextWatcher, Callback<LogInPOJO> {
    private EditText edt_email;
    private EditText edt_password;
    private Button btn_login;
    private TextView tv_logo;
    private CoordinatorLayout coordinatorLayout;
    private ProgressBar progressBar;
    private String token;
    private CheckBox checkBox;
    private TextView tv_remember_me;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /* Hide KeyBoard When Touch Outside */
        KeyboardDismisser.useWith(this);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        tv_logo = (TextView) findViewById(R.id.tv_logo);
        Utilities.setTypeFace3(this, tv_logo);

        edt_email = (EditText) findViewById(R.id.edt_email);
        Utilities.setTypeFace(this, edt_email);
        edt_password = (EditText) findViewById(R.id.edt_password);
        Utilities.setTypeFace(this, edt_password);
        edt_password.addTextChangedListener(this);
        btn_login = (Button) findViewById(R.id.btn_login);
        Utilities.setTypeFace(this, btn_login);
        btn_login.setOnClickListener(this);

        checkBox = (CheckBox) findViewById(R.id.checkbox);
        tv_remember_me = (TextView) findViewById(R.id.tv_remember_me);
        Utilities.setTypeFace(this, tv_remember_me);

        Preference obj = new Preference(this);
        if (obj != null) {
            token = obj.getData("token");
        }

        /* Set Remember Me */
        if (Preference.getRememberMe(this)) {
            checkBox.setChecked(true);
            edt_email.setText(Preference.getUserName(this));
            edt_password.setText(Preference.getPassword(this));
            edt_password.addTextChangedListener(this);


        } else {
            checkBox.setChecked(false);
            Preference.setRememberMeCredentials(this, "", "");
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:

                if (Utilities.isNetworkAvailable(getApplicationContext())) {

                    if (Utilities.isEmpty(edt_email)) {
                        Utilities.showSnackBar(this, coordinatorLayout, getString(R.string.enter_email));
                        return;
                    }

                    if (!Utilities.isValidEmail(edt_email.getText().toString())) {
                        Utilities.showSnackBar(this, coordinatorLayout, getString(R.string.enter_valid_email));
                        return;
                    }

                    if (Utilities.isEmpty(edt_password)) {
                        Utilities.showSnackBar(this, coordinatorLayout, getString(R.string.enter_password));
                        return;
                    }

                    makeRestClientCallForLogIn();

                } else {
                    Utilities.showSnackBarInternet(this, coordinatorLayout,
                            getString(R.string.no_internet_connection));
                }

                break;
            default:
                break;
        }
    }

    /* Text Watcher */
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() > 0) {
            btn_login.setBackgroundColor(Color.parseColor("#19A3FF"));
            btn_login.setEnabled(true);
            edt_password.requestFocus();
            edt_password.setSelection(s.length());

        } else {
            btn_login.setEnabled(false);
            btn_login.setBackgroundColor(Color.parseColor("#CCCCCC"));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    /* Make Rest Client Call For LogIn */
    private void makeRestClientCallForLogIn() {
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

        Call<LogInPOJO> call = restClientAPI.logInRequest(edt_email.getText().toString(),
                edt_password.getText().toString(), token);

        //asynchronous call
        call.enqueue(this);
    }

    /* Get Response From LogIn */
    @Override
    public void onResponse(Call<LogInPOJO> call, Response<LogInPOJO> response) {

        LogInPOJO logIn = response.body();

        int code = response.code();
        if (code == RESPONSE_CODE) {

            Utilities.dismissProgressDialog(this, progressBar);

            if (logIn.getSuccess() == SUCCESS) {

                Preference.setRememberMe(this, checkBox.isChecked());
                if (Preference.getRememberMe(this)) {

                    Preference.setRememberMeCredentials(this, edt_email.getText().toString(),
                            edt_password.getText().toString());

                } else {
                    Preference.setRememberMeCredentials(this, "", "");

                }
                /* Set LogIn User Details */
                Preference.setUser(LogInActivity.this, logIn);

                if (logIn.getData().get(0).getId() != null && !logIn.getData().get(0).getId().equals("")) {
                    Preference.setUserId(LogInActivity.this, String.valueOf(logIn.getData().get(0).getId()));
                }

                Preference.setLogOut(LogInActivity.this, false);
                Utilities.showSnackBarWithCallBack(LogInActivity.this, coordinatorLayout,
                        String.valueOf(getString(R.string.login_success)), HomeActivity.class);

            } else {
                Utilities.showSnackBar(LogInActivity.this, coordinatorLayout,
                        String.valueOf(getString(R.string.invalid_message)));

            }

        } else {

            Utilities.dismissProgressDialog(this, progressBar);
        }
    }

    @Override
    public void onFailure(Call<LogInPOJO> call, Throwable t) {
        Utilities.dismissProgressDialog(this, progressBar);
        Utilities.showSnackBar(this, coordinatorLayout,
                String.valueOf(getString(R.string.failed_to_connect_with_server)));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
