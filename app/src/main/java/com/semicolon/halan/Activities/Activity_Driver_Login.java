package com.semicolon.halan.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.semicolon.halan.R;

import java.util.Locale;

import me.anwarshahriar.calligrapher.Calligrapher;

public class Activity_Driver_Login extends AppCompatActivity {
    private EditText user_name,password;
    private Button loginBtn;
    private TextView forget_password,newAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);
        Calligrapher calligrapher=new Calligrapher(this);
        calligrapher.setFont(this,"JannaLT-Regular.ttf",true);

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
    }
}
