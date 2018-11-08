package com.wassimapp.a3dprinter.custom_view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by MTAJ-08 on 10/27/2016.
 */
public class MyCustomTextView extends android.support.v7.widget.AppCompatTextView {

    public MyCustomTextView(Context context, AttributeSet attrs)
    {
        super(context,attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),"Fonts/Roboto-Thin.ttf"));
    }

}
