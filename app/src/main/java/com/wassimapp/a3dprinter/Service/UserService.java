package com.wassimapp.a3dprinter.Service;


import com.wassimapp.a3dprinter.Models.User;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;


/**
 * Created by wassim on 10/02/2018.
 */

public interface UserService {

    @FormUrlEncoded
    @POST("authenticate")
    Observable<Response<ResponseBody>> authenticate(@Field("username") String username, @Field("password")String password);

    @FormUrlEncoded
    @POST("register")
    Observable<Response<ResponseBody>> register(@Field("username") String username,@Field("email")String email,@Field("password")String password);

    @PUT("api/updateprofile/{iduser}")
    Observable<Response<ResponseBody>> updateProfile(@Header("x-access-token") String token,@Path("iduser") String userid,@Body User user);

    @GET("api/user/{iduser}")
    Observable<User> getUserById(@Header("x-access-token") String token,@Path("iduser") String userid);


    @Multipart
    @POST("api/updateprofilepic/{iduser}")
    Observable<Response<ResponseBody>> uploadSingleFile(@Header("x-access-token") String token,@Part MultipartBody.Part file, @Part("name") RequestBody name,@Path("iduser") String userid);


}
