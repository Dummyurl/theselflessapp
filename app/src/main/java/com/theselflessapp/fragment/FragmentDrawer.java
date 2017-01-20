package com.theselflessapp.fragment;

import android.content.Context;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.theselflessapp.R;
import com.theselflessapp.adapters.NavigationDrawerAdapter;
import com.theselflessapp.interfaces.Constant;
import com.theselflessapp.modal.LogInPOJO;
import com.theselflessapp.utility.NavDrawerItem;
import com.theselflessapp.utility.Utilities;

import java.util.ArrayList;
import java.util.List;


public class FragmentDrawer extends Fragment implements Constant {

    private static String TAG = FragmentDrawer.class.getSimpleName();
    private static TypedArray imgs;
    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationDrawerAdapter adapter;
    private View containerView;
    private static String[] titles = null;
    private FragmentDrawerListener drawerListener;
    private SimpleDraweeView img_profile;
    private TextView tv_profile_name, tv_profile_des;
    private LogInPOJO logInPojo;

    public FragmentDrawer() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }


    public static List<NavDrawerItem> getData() {
        List<NavDrawerItem> data = new ArrayList<>();

        // preparing navigation drawer items
        for (int i = 0; i < titles.length; i++) {
            NavDrawerItem navItem = new NavDrawerItem();
            navItem.setTitle(titles[i]);
            navItem.setIcons(imgs.getResourceId(i, -1));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //imgs.recycle();
            } else {
                imgs.recycle();
            }

            data.add(navItem);
        }
        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // drawer labels
        titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);

        // drawer icons
        imgs = getActivity().getResources().obtainTypedArray(R.array.nav_drawer_icons);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        img_profile = (SimpleDraweeView) layout.findViewById(R.id.img_profile);
        tv_profile_name = (TextView) layout.findViewById(R.id.tv_profile_name);
        Utilities.setTypeFace2(getActivity(), tv_profile_name);
        tv_profile_des = (TextView) layout.findViewById(R.id.tv_profile_des);
        Utilities.setTypeFace(getActivity(), tv_profile_des);

        adapter = new NavigationDrawerAdapter(getActivity(), getData());
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                drawerListener.onDrawerItemSelected(view, position);
                mDrawerLayout.closeDrawer(containerView);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return layout;
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerLayout.setScrimColor(getResources().getColor(android.R.color.transparent));
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout,
                R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                //toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    public DrawerLayout getDrawerLayout() {

        return mDrawerLayout;
    }

    public View getContainerView() {

        return containerView;
    }

    /* Set User Information */
    public void setUserInformation(LogInPOJO logInPojo) {
        this.logInPojo = logInPojo;
        if (logInPojo.getData().get(0).getUsername() != null && !logInPojo.getData().get(0).getUsername().equals("")) {
            tv_profile_name.setText(logInPojo.getData().get(0).getUsername());
        } else {
            tv_profile_name.setText(getString(R.string.user_name));
        }

        if (!logInPojo.getData().get(0).getGender().equals("") || !logInPojo.getData().get(0).getCountry().equals("") ||
                !logInPojo.getData().get(0).getDateCreated().equals("")) {
            tv_profile_des.setText(logInPojo.getData().get(0).getGender() + " " + getString(R.string.from) + " " +
                    logInPojo.getData().get(0).getCountry() + "\n"
                    + getString(R.string.active_member) + " " +
                    Utilities.formatDateFromString(logInPojo.getData().get(0).getDateCreated()) + "\n"
                    + logInPojo.getData().get(0).getTotalPhotos() + " " + getString(R.string.memory_snaped) + " | " + logInPojo.getData().get(0).getMemoryGiven()
                    + " " + getString(R.string.memory_given));
        }

        if (logInPojo.getData().get(0).getImage() != null && !logInPojo.getData().get(0).getImage().equals("")) {

            Uri uri = Uri.parse(logInPojo.getData().get(0).getImage());
            img_profile.setImageURI(uri);

        }

    }


    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }


    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }
}
