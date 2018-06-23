package com.semicolon.Halan.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.semicolon.Halan.R;
import com.semicolon.Halan.Services.Tags;

import me.anwarshahriar.calligrapher.Calligrapher;

public class Activity_Driver_Register extends AppCompatActivity implements View.OnClickListener {

    private EditText d_city,d_identity_number,d_vehicle_number,d_car_color,c_age,user_country;
    private Button next;
    private ImageView back;
    private Spinner gender_spinner;
    private String [] gender;
    private String c_gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_register);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "JannaLT-Regular.ttf", true);

        initView();

    }

    private void initView() {
        c_age = findViewById(R.id.user_age);
        gender = getResources().getStringArray(R.array.gender);
        gender_spinner = findViewById(R.id.gender_spinner);
        gender_spinner.setAdapter(new ArrayAdapter<String>(this,R.layout.spinner_item,gender));
        d_city=findViewById(R.id.city);
        user_country = findViewById(R.id.user_country);
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

        if (gender_spinner.getSelectedItemPosition()==0)
        {
            c_gender= Tags.gender_male;
        }
        else if (gender_spinner.getSelectedItemPosition()==1)
        {
            c_gender=Tags.gender_female;

        }
        gender_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i==0)
                {
                    c_gender=Tags.gender_male;

                }else if (i==1)
                {
                    c_gender=Tags.gender_female;


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

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
        String country = user_country.getText().toString();
        String identity = d_identity_number.getText().toString();
        String vihile_number = d_vehicle_number.getText().toString();
        String car_color = d_car_color.getText().toString();
        String mAge = c_age.getText().toString();

        if (TextUtils.isEmpty(mAge)&&TextUtils.isEmpty(city) &&TextUtils.isEmpty(country)&& TextUtils.isEmpty(identity)&& TextUtils.isEmpty(vihile_number) && TextUtils.isEmpty(car_color) )
        {
            c_age.setError(getString(R.string.enter_age));
            d_city.setError(getString(R.string.enter_city));
            d_identity_number.setError(getString(R.string.enter_identity_number));
            d_vehicle_number.setError(getString(R.string.enter_vehicle_number));
            d_car_color.setError(getString(R.string.enter_car_color));
            user_country.setError(getString(R.string.enter_country));
        }
        else if (TextUtils.isEmpty(mAge))
        {
            c_age.setError(getString(R.string.enter_age));

        }
        else if (TextUtils.isEmpty(city))
        {
            c_age.setError(null);

            d_city.setError(getString(R.string.enter_city));

        }else if (TextUtils.isEmpty(country))
        {
            d_city.setError(null);
            c_age.setError(null);
            user_country.setError(getString(R.string.enter_country));

        }else if (TextUtils.isEmpty(identity))
        {
            d_identity_number.setError(getString(R.string.enter_identity_number));
            d_city.setError(null);

        }else if (TextUtils.isEmpty(vihile_number))
        {
            d_vehicle_number.setError(getString(R.string.enter_vehicle_number));
            d_identity_number.setError(null);
            c_age.setError(null);
            user_country.setError(null);

        }
        else if (TextUtils.isEmpty(car_color))
        {
            d_car_color.setError(getString(R.string.enter_car_color));
            d_vehicle_number.setError(null);
            d_identity_number.setError(null);
            c_age.setError(null);
            user_country.setError(null);
        }else
        {
            d_vehicle_number.setError(null);
            d_identity_number.setError(null);
            c_age.setError(null);
            user_country.setError(null);
            d_car_color.setError(null);

            Intent intent=new Intent(this,Activity_Driver_Register2.class);
            intent.putExtra("age",mAge);
            intent.putExtra("country",country);
            intent.putExtra("gender",c_gender);
            intent.putExtra("city",city);
            intent.putExtra("identity",identity);
            intent.putExtra("vihile_number",vihile_number);
            intent.putExtra("car_color",car_color);
            startActivity(intent);
            finish();

        }
    }
}
