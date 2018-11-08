package com.wassimapp.a3dprinter.Configuration;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wassim on 10/02/2018.
 *
 */

public  class ServiceConfig {

    static final String BASE_URL="http://192.168.137.84:3000";
    public static  final String IMG_DIR ="http://192.168.137.84:3000/uploads/";
    public static  final String FILE_DIR ="http://192.168.137.84:3000/STLFiles/";

    static final String BASE_URL2="http://192.168.1.2:3000";
    public static  final String IMG_DIR2 ="http://192.168.1.2:3000/uploads/";
    public static  final String FILE_DIR2 ="http://192.168.1.2:3000/STLFiles/";

    public  static Retrofit getServiceConfig (){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public  static Retrofit getServiceConfig2 (){

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL2)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
