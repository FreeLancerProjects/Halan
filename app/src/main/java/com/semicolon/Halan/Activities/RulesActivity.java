package com.semicolon.Halan.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.semicolon.Halan.Models.AboutUsModel;
import com.semicolon.Halan.Models.Finishied_Order_Model;
import com.semicolon.Halan.R;
import com.semicolon.Halan.Services.Api;
import com.semicolon.Halan.Services.Services;
import com.semicolon.Halan.Services.Tags;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RulesActivity extends AppCompatActivity {
    private TextView title, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "JannaLT-Regular.ttf", true);
        EventBus.getDefault().register(this);
        initView();
        GetDataFromServer();
    }

    private void initView() {
        title = findViewById(R.id.txt_title);
        content = findViewById(R.id.txt_content);

    }

    private void GetDataFromServer() {

        Services services = Api.getClient(Tags.BASE_URL).create(Services.class);
        Call<List<AboutUsModel>> call = services.GetAboutUs();
        call.enqueue(new Callback<List<AboutUsModel>>() {
            @Override
            public void onResponse(Call<List<AboutUsModel>> call, Response<List<AboutUsModel>> response) {

                if (response.isSuccessful()) {
                    title.setText(response.body().get(2).getTitle());
                    content.setText(response.body().get(2).getContent());
                }
            }

            @Override
            public void onFailure(Call<List<AboutUsModel>> call, Throwable t) {
                Log.e("error",t.getMessage());
            }
        });
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Driver_delivered_Order(Finishied_Order_Model finishied_order_model)
    {
        CreateCustomAlertDialog(finishied_order_model);
    }

    private void CreateCustomAlertDialog(final Finishied_Order_Model finishied_order_model) {
        View view = LayoutInflater.from(this).inflate(R.layout.custom_alert_dialog,null);
        CircleImageView driver_img = view.findViewById(R.id.driver_image);
        TextView driver_name = view.findViewById(R.id.driver_name);
        TextView order_details = view.findViewById(R.id.order_details);

        Picasso.with(RulesActivity.this).load(Uri.parse(Tags.ImgPath+finishied_order_model.getDriver_image())).into(driver_img);
        driver_name.setText(finishied_order_model.getDriver_name());
        order_details.setText(finishied_order_model.getOrder_details());
        Button addRateBtn = view.findViewById(R.id.add_rate);
        final AlertDialog alertDialog = new AlertDialog.Builder(RulesActivity.this)
                .setCancelable(false)
                .setView(view)
                .create();

        alertDialog.show();
        addRateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RulesActivity.this,AddRateActivity.class);
                intent.putExtra("driver_id",finishied_order_model.getDriver_id());
                intent.putExtra("order_id",finishied_order_model.getOrder_id());
                intent.putExtra("driver_name",finishied_order_model.getDriver_name());
                intent.putExtra("driver_image",finishied_order_model.getDriver_image());
                startActivity(intent);
                alertDialog.dismiss();


            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
