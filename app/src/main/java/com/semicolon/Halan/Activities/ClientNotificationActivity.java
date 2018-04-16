package com.semicolon.Halan.Activities;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.semicolon.Halan.Adapters.ClientNotificationAdapter;
import com.semicolon.Halan.Models.ClientNotificationModel;
import com.semicolon.Halan.Models.ResponseModel;
import com.semicolon.Halan.Models.UserModel;
import com.semicolon.Halan.R;
import com.semicolon.Halan.Services.Api;
import com.semicolon.Halan.Services.Services;
import com.semicolon.Halan.Services.Tags;
import com.semicolon.Halan.SingleTone.Users;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ClientNotificationActivity extends AppCompatActivity implements Users.UserData{
    private Users users;
    private UserModel userModel;
    private SwipeRefreshLayout swipe_refresh;
    private RecyclerView recView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    private LinearLayout no_not_container;
    private ImageView back;
    private ProgressBar bar;
    private List<ClientNotificationModel> clientNotificationModelList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_notification);
        initView();

        users = Users.getInstance();
        users.getUserData(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        DisplayNotification();
    }

    private void initView()
    {
        swipe_refresh = findViewById(R.id.swipe_refresh);
        swipe_refresh.setRefreshing(false);
        swipe_refresh.setColorSchemeColors(ContextCompat.getColor(this,R.color.colorPrimary), Color.BLUE,Color.RED,ContextCompat.getColor(this,R.color.rate));
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                DisplayNotification();
            }

        });
        recView = findViewById(R.id.recView);
        manager = new LinearLayoutManager(this);
        back = findViewById(R.id.back);
        recView.setLayoutManager(manager);
        recView.setHasFixedSize(true);
        no_not_container = findViewById(R.id.no_not_container);
        bar = findViewById(R.id.progBar);
        bar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        clientNotificationModelList = new ArrayList<>();
        adapter = new ClientNotificationAdapter(this,clientNotificationModelList);
        recView.setAdapter(adapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void DisplayNotification()
    {
        Retrofit retrofit = Api.getClient(Tags.BASE_URL);
        Services services = retrofit.create(Services.class);
        Call<List<ClientNotificationModel>> call = services.getclientNotification(userModel.getUser_id());
        call.enqueue(new Callback<List<ClientNotificationModel>>() {
            @Override
            public void onResponse(Call<List<ClientNotificationModel>> call, Response<List<ClientNotificationModel>> response) {
                if (response.isSuccessful())
                {
                    clientNotificationModelList.clear();
                    bar.setVisibility(View.GONE);
                    swipe_refresh.setRefreshing(false);
                    if (response.body().size()>0)
                    {
                        no_not_container.setVisibility(View.GONE);
                        clientNotificationModelList.addAll(response.body());
                        adapter.notifyDataSetChanged();
                    }else
                    {
                        bar.setVisibility(View.GONE);
                        no_not_container.setVisibility(View.VISIBLE);

                    }
                }
            }

            @Override
            public void onFailure(Call<List<ClientNotificationModel>> call, Throwable t) {
                swipe_refresh.setRefreshing(false);
                Log.e("Error",t.getMessage());
                Toast.makeText(ClientNotificationActivity.this,getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void UserDataSuccess(UserModel userModel) {
        this.userModel = userModel;
    }

    public void setPos(int pos,String action_type)
    {

        ClientNotificationModel model = clientNotificationModelList.get(pos);

        if (action_type.equals(Tags.accept))
        {
            SendAccept(model);

        }else if (action_type.equals(Tags.refuse))
        {
            SendRefuse(model);
        }
    }



    private void SendAccept(ClientNotificationModel model) {

        Map<String,String> map = new HashMap<>();
        map.put("action","1");
        map.put("message_id",model.getMessage_id());
        map.put("order_id_fk",model.getOrder_id_fk());
        map.put("driver_id_fk",model.getDriver_id_fk());
        map.put("user_id",userModel.getUser_id());

        Retrofit retrofit = Api.getClient(Tags.BASE_URL);
        Services services = retrofit.create(Services.class);
        Call<ResponseModel> call = services.sendClientRequest_Accept(map);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess()==1)
                    {
                        Toast.makeText(ClientNotificationActivity.this, "تم إرسال ردك الي السائق", Toast.LENGTH_LONG).show();
                    }else
                        {
                            Toast.makeText(ClientNotificationActivity.this, "لم تم إرسال ردك الي السائق حاول مره أخرى لاحقا", Toast.LENGTH_LONG).show();

                        }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e("Error",t.getMessage());
                Toast.makeText(ClientNotificationActivity.this, getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SendRefuse(ClientNotificationModel model) {

        Map<String,String> map = new HashMap<>();
        map.put("action","2");
        map.put("message_id",model.getMessage_id());
        map.put("order_id_fk",model.getOrder_id_fk());
        map.put("driver_id_fk",model.getDriver_id_fk());
        map.put("user_id",userModel.getUser_id());

        Retrofit retrofit = Api.getClient(Tags.BASE_URL);
        Services services = retrofit.create(Services.class);
        Call<ResponseModel> call = services.sendClientRequest_Refuse(map);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess()==1)
                    {
                        Toast.makeText(ClientNotificationActivity.this, "تم إرسال ردك الي السائق", Toast.LENGTH_LONG).show();
                    }else
                    {
                        Toast.makeText(ClientNotificationActivity.this, "لم تم إرسال ردك الي السائق حاول مره أخرى لاحقا", Toast.LENGTH_LONG).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e("Error",t.getMessage());
                Toast.makeText(ClientNotificationActivity.this, getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
