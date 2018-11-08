package com.wassimapp.a3dprinter;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alexzh.circleimageview.CircleImageView;
import com.dmitrybrant.modelviewer.MainActivityViewer;
import com.squareup.picasso.Picasso;
import com.wassimapp.a3dprinter.Adapter.StoreAdapter;
import com.wassimapp.a3dprinter.Configuration.ServiceConfig;
import com.wassimapp.a3dprinter.Listeners.BtnClickListener;
import com.wassimapp.a3dprinter.Models.CustomModels.RecyclerViewEmptySupport;
import com.wassimapp.a3dprinter.Models.Model3D;
import com.wassimapp.a3dprinter.Models.User;
import com.wassimapp.a3dprinter.Service.ModelService;
import com.wassimapp.a3dprinter.Service.UserService;
import com.wassimapp.a3dprinter.customfonts.MyEditText;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import it.gmariotti.recyclerview.adapter.SlideInRightAnimatorAdapter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Retrofit;

import static com.wassimapp.a3dprinter.R.id.filename;


public class StoreActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,BtnClickListener, SearchView.OnQueryTextListener {
    private RecyclerViewEmptySupport rv;
    public static final String PREFS = "MyPrefs";

    ArrayList<Model3D> list_model = new ArrayList<>();

    String userid,token;
    CircleImageView profilepic;
    TextView username,email;
    TextView selectedfile;
    File file;

    private StoreAdapter storeAdapter;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Store Models");
        setSupportActionBar(toolbar);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerContainer = navigationView.getHeaderView(0); // This returns the container layout in nav_drawer_header.xml (e.g., your RelativeLayout or LinearLayout)        navigationView.addHeaderView(view);
        navigationView.setNavigationItemSelectedListener(this);


        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        userid = prefs.getString("iduser", null);
        token = prefs.getString("idToken", null);

        profilepic = (CircleImageView)headerContainer.findViewById(R.id.drawer_profilepic);
        username = (TextView) headerContainer.findViewById(R.id.drawer_username);
        email = (TextView) headerContainer.findViewById(R.id.drawer_email);



        Retrofit retrofit = ServiceConfig.getServiceConfig();
        ModelService modelService = retrofit.create(ModelService.class);
        loadModelData(modelService);


        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override

