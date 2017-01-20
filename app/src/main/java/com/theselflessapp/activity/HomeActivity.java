package com.theselflessapp.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gabrielsamojlo.keyboarddismisser.KeyboardDismisser;
import com.theselflessapp.R;
import com.theselflessapp.fragment.CheckIn_OutFragment;
import com.theselflessapp.fragment.EditProfileFragment;
import com.theselflessapp.fragment.FragmentDrawer;
import com.theselflessapp.fragment.HelpContactFragment;
import com.theselflessapp.fragment.HomeFragment;
import com.theselflessapp.fragment.SnapAMemoryFragment;
import com.theselflessapp.interfaces.Constant;
import com.theselflessapp.modal.LogInPOJO;
import com.theselflessapp.prefrence.Preference;
import com.theselflessapp.services.MyService;
import com.theselflessapp.utility.Utilities;
import com.theselflessapp.view.MaterialRippleLayout;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener, Constant,
        FragmentDrawer.FragmentDrawerListener {

    private RelativeLayout header;
    private LinearLayout footer;

    private ImageButton btn_location;
    private ImageButton btn_camera;
    private ImageButton btn_menu_icon;
    private ImageButton btn_home;
    private ImageButton btn_back_icon;

    private MaterialRippleLayout ripple_location;
    private MaterialRippleLayout ripple_camera;
    private MaterialRippleLayout ripple_home;

    private TextView home_title;

    private FragmentDrawer drawerFragment;
    private CoordinatorLayout coordinatorLayout;
    private static HomeActivity mContext;
    private LogInPOJO logInPojo = null;

    // HomeActivity Instance
    public static HomeActivity getInstance() {
        return mContext;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /* Hide KeyBoard When Touch Outside */
        KeyboardDismisser.useWith(this);
        mContext = HomeActivity.this;

        /* Get LogIn User Details */
        if (Preference.getUser(this) != null) {
            logInPojo = Preference.getUser(this);
        }

        header = (RelativeLayout) findViewById(R.id.header);
        footer = (LinearLayout) findViewById(R.id.footer);
        btn_menu_icon = (ImageButton) findViewById(R.id.btn_menu_icon);
        btn_menu_icon.setOnClickListener(this);
        btn_back_icon = (ImageButton) findViewById(R.id.btn_back_icon);
        btn_back_icon.setOnClickListener(this);
        btn_back_icon.setVisibility(View.GONE);
        btn_location = (ImageButton) findViewById(R.id.btn_location);
        btn_location.setOnClickListener(this);
        btn_camera = (ImageButton) findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(this);
        btn_home = (ImageButton) findViewById(R.id.btn_home);
        btn_home.setOnClickListener(this);

        ripple_location = (MaterialRippleLayout) findViewById(R.id.ripple_location);
        ripple_location.setOnClickListener(this);
        ripple_camera = (MaterialRippleLayout) findViewById(R.id.ripple_camera);
        ripple_camera.setOnClickListener(this);
        ripple_home = (MaterialRippleLayout) findViewById(R.id.ripple_home);
        ripple_home.setOnClickListener(this);

        home_title = (TextView) findViewById(R.id.home_title);
        Utilities.setTypeFace(this, home_title);

        /* Load Fragment For First Time */
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        setUpFragmentDrawer(logInPojo);


        loadFragment(HOME_FRAGMENT_CODE);

    }

    /* Set Up Fragment Drawer */
    public void setUpFragmentDrawer(LogInPOJO logInPojo) {
        drawerFragment = (FragmentDrawer) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        drawerFragment.setDrawerListener(this);
        drawerFragment.setUserInformation(logInPojo);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_menu_icon:
                navigateDrawer();
                break;

            case R.id.btn_back_icon:
                loadFragment(HOME_FRAGMENT_CODE);
                break;

            case R.id.btn_location:

                loadFragment(LOCATION_FRAGMENT_CODE);
                break;

            case R.id.btn_camera:

                loadFragment(CAMERA_FRAGMENT_CODE);
                break;

            case R.id.btn_home:

                loadFragment(HOME_FRAGMENT_CODE);
                break;

            case R.id.ripple_location:

                //loadFragment(LOCATION_FRAGMENT_CODE);
                break;

            case R.id.ripple_camera:

                //loadFragment(CAMERA_FRAGMENT_CODE);
                break;

            case R.id.ripple_home:

                //loadFragment(HOME_FRAGMENT_CODE);
                break;

            default:
                break;
        }
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        loadFragment(position);
    }

    public void loadFragment(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                btn_location.setBackgroundResource(R.drawable.location_icon_light);
                btn_camera.setBackgroundResource(R.drawable.camera_icon_dark);
                btn_home.setBackgroundResource(R.drawable.home_icon_dark);
                btn_back_icon.setVisibility(View.GONE);
                fragment = new CheckIn_OutFragment();
                title = getString(R.string.checkIn_checkout);
                break;
            case 1:
                btn_location.setBackgroundResource(R.drawable.location_icon_dark);
                btn_camera.setBackgroundResource(R.drawable.camera_icon_light);
                btn_home.setBackgroundResource(R.drawable.home_icon_dark);
                btn_back_icon.setVisibility(View.GONE);
                fragment = new SnapAMemoryFragment();
                title = getString(R.string.snap_a_memory);
                break;
            case 2:
                btn_location.setBackgroundResource(R.drawable.location_icon_dark);
                btn_camera.setBackgroundResource(R.drawable.camera_icon_dark);
                btn_home.setBackgroundResource(R.drawable.home_icon_light);
                btn_back_icon.setVisibility(View.GONE);
                fragment = new HomeFragment();
                title = getString(R.string.checkIn_memory_feed);
                break;

            case 3:
                btn_location.setBackgroundResource(R.drawable.location_icon_dark);
                btn_camera.setBackgroundResource(R.drawable.camera_icon_dark);
                btn_home.setBackgroundResource(R.drawable.home_icon_dark);
                btn_back_icon.setVisibility(View.VISIBLE);
                fragment = new EditProfileFragment();
                title = getString(R.string.edit_profile);
                break;

            case 4:
                btn_location.setBackgroundResource(R.drawable.location_icon_dark);
                btn_camera.setBackgroundResource(R.drawable.camera_icon_dark);
                btn_home.setBackgroundResource(R.drawable.home_icon_dark);
                btn_back_icon.setVisibility(View.VISIBLE);
                fragment = new HelpContactFragment();
                title = getString(R.string.help_contact);
                break;

            case 5:
                showLogOutDialog();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            home_title.setText(title);
        }
    }

    /* Show LogOut Dialog*/
    private void showLogOutDialog() {
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPath2);
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .content(getString(R.string.message))
                .typeface(tf2, tf)
                .positiveText(getString(R.string.yes))
                .negativeText(getString(R.string.no));

        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
                finish();

                Preference.setLogOut(HomeActivity.this, true);
                Utilities.passActivityIntent(HomeActivity.this, LogInJoinActivity.class);
            }
        })

                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });

        MaterialDialog dialog = builder.build();
        dialog.show();
    }

    /* Show Exit Dialog*/
    private void showExitDialog() {
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPath2);
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this)
                .content(getString(R.string.message_exit))
                .typeface(tf2, tf)
                .positiveText(getString(R.string.yes))
                .negativeText(getString(R.string.no));

        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                dialog.dismiss();
                finish();
            }
        })

                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });

        MaterialDialog dialog = builder.build();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (MyService.getInstance() != null) {
            MyService.getInstance().stopFusedLocation();
        }
        finish();
    }

    /*Navigate Drawer Open Close*/
    private void navigateDrawer() {
        if (drawerFragment.getDrawerLayout().isDrawerOpen(GravityCompat.START)) {
            drawerFragment.getDrawerLayout().closeDrawers();
        } else {
            drawerFragment.getDrawerLayout().openDrawer(drawerFragment.getContainerView());
        }

    }

    /* Get Coordinate Layout */
    public CoordinatorLayout getCoordinateLayout() {
        return coordinatorLayout;
    }

    @Override
    public void onBackPressed() {
        if (drawerFragment.getDrawerLayout().isDrawerOpen(GravityCompat.START)) {
            drawerFragment.getDrawerLayout().closeDrawers();
            return;

        } else if (!drawerFragment.getDrawerLayout().isDrawerVisible(GravityCompat.START)) {
            showExitDialog();
            return;
        }

        //super.onBackPressed();

    }

}
