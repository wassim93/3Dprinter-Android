package com.wassimapp.a3dprinter.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.curioustechizen.ago.RelativeTimeTextView;
import com.squareup.picasso.Picasso;
import com.wassimapp.a3dprinter.Configuration.ServiceConfig;
import com.wassimapp.a3dprinter.Listeners.BtnCommentListener;
import com.wassimapp.a3dprinter.Models.Post;
import com.wassimapp.a3dprinter.R;
import com.wassimapp.a3dprinter.customfonts.MyTextView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by wassim on 24/02/2018.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Post> postArrayList;
    BtnCommentListener btnCommentListener;
    String userid;

    public static final String PREFS = "MyPrefs";





    public PostAdapter(Context context, ArrayList<Post> postArrayList) {
        this.context = context;
        this.postArrayList = postArrayList;
    }

    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_item, parent, false);

        return new ViewHolder(itemView);
    }

    public void setButtonListner(BtnCommentListener listener) {
        this.btnCommentListener = listener;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, MODE_PRIVATE);
        userid = prefs.getString("iduser", null);

         holder.name.setText(postArrayList.get(position).getUser().getUsername());
        String givenDateString = postArrayList.get(position).getDate();
        long millisSinceEpoch = new DateTime( givenDateString ).getMillis();
        holder.timestamp.setReferenceTime(millisSinceEpoch);

        holder.btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnCommentListener != null) {
                    btnCommentListener.onBtnCommentClick(position, postArrayList.get(position).getId());
                }
            }
        });

         holder.txtStatusMsg.setText(postArrayList.get(position).getContent());
            Picasso.with(context).load(ServiceConfig.IMG_DIR + postArrayList.get(position).getUser().getProfilepicUrl()).into(holder.profilePic);


        if(Objects.equals(postArrayList.get(position).getUser().getId(), userid.split("\"")[1])){
            holder.btnmore.setVisibility(View.VISIBLE);
            holder.btnmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (btnCommentListener != null) {
                        btnCommentListener.onBtnMoreClick(v,position, postArrayList.get(position).getId(),postArrayList.get(position).getContent());
                    }
                }
            });
        }

    }




    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    public void refreshlist(ArrayList<Post> posts) {

        this.postArrayList = posts;
        notifyDataSetChanged();
    }

    public void deleteItem(int position){
        this.postArrayList.remove(position);
        notifyDataSetChanged();


    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        MyTextView name, txtStatusMsg,btn_comment;
        RelativeTimeTextView timestamp;
        ImageView profilePic,btnmore;


        public ViewHolder(View view) {
            super(view);

            name = (MyTextView) view.findViewById(R.id.name);
            timestamp = (RelativeTimeTextView) view.findViewById(R.id.timestamp);
            txtStatusMsg = (MyTextView) view.findViewById(R.id.txtStatusMsg);
            profilePic = (ImageView)view.findViewById(R.id.profilePic);
            btn_comment = (MyTextView)view.findViewById(R.id.comment_btn);
            btnmore = (ImageView)view.findViewById(R.id.btnmore);

        }
    }
}
