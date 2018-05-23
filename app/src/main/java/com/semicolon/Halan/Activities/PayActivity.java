package com.semicolon.Halan.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.semicolon.Halan.Models.Finishied_Order_Model;
import com.semicolon.Halan.Models.UserModel;
import com.semicolon.Halan.R;
import com.semicolon.Halan.Services.Api;
import com.semicolon.Halan.Services.Preferences;
import com.semicolon.Halan.Services.Services;
import com.semicolon.Halan.Services.Tags;
import com.semicolon.Halan.SingleTone.Users;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayActivity extends AppCompatActivity implements View.OnClickListener , Users.UserData {

    private EditText transMoney,transBank,code;
    private TextView transDate,link;
    private Button send;
    private ProgressDialog dialog;
    private Users users;
    private Preferences preferences;
    private UserModel userModel;
    private ImageView back;
    private String date="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        /*Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "JannaLT-Regular.ttf", true);
      */  EventBus.getDefault().register(this);
        initView();
        CreateProgressDialog();

        users=Users.getInstance();
        preferences=new Preferences(this);
        users.getUserData(this);


    }

    @Override
    public void UserDataSuccess(UserModel userModel) {

        this.userModel=userModel;
    }

    private void initView() {
        transMoney=findViewById(R.id.edt_trans_money);
        transDate=findViewById(R.id.tv_date);
        transBank=findViewById(R.id.edt_bank);
        code=findViewById(R.id.edt_code);
        send=findViewById(R.id.btn_send);
        link = findViewById(R.id.link);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        transDate.setOnClickListener(this);
        send.setOnClickListener(this);
        link.setOnClickListener(this);


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

        Picasso.with(PayActivity.this).load(Uri.parse(Tags.ImgPath+finishied_order_model.getDriver_image())).into(driver_img);
        driver_name.setText(finishied_order_model.getDriver_name());
        order_details.setText(finishied_order_model.getOrder_details());
        Button addRateBtn = view.findViewById(R.id.add_rate);
        final AlertDialog alertDialog = new AlertDialog.Builder(PayActivity.this)
                .setCancelable(false)
                .setView(view)
                .create();

        alertDialog.show();
        addRateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PayActivity.this,AddRateActivity.class);
                intent.putExtra("driver_id",finishied_order_model.getDriver_id());
                intent.putExtra("order_id",finishied_order_model.getOrder_id());
                intent.putExtra("driver_name",finishied_order_model.getDriver_name());
                intent.putExtra("driver_image",finishied_order_model.getDriver_image());
                startActivity(intent);
                alertDialog.dismiss();


            }
        });

    }

    public void DateDialog() {

        Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        DatePickerDialog datePickerDialog = new DatePickerDialog(PayActivity.this, new DatePickerDialog.OnDateSetListener() {

            @SuppressLint("ResourceAsColor")
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                date = dateFormatter.format(newDate.getTime());

                Date dateSpecified = newDate.getTime();

                Calendar curr_cal = Calendar.getInstance();
                Date curDate = curr_cal.getTime();
                if (dateSpecified.before(curDate))
                {
                    Toast.makeText(PayActivity.this, "choose new date", Toast.LENGTH_SHORT).show();
                }else
                    {
                        transDate.setText(date);

                    }



            }

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.tv_date:
                DateDialog();
                break;
            case R.id.btn_send:
                saveDataToServer();
                break;
            case R.id.link:
                Intent intent = new Intent(PayActivity.this,WebViewActivity.class);
                intent.putExtra("link","https://raqytech.com/");
                startActivity(intent);
                break;
        }

    }

    private void saveDataToServer() {
        String amount=transMoney.getText().toString();
        String bank=transBank.getText().toString();
        String dcode =code.getText().toString();
        final String userId=userModel.getUser_id();

        if (TextUtils.isEmpty(amount))
        {
            transMoney.setError(getString(R.string.enter_transmoney));
        }else if (TextUtils.isEmpty(date))
        {
            transDate.setError(getString(R.string.enter_transdate));
            transMoney.setError(null);

        }
        else if (TextUtils.isEmpty(bank))
        {
            transBank.setError(getString(R.string.enter_transbank));
            transDate.setError(null);

        }
        else if (TextUtils.isEmpty(dcode))
        {

            code.setError(getString(R.string.enter_discode));
            transBank.setError(null);

        }else
            {
                code.setError(null);

                dialog.show();
                Services services = Api.getClient(Tags.BASE_URL).create(Services.class);
                Call<UserModel> call = services.DriverPayment(userId,amount,date,bank,dcode);
                call.enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getSuccess() == 1) {

                                dialog.dismiss();
                                Toast.makeText(PayActivity.this, R.string.data_send, Toast.LENGTH_SHORT).show();
                                finish();

                            } else {
                                dialog.dismiss();

                                Toast.makeText(PayActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            dialog.dismiss();

                            Toast.makeText(PayActivity.this, "" + getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        Log.e("mmmmm", t.getMessage() + "");
                        dialog.dismiss();
                        Toast.makeText(PayActivity.this, "" + getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();

                    }
                });
            }




    }
    private void CreateProgressDialog() {
        ProgressBar bar = new ProgressBar(this);
        Drawable drawable = bar.getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.sending));
        dialog.setIndeterminateDrawable(drawable);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
