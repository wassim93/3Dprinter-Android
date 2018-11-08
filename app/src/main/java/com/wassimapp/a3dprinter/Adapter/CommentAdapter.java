package com.wassimapp.a3dprinter.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexzh.circleimageview.CircleImageView;
import com.squareup.picasso.Picasso;
import com.wassimapp.a3dprinter.Configuration.ServiceConfig;
import com.wassimapp.a3dprinter.Models.Comment;
import com.wassimapp.a3dprinter.Models.CustomModels.LeftRoundedCornersBitmap;
import com.wassimapp.a3dprinter.R;
import com.wassimapp.a3dprinter.customfonts.MyTextView;

import java.util.ArrayList;

/**
 * Created by wassim on 03/03/2018.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Comment> commentArrayList;



    public CommentAdapter(Context context, ArrayList<Comment> commentArrayList) {
        this.context = context;
        this.commentArrayList = commentArrayList;


    }

    public void deleteItem(int position){
        this.commentArrayList.remove(position);
        notifyDataSetChanged();


    }



    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_item, parent, false);

        return new ViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.name.setText(commentArrayList.get(position).getUser().getUsername());

        holder.txtComment.setText(commentArrayList.get(position).getContent());


        Picasso.with(context).load(ServiceConfig.IMG_DIR + commentArrayList.get(position).getUser().getProfilepicUrl()).transform(new LeftRoundedCornersBitmap()).into(holder.profilePic);


    }




    @Override
    public int getItemCount() {
        return commentArrayList.size();
    }

    public void refreshlist(ArrayList<Comment> comments) {
        this.commentArrayList = comments;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        MyTextView name, txtComment;
        CircleImageView profilePic;


        public ViewHolder(View view) {
            super(view);

            name = (MyTextView) view.findViewById(R.id.name);
            txtComment = (MyTextView) view.findViewById(R.id.txtComment);
            profilePic = (CircleImageView)view.findViewById(R.id.profilePic);

        }
    }
}
