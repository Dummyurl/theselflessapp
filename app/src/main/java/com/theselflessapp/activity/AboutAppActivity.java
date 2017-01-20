package com.theselflessapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.theselflessapp.R;
import com.theselflessapp.interfaces.Constant;
import com.theselflessapp.utility.Utilities;


public class AboutAppActivity extends AppCompatActivity implements Constant, View.OnClickListener {
    private CoordinatorLayout coordinatorLayout;
    private ImageButton btn_menu_icon;
    private ImageButton btn_back_icon;
    private TextView home_title;
    private TextView tv_about_us;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        btn_menu_icon = (ImageButton) findViewById(R.id.btn_menu_icon);
        btn_menu_icon.setVisibility(View.GONE);

        btn_back_icon = (ImageButton) findViewById(R.id.btn_back_icon);
        btn_back_icon.setOnClickListener(this);
        btn_back_icon.setVisibility(View.VISIBLE);

        home_title = (TextView) findViewById(R.id.home_title);
        Utilities.setTypeFace(this, home_title);
        home_title.setText(getString(R.string.about_the_app));
        tv_about_us = (TextView) findViewById(R.id.tv_about_us);
        Utilities.setTypeFace(this, tv_about_us);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back_icon:
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }


}
