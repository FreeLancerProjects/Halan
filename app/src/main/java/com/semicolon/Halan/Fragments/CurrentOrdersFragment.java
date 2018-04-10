package com.semicolon.Halan.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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


public class CurrentOrdersFragment extends Fragment {

    ArrayList<MyOrderModel> model;
    MyOrdersAdapter adapter;
    RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;






    public CurrentOrdersFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view= inflater.inflate(R.layout.fragment_current_orders, container, false);

        recyclerView = view.findViewById(R.id.rec_current);

        model = new ArrayList<>();

        mLayoutManager=new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new MyOrdersAdapter(getContext(), model);
        recyclerView.setAdapter(adapter);

        Services services= Api.getClient(Tags.BASE_URL).create(Services.class);
        Call<List<MyOrderModel>> call=services.getCurrentOrders();
        call.enqueue(new Callback<List<MyOrderModel>>() {
            @Override
            public void onResponse(Call<List<MyOrderModel>> call, Response<List<MyOrderModel>> response) {

                model.clear();
                model.addAll( response.body());
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onFailure(Call<List<MyOrderModel>> call, Throwable t) {

            }
        });

        return view;
    }




}
