package com.semicolon.Halan.Services;

import com.semicolon.Halan.Models.PlaceModel;
import com.semicolon.Halan.Models.UserModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

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


    @FormUrlEncoded
    @POST("Api/DriverRegistration/{user_id}")
    Call<UserModel> driverSignIn(@Path("user_id") String user_id,
                                 @Field("user_city") String user_city ,
                                 @Field("user_national_num") String user_national_num ,
                                 @Field("user_car_model") String user_car_model ,
                                 @Field("user_car_color") String user_car_color ,
                                 @Field("user_car_form") String user_car_form ,
                                 @Field("user_car_license") String user_car_license  ,
                                 @Field("user_car_photo") String user_car_photo,
                                 @Field("user_google_lat") String user_google_lat,
                                 @Field("user_google_long") String user_google_long);

    @FormUrlEncoded
    @POST("Api/UpdateClient/{user_id}")
    Call<UserModel> UpdateClient(@Field("user_name") String user_name,
                                 @Field("user_phone") String user_phone,
                                 @Field("user_email") String user_email,
                                 @Field("user_photo") String user_photo
    );


    @GET("https://maps.googleapis.com/maps/api/directions/json?origin={origin_lat},{origin_lng}&destination={dist_lat},{dist_lng}&key={api_key}")
    Call<PlaceModel> getDirection(@Path("origin_lat") double origin_lat,
                                  @Path("origin_lng") double origin_lng,
                                  @Path("origin_lat") double dist_lat,
                                  @Path("origin_lng") double dist_lng,
                                  @Path("api_key") String api_key
                                  );

}
