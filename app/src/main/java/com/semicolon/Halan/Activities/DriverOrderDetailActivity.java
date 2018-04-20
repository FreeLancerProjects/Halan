package com.semicolon.Halan.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.semicolon.Halan.Models.MyOrderModel;
import com.semicolon.Halan.R;
import com.semicolon.Halan.Services.Tags;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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

        initView();
        getDataFromIntent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Picasso.with(this).cancelRequest(target);
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
}
