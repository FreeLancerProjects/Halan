package com.semicolon.Halan.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.semicolon.Halan.Models.Finishied_Order_Model;
import com.semicolon.Halan.Models.MyOrderModel;
import com.semicolon.Halan.R;
import com.semicolon.Halan.Services.Tags;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import de.hdodenhof.circleimageview.CircleImageView;
import me.anwarshahriar.calligrapher.Calligrapher;

public class ClientOrderDetailsActivity extends AppCompatActivity {

    private ImageView back;
    private TextView order_date,txt_order_from,txt_order_detail,txt_order_to,driver_name,car_model,car_number,cost,delivery_time;
    private Button close;
    private MyOrderModel myOrderModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_order_details);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "JannaLT-Regular.ttf", true);
        EventBus.getDefault().register(this);
        initView();
        getDataFromIntent();
    }
    private void initView()
    {
        back = findViewById(R.id.back);
        order_date = findViewById(R.id.txt_order_date);
        txt_order_from = findViewById(R.id.txt_order_from);
        txt_order_to = findViewById(R.id.txt_order_to);
        txt_order_detail = findViewById(R.id.txt_order_details);
        driver_name = findViewById(R.id.driver_name);
        car_model = findViewById(R.id.car_model);
        car_number = findViewById(R.id.car_number);
        //delivery_time = findViewById(R.id.delivery_time);
        cost = findViewById(R.id.txt_cost);
        close = findViewById(R.id.close);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void getDataFromIntent() {
        Intent intent =  getIntent();
        if (intent!=null)
        {
            myOrderModel = (MyOrderModel) intent.getSerializableExtra("order");
            UpdateUi(myOrderModel);
        }
    }

    private void UpdateUi(MyOrderModel myOrderModel)
    {
        try
        {
            order_date.setText(myOrderModel.getOrder_date());
            txt_order_from.setText(myOrderModel.getMarket_location());
            txt_order_to.setText(myOrderModel.getClient_location());
            txt_order_detail.setText(myOrderModel.getOrder_details());
            driver_name.setText(myOrderModel.getDriver_name());
            car_model.setText(myOrderModel.getDriver_car_model());
            car_number.setText(myOrderModel.getDriver_car_num());
            cost.setText(myOrderModel.getCost());

        }catch (NullPointerException e)
        {

        }

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

        Picasso.with(ClientOrderDetailsActivity.this).load(Uri.parse(Tags.ImgPath+finishied_order_model.getDriver_image())).into(driver_img);
        driver_name.setText(finishied_order_model.getDriver_name());
        order_details.setText(finishied_order_model.getOrder_details());
        Button addRateBtn = view.findViewById(R.id.add_rate);
        final AlertDialog alertDialog = new AlertDialog.Builder(ClientOrderDetailsActivity.this)
                .setCancelable(false)
                .setView(view)
                .create();

        alertDialog.show();
        addRateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClientOrderDetailsActivity.this,AddRateActivity.class);
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
