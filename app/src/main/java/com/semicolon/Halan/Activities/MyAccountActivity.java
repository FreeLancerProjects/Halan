package com.semicolon.Halan.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.semicolon.Halan.Models.AppRateModel;
import com.semicolon.Halan.Models.UserModel;
import com.semicolon.Halan.R;
import com.semicolon.Halan.Services.Api;
import com.semicolon.Halan.Services.Services;
import com.semicolon.Halan.Services.Tags;
import com.semicolon.Halan.SingleTone.Users;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyAccountActivity extends AppCompatActivity implements Users.UserData,View.OnClickListener{
    private RatingBar ratingBar;
    private TextView app_rate,order_num;
    private LinearLayout myorder,rule,share,send_prob,pay,aboutApp,politics;
    private Button add_rateBtn;
    private CircleImageView user_image;
    private Users users;
    private UserModel userModel;
    private View view;
    private AlertDialog.Builder builder;
    private ProgressDialog dialog;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        initView();
        users =Users.getInstance();
        users.getUserData(this);
        CreateProgressDialog();
        getAppEvaluation();
    }

    private void getAppEvaluation() {
        Retrofit retrofit = Api.getClient(Tags.BASE_URL);
        Services services = retrofit.create(Services.class);
        Call<AppRateModel> call = services.getApp_Evaluation(userModel.getUser_id());
        call.enqueue(new Callback<AppRateModel>() {
            @Override
            public void onResponse(Call<AppRateModel> call, Response<AppRateModel> response) {
                if (response.isSuccessful())
                {
                    app_rate.setText(String.valueOf(response.body().getApp_evaluation()));
                    ratingBar.setRating((float) response.body().getApp_evaluation());
                }
            }

            @Override
            public void onFailure(Call<AppRateModel> call, Throwable t) {
                Toast.makeText(MyAccountActivity.this,R.string.something_haywire, Toast.LENGTH_LONG).show();
                Log.e("Error",t.getMessage());
            }
        });
    }

    private void initView() {
        ratingBar = findViewById(R.id.rateBar);
        LayerDrawable drawable = (LayerDrawable) ratingBar.getProgressDrawable();
        drawable.getDrawable(0).setColorFilter(ContextCompat.getColor(this,R.color.gray2), PorterDuff.Mode.SRC_ATOP);
        drawable.getDrawable(1).setColorFilter(ContextCompat.getColor(this,R.color.rate2), PorterDuff.Mode.SRC_ATOP);
        drawable.getDrawable(2).setColorFilter(ContextCompat.getColor(this,R.color.rate2), PorterDuff.Mode.SRC_ATOP);

        order_num = findViewById(R.id.order_num);
        myorder = findViewById(R.id.myorder);
        rule = findViewById(R.id.rule);
        share = findViewById(R.id.share);
        aboutApp = findViewById(R.id.aboutApp);
        politics = findViewById(R.id.politics);
        send_prob = findViewById(R.id.send_prob);
        pay = findViewById(R.id.pay);
        app_rate = findViewById(R.id.app_rate);
        user_image = findViewById(R.id.user_image);
        add_rateBtn = findViewById(R.id.add_rateBtn);
        view = findViewById(R.id.view);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rule.setOnClickListener(this);
        share.setOnClickListener(this);
        send_prob.setOnClickListener(this);
        aboutApp.setOnClickListener(this);
        politics.setOnClickListener(this);
        pay.setOnClickListener(this);
        ratingBar.setEnabled(false);
        ratingBar.setFocusable(false);
        add_rateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // dialog.show();
               // AddApp_Rate();
                View v  = LayoutInflater.from(MyAccountActivity.this).inflate(R.layout.custom_dialog_rate,null);
                Button addRateBtn = v.findViewById(R.id.addRateBtn);
                Button cancel = v.findViewById(R.id.cancelBtn);
                final RatingBar ratingBar = v.findViewById(R.id.alert_rateBar);
                final TextView rate = v.findViewById(R.id.rate);

                LayerDrawable drawable = (LayerDrawable) ratingBar.getProgressDrawable();
                drawable.getDrawable(0).setColorFilter(ContextCompat.getColor(MyAccountActivity.this,R.color.gray2), PorterDuff.Mode.SRC_ATOP);
                drawable.getDrawable(1).setColorFilter(ContextCompat.getColor(MyAccountActivity.this,R.color.rate), PorterDuff.Mode.SRC_ATOP);
                drawable.getDrawable(2).setColorFilter(ContextCompat.getColor(MyAccountActivity.this,R.color.rate), PorterDuff.Mode.SRC_ATOP);

                final AlertDialog alertDialog = new AlertDialog.Builder(MyAccountActivity.this)
                        .setCancelable(true)
                        .setView(v)
                        .create();
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });


                addRateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                        dialog.show();
                        AddApp_Rate(String.valueOf(ratingBar.getRating()));
                    }
                });

                ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                        rate.setText(String.valueOf(v));
                    }
                });


            }
        });
    }

    private void AddApp_Rate(String rate) {
        Retrofit retrofit = Api.getClient(Tags.BASE_URL);
        Services services = retrofit.create(Services.class);
        Call<AppRateModel> call = services.setApp_Evaluation(userModel.getUser_id(),rate);
        call.enqueue(new Callback<AppRateModel>() {
            @Override
            public void onResponse(Call<AppRateModel> call, Response<AppRateModel> response) {

                if (response.isSuccessful())
                {
                    if (response.body().getSuccess()==1)
                    {
                        Toast.makeText(MyAccountActivity.this,R.string.success, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }else if (response.body().getSuccess()==0)
                    {
                        Toast.makeText(MyAccountActivity.this,R.string.faild, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<AppRateModel> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(MyAccountActivity.this,R.string.something_haywire, Toast.LENGTH_LONG).show();
                Log.e("Error",t.getMessage());
            }
        });
    }
    @Override
    public void UserDataSuccess(UserModel userModel) {
        this.userModel = userModel;
        updateUi(userModel);

    }

    private void updateUi(UserModel userModel) {
        if (userModel.getUser_type().equals(Tags.Driver))
        {
            myorder.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
        }else if (userModel.getUser_type().equals(Tags.Client))
        {
            myorder.setVisibility(View.GONE);
            view.setVisibility(View.GONE);


        }
        Picasso.with(this).load(Uri.parse(Tags.ImgPath+userModel.getUser_photo())).into(user_image);
        order_num.setText(String.valueOf(userModel.getOrder_count()));


    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id)
        {
            case R.id.rule:
                Intent intent = new Intent(MyAccountActivity.this,RulesActivity.class);
                startActivity(intent);
                break;
            case R.id.share:
                share();
                break;
            case R.id.send_prob:
                Intent intent1=new Intent(MyAccountActivity.this,ContactUsActivity.class);
                startActivity(intent1);
                break;
            case R.id.pay:
                if (userModel.getUser_type().equals(Tags.Driver))
                {

                    Intent intent2=new Intent(this,PayActivity.class);
                    startActivity(intent2);
                }else
                {
                    CreateAlertDialog2(getString(R.string.serv_aval_drivers));
                    builder.show();
                }
                break;
            case R.id.aboutApp:
                Intent intent_about=new Intent(MyAccountActivity.this,AboutAppActivity.class);
                startActivity(intent_about);
                break;
            case R.id.politics:
                Intent intent_politics=new Intent(MyAccountActivity.this,PolicyActivity.class);
                startActivity(intent_politics);
                break;

        }
    }

    private void share() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"تطبيق حالا");
        startActivity(intent);
    }

    private void CreateProgressDialog()
    {
        ProgressBar bar = new ProgressBar(this);
        Drawable drawable = bar.getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setMessage(getString(R.string.rating_app));
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setIndeterminateDrawable(drawable);
    }
    private void CreateAlertDialog2(String msg)
    {
        builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AlertDialog dialog = builder.create();
                dialog.dismiss();
            }
        });


    }
}
