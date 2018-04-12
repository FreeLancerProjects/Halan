package com.semicolon.Halan.Services;

import com.semicolon.Halan.Models.AvailableDriversModel;
import com.semicolon.Halan.Models.MyOrderModel;
import com.semicolon.Halan.Models.PlaceModel;
import com.semicolon.Halan.Models.ResponseModel;
import com.semicolon.Halan.Models.TotalCostModel;
import com.semicolon.Halan.Models.UserModel;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

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
    Call<UserModel> UpdateClient(@Path("user_id") String user_id,
                                 @Field("user_name") String user_name,
                                 @Field("user_phone") String user_phone,
                                 @Field("user_email") String user_email,
                                 @Field("user_photo") String user_photo
    );


    @GET()
    Call<PlaceModel> getDirection(@Url String url);

    @FormUrlEncoded
    @POST("Api/UpdateTokenId/{user_id}")
    Call<ResponseModel>Update_token(@Path("user_id")String user_id,
                                    @Field("user_token_id") String token);


    @FormUrlEncoded
    @POST("Api/DriverPayment")
    Call<UserModel> DriverPayment(@Field("user_id") String user_id,
                                           @Field("amount") String amount,
                                           @Field("date") String date,
                                           @Field("bank") String bank,
                                           @Field("code") String code
    );

    @FormUrlEncoded
    @POST("Api/ContactUs")
    Call<UserModel> ContactUs (@Field("name") String name,
                                  @Field("email") String email,
                                  @Field("subject") String subject,
                                  @Field("message") String message
    );

    @GET("Api/ContactUs")
    Call<UserModel> getNumber ();

    @FormUrlEncoded
    @POST("Api/UpdateLocation/{driver_id}")
    Call<ResponseModel> UpdateDriver_Locaion(@Path("driver_id")String driver_id,
                                             @Field("user_google_lat") String lat,
                                             @Field("user_google_long") String lng);

    @GET("Api/ViewDriverOrders/1/{user_id}")
    Call<List<MyOrderModel>> getCurrentOrders (@Path("user_id")String user_id);

    @GET("Api/ViewDriverOrders/4/{user_id}")
    Call<List<MyOrderModel>> getPreviousOrders (@Path("user_id")String user_id);

    @GET("Api/ViewDriverOrders/2/{user_id}")
    Call<List<MyOrderModel>> getCanceledOrders (@Path("user_id")String user_id);
    @GET("Api/ShowDrivers")
    Call<List<AvailableDriversModel>> ShowAvailable_Drivers();

    @FormUrlEncoded
    @POST("Api/DestanceCost")
    Call<TotalCostModel> getCostByDistance(@Field("my_distance") String distance);
    @FormUrlEncoded
    @POST("Api/AddMyOrder")
    Call<ResponseModel> sendOrder(@FieldMap Map<String,String> map,@Field("driver_id[] array") List<String> driver_ids);
}
