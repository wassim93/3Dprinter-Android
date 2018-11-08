package com.wassimapp.a3dprinter.Listeners;

import com.wassimapp.a3dprinter.Models.Model3D;

/**
 * Created by wassim on 24/02/2018.
 * interface for handling clicking button in store adapter
 */

public interface BtnClickListener {
      void onBtnClick(int position,String value);
      void onImgClick(int position, Model3D model3D);
}
