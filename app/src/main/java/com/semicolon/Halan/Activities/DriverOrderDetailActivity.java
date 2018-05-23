package com.semicolon.Halan.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import de.hdodenhof.circleimageview.CircleImageView;
import me.anwarshahriar.calligrapher.Calligrapher;

public class DriverOrderDetailActivity extends AppCompatActivity {

    private ImageView back;
    private TextView order_date,txt_order_from,txt_order_detail,txt_order_to,client_name,client_phone,client_email,cost,delivery_time;
    private Button close;
    private MyOrderModel myOrderModel;
    private CircleImageView client_photo;
    private Target target;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_order_detail);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "JannaLT-Regular.ttf", true);
        EventBus.getDefault().register(this);
        initView();
        getDataFromIntent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Picasso.with(this).cancelRequest(target);
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        back = findViewById(R.id.back);
        order_date = findViewById(R.id.txt_order_date);
        txt_order_from = findViewById(R.id.txt_order_from);
        txt_order_to = findViewById(R.id.txt_order_to);
        txt_order_detail = findViewById(R.id.txt_order_details);
        client_name = findViewById(R.id.client_name);
        client_phone = findViewById(R.id.client_phone);
        client_email = findViewById(R.id.client_email);
        //delivery_time = findViewById(R.id.delivery_time);
        client_photo = findViewById(R.id.client_photo);
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

    private void UpdateUi(MyOrderModel myOrderModel) {
        try {
            order_date.setText(myOrderModel.getOrder_date());
            txt_order_from.setText(myOrderModel.getMarket_location());
            txt_order_to.setText(myOrderModel.getClient_location());
            txt_order_detail.setText(myOrderModel.getOrder_details());
            client_name.setText(myOrderModel.getClient_name());
            client_phone.setText(myOrderModel.getClient_phone());
            client_email.setText(myOrderModel.getClient_email());
            cost.setText(myOrderModel.getCost());

            target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    client_photo.setImageBitmap(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };

            if (!myOrderModel.getClient_photo().equals("0") || !TextUtils.isEmpty(myOrderModel.getClient_photo())|| myOrderModel.getClient_photo()!=null)
            {
                Picasso.with(this).load(Uri.parse(Tags.ImgPath+myOrderModel.getClient_photo())).into(target);
            }
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

        Picasso.with(DriverOrderDetailActivity.this).load(Uri.parse(Tags.ImgPath+finishied_order_model.getDriver_image())).into(driver_img);
        driver_name.setText(finishied_order_model.getDriver_name());
        order_details.setText(finishied_order_model.getOrder_details());
        Button addRateBtn = view.findViewById(R.id.add_rate);
        final AlertDialog alertDialog = new AlertDialog.Builder(DriverOrderDetailActivity.this)
                .setCancelable(false)
                .setView(view)
                .create();

        alertDialog.show();
        addRateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverOrderDetailActivity.this,AddRateActivity.class);
                intent.putExtra("driver_id",finishied_order_model.getDriver_id());
                intent.putExtra("order_id",finishied_order_model.getOrder_id());
                intent.putExtra("driver_name",finishied_order_model.getDriver_name());
                intent.putExtra("driver_image",finishied_order_model.getDriver_image());
                startActivity(intent);
                alertDialog.dismiss();


            }
        });

    }

}
