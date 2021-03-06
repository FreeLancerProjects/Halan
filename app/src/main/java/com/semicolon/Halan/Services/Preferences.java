package com.semicolon.Halan.Services;

import android.content.Context;
import android.content.SharedPreferences;

import com.semicolon.Halan.Models.UserModel;

/**
 * Created by Delta on 06/04/2018.
 */

public class Preferences {
    private Context context;

    public Preferences(Context context) {
        this.context = context;
    }

    public void CreatePref(UserModel userModel)
    {
        SharedPreferences pref = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("user_id",userModel.getUser_id());
        editor.putString("user_type",userModel.getUser_type());
        editor.putString("name",userModel.getName());
        editor.putString("user_age",userModel.getUser_age());
        editor.putString("user_gender",userModel.getUser_gender());

        editor.putString("rate_evaluation",String.valueOf(userModel.getRate_evaluation()));
        editor.putString("stars_evaluation",String.valueOf(userModel.getStars_evaluation()));
        editor.putString("order_count",String.valueOf(userModel.getOrder_count()));


        editor.putString("user_name",userModel.getUser_name());
        editor.putString("user_phone",userModel.getUser_phone());
        editor.putString("user_email",userModel.getUser_email());
        editor.putString("user_token_id",userModel.getUser_token_id());
        editor.putString("user_photo",userModel.getUser_photo());
        editor.putString("user_city",userModel.getUser_city());
        editor.putString("user_country",userModel.getUser_country());

        editor.putString("user_national_num",userModel.getUser_national_num());
        editor.putString("user_car_num",userModel.getUser_car_num());
        editor.putString("user_car_model",userModel.getUser_car_model());
        editor.putString("user_car_color",userModel.getUser_car_color());
        editor.putString("user_car_license",userModel.getUser_car_license());
        editor.putString("user_car_form",userModel.getUser_car_form());
        editor.putString("user_car_photo",userModel.getUser_car_photo());
        editor.putString("user_confirm_code",userModel.getUser_confirm_code());
        editor.putString("date_registration",userModel.getDate_registration());
        editor.putString("user_google_lat",userModel.getUser_google_lat());
        editor.putString("user_google_long",userModel.getUser_google_long());
        editor.putString("session",Tags.login);
        editor.apply();

    }
    public void UpdatePref(UserModel userModel)
    {
        SharedPreferences pref = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("user_id",userModel.getUser_id());
        editor.putString("user_type",userModel.getUser_type());
        editor.putString("user_name",userModel.getUser_name());
        editor.putString("name",userModel.getName());
        editor.putString("user_age",userModel.getUser_age());
        editor.putString("user_gender",userModel.getUser_gender());

        editor.putString("rate_evaluation",String.valueOf(userModel.getRate_evaluation()));
        editor.putString("stars_evaluation",String.valueOf(userModel.getStars_evaluation()));
        editor.putString("order_count",String.valueOf(userModel.getOrder_count()));


        editor.putString("user_phone",userModel.getUser_phone());
        editor.putString("user_email",userModel.getUser_email());
        editor.putString("user_token_id",userModel.getUser_token_id());
        editor.putString("user_photo",userModel.getUser_photo());
        editor.putString("user_city",userModel.getUser_city());
        editor.putString("user_country",userModel.getUser_country());

        editor.putString("user_national_num",userModel.getUser_national_num());
        editor.putString("user_car_num",userModel.getUser_car_num());
        editor.putString("user_car_model",userModel.getUser_car_model());
        editor.putString("user_car_color",userModel.getUser_car_color());
        editor.putString("user_car_license",userModel.getUser_car_license());
        editor.putString("user_car_form",userModel.getUser_car_form());
        editor.putString("user_car_photo",userModel.getUser_car_photo());
        editor.putString("user_confirm_code",userModel.getUser_confirm_code());
        editor.putString("date_registration",userModel.getDate_registration());
        editor.putString("user_google_lat",userModel.getUser_google_lat());
        editor.putString("user_google_long",userModel.getUser_google_long());
        editor.apply();

    }

