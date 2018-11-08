package com.wassimapp.a3dprinter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wassimapp.a3dprinter.Configuration.ServiceConfig;
import com.wassimapp.a3dprinter.Service.UserService;
import com.wassimapp.a3dprinter.customfonts.MyEditText;
import com.wassimapp.a3dprinter.customfonts.MyTextView;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;


public class ActivitySignin extends AppCompatActivity {


    ImageView signinback;
    MyTextView signinbtn;
    MyEditText username, password;
    FrameLayout frameLayout;
    public static final String PREFS = "MyPrefs";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        frameLayout = (FrameLayout)findViewById(R.id.frameLayout);
        signinback = (ImageView) findViewById(R.id.signinback);
        signinbtn = (MyTextView) findViewById(R.id.signin);

        username = (MyEditText) findViewById(R.id.username);
        password = (MyEditText) findViewById(R.id.password);

        signinbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().isEmpty()|| password.getText().toString().isEmpty()){
                    Snackbar snackbar = Snackbar.make(frameLayout,"Please verify all fields",Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }else{
                    loginProcessWithRetrofit(username.getText().toString(), password.getText().toString());

                }


            }
        });


        signinback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(ActivitySignin.this, MainActivity.class);
                startActivity(it);
            }
        });
    }


    private void loginProcessWithRetrofit(final String username, String password) {

        Retrofit retrofit = ServiceConfig.getServiceConfig();
        UserService userService = retrofit.create(UserService.class);

        Observable<Response<ResponseBody>> authenticateUser = userService.authenticate(username, password);

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(ActivitySignin.this);
        progressDoalog.setMessage("Loading....");

        authenticateUser.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<ResponseBody>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull Response<ResponseBody> responseBodyResponse) {


                        JsonParser parser = new JsonParser();
                        try {
                            JsonObject o = parser.parse(responseBodyResponse.body().string()).getAsJsonObject();

                            if (o.get("success").getAsString().equals("true")){
                                progressDoalog.show();

                                //store user id in shared preferences
                                SharedPreferences.Editor editor = getSharedPreferences(PREFS, MODE_PRIVATE).edit();
                                editor.putString("iduser", String.valueOf(o.get("user").getAsJsonObject().get("_id")));
                                editor.putString("idToken",String.valueOf(o.get("idToken")));
                                editor.apply();

                                new Handler().postDelayed(new Runnable() {

                                    @Override
                                    public void run() {
                                        progressDoalog.dismiss();
                                        Toast.makeText(getApplicationContext(),"Logged Successfully",Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(ActivitySignin.this, HistoryActivity.class);
                                        startActivity(i);
                                    }

                                }, 1500);
                            }else{
                                progressDoalog.show();

                                new Handler().postDelayed(new Runnable() {

                                    @Override
                                    public void run() {
                                        progressDoalog.dismiss();
                                        Toast.makeText(getApplicationContext(),"Username or Password incorrect",Toast.LENGTH_SHORT).show();

                                    }

                                }, 1500);
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }



                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(getApplicationContext(),"No network connection",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }
}
