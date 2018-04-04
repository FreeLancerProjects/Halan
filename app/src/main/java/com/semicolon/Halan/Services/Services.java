package com.semicolon.Halan.Services;

import com.semicolon.Halan.Models.UserModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Delta on 25/03/2018.
 */

public interface Services {

      /*---------------------------------------- user login & register -------------------------------------------*/

    @FormUrlEncoded
    @POST("Api/ClientRegistration")
    Call<UserModel> userSignUp(@Field("user_name") String user_name,
                               @Field("user_pass") String user_pass,
                               @Field("user_phone") String user_phone,
                               @Field("user_email") String user_email,
                               @Field("user_token_id") String user_token_id,
                               @Field("user_photo") String user_photo);


    @FormUrlEncoded
    @POST("Api/Login")
    Call<UserModel> userSignIn(@Field("user_name") String user_name,
                               @Field("user_pass") String user_pass
    );


}