    public void ClearPref()
    {
        SharedPreferences pref = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("user_id","");
        editor.putString("user_type","");
        editor.putString("user_name","");
        editor.putString("name","");
        editor.putString("user_age","");
        editor.putString("user_gender","");

        editor.putString("rate_evaluation","");
        editor.putString("stars_evaluation","");
        editor.putString("order_count","");


        editor.putString("user_phone","");
        editor.putString("user_email","");
        editor.putString("user_token_id","");
        editor.putString("user_photo","");
        editor.putString("user_city","");
        editor.putString("user_country","");

        editor.putString("user_national_num","");
        editor.putString("user_car_num","");
        editor.putString("user_car_model","");
        editor.putString("user_car_color","");
        editor.putString("user_car_license","");
        editor.putString("user_car_form","");
        editor.putString("user_car_photo","");
        editor.putString("user_confirm_code","");
        editor.putString("date_registration","");
        editor.putString("user_google_lat","");
        editor.putString("user_google_long","");
        editor.putString("session",Tags.logout);
        editor.apply();


    }
    public UserModel getUserData()
    {
        SharedPreferences pref = context.getSharedPreferences("user",Context.MODE_PRIVATE);
       String  user_id= pref.getString("user_id","");
       String  user_type= pref.getString("user_type","");
       String  user_name= pref.getString("user_name","");
        String  name= pref.getString("name","");
        String  user_age= pref.getString("user_age","");
        String  user_gender= pref.getString("user_gender","");
        String rate_evaluation= pref.getString("rate_evaluation","");
        String stars_evaluation = pref.getString("stars_evaluation","");
        String order_count = pref.getString("order_count","");
        String  user_phone= pref.getString("user_phone","");
       String  user_email= pref.getString("user_email","");
       String  user_token_id= pref.getString("user_token_id","");
       String  user_photo= pref.getString("user_photo","");
       String  user_city= pref.getString("user_city","");
        String  user_country= pref.getString("user_country","");

        String  user_national_num= pref.getString("user_national_num","");
       String  user_car_num= pref.getString("user_car_num","");
       String  user_car_model= pref.getString("user_car_model","");
       String  user_car_color= pref.getString("user_car_color","");
       String  user_car_license= pref.getString("user_car_license","");
       String  user_car_form= pref.getString("user_car_form","");
       String  user_car_photo= pref.getString("user_car_photo","");
       String  user_confirm_code= pref.getString("user_confirm_code","");
       String  date_registration= pref.getString("date_registration","");
       String  user_google_lat= pref.getString("user_google_lat","");
       String  user_google_long= pref.getString("user_google_long","");

       UserModel userModel = new UserModel();
       userModel.setUser_id(user_id);
       userModel.setUser_type(user_type);
       userModel.setName(name);
       userModel.setUser_age(user_age);
       userModel.setUser_gender(user_gender);

        userModel.setRate_evaluation(Double.parseDouble(rate_evaluation));
        userModel.setStars_evaluation(Integer.parseInt(stars_evaluation));
        userModel.setOrder_count(Integer.parseInt(order_count));

       userModel.setUser_name(user_name);
       userModel.setUser_phone(user_phone);
       userModel.setUser_email(user_email);
       userModel.setUser_token_id(user_token_id);
       userModel.setUser_photo(user_photo);
       userModel.setUser_city(user_city);
       userModel.setUser_country(user_country);
       userModel.setUser_national_num(user_national_num);
       userModel.setUser_car_num(user_car_num);
       userModel.setUser_car_model(user_car_model);
       userModel.setUser_car_color(user_car_color);
       userModel.setUser_car_license(user_car_license);
       userModel.setUser_car_form(user_car_form);
       userModel.setUser_car_photo(user_car_photo);
       userModel.setUser_confirm_code(user_confirm_code);
       userModel.setDate_registration(date_registration);
       userModel.setUser_google_lat(user_google_lat);
       userModel.setUser_google_long(user_google_long);

       return userModel;
    }

    public void Update_UserState(String state)
    {
        SharedPreferences pref = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("state",state);
        editor.apply();
    }

    public void UpdateSoundPref(String state)
    {

        SharedPreferences preferences = context.getSharedPreferences("sound",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("state",state);
        editor.apply();
    }

    public String getSoundState()
    {
        SharedPreferences preferences = context.getSharedPreferences("sound",Context.MODE_PRIVATE);
        String state = preferences.getString("state","");
        return state;
    }

    public void Createpref_chat_user_id(String id)
    {
        SharedPreferences preferences = context.getSharedPreferences("chat_id",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("chat_id",id);
        editor.apply();


    }
}
