package com.semicolon.Halan.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.semicolon.Halan.Models.UserModel;
import com.semicolon.Halan.R;
import com.semicolon.Halan.Services.Api;
import com.semicolon.Halan.Services.Preferences;
import com.semicolon.Halan.Services.Services;
import com.semicolon.Halan.Services.Tags;
import com.semicolon.Halan.SingleTone.Users;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity implements Users.UserData,View.OnClickListener {

    Users users;
    private UserModel userModel;
    private ImageView imageView,edtname,edtemail,edtphone;
    TextView name,email,phone;
    private Preferences preferences;
    private final int IMG_REQ = 100;
    private String enCodedImage;
    private Bitmap bitmap;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "JannaLT-Regular.ttf", true);

        initView();
        users=Users.getInstance();
        preferences=new Preferences(this);
        users.getUserData(this);

    }

    @Override
    public void UserDataSuccess(UserModel userModel) {

        this.userModel=userModel;
        UpdateUi(userModel);

    }
    private void UpdateUi(UserModel userModel) {
        name.setText(userModel.getUser_name());
        phone.setText(userModel.getUser_phone());
        email.setText(userModel.getUser_email());
        Picasso.with(this).load(Uri.parse(Tags.ImgPath + userModel.getUser_photo())).placeholder(R.drawable.user_profile).into(imageView);

    }

    private void initView() {
        back = findViewById(R.id.back);
        imageView=findViewById(R.id.img_profile);
        name=findViewById(R.id.user_name);
        phone=findViewById(R.id.txt_phone);
        email=findViewById(R.id.txt_email);

        edtname=findViewById(R.id.img_username);
        edtemail=findViewById(R.id.img_email);
        edtphone=findViewById(R.id.img_phone);

        edtname.setOnClickListener(this);
        edtemail.setOnClickListener(this);
        edtphone.setOnClickListener(this);
        imageView.setOnClickListener(this);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.img_username:
                  updateName();
                break;
            case R.id.img_email:
                updateEmail();
                break;
            case R.id.img_phone:
                updatePhone();
            break;
            case R.id.img_profile:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent.createChooser(intent,getString(R.string.choose_image)),IMG_REQ);
              //  sendImageToServer();
                break;

        }

    }

    private void sendDataToServer()
    {

        Services services= Api.getClient(Tags.BASE_URL).create(Services.class);
        Call<UserModel> call=services.UpdateClient(userModel.getUser_id(),name.getText().toString(),phone.getText().toString(),email.getText().toString(),"");
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess()==1){

                        preferences.UpdatePref(response.body());
                        users.setUserData(response.body());
                     //   Toast.makeText(UserProfileActivity.this, ""+userModel.getUser_name(), Toast.LENGTH_SHORT).show();
                     //   Log.e("name",userModel.getUser_name());
                        Toast.makeText(UserProfileActivity.this, R.string.data_send, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(UserProfileActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();

                    }
                }else {
                    Toast.makeText(UserProfileActivity.this, ""+getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.e("mmmmm",t.getMessage()+"");
                Toast.makeText(UserProfileActivity.this, ""+getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void sendImageToServer()
    {
        enCode(bitmap);

        Services services= Api.getClient(Tags.BASE_URL).create(Services.class);
        Call<UserModel> call=services.UpdateClient(userModel.getUser_id(),name.getText().toString(),phone.getText().toString(),email.getText().toString(),enCodedImage);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess()==1){

                        preferences.UpdatePref(response.body());
                        users.setUserData(response.body());
                       // Toast.makeText(UserProfileActivity.this, ""+userModel.getUser_name(), Toast.LENGTH_SHORT).show();
                      //  Log.e("name",userModel.getUser_name());
                        Toast.makeText(UserProfileActivity.this, R.string.data_send, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(UserProfileActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();

                    }
                }else {
                    Toast.makeText(UserProfileActivity.this, ""+getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.e("mmmmm",t.getMessage()+"");
                Toast.makeText(UserProfileActivity.this, ""+getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void updateName()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
        builder.setTitle(R.string.update_username);
        final EditText input = new EditText(UserProfileActivity.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_TEXT);
        input.setHint(getString(R.string.enter_new_username));
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                name.setText(input.getText().toString());
                sendDataToServer();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void updatePhone()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
        builder.setTitle(R.string.update_phone);
        final EditText input = new EditText(UserProfileActivity.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_PHONE);
        input.setHint(getString(R.string.enter_new_phone));
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                phone.setText(input.getText().toString());
                sendDataToServer();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void updateEmail()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfileActivity.this);
        builder.setTitle(R.string.update_email);
        final EditText input = new EditText(UserProfileActivity.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        input.setHint(getString(R.string.enter_new_email));
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                email.setText(input.getText().toString());
                sendDataToServer();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
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
                imageView.setImageBitmap(bitmap);
               enCode(bitmap);
               sendImageToServer();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
