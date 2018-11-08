package com.wassimapp.a3dprinter.custom_view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by kuldeep on 17/01/18.
 */

public class MyCustomTextView4 extends android.support.v7.widget.AppCompatTextView {

    public MyCustomTextView4(Context context, AttributeSet attrs)
    {
        super(context,attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),"Fonts/Roboto-Bold.ttf"));
    }
}
