package com.wassimapp.a3dprinter.Utils;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.wassimapp.a3dprinter.Models.Model3D;

import java.util.List;

/**
 * Created by wassim on 28/02/2018.
 */
public class StoreListDiffCallback extends DiffUtil.Callback{

    List<Model3D> oldModels;
    List<Model3D> newModels;

    public StoreListDiffCallback(List<Model3D> newModelss, List<Model3D> oldModels) {
        this.newModels = newModelss;
        this.oldModels = oldModels;
    }

    @Override
    public int getOldListSize() {
        return oldModels.size();
    }

    @Override
    public int getNewListSize() {
        return newModels.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldModels.get(oldItemPosition).getId() == newModels.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldModels.get(oldItemPosition).equals(newModels.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        //you can return particular field for changed item.

        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
