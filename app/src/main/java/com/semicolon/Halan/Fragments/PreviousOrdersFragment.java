package com.semicolon.Halan.Fragments;

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
import com.semicolon.Halan.R;
import com.semicolon.Halan.Services.Api;
import com.semicolon.Halan.Services.Services;
import com.semicolon.Halan.Services.Tags;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PreviousOrdersFragment extends Fragment {

    ArrayList<MyOrderModel> model;
    MyOrdersAdapter adapter;
    RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private ProgressBar progBar;
    private LinearLayout nodata_container;
    private SwipeRefreshLayout sr;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_previous_orders, container, false);

        recyclerView = view.findViewById(R.id.rec_previous);

        model = new ArrayList<>();

        mLayoutManager=new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new MyOrdersAdapter(getContext(), model);
        recyclerView.setAdapter(adapter);
        sr =view. findViewById(R.id.sr);
        sr.setRefreshing(false);

        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getContext(),R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        nodata_container = view.findViewById(R.id.nodata_container);
        sr.setColorSchemeResources(R.color.colorPrimary);
        sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromServer();
            }
        });
        getDataFromServer();


        return view;
    }


    private void getDataFromServer() {
        progBar.setVisibility(View.VISIBLE);
        Services services= Api.getClient(Tags.BASE_URL).create(Services.class);
        Call<List<MyOrderModel>> call=services.getPreviousOrders();
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


}
