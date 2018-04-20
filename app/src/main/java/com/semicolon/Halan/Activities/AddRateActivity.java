package com.semicolon.Halan.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.semicolon.Halan.Models.MyOrderModel;
import com.semicolon.Halan.Models.ResponseModel;
import com.semicolon.Halan.Models.UserModel;
import com.semicolon.Halan.R;
import com.semicolon.Halan.Services.Api;
import com.semicolon.Halan.Services.Services;
import com.semicolon.Halan.Services.Tags;
import com.semicolon.Halan.SingleTone.Users;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddRateActivity extends AppCompatActivity implements Users.UserData{

    private CircleImageView driver_image;
    private TextView driver_name,txt_rate;
    private ImageView back;
    private RatingBar ratingBar;
    private Button addRate_btn;
    private MyOrderModel myOrderModel;
    private Target target;
    private Users users;
    private UserModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rate);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "JannaLT-Regular.ttf", true);

        users = Users.getInstance();
        users.getUserData(this);
        initView();
        getDataFromIntent();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        Picasso.with(this).cancelRequest(target);
    }

    private void initView() {

        driver_image = findViewById(R.id.driver_image);
        driver_name  = findViewById(R.id.driver_name);
        back         = findViewById(R.id.back);
        ratingBar    = findViewById(R.id.rateBar);
        txt_rate     = findViewById(R.id.txt_rate);
        addRate_btn  = findViewById(R.id.addRate_btn);

        addRate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveDataToServer();
               // Toast.makeText(AddRateActivity.this, "rate = "+ratingBar.getRating(), Toast.LENGTH_SHORT).show();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        LayerDrawable layerDrawable = (LayerDrawable) ratingBar.getProgressDrawable();

        layerDrawable.getDrawable(0).setColorFilter(ContextCompat.getColor(this,R.color.gray2), PorterDuff.Mode.SRC_ATOP);
        layerDrawable.getDrawable(1).setColorFilter(ContextCompat.getColor(this,R.color.rate), PorterDuff.Mode.SRC_ATOP);
        layerDrawable.getDrawable(2).setColorFilter(ContextCompat.getColor(this,R.color.rate), PorterDuff.Mode.SRC_ATOP);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (v>0.0f && v<=1.0f)
                {
                    txt_rate.setText("توصيل سيئ");
                }else if (v>=1.5f && v<=3.0f)
                {
                    txt_rate.setText("توصيل جيد");

                }else if (v>=3.5f && v<=5.0f)
                {
                    txt_rate.setText("توصيل ممتاز");

                }
            }
        });

    }

    private void saveDataToServer() {
        Map<String,String> map = new HashMap<>();
        map.put("evaluation_count",ratingBar.getRating()+"");
        map.put("driver_id",myOrderModel.getDriver_id());

        Retrofit retrofit = Api.getClient(Tags.BASE_URL);
        Services services = retrofit.create(Services.class);
        Call<ResponseModel> call = services.sendDriverEvaluate(myOrderModel.getOrder_id(),map);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess()==1)
                    {
                        Toast.makeText(AddRateActivity.this, R.string.rated_added, Toast.LENGTH_LONG).show();
                        finish();
                    }else
                    {
                        Toast.makeText(AddRateActivity.this, R.string.rate_notadded, Toast.LENGTH_LONG).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e("Error",t.getMessage());
                Toast.makeText(AddRateActivity.this, getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDataFromIntent() {

        Intent intent = getIntent();
        if (intent!=null)
        {
            myOrderModel = (MyOrderModel) intent.getSerializableExtra("order");
            updateUi(myOrderModel);
        }
    }

    private void updateUi(MyOrderModel myOrderModel) {
        try {
            target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    driver_image.setImageBitmap(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };

            if (myOrderModel.getDriver_photo()!=null || !TextUtils.isEmpty(myOrderModel.getDriver_photo()) || !myOrderModel.getDriver_photo().equals("0"))
            {
                Picasso.with(this).load(Uri.parse(Tags.ImgPath+myOrderModel.getDriver_photo())).into(target);
            }
            driver_name.setText(myOrderModel.getDriver_name());

        }catch (NullPointerException e)
        {

        }



    }

    @Override
    public void UserDataSuccess(UserModel userModel) {
        this.userModel = userModel;
    }
}
