package com.semicolon.Halan.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.semicolon.Halan.Models.UserModel;
import com.semicolon.Halan.R;
import com.semicolon.Halan.Services.Api;
import com.semicolon.Halan.Services.Preferences;
import com.semicolon.Halan.Services.Services;
import com.semicolon.Halan.Services.Tags;
import com.semicolon.Halan.SingleTone.Users;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import de.hdodenhof.circleimageview.CircleImageView;
import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Activity_Driver_Register2 extends AppCompatActivity implements View.OnClickListener ,Users.UserData{

    private String city,identety,vihile_number,car_color,car_model,mAge,mGender,country;
    private EditText e_car_model;
    private TextView t_photo_car_form,t_license_photo,t_front_image,t_back_image;
    private ImageView photo_car_form,license_photo,front_image,back_image;
    private Preferences preferences;
    private Button register;
    private ImageView back;
    private String encoded_user_image="";
    private AlertDialog alertDialog;
    private final int IMG_REQ1 = 100;
    private final int IMG_REQ2 = 200;
    private final int IMG_REQ3 = 300;
    private final int IMG_REQ4 = 400;
    private final int IMG_REQ5 = 500;

    private CircleImageView image;
    private String enCodedImage1,enCodedImage2,enCodedImage3,enCodedImage4;
    private Bitmap bitmap1,bitmap2,bitmap3,bitmap4,user_bitmap;
    private UserModel userModel;
    private Users users;
    private ProgressDialog dialog;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private String Fine_Loc = Manifest.permission.ACCESS_FINE_LOCATION;
    private String Coarse_Loc = Manifest.permission.ACCESS_COARSE_LOCATION;
    private LatLng myLatLng=null;
    private boolean isGranted = false;
    private final int per_req = 1300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_register2);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "JannaLT-Regular.ttf", true);

        preferences = new Preferences(getApplicationContext());
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        initView();
        CreateAlertDialog();
        getDataFromIntent();
        CheckPermission();
        users=Users.getInstance();
        users.getUserData(this);


    }

    private void CheckPermission() {
        String [] premissions = new String[]{Fine_Loc,Coarse_Loc};

        if (ContextCompat.checkSelfPermission(this,Fine_Loc)== PackageManager.PERMISSION_GRANTED)
        {
            if (ContextCompat.checkSelfPermission(this,Coarse_Loc)== PackageManager.PERMISSION_GRANTED)
            {
                isGranted = true;
                getDeviceLocation();
            }else
                {
                    ActivityCompat.requestPermissions(Activity_Driver_Register2.this,premissions,per_req);
                }
        }else
            {
                ActivityCompat.requestPermissions(Activity_Driver_Register2.this,premissions,per_req);

            }
    }

    private void initView() {
        image = findViewById(R.id.image);
        back = findViewById(R.id.back);
        e_car_model=findViewById(R.id.car_model);
        t_photo_car_form=findViewById(R.id.photo_car_form);
        t_license_photo=findViewById(R.id.license_photo);
        t_front_image=findViewById(R.id.front_image);
        t_back_image=findViewById(R.id.back_image);

        photo_car_form=findViewById(R.id.img_photo_car_form);
        license_photo=findViewById(R.id.img_license_photo);
        front_image=findViewById(R.id.img_front);
        back_image=findViewById(R.id.img_back);

        register=findViewById(R.id.regiser_Btn);
        register.setOnClickListener(this);
        t_photo_car_form.setOnClickListener(this);
        t_license_photo.setOnClickListener(this);
        t_front_image.setOnClickListener(this);
        t_back_image.setOnClickListener(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent.createChooser(intent,getString(R.string.choose_image)),IMG_REQ5);
            }
        });


    }

    private void getDeviceLocation()
    {
        if (isGranted)
        {
            try {
                Task<Location> task = fusedLocationProviderClient.getLastLocation();
                task.addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful())
                        {
                            Location location = task.getResult();
                            if (location!=null)
                            {
                                LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                                getLatLng(latLng);
                            }
                        }
                    }
                });
            }catch (SecurityException e)
            {

            }catch (NullPointerException e)
            {
                Toast.makeText(this, R.string.loc_notfounded, Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void getLatLng(LatLng latLng)
    {
        myLatLng = latLng;
    }
    private void getDataFromIntent() {

        Intent intent=getIntent();

        if (intent!=null){
            country = intent.getStringExtra("country");
            city=intent.getStringExtra("city");
            identety=intent.getStringExtra("identity");
            vihile_number=intent.getStringExtra("vihile_number");
            car_color=intent.getStringExtra("car_color");
            mAge= intent.getStringExtra("age");
            mGender =intent.getStringExtra("gender");

        }else
        {
            Toast.makeText(this, "data is empty", Toast.LENGTH_SHORT).show();
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
            case R.id.front_image:
                Intent intent3 = new Intent(Intent.ACTION_GET_CONTENT);
                intent3.setType("image/*");
                startActivityForResult(intent3.createChooser(intent3,getString(R.string.choose_image)),IMG_REQ3);
            break;
            case R.id.back_image:
                Intent intent4 = new Intent(Intent.ACTION_GET_CONTENT);
                intent4.setType("image/*");
                startActivityForResult(intent4.createChooser(intent4,getString(R.string.choose_image)),IMG_REQ4);
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

        }else if (bitmap4==null){
            Toast.makeText(Activity_Driver_Register2.this,getString(R.string.choose_image), Toast.LENGTH_LONG).show();
            e_car_model.setError(null);

        }else if (userModel.getUser_photo().equals("0")&&user_bitmap==null)
        {
            Toast.makeText(this,getString(R.string.choose_image), Toast.LENGTH_LONG).show();
        }
        else {
            CreateProgressDialog();
            dialog.show();
            enCode1(bitmap1);
            enCode2(bitmap2);
            enCode3(bitmap3);
            enCode4(bitmap4);
            if (user_bitmap!=null)
            {
                enCode5(user_bitmap);
            }else
                {
                    encoded_user_image="";
                }
            String lat = String.valueOf(myLatLng.latitude);
            String lng = String.valueOf(myLatLng.longitude);
            Log.e("latLng",""+myLatLng.latitude);
            Log.e("latLng",""+myLatLng.longitude);

            Services services= Api.getClient(Tags.BASE_URL).create(Services.class);
        Call<UserModel> call=services.driverSignIn(userModel.getUser_id(),encoded_user_image,city,mAge,mGender,country,identety,car_model,vihile_number,car_color,enCodedImage1,enCodedImage2,enCodedImage3,enCodedImage4,lat,lng);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {

                if (response.isSuccessful()){

                    if (response.body().getSuccess()==1){
                        UserModel userModel=response.body();
                        Log.e("mmmm",userModel.getUser_id()+city+identety+car_model+car_color+enCodedImage1+enCodedImage2+enCodedImage3);
                        /*preferences.UpdatePref(userModel);
                        users.setUserData(userModel);*/
                        dialog.dismiss();
                        alertDialog.show();

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
                dialog.dismiss();
                Toast.makeText(Activity_Driver_Register2.this, ""+getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();
                Log.e("error",t.getMessage());
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
    private void CreateAlertDialog()
    {
        alertDialog = new AlertDialog.Builder(this)
                .setMessage(R.string.req_sent)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        alertDialog.dismiss();
                    }
                })
                .create();
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

                front_image.setImageBitmap(bitmap3);

                //enCode(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode==IMG_REQ4 && resultCode == RESULT_OK && data!=null)
        {
            Uri uri = data.getData();
            try {
                bitmap4= BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));

                back_image.setImageBitmap(bitmap4);

                //enCode(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode==IMG_REQ5 && resultCode == RESULT_OK && data!=null)
        {
            Uri uri = data.getData();
            try {
                user_bitmap= BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));

                image.setImageBitmap(user_bitmap);

                //enCode(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        isGranted = false;
        switch (requestCode)
        {
            case per_req:
                if (grantResults.length>0)
                {
                    for (int i=0;i<grantResults.length;i++)
                    {
                        if (grantResults[i]!=PackageManager.PERMISSION_GRANTED)
                        {
                            return;
                        }
                    }

                }
                isGranted = true;
                getDeviceLocation();
                break;

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

    private String enCode4(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,90,outputStream);
        byte [] bytes = outputStream.toByteArray();

        enCodedImage4 = Base64.encodeToString(bytes,Base64.DEFAULT);

        return enCodedImage4;
    }

    private String enCode5(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,90,outputStream);
        byte [] bytes = outputStream.toByteArray();

        encoded_user_image = Base64.encodeToString(bytes,Base64.DEFAULT);

        return encoded_user_image;
    }

    @Override
    public void UserDataSuccess(UserModel userModel) {
        this.userModel=userModel;
        if (userModel.getUser_photo().equals("0"))
        {
            image.setVisibility(View.VISIBLE);
        }else
            {
                image.setVisibility(View.GONE);

            }

    }
}
