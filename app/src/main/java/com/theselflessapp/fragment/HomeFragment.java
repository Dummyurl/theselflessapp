package com.theselflessapp.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.theselflessapp.R;
import com.theselflessapp.activity.HomeActivity;
import com.theselflessapp.activity.RecentMemoryGivenActivity;
import com.theselflessapp.interfaces.Constant;
import com.theselflessapp.interfaces.RestClient;
import com.theselflessapp.modal.RecentCheckInPOJO;
import com.theselflessapp.prefrence.Preference;
import com.theselflessapp.utility.Utilities;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeFragment extends Fragment implements Constant,
        Callback<RecentCheckInPOJO> {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private SectionedRecyclerViewAdapter sectionAdapter;
    private TextView text_snap_message;
    private int lastPosition = -1;
    private static HomeFragment mContext;
    private String user_id;
    private String image;
    private String imageTime;
    private String headerTitle;

    public HomeFragment() {
        // Required empty public constructor
    }

    // HomeActivity Instance
    public static HomeFragment getInstance() {
        return mContext;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = HomeFragment.this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        text_snap_message = (TextView) rootView.findViewById(R.id.text_snap_message);
        text_snap_message.setVisibility(View.GONE);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        sectionAdapter = new SectionedRecyclerViewAdapter();
        setAdapter();
        recentCheckInRequestCall();
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
    }


    public void setUserId(String user_id) {
        this.user_id = user_id;
    }

    public String getUserId() {
        return user_id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {

        return image;
    }

    public void setImageTime(String imageTime) {
        this.imageTime = imageTime;
    }

    public String getImageTime() {
        return imageTime;
    }

    class MovieSection extends StatelessSection {

        String title;
        List<Movie> list;

        public MovieSection(String title, List<Movie> list) {
            super(R.layout.home_header, R.layout.home_items);
            this.title = title;
            this.list = list;
        }

        @Override
        public int getContentItemsTotal() {
            return list.size();
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final ItemViewHolder itemHolder = (ItemViewHolder) holder;

            Uri uri = Uri.parse(list.get(position).getImage());
            itemHolder.imgItem.setImageURI(uri);

            itemHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Utilities.isNetworkAvailable(getActivity())) {

                        setUserId(list.get(position).getUserId());
                        setImage(list.get(position).getImage());
                        setImageTime(list.get(position).getImageTime());

                        Utilities.passActivityIntent(getActivity(),
                                RecentMemoryGivenActivity.class);

                    } else {
                        Utilities.showSnackBarInternet(getActivity(),
                                HomeActivity.getInstance().getCoordinateLayout(),
                                getString(R.string.no_internet_connection));
                    }

                }
            });
        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new HeaderViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.tvTitle.setText(title);
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitle;

        public HeaderViewHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            Utilities.setTypeFace(getActivity(), tvTitle);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final View rootView;
        private final SimpleDraweeView imgItem;

        public ItemViewHolder(View view) {
            super(view);

            rootView = view;
            imgItem = (SimpleDraweeView) view.findViewById(R.id.img_home_item);

        }
    }

    /* Make Snap A Memory Request Call */
    private void recentCheckInRequestCall() {
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

        Call<RecentCheckInPOJO> call = restClientAPI.recentCheckInRequest(
                Preference.getUserId(getActivity()));

        //asynchronous call
        call.enqueue(this);
    }

    /* Get Response From LogIn */
    @Override
    public void onResponse(Call<RecentCheckInPOJO> call, Response<RecentCheckInPOJO> response) {
        RecentCheckInPOJO recentCheckInPOJO = response.body();

        int code = response.code();
        if (code == RESPONSE_CODE) {

            Utilities.dismissProgressDialog(getActivity(), progressBar);

            if (recentCheckInPOJO.getSuccess() == SUCCESS) {

                recyclerView.setVisibility(View.VISIBLE);
                text_snap_message.setVisibility(View.GONE);

                if (recentCheckInPOJO.getData() != null && recentCheckInPOJO.getData().size() > 0) {

                    for (int i = 0; i < recentCheckInPOJO.getData().size(); i++) {

                        if (recentCheckInPOJO.getData().get(i).size() > 0) {

                            List<Movie> imageList = new ArrayList<Movie>();

                            headerTitle = Utilities.formatDateFromString2(recentCheckInPOJO.getData().get(i).get(0).getCheckinTime())
                                    + " " + getString(R.string.checkIn) + ":" + " " +
                                    Utilities.formatDateFromString3(recentCheckInPOJO.getData().get(i).get(0).getCheckinTime())
                                    + " " + getString(R.string.checkOut) + ":" + " " +
                                    Utilities.formatDateFromString3(recentCheckInPOJO.getData().get(i).get(0).getCheckoutTime());

                            for (int j = 0; j < recentCheckInPOJO.getData().get(i).size(); j++) {
                                Movie movie = new Movie();
                                movie.setImage(recentCheckInPOJO.getData().get(i).get(j).getPhotoName());
                                movie.setUserId(recentCheckInPOJO.getData().get(i).get(j).getUserid());
                                movie.setImageTime(recentCheckInPOJO.getData().get(i).get(j).getTimess());
                                imageList.add(movie);
                            }

                            sectionAdapter.addSection(new MovieSection(headerTitle, imageList));

                        }
                    }

                    setAdapter();

                } else {
                    recyclerView.setVisibility(View.GONE);
                    text_snap_message.setVisibility(View.VISIBLE);
                }

            } else {

                recyclerView.setVisibility(View.GONE);
                text_snap_message.setVisibility(View.VISIBLE);

            }

        } else {

            Utilities.dismissProgressDialog(getActivity(), progressBar);
            recyclerView.setVisibility(View.GONE);
            text_snap_message.setVisibility(View.VISIBLE);

        }
    }

    // Set Adapter For Image List Tagged Memory Snapped By User
    private void setAdapter() {
        GridLayoutManager glm = new GridLayoutManager(getActivity(), 3);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (sectionAdapter.getSectionItemViewType(position)) {
                    case SectionedRecyclerViewAdapter.VIEW_TYPE_HEADER:
                        return 3;
                    default:
                        return 1;
                }
            }
        });
        recyclerView.setLayoutManager(glm);
        recyclerView.setAdapter(sectionAdapter);
    }

    @Override
    public void onFailure(Call<RecentCheckInPOJO> call, Throwable t) {
        Utilities.dismissProgressDialog(getActivity(), progressBar);
        recyclerView.setVisibility(View.GONE);
        text_snap_message.setVisibility(View.VISIBLE);
        Utilities.showSnackBar(getActivity(), HomeActivity.getInstance().getCoordinateLayout(),
                String.valueOf(getString(R.string.failed_to_connect_with_server)));

    }

    public class Movie {
        String image;
        String userId;
        String imageTime;

        public String getImageTime() {
            return imageTime;
        }

        public void setImageTime(String imageTime) {
            this.imageTime = imageTime;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }


    }
}
