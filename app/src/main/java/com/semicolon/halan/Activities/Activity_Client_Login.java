package com.semicolon.halan.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.semicolon.halan.R;

import java.util.Locale;

import me.anwarshahriar.calligrapher.Calligrapher;

public class Activity_Client_Login extends AppCompatActivity {
    private EditText user_name,password;
    private Button loginBtn;
    private TextView forget_password,newAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_login);
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
                    Toast.makeText(Activity_Client_Login.this, "login", Toast.LENGTH_SHORT).show();
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
}