            public void onRefresh() {

                // Your code to refresh the list here.

                // Make sure you call swipeContainer.setRefreshing(false)

                // once the network request has completed successfully.

                fetchTimelineAsync(0);

            }

        });

        // Configure the refreshing colors

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,

                android.R.color.holo_green_light,

                android.R.color.holo_orange_light,

                android.R.color.holo_red_light);

    }

    public void fetchTimelineAsync(int page) {

        // Send the network request to fetch the updated data

        // `client` here is an instance of Android Async HTTP

        // getHomeTimeline is an example endpoint.



        storeAdapter.clear();
        Retrofit retrofit = ServiceConfig.getServiceConfig();
        ModelService modelService = retrofit.create(ModelService.class);
        loadModelData(modelService);

            }




            public void onFailure(Throwable e) {

                Log.d("DEBUG", "Fetch timeline error: " + e.toString());

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
                Picasso.with(StoreActivity.this).load(ServiceConfig.IMG_DIR+user.getProfilepicUrl()).into(profilepic);

            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.history, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_upload) {

            ShowUploadFilePopup();
            return true;
        }
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void ShowUploadFilePopup() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(StoreActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.uploadfile_popup, null);
        dialogBuilder.setView(dialogView);

        final MyEditText edt = (MyEditText) dialogView.findViewById(R.id.objname);
        final Spinner sp = (Spinner)dialogView.findViewById(R.id.category);
        selectedfile = (TextView)dialogView.findViewById(filename);

        TextView btnupload = (TextView)dialogView.findViewById(R.id.uploadfile);
        btnupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkPermissions();
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("file/*");
                    startActivityForResult(intent, 200);

                }

            }
        });
        //edt.setText(content);

        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Retrofit retrofit = ServiceConfig.getServiceConfig();
                ModelService modelService = retrofit.create(ModelService.class);

                uploadFile(modelService,edt.getText().toString(),sp.getSelectedItem().toString());

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void uploadFile(ModelService modelService, String objname, String category) {
        RequestBody mFile = RequestBody.create(MediaType.parse("file/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), mFile);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());
        RequestBody obj = RequestBody.create(MediaType.parse("text/plain"), objname);
        RequestBody cat = RequestBody.create(MediaType.parse("text/plain"), category);
        RequestBody vis = RequestBody.create(MediaType.parse("text/plain"), "shared");


        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(StoreActivity.this);
        progressDoalog.setMessage("Loading....");


        Observable<ArrayList<Model3D>> fileUpload = modelService.uploadModelFile(token.split("\"")[1],fileToUpload, filename,userid.split("\"")[1],obj,cat,vis);
        fileUpload.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ArrayList<Model3D>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        progressDoalog.show();

                    }

                    @Override
                    public void onNext(@NonNull ArrayList<Model3D> model3Ds) {
                        list_model = model3Ds;

                        storeAdapter.refreshList(model3Ds);

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();

            if (getMimeType(uri).endsWith(".stl")){
            String filePath = getRealPathFromURIPath(uri, StoreActivity.this);
            file  = new File(filePath);
            selectedfile.setText(file.getName());
            }else{
                Toast.makeText(getApplicationContext(),"Format not supported \n Please Select an STL file",Toast.LENGTH_SHORT).show();
            }

        }

    }

    private String getMimeType(Uri uri) {
        String mimeType = null;
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            ContentResolver cr = getApplicationContext().getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }



    private void checkPermissions(){

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    },
                    1052);

        }

    }
    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent i = new Intent(StoreActivity.this,ProfileActivity.class);
            startActivity(i);
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent i = new Intent(StoreActivity.this,StoreActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_slideshow) {
            Intent i = new Intent(StoreActivity.this,PostActivity.class);
            startActivity(i);

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

            Intent i = new Intent(StoreActivity.this, ActivitySignin.class);
            SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
            prefs.edit().clear().apply();
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void loadModelData(ModelService modelService) {
       // Observable<ArrayList<Model3D>> getModels = modelService.getModels(token.split("\"")[1]);

        Observable<ArrayList<Model3D>> getModels = modelService.getModelById(token.split("\"")[1],userid.split("\"")[1]);

       /* final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(ActivitySignup.this);
        progressDoalog.setMessage("Loading....");*/

        getModels.subscribeOn(Schedulers.io()) // optional if you do not wish to override the default behavior
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(new Observer<ArrayList<Model3D>>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull ArrayList<Model3D> model3Ds) {


                list_model = model3Ds;



                rv = (RecyclerViewEmptySupport) findViewById(R.id.rv);
                rv.setEmptyView(findViewById(R.id.list_empty));


                storeAdapter = new StoreAdapter(StoreActivity.this, model3Ds);
                swipeContainer.setRefreshing(false);

                storeAdapter.setButtonListner(StoreActivity.this);

                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(StoreActivity.this);

                SlideInRightAnimatorAdapter slideInRightAnimatorAdapter = new SlideInRightAnimatorAdapter(storeAdapter, rv);

                rv.setLayoutManager(mLayoutManager);
                rv.setAdapter(slideInRightAnimatorAdapter);
                }




            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println("error"+e.getMessage());

            }

            @Override
            public void onComplete() {

            }
        });


    }


    public void getModelList(ModelService modelService){

        Observable<ArrayList<Model3D>> getModels = modelService.getModelById(token.split("\"")[1],userid.split("\"")[1]);

       /* final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(ActivitySignup.this);
        progressDoalog.setMessage("Loading....");*/

        getModels.subscribeOn(Schedulers.io()) // optional if you do not wish to override the default behavior
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(new Observer<ArrayList<Model3D>>() {

            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull ArrayList<Model3D> model3Ds) {

                list_model = model3Ds;

            }




            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println("error"+e.getMessage());

            }

            @Override
            public void onComplete() {

            }
        });

    }


    @Override
    public void onBtnClick(int position, String value) {
        storeAdapter.deleteItem(position);
        Retrofit retrofit = ServiceConfig.getServiceConfig();
        ModelService modelService = retrofit.create(ModelService.class);
        Observable<ArrayList<Model3D>> dltModel = modelService.deleteModel(token.split("\"")[1],value);
        dltModel.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ArrayList<Model3D>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ArrayList<Model3D> models) {

                        list_model = models;

                        storeAdapter.refreshList(models);



                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onImgClick(int position, Model3D model3D) {

        Intent i = new Intent(StoreActivity.this, MainActivityViewer.class);
        i.putExtra("obj", model3D);
        startActivity(i);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {

        //List<Model3D> allmodels = storeAdapter.getList();


        Retrofit retrofit = ServiceConfig.getServiceConfig();
        ModelService modelService = retrofit.create(ModelService.class);
        getModelList(modelService);

            query = query.toLowerCase();

            final ArrayList<Model3D> filteredModelList = new ArrayList<>();
            for (Model3D model : list_model) {
                final String text = model.getCategory().toLowerCase();
                final String name = model.getImageName().toLowerCase();
                if (text.contains(query) || name.contains(query)) {
                    filteredModelList.add(model);
                }
            }
            storeAdapter.animateTo(filteredModelList);
            rv.scrollToPosition(0);

            return true;




    }
}
