package com.wassimapp.a3dprinter.Service;

import com.wassimapp.a3dprinter.Models.Post;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by wassim on 24/02/2018.
 */

public interface PostService {

    @GET("api/posts")
    Observable<ArrayList<Post>> getPosts(@Header("x-access-token") String token);

    @FormUrlEncoded
    @POST("api/post/{iduser}/add")
    Observable<ArrayList<Post>> addPost(@Header("x-access-token") String token ,@Path("iduser") String userid, @Field("content") String content);

    @FormUrlEncoded
    @PUT("api/post/{idpost}/update")
    Observable<ArrayList<Post>> updatePost(@Header("x-access-token") String token ,@Path("idpost") String idpost, @Field("content") String content);

    @GET("api/post/{idpost}")
    Observable<Post> getPostById(@Header("x-access-token") String token, @Path("idpost") String userid);

    @DELETE("api/post/{idpost}/delete")
    Observable<ArrayList<Post>> deletePost(@Header("x-access-token") String token, @Path("idpost") String idpost);



}
