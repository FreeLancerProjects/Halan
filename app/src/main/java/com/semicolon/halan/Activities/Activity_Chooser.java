package com.semicolon.halan.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.semicolon.halan.R;

import me.anwarshahriar.calligrapher.Calligrapher;

public class Activity_Chooser extends AppCompatActivity {

    private Button clientBtn,driverBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooser);
        Calligrapher calligrapher=new Calligrapher(this);
        calligrapher.setFont(this,"JannaLT-Regular.ttf",true);

        initView();
    }

    private void initView() {
        clientBtn = findViewById(R.id.clientBtn);
        driverBtn = findViewById(R.id.driverBtn);

        clientBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Chooser.this,Activity_Client_Login.class);
                startActivity(intent);
            }
        });
        driverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Chooser.this,Activity_Driver_Login.class);
                startActivity(intent);
            }
        });
    }
}
