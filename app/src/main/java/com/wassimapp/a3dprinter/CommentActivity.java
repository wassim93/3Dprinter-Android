package com.wassimapp.a3dprinter;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.wassimapp.a3dprinter.Adapter.CommentAdapter;
import com.wassimapp.a3dprinter.Configuration.ServiceConfig;
import com.wassimapp.a3dprinter.Listeners.RecyclerViewClickListener;
import com.wassimapp.a3dprinter.Models.Comment;
import com.wassimapp.a3dprinter.Models.CustomModels.RecyclerViewEmptySupport;
import com.wassimapp.a3dprinter.Service.CommentService;
import com.wassimapp.a3dprinter.ServiceImplem.RecyclerViewTouchListener;
import com.wassimapp.a3dprinter.customfonts.MyEditText;

import java.util.ArrayList;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import it.gmariotti.recyclerview.adapter.SlideInRightAnimatorAdapter;
import retrofit2.Retrofit;

public class CommentActivity extends AppCompatActivity  {
    String token;
    String userid;
    public static final String PREFS = "MyPrefs";
    private CommentAdapter commentAdapter;
    RecyclerViewEmptySupport commentList ;
    String idpost;
    ArrayList<Comment> list;

    MyEditText ed_comment;
    ImageView add_comment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Intent i = getIntent();
        idpost = (String) i.getSerializableExtra("idpost");

        //get userid from shared pref
        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        userid = prefs.getString("iduser", null);
        token = prefs.getString("idToken", null);

        ed_comment = (MyEditText)findViewById(R.id.ed_comment);
        ed_comment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                if(s.length() != 0){
                    add_comment.setVisibility(View.VISIBLE);

                }else{
                    add_comment.setVisibility(View.INVISIBLE);

                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        add_comment = (ImageView)findViewById(R.id.add_comment);
        add_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = ServiceConfig.getServiceConfig();
                CommentService commentService = retrofit.create(CommentService.class);
                AddComment(commentService,ed_comment.getText().toString());
                ed_comment.setText("");
            }
        });

        commentList = (RecyclerViewEmptySupport)findViewById(R.id.comment_list);
        commentList.setEmptyView(findViewById(R.id.list_empty));

        commentList.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(), commentList, new RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {

                // if comment user != user connected
                if(!Objects.equals(list.get(position).getUser().getId(), userid.split("\"")[1])){
                    //nothing to show
                }else {

                    PopupMenu p = new PopupMenu(CommentActivity.this, view);
                    p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.edit_comment:
                                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CommentActivity.this);
                                    LayoutInflater inflater = getLayoutInflater();
                                    final View dialogView = inflater.inflate(R.layout.edit_post_custom_dialog, null);
                                    dialogBuilder.setView(dialogView);

                                    final MyEditText edt = (MyEditText) dialogView.findViewById(R.id.ed_post);
                                    edt.setText(list.get(position).getContent());

                                    dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            Retrofit retrofit = ServiceConfig.getServiceConfig();
                                            CommentService commentService = retrofit.create(CommentService.class);
                                            updateComment(commentService,list.get(position).getId(),edt.getText().toString());
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
                                    CommentService commentService = retrofit.create(CommentService.class);
                                    deleteComment(commentService, list.get(position).getId(), idpost, position);
                                    break;
                            }
                            return false;
                        }
                    });
                    MenuInflater inflater = p.getMenuInflater();
                    inflater.inflate(R.menu.menu_comment, p.getMenu());
                    p.show();
                }

            }
        }));



        Retrofit retrofit = ServiceConfig.getServiceConfig();
        CommentService commentService = retrofit.create(CommentService.class);
        LoadCommentList(commentService,idpost);


    }

    private void updateComment(CommentService commentService, String idcom, String content) {
        Observable<ArrayList<Comment>> update = commentService.updateCom(token.split("\"")[1], idcom, content);
        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(CommentActivity.this);
        progressDoalog.setMessage("Loading....");

        update.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ArrayList<Comment>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        progressDoalog.show();

                    }

                    @Override
                    public void onNext(@NonNull ArrayList<Comment> comments) {
                        System.out.println("comments"+comments);
                        list = comments;
                        commentAdapter.refreshlist(comments);

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

    private void AddComment(CommentService commentService, String content) {
        Observable<ArrayList<Comment>> addcmt = commentService.addComment(token.split("\"")[1], idpost, userid.split("\"")[1], content);

        final ProgressDialog progressDoalog;
        progressDoalog = new ProgressDialog(CommentActivity.this);
        progressDoalog.setMessage("Loading....");

        addcmt.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ArrayList<Comment>>() {


                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        progressDoalog.show();

                    }

                    @Override
                    public void onNext(@NonNull ArrayList<Comment> comments) {
                        list = comments;
                        commentAdapter.refreshlist(comments);


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

    private void deleteComment(CommentService commentService, String idcomment, String idpost, final int position) {
        Observable<ArrayList<Comment>> delete = commentService.deleteComment(token.split("\"")[1],idcomment,idpost);
        delete.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ArrayList<Comment>>() {


                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ArrayList<Comment> comments) {
                        commentAdapter.deleteItem(position);


                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    private void LoadCommentList(CommentService commentService, String idpost) {

        Observable<ArrayList<Comment>> getCommentsList = commentService.getComments(token.split("\"")[1],idpost);

        getCommentsList.observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<ArrayList<Comment>>() {

                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull ArrayList<Comment> comments) {

                        list =comments;


                        commentAdapter = new CommentAdapter(CommentActivity.this, comments);

                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CommentActivity.this);

                        SlideInRightAnimatorAdapter slideInRightAnimatorAdapter = new SlideInRightAnimatorAdapter(commentAdapter, commentList);
                        commentList.setLayoutManager(mLayoutManager);
                        //rv.setItemAnimator(new DefaultItemAnimator());
                        commentList.setAdapter(slideInRightAnimatorAdapter);



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
