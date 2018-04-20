package com.semicolon.Halan.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.semicolon.Halan.R;

import me.anwarshahriar.calligrapher.Calligrapher;

public class Activity_Driver_Register extends AppCompatActivity implements View.OnClickListener {

    private EditText d_city,d_identity_number,d_vehicle_number,d_car_color;
    private Button next;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_register);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "JannaLT-Regular.ttf", true);

        initView();

    }

    private void initView() {
        d_city=findViewById(R.id.city);
        d_identity_number=findViewById(R.id.identity);
        d_vehicle_number=findViewById(R.id.vehicle_number);
        d_car_color =findViewById(R.id.car_color);
        next=findViewById(R.id.nextBtn);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        next.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        
        switch (view.getId()){
            case R.id.nextBtn:
                sendDataByIntent();
                break;
                
        }
        
    }
    
    private void sendDataByIntent(){

        String city = d_city.getText().toString();
        String identity = d_identity_number.getText().toString();
        String vihile_number = d_vehicle_number.getText().toString();
        String car_color = d_car_color.getText().toString();
        if (TextUtils.isEmpty(city) && TextUtils.isEmpty(identity)&& TextUtils.isEmpty(vihile_number) && TextUtils.isEmpty(car_color) )
        {
            d_city.setError(getString(R.string.enter_city));
            d_identity_number.setError(getString(R.string.enter_identity_number));
            d_vehicle_number.setError(getString(R.string.enter_vehicle_number));
            d_car_color.setError(getString(R.string.enter_car_color));
        }
        else if (TextUtils.isEmpty(city))
        {
            d_city.setError(getString(R.string.enter_city));

        }else if (TextUtils.isEmpty(identity))
        {
            d_identity_number.setError(getString(R.string.enter_identity_number));
            d_city.setError(null);

        }else if (TextUtils.isEmpty(vihile_number))
        {
            d_vehicle_number.setError(getString(R.string.enter_vehicle_number));
            d_identity_number.setError(null);
        }
        else if (TextUtils.isEmpty(car_color))
        {
            d_car_color.setError(getString(R.string.enter_car_color));
            d_vehicle_number.setError(null);
        }else
        {
            Intent intent=new Intent(this,Activity_Driver_Register2.class);
            intent.putExtra("city",city);
            intent.putExtra("identity",identity);
            intent.putExtra("vihile_number",vihile_number);
            intent.putExtra("car_color",car_color);
            startActivity(intent);
            finish();

        }
    }
}
