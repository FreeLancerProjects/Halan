package com.semicolon.Halan.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.semicolon.Halan.Models.ResponseModel;
import com.semicolon.Halan.R;
import com.semicolon.Halan.Services.Api;
import com.semicolon.Halan.Services.Services;
import com.semicolon.Halan.Services.Tags;

import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    EditText email,username;
    Button send;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initView();


    }


    private void initView() {
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "JannaLT-Regular.ttf", true);

        email=findViewById(R.id.edt_email);
        username=findViewById(R.id.edt_username);
        send=findViewById(R.id.btn_send);

        send.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.btn_send:
                saveDataToServer();
                break;
        }

    }

    private void saveDataToServer() {
        String uemail=email.getText().toString();
        String uname=username.getText().toString();
        if (uemail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(uemail).matches()) {
            email.setError(getString(R.string.inv_email));
        } else {
            email.setError(null);
        }
        if (uname.isEmpty() || uname.length() < 3) {
            username.setError(getString(R.string.enter_username));
        } else {
            username.setError(null);


            pDialog = new ProgressDialog(ForgetPasswordActivity.this);
            pDialog.setIndeterminate(true);
            pDialog.setMessage(getString(R.string.send_data));
            pDialog.setCancelable(true);
            pDialog.setCanceledOnTouchOutside(false);

            showpDialog();

            Services service = Api.getClient(Tags.BASE_URL).create(Services.class);
            Call<ResponseModel> userCall = service.RestMyPass(uname,uemail);
            userCall.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    hidepDialog();

                    if (response.isSuccessful())
                    {

                        if (response.body().getSuccess() == 1) {

                            Toast.makeText(ForgetPasswordActivity.this, "" +getString(R.string.success), Toast.LENGTH_SHORT).show();
                            finish();


                        } else {
                            Toast.makeText(ForgetPasswordActivity.this, "" +getString(R.string.faild), Toast.LENGTH_SHORT).show();
                        }
                    }else
                    {
                        Toast.makeText(ForgetPasswordActivity.this, "" +getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    hidepDialog();
                    Toast.makeText(ForgetPasswordActivity.this, "" +getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();

                    Log.d("onFailure", t.toString());
                }
            });
        }

    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
