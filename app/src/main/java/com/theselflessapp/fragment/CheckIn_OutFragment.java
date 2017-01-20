package com.theselflessapp.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.theselflessapp.R;
import com.theselflessapp.activity.HomeActivity;
import com.theselflessapp.activity.LogInActivity;
import com.theselflessapp.interfaces.Constant;
import com.theselflessapp.interfaces.RestClient;
import com.theselflessapp.modal.CheckInCheckOutPOJO;
import com.theselflessapp.modal.LogInPOJO;
import com.theselflessapp.prefrence.Preference;
import com.theselflessapp.services.MyService;
import com.theselflessapp.utility.Utilities;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CheckIn_OutFragment extends Fragment implements Constant, View.OnClickListener,
        Callback<CheckInCheckOutPOJO> {
    private MapView mMapView;
    private GoogleMap googleMap;
    private Button checkIn_Out;
    private String status = ZERO;
    private ProgressBar progressBar;
    private LogInPOJO logInPojo = null;

    public CheckIn_OutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_location, container, false);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        checkIn_Out = (Button) rootView.findViewById(R.id.checkIn_Out);
        checkIn_Out.setOnClickListener(this);
        Utilities.setTypeFace(getActivity(), checkIn_Out);

         /* Get LogIn User Details */
        if (Preference.getUser(getActivity()) != null) {
            logInPojo = Preference.getUser(getActivity());
            if (logInPojo.getData().get(0).getStatus().equals(ONE)) {
                status = ONE;
                checkIn_Out.setText(getString(R.string.checkOut));
            } else {
                status = ZERO;
                checkIn_Out.setText(getString(R.string.checkIn));
            }
        }

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                googleMap.setMyLocationEnabled(true);

                if (MyService.getInstance().getLocation() != null) {

                    // For dropping a marker at a point on the Map
                    LatLng sydney = new LatLng(MyService.getInstance().getFusedLatitude(), MyService.getInstance().getFusedLongitude());
                    googleMap.addMarker(new MarkerOptions().position(sydney));

                    // For zooming automatically to the location of the marker
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    checkIn_Out.setEnabled(true);
                    checkIn_Out.setBackgroundColor(Color.parseColor("#19A3FF"));

                } else {
                    checkIn_Out.setEnabled(false);
                    checkIn_Out.setBackgroundColor(Color.parseColor("#CCCCCC"));
                    Utilities.showSettingsAlert(getActivity());
                }

            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkIn_Out:
                if (Utilities.isNetworkAvailable(getActivity())) {
                    if (checkIn_Out.getText().equals(getString(R.string.checkIn))) {
                        status = ONE;
                        checkInCheckOutRequestCall();

                    } else {
                        status = ZERO;
                        checkInCheckOutRequestCall();

                    }
                } else {
                    Utilities.showSnackBarInternet(getActivity(), HomeActivity.getInstance().getCoordinateLayout(),
                            getString(R.string.no_internet_connection));
                }

                break;
            default:
                break;
        }
    }

    /* Make CheckIn CheckOut Request Call */
    private void checkInCheckOutRequestCall() {
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

        Call<CheckInCheckOutPOJO> call = restClientAPI.checkInCheckOutRequest(
                Preference.getUserId(getActivity()),
                String.valueOf(MyService.getInstance().getFusedLatitude()),
                String.valueOf(MyService.getInstance().getFusedLongitude()),
                status);

        //asynchronous call
        call.enqueue(this);
    }

    /* Get Response From LogIn */
    @Override
    public void onResponse(Call<CheckInCheckOutPOJO> call, Response<CheckInCheckOutPOJO> response) {

        CheckInCheckOutPOJO checkInCheckOutPOJO = response.body();

        int code = response.code();
        if (code == RESPONSE_CODE) {

            Utilities.dismissProgressDialog(getActivity(), progressBar);

            if (checkInCheckOutPOJO.getSuccess() == SUCCESS) {

                 /* Set CheckIn-Out User Details */
                LogInPOJO logInPOJO = Preference.getUser(getActivity());
                List list = checkInCheckOutPOJO.getData();
                logInPOJO.setData(list);
                Preference.setUser(getActivity(), logInPOJO);

                if (checkInCheckOutPOJO.getData().get(0).getId() != null && !checkInCheckOutPOJO.getData().get(0).getId().equals("")) {
                    Preference.setUserId(getActivity(), String.valueOf(checkInCheckOutPOJO.getData().get(0).getId()));
                }

                if (checkIn_Out.getText().equals(getString(R.string.checkIn))) {
                    checkIn_Out.setText(getString(R.string.checkOut));
                    Utilities.showSnackBarWithCallBackLoadFragment(getActivity(),
                            HomeActivity.getInstance().getCoordinateLayout(),
                            String.valueOf(getString(R.string.checkin_success)), 1);

                } else {
                    checkIn_Out.setText(getString(R.string.checkIn));
                    Utilities.showSnackBar(getActivity(), HomeActivity.getInstance().getCoordinateLayout(),
                            String.valueOf(getString(R.string.checkout_success)));
                }


            } else {
                Utilities.showSnackBar(getActivity(), HomeActivity.getInstance().getCoordinateLayout(),
                        String.valueOf(checkInCheckOutPOJO.getMessage()));
            }

        } else {

            Utilities.dismissProgressDialog(getActivity(), progressBar);
        }
    }

    @Override
    public void onFailure(Call<CheckInCheckOutPOJO> call, Throwable t) {
        Utilities.dismissProgressDialog(getActivity(), progressBar);
        Utilities.showSnackBar(getActivity(), HomeActivity.getInstance().getCoordinateLayout(),
                String.valueOf(getString(R.string.failed_to_connect_with_server)));

    }
}
