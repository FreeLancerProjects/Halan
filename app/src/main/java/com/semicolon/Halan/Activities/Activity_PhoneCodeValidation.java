package com.semicolon.Halan.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.semicolon.Halan.R;

public class Activity_PhoneCodeValidation extends AppCompatActivity {
    private ImageView back;
    private EditText code_txt;
    private Button confirm;
    private String smsCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_code_validation);
        initView();
        getDataFromIntent();
    }



    private void initView() {

        back = findViewById(R.id.back);
        code_txt = findViewById(R.id.code_txt);
        confirm = findViewById(R.id.confirm);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = code_txt.getText().toString();

                if (TextUtils.isEmpty(code))
                {
                    Toast.makeText(Activity_PhoneCodeValidation.this, R.string.plz_enter_code, Toast.LENGTH_LONG).show();
                }else
                    {
                        if (code.equals(smsCode))
                        {
                            Toast.makeText(Activity_PhoneCodeValidation.this, "Success code", Toast.LENGTH_SHORT).show();
                        }else
                            {
                                Toast.makeText(Activity_PhoneCodeValidation.this, R.string.error_code, Toast.LENGTH_LONG).show();

                            }
                    }
            }
        });
    }
    private void getDataFromIntent()
    {

    }
}
