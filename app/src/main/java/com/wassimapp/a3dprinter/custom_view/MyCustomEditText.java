package com.wassimapp.a3dprinter.custom_view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by kuldeep on 18/01/18.
 */

public class MyCustomEditText extends android.support.v7.widget.AppCompatEditText {

    public MyCustomEditText(Context context, AttributeSet attrs)
    {
        super(context,attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),"Fonts/Roboto-Regular.ttf"));
    }
}
