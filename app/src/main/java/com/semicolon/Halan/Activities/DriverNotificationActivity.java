package com.semicolon.Halan.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.semicolon.Halan.Adapters.DriverNotficationAdapter;
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

public class DriverNotificationActivity extends AppCompatActivity implements Users.UserData{
    ArrayList<MyOrderModel> model;
    DriverNotficationAdapter adapter;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_notification);
        users = Users.getInstance();
        preferences=new Preferences(this);
        users.getUserData(this);
        initView();
        getDataFromServer();
    }

    private void initView() {
        recyclerView =findViewById(R.id.rec_driver_notfi);
        sr = findViewById(R.id.sr);

        model = new ArrayList<>();

        mLayoutManager=new LinearLayoutManager(this);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapter = new DriverNotficationAdapter(this, model);
        recyclerView.setAdapter(adapter);
        sr.setRefreshing(false);

        progBar = findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        nodata_container =findViewById(R.id.nodata_container);
        sr.setColorSchemeColors(ContextCompat.getColor(this,R.color.colorPrimary),ContextCompat.getColor(this,R.color.rate), Color.RED,Color.BLUE);
        sr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromServer();
            }
        });
    }
    private void getDataFromServer() {
        Services services= Api.getClient(Tags.BASE_URL).create(Services.class);
        Call<List<MyOrderModel>> call=services.getNotification(userId);
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
        this.userModel = userModel;
        userId=userModel.getUser_id();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,DriverOrdersActivity.class);
        startActivity(intent);
        finish();
    }
}
