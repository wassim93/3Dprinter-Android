package com.wassimapp.a3dprinter.Listeners;

import android.view.View;

/**
 * Created by wassim on 03/03/2018.
 */

public interface BtnCommentListener {
    void onBtnCommentClick(int position,String idPost);
    void onBtnMoreClick(View view ,int position, String idPost,String content);


}
