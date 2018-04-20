package com.semicolon.Halan.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.semicolon.Halan.Models.UserModel;
import com.semicolon.Halan.R;
import com.semicolon.Halan.Services.Api;
import com.semicolon.Halan.Services.Preferences;
import com.semicolon.Halan.Services.Services;
import com.semicolon.Halan.Services.Tags;
import com.semicolon.Halan.SingleTone.Users;

import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayActivity extends AppCompatActivity implements View.OnClickListener , Users.UserData {

    private int year, month, day;
    private EditText transMoney,transDate,transBank,code;
    private Button send;
    private ProgressDialog dialog;
    private Users users;
    private Preferences preferences;
    private UserModel userModel;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "JannaLT-Regular.ttf", true);

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
        transDate=findViewById(R.id.edt_date);
        transBank=findViewById(R.id.edt_bank);
        code=findViewById(R.id.edt_code);
        send=findViewById(R.id.btn_send);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        transDate.setOnClickListener(this);
        send.setOnClickListener(this);


    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        return new DatePickerDialog(this, datePickerListener, year, month, day);
    }

    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            day = selectedDay;
            month = selectedMonth;
            year = selectedYear;
            transDate.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
                    + selectedYear);
        }
    };

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.edt_date:
                showDialog(0);
                break;
            case R.id.btn_send:
                saveDataToServer();
                break;
        }

    }

    private void saveDataToServer() {
        String amount=transMoney.getText().toString();
        final String date=transDate.getText().toString();
        String bank=transBank.getText().toString();
        String dcode =code.getText().toString();
        final String userId=userModel.getUser_id();
        dialog.show();
        Services services = Api.getClient(Tags.BASE_URL).create(Services.class);
        Call<UserModel> call = services.DriverPayment(userId,amount,date,bank,dcode);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getSuccess() == 1) {

                        Log.e("id",userId+" ");
                        Log.e("date",date+" ");
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


}
