package com.apploop.face.changer.app.helpers.stickerviewclass;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;


public class StickerImageView extends StickerView {

    private String owner_id;
    private ImageView iv_main;
    private Bitmap originalBitmap;

    public StickerImageView(Context context, OnTouchSticker onTouchSticker) {
        super(context, onTouchSticker);
    }

    public StickerImageView(Context context, AttributeSet attrs, OnTouchSticker onTouchSticker) {
        super(context, attrs, onTouchSticker);
    }

    public StickerImageView(Context context, AttributeSet attrs, int defStyle, OnTouchSticker onTouchSticker) {
        super(context, attrs, defStyle, onTouchSticker);
    }

    public void setOwnerId(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getOwnerId() {
        return this.owner_id;
    }

    @Override
    public View getMainView() {
        if (this.iv_main == null) {
            this.iv_main = new ImageView(getContext());
            this.iv_main.setScaleType(ImageView.ScaleType.FIT_XY);

        }
        return iv_main;
    }

    public void setImageBitmap(Bitmap bmp) {
        this.iv_main.setImageBitmap(bmp);
        this.originalBitmap = bmp;
    }

    public void applyColorFilter(String str) {
        Bitmap copy = originalBitmap.copy(originalBitmap.getConfig(), true);
        Paint paint = new Paint();
        paint.setColorFilter(new LightingColorFilter(Color.parseColor(str), 1));
        new Canvas(copy).drawBitmap(copy, 0.0f, 0.0f, paint);
        iv_main.setImageBitmap(copy);


    }

    public void setImageResource(int res_id) {
        this.iv_main.setImageResource(res_id);
    }

    public void setImageDrawable(Drawable drawable) {
        this.iv_main.setImageDrawable(drawable);
    }

    public Bitmap getImageBitmap() {
        return ((BitmapDrawable) this.iv_main.getDrawable()).getBitmap();
    }
}
