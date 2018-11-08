package com.wassimapp.a3dprinter.Service;

import com.wassimapp.a3dprinter.Models.Model3D;

import java.util.ArrayList;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by wassim on 18/02/2018.
 */

public interface ModelService {

    @GET("api/android/models")
    Call<ArrayList<Model3D>> getModels(@Header("x-access-token") String token);

    @GET("api/models/{iduser}")
    Observable<ArrayList<Model3D>> getModelById(@Header("x-access-token") String token,@Path("iduser") String userid);

    @DELETE("api/models/delete/{modelname}")
    Observable<ArrayList<Model3D>> deleteModel(@Header("x-access-token") String token, @Path("modelname") String name);

    @Multipart
    @POST("api/uploadfile/{iduser}")
    Observable<ArrayList<Model3D>> uploadModelFile(@Header("x-access-token") String token, @Part MultipartBody.Part file, @Part("name") RequestBody name, @Path("iduser") String userid, @Part("imagename") RequestBody objname, @Part("category") RequestBody category,@Part("visibility") RequestBody visibility);


}
