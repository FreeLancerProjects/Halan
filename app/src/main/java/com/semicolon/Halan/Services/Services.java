package com.semicolon.Halan.Services;

import com.semicolon.Halan.Models.AppRateModel;
import com.semicolon.Halan.Models.AvailableDriversModel;
import com.semicolon.Halan.Models.BankAccountModel;
import com.semicolon.Halan.Models.ClientLastOrderModel;
import com.semicolon.Halan.Models.ClientNotificationModel;
import com.semicolon.Halan.Models.ContactModel;
import com.semicolon.Halan.Models.MyOrderModel;
import com.semicolon.Halan.Models.NearbyModel;
import com.semicolon.Halan.Models.PlaceModel;
import com.semicolon.Halan.Models.PolicyModel;
import com.semicolon.Halan.Models.ResponseModel;
import com.semicolon.Halan.Models.RuleModel;
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
    Call<UserModel> userSignUp(@Field("user_full_name") String name,
                               @Field("user_name") String user_name,
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
                                 @Field("user_photo") String photo,
                                 @Field("user_city") String user_city ,
                                 @Field("user_age") String age,
                                 @Field("user_gender") String gender,
                                 @Field("user_country") String country,
                                 @Field("user_national_num") String user_national_num ,
                                 @Field("user_car_model") String user_car_model ,
                                 @Field("user_car_num") String user_car_num,
                                 @Field("user_car_color") String user_car_color ,
                                 @Field("user_car_form") String user_car_form ,
                                 @Field("user_car_license") String user_car_license  ,
                                 @Field("user_car_photo") String user_car_photo,
                                 @Field("user_car_behind_photo") String user_car_photo2,
                                 @Field("user_google_lat") String user_google_lat,
                                 @Field("user_google_long") String user_google_long);

    @FormUrlEncoded
    @POST("Api/UpdateClient/{user_id}")
    Call<UserModel> UpdateClient(@Path("user_id") String user_id,
                                 @Field("user_photo") String user_photo,
                                 @Field("user_name") String user_name,
                                 @Field("user_full_name") String name,
                                 @Field("user_phone") String user_phone,
                                 @Field("user_email") String user_email,
                                 @Field("user_age") String user_age,
                                 @Field("user_gender") String user_gender,
                                 @Field("user_city") String user_city,
                                 @Field("user_country") String country
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
    Call<ContactModel> getContacts ();

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


    @GET("Api/ViewClientOrders/1/{user_id}")
    Call<List<MyOrderModel>> getCurrentOrders_Client (@Path("user_id")String user_id);

    @GET("Api/ViewClientOrders/4/{user_id}")
    Call<List<MyOrderModel>> getPreviousOrders_Client (@Path("user_id")String user_id);

    @GET("Api/ViewClientOrders/2/{user_id}")
    Call<List<MyOrderModel>> getCanceledOrders_Client (@Path("user_id")String user_id);

    @GET("Api/ReceivedRequests/{user_id}")
    Call<List<MyOrderModel>> getNotification (@Path("user_id")String user_id);

    @GET("Api/ShowDrivers")
    Call<List<AvailableDriversModel>> ShowAvailable_Drivers();

    @FormUrlEncoded
    @POST("Api/DestanceCost")
    Call<TotalCostModel> getCostByDistance(@Field("my_distance") String distance);

    @FormUrlEncoded
    @POST("Api/AddMyOrder")
    Call<ResponseModel> sendOrder(@FieldMap Map<String,String> map,@Field("driver_id[] array") List<String> driver_ids);

    @GET("Api/ShowMyRequests/{id}")
    Call<List<ClientNotificationModel>> getclientNotification(@Path("id") String user_id);

    @FormUrlEncoded
    @POST("Api/ClientAction")
    Call<ResponseModel> sendClientRequest_Accept(@FieldMap Map<String,String> map);

    @FormUrlEncoded
    @POST("Api/ClientAction")
    Call<ResponseModel> sendClientRequest_Refuse(@FieldMap Map<String,String> map);

    @FormUrlEncoded
    @POST("Api/DriverAction")
    Call<ResponseModel> sendDriverRequest_Accept(@FieldMap Map<String,String> map);

    @FormUrlEncoded
    @POST("Api/DriverAction")
    Call<ResponseModel> sendDriverRequest_Refuse(@FieldMap Map<String,String> map);

    @FormUrlEncoded
    @POST("Api/ClientCancelOrder/{user_id}")
    Call<ResponseModel> client_cancel_order(@Path("user_id") String user_id,@FieldMap Map<String,String> map);

    @FormUrlEncoded
    @POST("Api/DriverEvaluate/{order_id_fk}")
    Call<ResponseModel> sendDriverEvaluate(@Path("order_id_fk") String order_id_fk,@FieldMap Map<String,String> map);

    @GET("Api/AboutApp")
    Call<PolicyModel> GetAboutUs();

    @GET("Api/TermsAndConditions")
    Call<RuleModel> getRules();

    @FormUrlEncoded
    @POST("Api/SendToNewDrivers")
    Call<ResponseModel> SendToNewDriver(@Field("order_id_fk") String order_id_fk,@Field("user_id") String user_id,@Field("driver_id[] array") List<String> driver_ids);

    @FormUrlEncoded
    @POST("Api/SendToOtherDrivers")
    Call<ResponseModel> SendToOtherDriver(@Field("order_id_fk") String order_id_fk,@Field("user_id") String user_id,@Field("driver_id[] array") List<String> driver_ids);

    @FormUrlEncoded
    @POST("Api/DriverCancelOrder/{user_id}")
    Call<ResponseModel> driverCancelOrder(@Path("user_id") String user_id,@Field("order_id_fk") String order_id_fk,@Field("client_id_fk") String client_id_fk);

    @FormUrlEncoded
    @POST("Api/ClientCancelOrder/{user_id}")
    Call<ResponseModel> clientCancelOrder(@Path("user_id") String user_id,@Field("order_id_fk") String order_id_fk,@Field("driver_id_fk") String driver_id_fk,@Field("cancel_reason_type") String cancel_reason_type);

    @FormUrlEncoded
    @POST("Api/OrderDelivered/{order_id_fk}")
    Call<ResponseModel> orderDeliverd(@Path("order_id_fk") String order_id_fk,@Field("user_type") String user_type);

    @FormUrlEncoded
    @POST("Api/AddOrderBill/{order_id}")
    Call<ResponseModel>SendOrderBill(@Path("order_id") String order_id,@Field("bill_image") String bill_img,@Field("order_bill_cost") String bill_cost);

    @FormUrlEncoded
    @POST("Api/RestMyPass")
    Call<ResponseModel> RestMyPass(@Field("user_name") String user_name,
                                   @Field("user_email") String user_email);




    @FormUrlEncoded
    @POST("Api/GetNotification/{chat_user_id}")
    Call<ResponseModel> PushNotification(@Path("chat_user_id") String chat_user_id,
                                         @Field("curr_id") String curr_id,
                                         @Field("chat_id") String chat_id,
                                         @Field("curr_type") String curr_type,
                                         @Field("chat_type") String chat_type,
                                         @Field("curr_photo") String curr_photo,
                                         @Field("chat_photo") String chat_photo,
                                         @Field("order_id") String order_id,
                                         @Field("title") String title,
                                         @Field("content") String content,
                                         @Field("content_type") String content_type,
                                         @Field("notification_type") String notification_type);


    @GET("Api/LastOrderReplay/{user_id}")
    Call<List<ClientLastOrderModel>>getClient_LastOrder(@Path("user_id") String user_id);

    @GET("Api/Logout/{user_id}")
    Call<ResponseModel> LogOut(@Path("user_id") String id);

    @GET()
    Call<NearbyModel> getNearbyPlaces(@Url String url);

    @GET("Api/AppEvaluation/{user_id}")
    Call<AppRateModel> getApp_Evaluation(@Path("user_id") String user_id);

    @FormUrlEncoded
    @POST("Api/AppEvaluation/{user_id}")
    Call<AppRateModel> setApp_Evaluation(@Path("user_id") String user_id,@Field("app_evaluation")String app_evaluation);

    @GET("Api/BankAccounts")
    Call<List<BankAccountModel>> getBankAccounts();

    @GET("Api/PolicyApp")
    Call<PolicyModel> getPolicy();


}
