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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.semicolon.Halan.Models.UserModel;
import com.semicolon.Halan.R;
import com.semicolon.Halan.Services.Api;
import com.semicolon.Halan.Services.Services;
import com.semicolon.Halan.SingleTone.Users;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Driver_Register2 extends AppCompatActivity implements View.OnClickListener ,Users.UserData{

    private String city,identety,vihile_number,car_color,car_model;
    private EditText e_car_model;
    private TextView t_photo_car_form,t_license_photo,t_front_back_image;
    private ImageView photo_car_form,license_photo,front_back_image;
    private Button register;
    private final int IMG_REQ1 = 100;
    private final int IMG_REQ2 = 200;
    private final int IMG_REQ3 = 300;

    private String enCodedImage1,enCodedImage2,enCodedImage3;
    private Bitmap bitmap1,bitmap2,bitmap3;
    private UserModel userModel;
    private Users users;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_register2);


        initView();
        getDataFromIntent();
        users=Users.getInstance();
        users.getUserData(this);

    }

    private void initView() {
        e_car_model=findViewById(R.id.car_model);
        t_photo_car_form=findViewById(R.id.photo_car_form);
        t_license_photo=findViewById(R.id.license_photo);
        t_front_back_image=findViewById(R.id.front_back_image);

        photo_car_form=findViewById(R.id.img_photo_car_form);
        license_photo=findViewById(R.id.img_license_photo);
        front_back_image=findViewById(R.id.img_front_back_image);

        register=findViewById(R.id.regiser_Btn);
        register.setOnClickListener(this);
        t_photo_car_form.setOnClickListener(this);
        t_license_photo.setOnClickListener(this);
        t_front_back_image.setOnClickListener(this);



    }

    private void getDataFromIntent() {

        Intent intent=getIntent();

        if (!intent.equals(null)){
            city=intent.getStringExtra("city");
            identety=intent.getStringExtra("identity");
            vihile_number=intent.getStringExtra("vihile_number");
            car_color=intent.getStringExtra("car_color");

        }else
        {
            Toast.makeText(this, "intent data is empty", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.regiser_Btn:
                saveDataToServer();
                break;
            case R.id.photo_car_form:
                Intent intent1 = new Intent(Intent.ACTION_GET_CONTENT);
                intent1.setType("image/*");
                startActivityForResult(intent1.createChooser(intent1,getString(R.string.choose_image)),IMG_REQ1);
                break;
            case R.id.license_photo:
                Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
                intent2.setType("image/*");
                startActivityForResult(intent2.createChooser(intent2,getString(R.string.choose_image)),IMG_REQ2);
                break;
            case R.id.front_back_image:
                Intent intent3 = new Intent(Intent.ACTION_GET_CONTENT);
                intent3.setType("image/*");
                startActivityForResult(intent3.createChooser(intent3,getString(R.string.choose_image)),IMG_REQ3);
            break;
        }

    }

    private void saveDataToServer() {

        car_model=e_car_model.getText().toString();
        if (TextUtils.isEmpty(car_model))
        {
            e_car_model.setError(getString(R.string.enter_car_model));

        }else if (bitmap1==null){
            Toast.makeText(Activity_Driver_Register2.this,getString(R.string.choose_image), Toast.LENGTH_LONG).show();
            e_car_model.setError(null);

        }else if (bitmap2==null){
            Toast.makeText(Activity_Driver_Register2.this,getString(R.string.choose_image), Toast.LENGTH_LONG).show();
            e_car_model.setError(null);

        }else if (bitmap3==null){
            Toast.makeText(Activity_Driver_Register2.this,getString(R.string.choose_image), Toast.LENGTH_LONG).show();
            e_car_model.setError(null);

        }else {
            CreateProgressDialog();
            enCode1(bitmap1);
            enCode2(bitmap2);
            enCode3(bitmap3);
        Services services= Api.getClient().create(Services.class);
        Call<UserModel> call=services.driverSignIn(userModel.getUser_id(),city,identety,car_model,car_color,enCodedImage1,enCodedImage2,enCodedImage3,"","");
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {

                if (response.isSuccessful()){

                    UserModel userModel=response.body();
                    if (response.body().getSuccess()==1){
                        Log.e("mmmm",userModel.getUser_id()+city+identety+car_model+car_color+enCodedImage1+enCodedImage2+enCodedImage3);
                        users.setUserData(userModel);
                        dialog.dismiss();
                        finish();

                    }else {

                        Toast.makeText(Activity_Driver_Register2.this, ""+R.string.regfailed, Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                    }
                }else {

                    Toast.makeText(Activity_Driver_Register2.this, ""+getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();

                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(Activity_Driver_Register2.this, ""+getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

    }
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMG_REQ1 && resultCode == RESULT_OK && data!=null)
        {
            Uri uri = data.getData();
            try {
                bitmap1 = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));

                photo_car_form.setImageBitmap(bitmap1);

                //enCode(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode==IMG_REQ2 && resultCode == RESULT_OK && data!=null)
        {
            Uri uri = data.getData();
            try {
                bitmap2 = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));

                license_photo.setImageBitmap(bitmap2);

                //enCode(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode==IMG_REQ3 && resultCode == RESULT_OK && data!=null)
        {
            Uri uri = data.getData();
            try {
                bitmap3= BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));

                front_back_image.setImageBitmap(bitmap3);

                //enCode(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    private String enCode1(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,90,outputStream);
        byte [] bytes = outputStream.toByteArray();

        enCodedImage1 = Base64.encodeToString(bytes,Base64.DEFAULT);

        return enCodedImage1;
    }
    private String enCode2(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,90,outputStream);
        byte [] bytes = outputStream.toByteArray();

        enCodedImage2 = Base64.encodeToString(bytes,Base64.DEFAULT);

        return enCodedImage2;
    }
    private String enCode3(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,90,outputStream);
        byte [] bytes = outputStream.toByteArray();

        enCodedImage3 = Base64.encodeToString(bytes,Base64.DEFAULT);

        return enCodedImage3;
    }

    @Override
    public void UserDataSuccess(UserModel userModel) {
        this.userModel=userModel;

    }
}
