package com.semicolon.Halan.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.semicolon.Halan.Models.MyOrderModel;
import com.semicolon.Halan.R;

public class ClientOrderDetailsActivity extends AppCompatActivity {

    private ImageView back;
    private TextView order_date,txt_order_from,txt_order_detail,txt_order_to,driver_name,car_model,car_number,cost,delivery_time;
    private Button close;
    private MyOrderModel myOrderModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_order_details);
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


}
