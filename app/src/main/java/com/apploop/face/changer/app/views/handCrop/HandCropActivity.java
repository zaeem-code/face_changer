package com.apploop.face.changer.app.views.handCrop;

import static com.apploop.face.changer.app.utils.UtilsCons.originalBitmap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

import com.apploop.face.changer.app.R;
import com.apploop.face.changer.app.databinding.ActivityHandCropBinding;
import com.apploop.face.changer.app.utils.UtilsCons;
import com.apploop.face.changer.app.views.faceChangeScreen.FaceChangeActivity;
import com.apploop.face.changer.app.views.MenPhotoScreen.MenPhotoActivity;
import com.apploop.face.changer.app.views.RemoveBgScreen.RemoveBgActivity;
import com.apploop.face.changer.app.views.handCrop.freecrop.FreeCropView;
import com.apploop.face.changer.app.views.removeBackground.ImageRemoveBgActivity;
import com.apploop.face.changer.app.views.removeBackground.StoreManager;

import java.io.ByteArrayOutputStream;

public class HandCropActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityHandCropBinding binding;
    int imageRotateCrop = 0;
    private int disHeight;
    private int disWidth;
    private Bitmap freeCrop;
    private ProgressDialog progressDialogCrop;
    private Bitmap createBitmap;
    int widthCrop = 0;
    int heightCrop = 0;
    private boolean isBackPressed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hand_crop);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(getResources().getColor(R.color.background, this.getTheme()));
        } else {
            getWindow().setStatusBarColor(getResources().getColor(R.color.background));
        }
        init();


    }

    private void init() {
//        Objects.requireNonNull(AdsManager.Companion.getInstance()).showNativeAd(binding.frameLayout, binding.frameLayout, getLayoutInflater(), R.layout.ad_media);
        try {
            freeCrop = originalBitmap;
        } catch (OutOfMemoryError e) {
            e.getLocalizedMessage();
        }
        binding.lvRotate.setOnClickListener(this);
        binding.done.setOnClickListener(this);
        binding.lvReset.setOnClickListener(this);
        binding.ivBack.setOnClickListener(this);
        binding.closeView.setOnClickListener(this);

        if (freeCrop != null) {
            widthCrop = freeCrop.getWidth();
            heightCrop = freeCrop.getHeight();
        }
        DisplayMetrics bundleCrop = ((Activity) this).getResources().getDisplayMetrics();

        try {
            disWidth = bundleCrop.widthPixels;
            disHeight = bundleCrop.heightPixels;
            float bundle = getResources().getDisplayMetrics().density;
            int i = disWidth - ((int) bundle);
            int i2 = disHeight - ((int) (bundle * 60.0f));
            if (widthCrop < i) {
                if (heightCrop < i2) {
                    while (widthCrop < i - 20 && heightCrop < i2) {
                        widthCrop = (int) (((double) widthCrop) * 1.1d);
                        heightCrop = (int) (((double) heightCrop) * 1.1d);
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("mImageWidth");
                        stringBuilder.append(widthCrop);
                        stringBuilder.append("mImageHeight");
                        stringBuilder.append(heightCrop);
                    }
                    freeCrop = Bitmap.createScaledBitmap(freeCrop, widthCrop, heightCrop, true);
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("mImageWidth");
                    stringBuilder2.append(widthCrop);
                    stringBuilder2.append("mImageHeight");
                    stringBuilder2.append(heightCrop);
                    return;
                }
            }
            while (true) {
                if (widthCrop > i || heightCrop > i2) {
                    widthCrop = (int) (((double) widthCrop) * 0.9d);
                    heightCrop = (int) (((double) heightCrop) * 0.9d);

                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("mImageWidth");
                    stringBuilder.append(widthCrop);
                    stringBuilder.append("mImageHeight");
                    stringBuilder.append(heightCrop);

                } else {
                    freeCrop = Bitmap.createScaledBitmap(freeCrop, widthCrop, heightCrop, true);

                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("mImageWidth");
                    stringBuilder2.append(widthCrop);
                    stringBuilder2.append("mImageHeight");
                    stringBuilder2.append(heightCrop);

                    return;
                }
            }
        } catch (NullPointerException e) {
            e.getLocalizedMessage();
        }
    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.closeView:
                if (FreeCropView.points.size() == 0) {
                    binding.closeView.setVisibility(View.GONE);
                    return;
                }
            case R.id.done:
                if (FreeCropView.points.size() == 0) {

//                    binding.progressBar.setVisibility(View.VISIBLE);
//                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                    originalBitmap = freeCrop;
                    if (UtilsCons.chooseLayout.contains("PHOTO_REMOVE_BG")) {
                        call();
                    } else if (UtilsCons.chooseLayout.contains("PHOTO_MEN")) {
                        Intent intent = new Intent(HandCropActivity.this, MenPhotoActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(HandCropActivity.this, FaceChangeActivity.class);
                        startActivity(intent);
                        finish();
                    }
//                    AdsManager.Companion.getInstance().showInterstitialAd(HandCropActivity.this, new OnAdLoaded() {
//                        @Override
//                        public void OnAdLoadedCallBack(Boolean loaded) {
//                            binding.progressBar.setVisibility(View.GONE);
//                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//
//                            originalBitmap = freeCrop;
//                            if (UtilsCons.chooseLayout.contains("PHOTO_REMOVE_BG")) {
//                                call();
//                            } else if (UtilsCons.chooseLayout.contains("PHOTO_MEN")) {
//                                Intent intent = new Intent(HandCropActivity.this, MenPhotoActivity.class);
//                                startActivity(intent);
//                                finish();
//                            } else {
//                                Intent intent = new Intent(HandCropActivity.this, FaceChangeActivity.class);
//                                startActivity(intent);
//                                finish();
//                            }
//                        }
//                    });
                    return;
                }
                boolean fc = FreeCropView.isCropImage();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("boolean_value");
                stringBuilder.append(view);
                CropImage(fc);
                saveImage();
                break;

            case R.id.lv_reset:
                setLayout();
                break;

            case R.id.lv_rotate:
                imageRotateCrop = 90;
                freeCrop = rotateSet(freeCrop, (float) imageRotateCrop);
                setLayout();
                break;
        }
    }

    public void call() {
        try {
            StoreManager.setCurrentCropedBitmap(this, (Bitmap) null);
            StoreManager.setCurrentCroppedMaskBitmap(this, (Bitmap) null);
            ImageRemoveBgActivity.setFaceBitmap(originalBitmap);
            StoreManager.setCurrentOriginalBitmap(this, originalBitmap);
            startActivity(new Intent(this, ImageRemoveBgActivity.class));
            finish();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void saveImage() {
        progressDialogCrop = ProgressDialog.show(this, "Please Wait", "Image Processing");
        new Handler().postDelayed(new SetImage(), 100);
    }

    class SetImage implements Runnable {
        public void run() {
//            UtilsCons.originalBitmap.recycle();
            originalBitmap = createBitmap;

            if (progressDialogCrop.isShowing() && progressDialogCrop != null)
                progressDialogCrop.dismiss();

            binding.progressBar.setVisibility(View.VISIBLE);
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//
//            AdsManager.Companion.getInstance().showInterstitialAd(HandCropActivity.this, new OnAdLoaded() {
//                @Override
//                public void OnAdLoadedCallBack(Boolean loaded) {
//                    binding.progressBar.setVisibility(View.GONE);
//                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
//
//
//                }
//            });

            if (UtilsCons.chooseLayout.contains("PHOTO_REMOVE_BG")) {
                Intent intent = new Intent(HandCropActivity.this, RemoveBgActivity.class);
                startActivity(intent);
                finish();
            } else if (UtilsCons.chooseLayout.contains("PHOTO_MEN")) {
                Intent intent = new Intent(HandCropActivity.this, MenPhotoActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(HandCropActivity.this, FaceChangeActivity.class);
                startActivity(intent);
                finish();
            }

        }
    }

    private Uri getImageUri(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bytes);
        String path = MediaStore.Images.Media.insertImage(
                getContentResolver(),
                bitmap,
                "Title",
                null
        );
        return Uri.parse(path);
    }


    protected void onResume() {
        super.onResume();
        setLayout();
    }

    private Bitmap getBitmap(View view) {
        Bitmap createBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(createBitmap));
        return createBitmap;
    }

    public static Bitmap rotateSet(Bitmap bitmap, float f) {
        Matrix matrix = new Matrix();
        matrix.postRotate(f);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private void setLayout() {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) binding.cropIt.getLayoutParams();
        layoutParams.height = freeCrop.getHeight();
        layoutParams.width = freeCrop.getWidth();


        binding.cropIt.setLayoutParams(layoutParams);
        FreeCropView freecropview = new FreeCropView(this, freeCrop);
        binding.cropIt.removeAllViews();
        binding.cropIt.addView(freecropview);
    }

    public void CropImage(boolean z) {
        createBitmap = Bitmap.createBitmap(disWidth, disHeight, freeCrop.getConfig());
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        paint.setMaskFilter(new BlurMaskFilter(40.0f, BlurMaskFilter.Blur.NORMAL));
        paint.setAntiAlias(true);
        Path path = new Path();
        int i = 0;
        while (i < FreeCropView.points.size()) {
            float f = (float) ((Point) FreeCropView.points.get(i)).x;

            path.lineTo(f, (float) ((Point) FreeCropView.points.get(i)).y);
            i++;
        }
        canvas.drawPath(path, paint);
        if (z) {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        } else {
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        }
        canvas.drawBitmap(freeCrop, 0.0f, 0.0f, paint);
    }

    @Override
    public void onBackPressed() {
        if (binding.closeView.getVisibility() == View.VISIBLE) {
            binding.closeView.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
            finish();
        }
    }
}
