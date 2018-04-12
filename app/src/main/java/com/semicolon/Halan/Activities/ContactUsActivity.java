package com.semicolon.Halan.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.semicolon.Halan.Models.UserModel;
import com.semicolon.Halan.R;
import com.semicolon.Halan.Services.Api;
import com.semicolon.Halan.Services.Services;
import com.semicolon.Halan.Services.Tags;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText name, email, subject, message;
    private Button call, send;
    private ProgressDialog dialog;
    private String phone;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        initView();
        CreateProgressDialog();
        getNumber();

    }

    private void SaveDataToServer() {

        String cname = name.getText().toString();
        String cemail = email.getText().toString();
        String csubject = subject.getText().toString();
        String cmessage = message.getText().toString();
        if (TextUtils.isEmpty(cname) && TextUtils.isEmpty(cemail) && TextUtils.isEmpty(csubject) && TextUtils.isEmpty(cmessage)) {
            name.setError(getString(R.string.enter_username));
            email.setError(getString(R.string.enter_email));
            subject.setError(getString(R.string.enter_subject));
            message.setError(getString(R.string.enter_message));
        } else if (TextUtils.isEmpty(cname)) {
            name.setError(getString(R.string.enter_username));

        } else if (TextUtils.isEmpty(cemail)) {
            email.setError(getString(R.string.enter_email));
            name.setError(null);
        } else if (!Patterns.EMAIL_ADDRESS.matcher(cemail).matches()) {
            email.setError(getString(R.string.inv_email));
            name.setError(null);


        } else if (TextUtils.isEmpty(csubject)) {
            email.setError(null);
            subject.setError(getString(R.string.enter_username));

        } else if (TextUtils.isEmpty(cmessage)) {
            subject.setError(null);
            message.setError(getString(R.string.enter_username));

        } else {

            dialog.show();

            Services services = Api.getClient(Tags.BASE_URL).create(Services.class);
            Call<UserModel> call = services.ContactUs(cname, cemail, csubject, cmessage);
            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess() == 1) {

                            dialog.dismiss();
                            Toast.makeText(ContactUsActivity.this, "Data Sent Succesfuly", Toast.LENGTH_SHORT).show();
                            finish();

                        } else {
                            dialog.dismiss();

                            Toast.makeText(ContactUsActivity.this, "error", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        dialog.dismiss();

                        Toast.makeText(ContactUsActivity.this, "" + getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {
                    Log.e("mmmmm", t.getMessage() + "");
                    dialog.dismiss();
                    Toast.makeText(ContactUsActivity.this, "" + getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();

                }
            });
        }

    }

    private void initView() {
        name = findViewById(R.id.edt_name);
        email = findViewById(R.id.edt_email);
        subject = findViewById(R.id.edt_subject);
        message = findViewById(R.id.edt_message);
        call = findViewById(R.id.btn_call);
        send = findViewById(R.id.btn_send);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        call.setOnClickListener(this);
        send.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_call:
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
                break;

            case R.id.btn_send:
                SaveDataToServer();

                break;
        }

    }

    private void getNumber() {

        Services services = Api.getClient(Tags.BASE_URL).create(Services.class);
        Call<UserModel> call = services.getNumber();
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful()) {
                 phone=response.body().getOur_phone_number();
                 Log.e("phone",phone);
                } else {

                    Toast.makeText(ContactUsActivity.this, "" + getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.e("mmmmm", t.getMessage() + "");
                dialog.dismiss();
                Toast.makeText(ContactUsActivity.this, "" + getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void CreateProgressDialog() {
        ProgressBar bar = new ProgressBar(this);
        Drawable drawable = bar.getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.waitreg));
        dialog.setIndeterminateDrawable(drawable);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
    }
}
