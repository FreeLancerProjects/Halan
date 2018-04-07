package com.semicolon.Halan.Activities;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.semicolon.Halan.Models.UserModel;
import com.semicolon.Halan.R;
import com.semicolon.Halan.Services.Tags;
import com.semicolon.Halan.SingleTone.Users;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity implements Users.UserData {

    Users users;
    private UserModel userModel;
    private ImageView imageView,edtname,edtemail,edtphone;
    TextView name,email,phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        initView();
        users=Users.getInstance();
        users.getUserData(this);
    }

    @Override
    public void UserDataSuccess(UserModel userModel) {

        this.userModel=userModel;
        UpdateUi(userModel);

    }
    private void UpdateUi(UserModel userModel) {
        name.setText(userModel.getUser_name());
        phone.setText(userModel.getUser_email());
        email.setText(userModel.getUser_phone());
        Picasso.with(this).load(Uri.parse(Tags.ImgPath + userModel.getUser_photo())).placeholder(R.drawable.user_profile).into(imageView);


    }

    private void initView() {
        imageView=findViewById(R.id.img_profile);
        name=findViewById(R.id.user_name);
        phone=findViewById(R.id.txt_phone);
        email=findViewById(R.id.txt_email);




    }
}
