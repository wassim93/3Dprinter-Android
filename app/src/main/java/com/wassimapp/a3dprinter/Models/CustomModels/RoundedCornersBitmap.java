package com.wassimapp.a3dprinter.Models.CustomModels;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by kuldeep on 26/01/18.
 */

public class RoundedCornersBitmap implements com.squareup.picasso.Transformation {

    private static final float DEFAULT_RADIUS = 50.f;
    private static final int DEFAULT_BORDER_COLOR = Color.WHITE;
    private static final int DEFAULT_STROKE_WIDTH = 0;

    protected float mCornerRadius;
    protected int mBorderColor;
    protected int mStrokeWidth;

    @Override
    public String key() {
        return "roundedCorners()";
    }

    public RoundedCornersBitmap() {
        mCornerRadius = DEFAULT_RADIUS;
        mBorderColor = DEFAULT_BORDER_COLOR;
        mStrokeWidth = DEFAULT_STROKE_WIDTH;
    }

    public RoundedCornersBitmap(float cornderRadius, int borderColor, int strokeWidth) {
        mCornerRadius = cornderRadius;
        mBorderColor = borderColor;
        mStrokeWidth = strokeWidth;
    }

    @Override
    public Bitmap transform(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(mStrokeWidth, mStrokeWidth, (bitmap.getWidth() - mStrokeWidth), bitmap.getHeight()
                - mStrokeWidth);
        final RectF rectF = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        paint.setColor(mBorderColor);
        paint.setStrokeWidth(0);

        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, mCornerRadius, mCornerRadius, paint);
        //canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        bitmap.recycle();
        return output;
    }

    /**
     * @return the mCornerRadius
     */
    public float getCornerRadius() {
        return mCornerRadius;
    }

    /**
     * @param mCornerRadius the mCornerRadius to set
     */
    public void setCornerRadius(float mCornerRadius) {
        this.mCornerRadius = mCornerRadius;
    }

    /**
     * @return the mBorderColor
     */
    public int getBorderColor() {
        return mBorderColor;
    }

    /**
     * @param mBorderColor the mBorderColor to set
     */
    public void setBorderColor(int mBorderColor) {
        this.mBorderColor = mBorderColor;
    }

    /**
     * @return the mStrokeWidth
     */
    public int getStrokeWidth() {
        return mStrokeWidth;
    }

    /**
     * @param mStrokeWidth the mStrokeWidth to set
     */
    public void setStrokeWidth(int mStrokeWidth) {
        this.mStrokeWidth = mStrokeWidth;
    }

}
