package com.wassimapp.a3dprinter.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wassimapp.a3dprinter.Configuration.ServiceConfig;
import com.wassimapp.a3dprinter.Listeners.BtnClickListener;
import com.wassimapp.a3dprinter.Models.CustomModels.LeftRoundedCornersBitmap;
import com.wassimapp.a3dprinter.Models.Model3D;
import com.wassimapp.a3dprinter.R;
import com.wassimapp.a3dprinter.custom_view.MyCustomTextView2;

import java.util.ArrayList;

/**
 * Created by wassim on 18/02/2018.
 */

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Model3D> model3DArrayList;
    private String[] text_review = {"(215)", "(415)", "(105)", "(232)"};
    BtnClickListener btnClickListener;


    public StoreAdapter(Context context, ArrayList<Model3D> model3DArrayList) {
        this.context = context;
        this.model3DArrayList = model3DArrayList;

    }

    @Override
    public StoreAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.menu_layout, parent, false);

        return new ViewHolder(itemView);
    }





    public void setButtonListner(BtnClickListener listener) {
        this.btnClickListener = listener;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.text_name.setText(model3DArrayList.get(position).getImageName());
         holder.text_menu1.setText(model3DArrayList.get(position).getCategory());
        //holder.text_menu3.setText(model3DArrayList.get(position).getText_menu3());
        // holder.text_review.setText(model3DArrayList.get(position).getText_review());
        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                holder.ratingBar.setEnabled(false);
            }
        });

        holder.indicate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnClickListener != null) {
                    btnClickListener.onBtnClick(position,model3DArrayList.get(position).getFileName());
                }
            }
        });
        Picasso.with(context).load(ServiceConfig.IMG_DIR+model3DArrayList.get(position).getName()).transform(new LeftRoundedCornersBitmap()).into(holder.image);

        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btnClickListener != null) {
                    btnClickListener.onImgClick(position,model3DArrayList.get(position));
                }

            }
        });


    }

    public void animateTo(ArrayList<Model3D> models) {
        applyAndAnimateRemovals(models);
        applyAndAnimateAdditions(models);
        applyAndAnimateMovedItems(models);
    }

    private Model3D removeItem(int position) {
        final Model3D model = this.model3DArrayList.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    private void addItem(int position, Model3D model) {
        this.model3DArrayList.add(position, model);
        notifyItemInserted(position);
    }

    private void moveItem(int fromPosition, int toPosition) {
        final Model3D model = this.model3DArrayList.remove(fromPosition);
        this.model3DArrayList.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    private void applyAndAnimateRemovals(ArrayList<Model3D> newModels) {
        for (int i = this.model3DArrayList.size() - 1; i >= 0; i--) {
            final Model3D model = this.model3DArrayList.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(ArrayList<Model3D> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Model3D model = newModels.get(i);
            if (!this.model3DArrayList.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(ArrayList<Model3D> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Model3D model = newModels.get(toPosition);
            final int fromPosition = this.model3DArrayList.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    @Override
    public int getItemCount()   {return model3DArrayList.size();}

    public void deleteItem(int position){
        this.model3DArrayList.remove(position);
        notifyDataSetChanged();


    }

    public void refreshList(ArrayList<Model3D> model3Ds) {
        this.model3DArrayList = model3Ds;
        notifyDataSetChanged();
    }

    public void clear() {

        this.model3DArrayList.clear();

        notifyDataSetChanged();

    }

    public void addAll(ArrayList<Model3D> list) {

        this.model3DArrayList.addAll(list);

        notifyDataSetChanged();

    }

    public ArrayList<Model3D> getList(){
        return this.model3DArrayList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView text_name,text_location,text_menu1,text_menu2,text_menu3,text_review;
        ImageView image,image_location,indicate;
        RatingBar ratingBar;

        public ViewHolder(View view) {
            super(view);

            text_name = (MyCustomTextView2) view.findViewById(R.id.text_name);
            text_menu1 = (MyCustomTextView2) view.findViewById(R.id.text_menu1);
            //text_menu2 = (MyCustomTextView2) view.findViewById(R.id.text_menu2);
            //text_menu3 = (MyCustomTextView2) view.findViewById(R.id.text_menu3);
            ratingBar = (RatingBar) view.findViewById(R.id.myRatingBar);
            text_review = (MyCustomTextView2) view.findViewById(R.id.text_review);
            image = (ImageView) view.findViewById(R.id.image);
            indicate = (ImageView) view.findViewById(R.id.delete);

        }
    }
}
