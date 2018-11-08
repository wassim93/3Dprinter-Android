package com.wassimapp.a3dprinter.Service;

import com.wassimapp.a3dprinter.Models.Comment;

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
 * Created by wassim on 03/03/2018.
 */

public interface CommentService {

    @GET("api/Comments/{idpost}")
    Observable<ArrayList<Comment>> getComments(@Header("x-access-token") String token, @Path("idpost") String idpost);

    @DELETE("api/Comments/{idcomment}/delete/{idpost}")
    Observable<ArrayList<Comment>> deleteComment(@Header("x-access-token") String token,@Path("idcomment") String idcomment, @Path("idpost") String idpost);


    @FormUrlEncoded
    @POST("api/comment/{idpost}/{iduser}/add")
    Observable<ArrayList<Comment>> addComment(@Header("x-access-token") String token , @Path("idpost") String idpost,@Path("iduser") String userid, @Field("Content") String content);

    @FormUrlEncoded
    @PUT("api/Comments/{idcomment}/update")
    Observable<ArrayList<Comment>> updateCom(@Header("x-access-token") String token , @Path("idcomment") String idcomment, @Field("Content") String content);

}
