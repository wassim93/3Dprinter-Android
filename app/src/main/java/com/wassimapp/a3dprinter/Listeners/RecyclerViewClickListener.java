package com.wassimapp.a3dprinter.Listeners;

import android.view.View;

/**
 * Created by wassim on 09/03/2018.
 */

public interface RecyclerViewClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}

