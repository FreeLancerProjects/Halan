package com.semicolon.halan.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.semicolon.halan.Models.UserModel;
import com.semicolon.halan.R;
import com.semicolon.halan.Services.Api;
import com.semicolon.halan.Services.Services;
import com.semicolon.halan.SingleTone.Users;

import java.util.Locale;

import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Client_Login extends AppCompatActivity {
    private EditText user_name,password;
    private Button loginBtn;
    private TextView forget_password,newAccount;
    private ProgressDialog pDialog;
    private Users users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_login);
        Calligrapher calligrapher=new Calligrapher(this);
        calligrapher.setFont(this,"JannaLT-Regular.ttf",true);
        users = Users.getInstance();
        initView();
    }

    private void initView() {

        user_name = findViewById(R.id.user_name);
        password  = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginBtn);
        forget_password = findViewById(R.id.forget_password);
        newAccount = findViewById(R.id.newAccount);

        if (Locale.getDefault().getLanguage().equals("ar"))
        {
            password.setGravity(Gravity.RIGHT);
            password.setGravity(Gravity.CENTER_VERTICAL);
        }else
            {
                password.setGravity(Gravity.LEFT);
                password.setGravity(Gravity.CENTER_VERTICAL);

            }


        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Client_Login.this,Activity_Client_Register.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String muser_name = user_name.getText().toString();
                String mpassword  = password.getText().toString();

                if (!TextUtils.isEmpty(muser_name) && !TextUtils.isEmpty(mpassword))
                {
                    loginByServer();

                    //Toast.makeText(Activity_Client_Login.this, "login", Toast.LENGTH_LONG).show();
                }

                if (TextUtils.isEmpty(muser_name) && TextUtils.isEmpty(mpassword))
                {
                    user_name.setError(getString(R.string.enter_username));
                    password.setError(getString(R.string.enter_password));
                }

                if (TextUtils.isEmpty(muser_name))
                {
                    user_name.setError(getString(R.string.enter_username));

                }else
                    {
                        user_name.setError(null);

                    }

                if (TextUtils.isEmpty(mpassword))
                {
                    password.setError(getString(R.string.enter_password));

                }else
                {
                    password.setError(null);

                }

            }
        });

    }
    private void loginByServer() {

        ProgressBar bar = new ProgressBar(Activity_Client_Login.this);
        Drawable drawable = bar.getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(Activity_Client_Login.this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        pDialog = new ProgressDialog(Activity_Client_Login.this);
        pDialog.setIndeterminate(true);
        pDialog.setMessage(getString(R.string.login));
        pDialog.setCancelable(true);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.setIndeterminateDrawable(drawable);
        pDialog.show();
        final String uname = user_name.getText().toString();
        final String upass = password.getText().toString();


        Services service = Api.getClient().create(Services.class);
        Call<UserModel> userCall = service.userSignIn(uname, upass);

        userCall.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {

                if (response.isSuccessful()) {

                    UserModel userModel = response.body();
                    if (userModel.getSuccess()==1) {

                        users.setUserData(userModel);
                        pDialog.dismiss();
                        Intent intent = new Intent(Activity_Client_Login.this,HomeActivity.class);
                        startActivity(intent);
                        finish();
                       

                    } else {
                        Toast.makeText(Activity_Client_Login.this, R.string.chk_uname_password, Toast.LENGTH_LONG).show();
                        pDialog.dismiss();
                        
                    }
                } else {
                    pDialog.dismiss();
                    Toast.makeText(Activity_Client_Login.this,getString(R.string.faild), Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                pDialog.dismiss();
                Toast.makeText(Activity_Client_Login.this, R.string.something_haywire, Toast.LENGTH_LONG).show();
                Log.d("onFailure", t.toString());
            }
        });
    }
}
