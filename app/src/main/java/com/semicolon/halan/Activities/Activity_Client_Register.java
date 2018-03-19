package com.semicolon.halan.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lamudi.phonefield.PhoneInputLayout;
import com.semicolon.halan.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Activity_Client_Register extends AppCompatActivity {
    private CircleImageView c_image;
    private EditText c_user_name,c_password,c_re_password,c_email;
    private PhoneInputLayout c_phone;
    private Button c_registerBtn;
    private TextView c_login;
    private final int IMG_REQ = 100;
    private String enCodedImage;
    private Bitmap bitmap;
    private boolean isValidPhonenumber=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_register);
        initView();
    }

    private void initView() {
        c_image = findViewById(R.id.image);
        c_user_name = findViewById(R.id.user_name);
        c_phone = findViewById(R.id.phone);
        c_password = findViewById(R.id.password);
        c_re_password = findViewById(R.id.retype_password);
        c_email = findViewById(R.id.email);
        c_registerBtn = findViewById(R.id.registerBtn);
        c_login = findViewById(R.id.login);

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
                finish();
            }
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

                String mUser_name = c_user_name.getText().toString();
                String mPhone = c_phone.getPhoneNumber();
                String mPassword1 = c_password.getText().toString();
                String mPassword2 = c_re_password.getText().toString();
                String mEmail = c_email.getText().toString();
                if (TextUtils.isEmpty(mUser_name) && TextUtils.isEmpty(mPassword1) &&TextUtils.isEmpty(mPassword2) && TextUtils.isEmpty(mPhone) && TextUtils.isEmpty(mEmail) && bitmap==null)
                {
                    c_user_name.setError(getString(R.string.enter_username));
                    c_phone.getTextInputLayout().getEditText().setError(getString(R.string.enter_phone));
                    c_password.setError(getString(R.string.enter_password));
                    c_re_password.setError(getString(R.string.enter_password));
                    c_email.setError(getString(R.string.enter_email));
                    Toast.makeText(Activity_Client_Register.this,getString(R.string.choose_image), Toast.LENGTH_LONG).show();
                }
                else if (TextUtils.isEmpty(mUser_name))
                {
                    c_user_name.setError(getString(R.string.enter_username));

                }else if (TextUtils.isEmpty(mPhone))
                {
                    c_phone.getTextInputLayout().getEditText().setError(getString(R.string.enter_phone));
                    c_user_name.setError(null);

                }else if (!c_phone.isValid())
                {
                    c_phone.getTextInputLayout().getEditText().setError(getString(R.string.inv_phone));
                    c_user_name.setError(null);

                }else if (TextUtils.isEmpty(mPassword1))
                {
                    c_password.setError(getString(R.string.enter_password));
                    c_phone.setError(null);
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
                    Toast.makeText(Activity_Client_Register.this, "كلمة السر غير متطابقة", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(mEmail))
                {
                    c_email.setError(getString(R.string.enter_email));
                    c_re_password.setError(null);
                }else if (!Patterns.EMAIL_ADDRESS.matcher(mEmail).matches())
                {
                    c_email.setError(getString(R.string.inv_email));
                    c_re_password.setError(null);


                }else if (bitmap==null)
                {
                    Toast.makeText(Activity_Client_Register.this,getString(R.string.choose_image), Toast.LENGTH_LONG).show();


                }else
                    {
                        Toast.makeText(Activity_Client_Register.this, "register ok", Toast.LENGTH_SHORT).show();
                    }




            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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

    private String enCode(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,90,outputStream);
        byte [] bytes = outputStream.toByteArray();

        enCodedImage = Base64.encodeToString(bytes,Base64.DEFAULT);

        return enCodedImage;
    }
}
