package com.wassimapp.a3dprinter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alexzh.circleimageview.CircleImageView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Picasso;
import com.wassimapp.a3dprinter.Adapter.PostAdapter;
import com.wassimapp.a3dprinter.Configuration.ServiceConfig;
import com.wassimapp.a3dprinter.Listeners.BtnCommentListener;
import com.wassimapp.a3dprinter.Models.CustomModels.RecyclerViewEmptySupport;
import com.wassimapp.a3dprinter.Models.Post;
import com.wassimapp.a3dprinter.Models.User;
import com.wassimapp.a3dprinter.Service.PostService;
import com.wassimapp.a3dprinter.Service.UserService;
import com.wassimapp.a3dprinter.custom_view.MyCustomEditText;
import com.wassimapp.a3dprinter.customfonts.MyEditText;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class PostActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener,BtnCommentListener {

    RecyclerViewEmptySupport post_list ;
    CircleImageView profilepic;
    TextView username,email;
    public static final String PREFS = "MyPrefs";
    String token;
    String userid;
    private PostAdapter postAdapter;
    MyCustomEditText myCustomEditText;
    Dialog myDialog;
    User userlogged;
    Retrofit retrofit = ServiceConfig.getServiceConfig();
    private ShimmerFrameLayout mShimmerViewContainer;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Community");
        setSupportActionBar(toolbar);
        myDialog = new Dialog(this);

        mShimmerViewContainer = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerContainer = navigationView.getHeaderView(0); // This returns the container layout in nav_drawer_header.xml (e.g., your RelativeLayout or LinearLayout)        navigationView.addHeaderView(view);
        navigationView.setNavigationItemSelectedListener(this);

        profilepic = (CircleImageView)headerContainer.findViewById(R.id.drawer_profilepic);
        username = (TextView) headerContainer.findViewById(R.id.drawer_username);
        email = (TextView) headerContainer.findViewById(R.id.drawer_email);
        myCustomEditText = (MyCustomEditText)findViewById(R.id.editText);
        myCustomEditText.setOnClickListener(this);

        //get userid from shared pref
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        userid = prefs.getString("iduser", null);
        token = prefs.getString("idToken", null);


    }


    @Override
    protected void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
        //get userid from shared pref



        UserService userService = retrofit.create(UserService.class);

        Observable<User> getUserLogged = userService.getUserById(token.split("\"")[1],userid.split("\"")[1]);



        getUserLogged.subscribeOn(Schedulers.io()) // optional if you do not wish to override the default behavior
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(new Observer<User>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull User user) {
                userlogged = user;
                username.setText(user.getUsername());
                email.setText(user.getEmail());
                Picasso.with(PostActivity.this).load(ServiceConfig.IMG_DIR+user.getProfilepicUrl()).into(profilepic);


            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();

            }

            @Override
            public void onComplete() {

            }
        } );


        PostService postService = retrofit.create(PostService.class);
        loadPosts(postService);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent i = new Intent(PostActivity.this,ProfileActivity.class);
            startActivity(i);
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent i = new Intent(PostActivity.this,StoreActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_slideshow) {
            Intent i = new Intent(PostActivity.this,PostActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {
            Intent i = new Intent(PostActivity.this, ActivitySignin.class);
            SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
            prefs.edit().clear().apply();
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void loadPosts(PostService postService) {
        Observable<ArrayList<Post>> getAllPosts = postService.getPosts(token.split("\"")[1]);

        getAllPosts.subscribeOn(Schedulers.io()) // optional if you do not wish to override the default behavior
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(new Observer<ArrayList<Post>>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull ArrayList<Post> posts) {



                post_list = (RecyclerViewEmptySupport)findViewById(R.id.post_list);
                post_list.setEmptyView(findViewById(R.id.list_empty));

                postAdapter = new PostAdapter(PostActivity.this, posts);
                postAdapter.setButtonListner(PostActivity.this);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(PostActivity.this);
                post_list.setLayoutManager(mLayoutManager);
                post_list.setItemAnimator(new DefaultItemAnimator());
                post_list.setAdapter(postAdapter);
            }




            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println("error"+e.getMessage());

            }

            @Override
            public void onComplete() {


                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.editText:
                ShowPopup();
                break;
        }
    }

    private void ShowPopup() {
        TextView txtclose,username;
        final EditText content;
        ImageView imguser;
        Button btnshare;

        myDialog.setContentView(R.layout.addpost_popup);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        imguser = (ImageView)myDialog.findViewById(R.id.imguser);
        content = (MyEditText)myDialog.findViewById(R.id.content);
        Picasso.with(PostActivity.this).load(ServiceConfig.IMG_DIR+userlogged.getProfilepicUrl()).into(imguser);

        username = (TextView)myDialog.findViewById(R.id.username);
        username.setText(userlogged.getUsername());
        btnshare = (Button) myDialog.findViewById(R.id.btnshare);
        btnshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postData(content.getText().toString());
                Toast.makeText(getApplicationContext(),"Published",Toast.LENGTH_SHORT).show();

                PostService postService = retrofit.create(PostService.class);
                loadPosts(postService);
                myDialog.dismiss();


            }
        });
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        //myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }

    private void postData(String content) {

        PostService postService = retrofit.create(PostService.class);

        Observable<ArrayList<Post>> sharepost = postService.addPost(token.split("\"")[1],userid.split("\"")[1],content);

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(PostActivity.this);
        progressDoalog.setMessage("Loading....");


        sharepost.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ArrayList<Post>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        progressDoalog.show();

                    }

                    @Override
                    public void onNext(@NonNull ArrayList<Post> posts) {


                        postAdapter.refreshlist(posts);


                    }


                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        progressDoalog.dismiss();

                    }
                });

    }


    @Override
    public void onBtnCommentClick(int position, String idPost) {
        //Toast.makeText(getApplicationContext(),"clicked position"+position,Toast.LENGTH_SHORT).show();
        Intent i = new Intent(PostActivity.this,CommentActivity.class);
        i.putExtra("idpost", idPost);
        startActivity(i);
    }

    @Override
    public void onBtnMoreClick(View view , final int position, final String idPost, final String content) {
        PopupMenu p = new PopupMenu(PostActivity.this, view);
        p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit_comment:
                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PostActivity.this);
                        LayoutInflater inflater = getLayoutInflater();
                        final View dialogView = inflater.inflate(R.layout.edit_post_custom_dialog, null);
                        dialogBuilder.setView(dialogView);

                        final MyEditText edt = (MyEditText) dialogView.findViewById(R.id.ed_post);
                        edt.setText(content);

                        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Retrofit retrofit = ServiceConfig.getServiceConfig();
                                PostService postService = retrofit.create(PostService.class);

                                updatePost(postService,idPost,edt.getText().toString());
                            }
                        });
                        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog b = dialogBuilder.create();
                        b.show();
                        break;
                    case R.id.delete_comment:
                        Retrofit retrofit = ServiceConfig.getServiceConfig();
                        PostService postService = retrofit.create(PostService.class);
                        deletePost(postService,idPost, position);
                        break;
                }
                return false;
            }
        });
        MenuInflater inflater = p.getMenuInflater();
        inflater.inflate(R.menu.menu_comment, p.getMenu());
        p.show();

    }

    private void updatePost(PostService postService, String idPost,String content) {
        Observable<ArrayList<Post>> update = postService.updatePost(token.split("\"")[1],idPost,content);
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(PostActivity.this);
        progressDoalog.setMessage("Loading....");
        update.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ArrayList<Post>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        progressDoalog.show();

                    }

                    @Override
                    public void onNext(@NonNull ArrayList<Post> posts) {
                        postAdapter.refreshlist(posts);


                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        progressDoalog.dismiss();

                    }
                });


                }

    private void deletePost(PostService postService, String idPost, final int position) {
        Observable<ArrayList<Post>> dltPost = postService.deletePost(token.split("\"")[1],idPost);
        dltPost.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ArrayList<Post>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ArrayList<Post> posts) {
                        postAdapter.deleteItem(position);

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


}
