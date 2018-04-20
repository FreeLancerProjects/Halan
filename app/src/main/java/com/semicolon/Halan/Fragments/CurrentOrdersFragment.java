package com.semicolon.Halan.Fragments;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.semicolon.Halan.Adapters.MyOrdersAdapter;
import com.semicolon.Halan.Models.MyOrderModel;
import com.semicolon.Halan.Models.UserModel;
import com.semicolon.Halan.R;
import com.semicolon.Halan.Services.Api;
import com.semicolon.Halan.Services.Preferences;
import com.semicolon.Halan.Services.Services;
import com.semicolon.Halan.Services.Tags;
import com.semicolon.Halan.SingleTone.Users;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CurrentOrdersFragment extends Fragment implements Users.UserData {

    ArrayList<MyOrderModel> model;
    MyOrdersAdapter adapter;
    RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private ProgressBar progBar;
    private LinearLayout nodata_container;
    private SwipeRefreshLayout sr;
    private Preferences preferences;
    private UserModel userModel;
    Users users;
    private String userId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_current_orders, container, false);

        users=Users.getInstance();
        preferences=new Preferences(getContext());
        users.getUserData(this);
        recyclerView = view.findViewById(R.id.rec_current);
        sr =view. findViewById(R.id.sr);

        model = new ArrayList<>();

        mLayoutManager=new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new MyOrdersAdapter(getActivity(), model,Tags.current_order,userModel.getUser_type());
        recyclerView.setAdapter(adapter);
        sr.setRefreshing(false);

        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getContext(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        nodata_container = view.findViewById(R.id.nodata_container);
        sr.setColorSchemeColors(ContextCompat.getColor(getActivity(),R.color.colorPrimary),ContextCompat.getColor(getActivity(),R.color.rate), Color.RED,Color.BLUE);

        sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromServer();
            }
        });



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getDataFromServer();

    }

    private void getDataFromServer() {
        //progBar.setVisibility(View.VISIBLE);

        if (userModel.getUser_type().equals(Tags.Client))
        {
            getClient_Current_Order();
        }else if (userModel.getUser_type().equals(Tags.Driver))
        {
            getDriver_Current_Order();
        }

    }

    private void getClient_Current_Order()
    {
        Services services= Api.getClient(Tags.BASE_URL).create(Services.class);
        Call<List<MyOrderModel>> call=services.getCurrentOrders_Client(userId);
        call.enqueue(new Callback<List<MyOrderModel>>() {
            @Override
            public void onResponse(Call<List<MyOrderModel>> call, Response<List<MyOrderModel>> response) {

                model.clear();
                model.addAll( response.body());
                if (model.size()>0){
                    adapter.notifyDataSetChanged();
                    progBar.setVisibility(View.GONE);
                    sr.setRefreshing(false);
                    // Toast.makeText(Activities.this, "no activities", Toast.LENGTH_SHORT).show();
                }else {
                    progBar.setVisibility(View.GONE);
                    nodata_container.setVisibility(View.VISIBLE);
                    sr.setRefreshing(false);
                }

            }

            @Override
            public void onFailure(Call<List<MyOrderModel>> call, Throwable t) {
                nodata_container.setVisibility(View.GONE);
                sr.setRefreshing(false);

            }
        });
    }

    private void getDriver_Current_Order()
    {
        Services services= Api.getClient(Tags.BASE_URL).create(Services.class);
        Call<List<MyOrderModel>> call=services.getCurrentOrders(userId);
        call.enqueue(new Callback<List<MyOrderModel>>() {
            @Override
            public void onResponse(Call<List<MyOrderModel>> call, Response<List<MyOrderModel>> response) {

                model.clear();
                model.addAll( response.body());
                if (model.size()>0){
                    adapter.notifyDataSetChanged();
                    progBar.setVisibility(View.GONE);
                    sr.setRefreshing(false);
                    // Toast.makeText(Activities.this, "no activities", Toast.LENGTH_SHORT).show();
                }else {
                    progBar.setVisibility(View.GONE);
                    nodata_container.setVisibility(View.VISIBLE);
                    sr.setRefreshing(false);
                }

            }

            @Override
            public void onFailure(Call<List<MyOrderModel>> call, Throwable t) {
                nodata_container.setVisibility(View.GONE);
                sr.setRefreshing(false);

            }
        });
    }



    @Override
    public void UserDataSuccess(UserModel userModel) {

        this.userModel=userModel;
        userId=userModel.getUser_id();
    }




}
