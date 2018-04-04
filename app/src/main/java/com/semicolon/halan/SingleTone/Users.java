package com.semicolon.halan.SingleTone;

import com.semicolon.halan.Models.UserModel;

/**
 * Created by Delta on 03/04/2018.
 */

public class Users {
    private static Users instance;
    private static UserData interface_userData;
    private UserModel userModel;
    private Users(){}
    public static synchronized Users getInstance()
    {
        if (instance==null)
        {
            instance = new Users();
        }
        return instance;
    }

    public interface UserData
    {
        void UserDataSuccess(UserModel userModel);
    }

    public  void setUserData(UserModel userData)
    {
        userModel = userData;
    }
    public  void getUserData(Users.UserData interface_user)
    {
        interface_userData = interface_user;
        interface_userData.UserDataSuccess(userModel);
    }

}
