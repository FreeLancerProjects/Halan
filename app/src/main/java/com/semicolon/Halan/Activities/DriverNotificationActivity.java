package com.semicolon.Halan.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.semicolon.Halan.Models.UserModel;
import com.semicolon.Halan.R;
import com.semicolon.Halan.SingleTone.Users;

public class DriverNotificationActivity extends AppCompatActivity implements Users.UserData{
    private Users users;
    private UserModel userModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_notification);
        users = Users.getInstance();
        users.getUserData(this);
        initView();
    }

    private void initView() {
    }

    @Override
    public void UserDataSuccess(UserModel userModel) {
        this.userModel = userModel;

    }
}
