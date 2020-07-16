package com.jahezly.services;


import com.jahezly.models.UserModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Service {

    @FormUrlEncoded
    @POST("api/login-restaurant")
    Call<UserModel> login(@Field("user_name") String user_name,
                          @Field("password") String password
    );

    @FormUrlEncoded
    @POST("api/logout-restaurant")
    Call<ResponseBody> logout(@Header("Authorization") String token,
                              @Field("phone_token") String firebase_token,
                              @Field("soft_type") String soft_type,
                              @Field("user_id") int user_id
    );


    @FormUrlEncoded
    @POST("api/update-firebase")
    Call<ResponseBody> updateFirebaseToken(@Header("Authorization") String token,
                                           @Field("phone_token") String firebase_token,
                                           @Field("soft_type") String soft_type,
                                           @Field("user_type") String user_type,
                                           @Field("user_id") int user_id
    );


}