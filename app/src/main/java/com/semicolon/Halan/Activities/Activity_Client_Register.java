package com.semicolon.Halan.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.lamudi.phonefield.PhoneInputLayout;
import com.semicolon.Halan.Models.UserModel;
import com.semicolon.Halan.R;
import com.semicolon.Halan.Services.Api;
import com.semicolon.Halan.Services.Preferences;
import com.semicolon.Halan.Services.Services;
import com.semicolon.Halan.Services.Tags;
import com.semicolon.Halan.SingleTone.Users;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Client_Register extends AppCompatActivity {
    private CircleImageView c_image;
    private EditText c_name,c_user_name,c_password,c_re_password,c_email;
    private PhoneInputLayout c_phone;
    private Button c_registerBtn;
    private TextView c_login;
    private final int IMG_REQ = 100;
    private String enCodedImage="";
    private Bitmap bitmap;
    private ProgressDialog dialog;
    private Users users;
    private Preferences preferences;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_register);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "JannaLT-Regular.ttf", true);

        initView();
        users = Users.getInstance();
        preferences = new Preferences(getApplicationContext());
        CreateProgressDialog();

    }



    private void initView() {
        c_image = findViewById(R.id.image);
        c_name = findViewById(R.id.name);
        c_user_name = findViewById(R.id.user_name);
        c_phone = findViewById(R.id.phone);
        c_password = findViewById(R.id.password);
        c_re_password = findViewById(R.id.retype_password);
        c_email = findViewById(R.id.email);
        c_registerBtn = findViewById(R.id.registerBtn);
        c_login = findViewById(R.id.login);
        c_phone.getTextInputLayout().getEditText().setHint(getString(R.string.phone));
        c_phone.getTextInputLayout().getEditText().setTextSize(TypedValue.COMPLEX_UNIT_SP,12f);
        if (Locale.getDefault().getLanguage().equals("ar"))
        {
            c_phone.setGravity(Gravity.RIGHT);
            c_phone.setGravity(Gravity.CENTER_VERTICAL);
            c_password.setGravity(Gravity.RIGHT);
            c_password.setGravity(Gravity.CENTER_VERTICAL);
            c_re_password.setGravity(Gravity.RIGHT);
            c_re_password.setGravity(Gravity.CENTER_VERTICAL);

        }

        c_phone.setDefaultCountry("sa");

        c_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Client_Register.this,Activity_Client_Login.class);
                startActivity(intent);
                finish();            }
        });

        c_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent.createChooser(intent,getString(R.string.choose_image)),IMG_REQ);

            }
        });

        c_registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //sendCodeValidation("");
                String mUser_name = c_user_name.getText().toString();
                String mPhone = c_phone.getPhoneNumber();
                String mPassword1 = c_password.getText().toString();
                String mPassword2 = c_re_password.getText().toString();
                String mEmail = c_email.getText().toString();
                String mName = c_name.getText().toString();



                if (TextUtils.isEmpty(mName)&&TextUtils.isEmpty(mUser_name) && TextUtils.isEmpty(mPassword1) &&TextUtils.isEmpty(mPassword2) && TextUtils.isEmpty(mPhone))
                {
                    c_user_name.setError(getString(R.string.enter_username));
                    c_phone.getTextInputLayout().getEditText().setError(getString(R.string.enter_phone));
                    c_password.setError(getString(R.string.enter_password));
                    c_re_password.setError(getString(R.string.enter_password));
                    //c_email.setError(getString(R.string.enter_email));
                    c_name.setError(getString(R.string.enter_name));
                    //Toast.makeText(Activity_Client_Register.this,getString(R.string.choose_image), Toast.LENGTH_LONG).show();
                }
                else if (TextUtils.isEmpty(mName))
                {
                    c_name.setError(getString(R.string.enter_name));

                }else if (TextUtils.isEmpty(mPhone))
                {
                    c_phone.getTextInputLayout().getEditText().setError(getString(R.string.enter_phone));
                    c_name.setError(null);

                }else if (!c_phone.isValid())
                {
                    c_phone.getTextInputLayout().getEditText().setError(getString(R.string.inv_phone));
                    c_name.setError(null);

                }else if (!TextUtils.isEmpty(mEmail)&&!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches())
                {
                   /* c_email.setError(getString(R.string.enter_email));
                    c_phone.getTextInputLayout().getEditText().setError(null);*/
                    c_email.setError(getString(R.string.inv_email));
                    c_phone.getTextInputLayout().getEditText().setError(null);
                }
                else if (TextUtils.isEmpty(mUser_name))
                {
                    c_user_name.setError(getString(R.string.enter_username));
                    c_email.setError(null);



                }else if (TextUtils.isEmpty(mPassword1))
                {
                    c_password.setError(getString(R.string.enter_password));
                    c_user_name.setError(null);
                }
                else if (TextUtils.isEmpty(mPassword2))
                {
                    c_re_password.setError(getString(R.string.enter_password));
                    c_password.setError(null);
                }
                else if (!mPassword1.equals(mPassword2))
                {
                    c_password.setError(null);
                    c_re_password.setText(null);
                    Toast.makeText(Activity_Client_Register.this, R.string.pass_notmatch, Toast.LENGTH_LONG).show();
                }else
                    {

                        saveToServerDB(mName,mUser_name,mPassword1,mEmail,mPhone);
                        //sendCodeValidation("");

                    }




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

    private void sendCodeValidation(String Phonenumber)
    {

        //saveToServerDB();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMG_REQ && resultCode == RESULT_OK && data!=null)
        {
            Uri uri = data.getData();
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                c_image.setImageBitmap(bitmap);
                //enCode(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveToServerDB(String mName, String user_name, String pass, String email, String phone) {
        if (bitmap!=null)
        {
            enCode(bitmap);

        }
        dialog.show();
        Services services = Api.getClient(Tags.BASE_URL).create(Services.class);
        Call<UserModel> userCall = services.userSignUp(mName,user_name,pass,phone,email, FirebaseInstanceId.getInstance().getToken(),enCodedImage);
        userCall.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {



                if (response.isSuccessful()) {

                    //Toast.makeText(Activity_Client_Register.this, ""+enCodedImage, Toast.LENGTH_SHORT).show();

                    UserModel userModel = response.body();
                    if (response.body().getSuccess()==1)
                    {

                        preferences.CreatePref(userModel);
                        users.setUserData(userModel);
                        dialog.dismiss();

                        Intent intent = new Intent(Activity_Client_Register.this,HomeActivity.class);
                        startActivity(intent);
                        finish();

                    }else if (response.body().getSuccess()==0)
                        {
                            dialog.dismiss();

                            Toast.makeText(Activity_Client_Register.this, R.string.regfailed, Toast.LENGTH_LONG).show();
                        }
                    else if (response.body().getSuccess()==2)
                    {
                        dialog.dismiss();

                        Toast.makeText(Activity_Client_Register.this, R.string.ue, Toast.LENGTH_LONG).show();
                    }
                   /*
                    Intent intent = new Intent(Activity_Client_Register.this, Activity_Client_Login.class);
                    startActivity(intent);
                    finish();*/


                }else
                {
                    dialog.dismiss();

                    Toast.makeText(Activity_Client_Register.this,getString(R.string.something_haywire)+0, Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                dialog.dismiss();

                Toast.makeText(Activity_Client_Register.this, "" +getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();
                Log.e("onFailure", t.getMessage());
            }
        });
    }
    private String enCode(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,90,outputStream);
        byte [] bytes = outputStream.toByteArray();

        enCodedImage = Base64.encodeToString(bytes,Base64.DEFAULT);

        return enCodedImage;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this,Activity_Client_Login.class);
        startActivity(intent);
        finish();
    }

    //Api/Client/Profile/1
}
