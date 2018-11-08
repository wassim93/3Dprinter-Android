package com.wassimapp.a3dprinter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alexzh.circleimageview.CircleImageView;
import com.squareup.picasso.Picasso;
import com.wassimapp.a3dprinter.Configuration.ServiceConfig;
import com.wassimapp.a3dprinter.Models.User;
import com.wassimapp.a3dprinter.Service.UserService;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class HistoryActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public ImageButton imageButton;
    private RecyclerView rv, rv1;
    public static final String PREFS = "MyPrefs";

    CircleImageView profilepic;
    TextView username,email;
    String token;
    String userid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Last Prints");
        setSupportActionBar(toolbar);




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerContainer = navigationView.getHeaderView(0); // This returns the container layout in nav_drawer_header.xml (e.g., your RelativeLayout or LinearLayout)        navigationView.addHeaderView(view);
        navigationView.setNavigationItemSelectedListener(this);


        //get userid from shared pref
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        userid = prefs.getString("iduser", null);
        token = prefs.getString("idToken", null);

        profilepic = (CircleImageView)headerContainer.findViewById(R.id.drawer_profilepic);
        username = (TextView) headerContainer.findViewById(R.id.drawer_username);
        email = (TextView) headerContainer.findViewById(R.id.drawer_email);

    }

    @Override
    protected void onResume() {
        super.onResume();



        Retrofit retrofit = ServiceConfig.getServiceConfig();
        UserService userService = retrofit.create(UserService.class);

        Observable<User> getUserLogged = userService.getUserById(token.split("\"")[1],userid.split("\"")[1]);



        getUserLogged.subscribeOn(Schedulers.io()) // optional if you do not wish to override the default behavior
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(new Observer<User>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull User user) {
                username.setText(user.getUsername());
                email.setText(user.getEmail());
                Picasso.with(HistoryActivity.this).load(ServiceConfig.IMG_DIR+user.getProfilepicUrl()).into(profilepic);


            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();

            }

            @Override
            public void onComplete() {

            }
        } );
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
        }
    }





    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent i = new Intent(HistoryActivity.this,ProfileActivity.class);
            startActivity(i);
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent i = new Intent(HistoryActivity.this,StoreActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_slideshow) {
            Intent i = new Intent(HistoryActivity.this,PostActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            Intent i = new Intent(HistoryActivity.this, ActivitySignin.class);
            SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
            prefs.edit().clear().apply();
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}
