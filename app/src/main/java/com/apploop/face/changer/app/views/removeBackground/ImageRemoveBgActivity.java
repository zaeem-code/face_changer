package com.apploop.face.changer.app.views.removeBackground;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;


import com.apploop.face.changer.app.R;
import com.apploop.face.changer.app.manager.AdsManager;
import com.apploop.face.changer.app.manager.OnAdLoaded;
import com.apploop.face.changer.app.utils.ImageUtils;
import com.apploop.face.changer.app.utils.SaveFileUtils;
import com.apploop.face.changer.app.utils.UtilsCons;
import com.apploop.face.changer.app.views.RemoveBgScreen.RemoveBgActivity;
import com.apploop.face.changer.app.views.handCrop.HandCropActivity;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageRemoveBgActivity extends AppCompatActivity {
    public static Bitmap eraserResultBmp;
    private static Bitmap faceBitmap;
    private static Bitmap faceBitmap2;
    boolean isFirstTime = true;
    private Bitmap selectedBitmap, cutBitmap;
    private static int IMAGE_GALLERY_REQUEST = 20;
    Bitmap bitmap;
    public int count = 0;
    public static Bitmap bgCircleBit = null;
    public static int curBgType = 1;
    public int width;
    public int height;
    private Context mContext;
    private RelativeLayout frameLayoutContent;
    private ImageView imageViewBackground;
    private ImageView imageViewBackgroundCover;

    RelativeLayout relative_layout_loading;
    ImageView done, imageViewCloseWings;
    FrameLayout bannerAdView,ads_layout;
    ShimmerFrameLayout shimmerFrameLayout;


    public static void setFaceBitmap(Bitmap bitmap) {
        faceBitmap = bitmap;
    }

    @SuppressLint("MissingInflatedId")
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_remove);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        if (UtilsCons.originalBitmap != null) {
            faceBitmap = UtilsCons.originalBitmap;
        }

        mContext = this;
        selectedBitmap = faceBitmap;

        initUI();

        Init();
//        shimmerFrameLayout.startShimmer();
//        AdsManager.Companion.getInstance().showAdMobBanner(this, bannerAdView, new OnAdLoaded() {
//            @Override
//            public void OnAdLoadedCallBack(Boolean loaded) {
//                if (loaded) {
//                    shimmerFrameLayout.setVisibility(View.INVISIBLE);
//                } else {
//                    shimmerFrameLayout.setVisibility(View.GONE);
//                    bannerAdView.setVisibility(View.GONE);
//                }
//            }
//        });

        new Handler().postDelayed(new Runnable() {
            public void run() {
                imageViewBackground.post(new Runnable() {
                    public void run() {
                        if (isFirstTime && selectedBitmap != null) {
                            isFirstTime = false;
                            initBMPNew();
                        }
                    }
                });
            }
        }, 1000);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int i = displayMetrics.heightPixels;
        this.width = displayMetrics.widthPixels;
        this.height = i - ImageUtils.dpToPx(this, 120.0f);
        curBgType = 1;
        imageViewBackgroundCover.setImageBitmap(ImageUtils.getTiledBitmap(ImageRemoveBgActivity.this, R.drawable.tbg2, width, height));
        bgCircleBit = ImageUtils.getBgCircleBit(ImageRemoveBgActivity.this, R.drawable.tbg2);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                ads_layout.setVisibility(View.VISIBLE);
//                AdsManager.Companion.getInstance().showInterstitialAd(ImageRemoveBgActivity.this, new OnAdLoaded() {
//                    @Override
//                    public void OnAdLoadedCallBack(Boolean loaded) {
//                        ads_layout.setVisibility(View.GONE);
//                        UtilsCons.originalBitmap = cutBitmap;
//                        Intent i = new Intent(ImageRemoveBgActivity.this, RemoveBgActivity.class);
//                        startActivity(i);
//                        finish();
//                    }
//                });

                UtilsCons.originalBitmap = cutBitmap;
                Intent i = new Intent(ImageRemoveBgActivity.this, RemoveBgActivity.class);
                startActivity(i);
                finish();


            }
        });

        imageViewCloseWings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void initBMPNew() {
        if (faceBitmap != null) {
            selectedBitmap = ImageUtils.getBitmapResize(mContext, faceBitmap, imageViewBackground.getWidth(), imageViewBackground.getHeight());
            setStartWings();
        }

    }

    private void initUI() {

        this.imageViewBackgroundCover = findViewById(R.id.imageViewBackgroundCover);
        bannerAdView = findViewById(R.id.bannerAdView);
        shimmerFrameLayout = findViewById(R.id.shimmerFrameLayout);
        ads_layout=findViewById(R.id.ads_layout);

    }

    private void Init() {
        imageViewBackground = findViewById(R.id.imageViewBackground);
        done = findViewById(R.id.imageViewSaveWings);
        imageViewCloseWings = findViewById(R.id.imageViewCloseWings);
        frameLayoutContent = findViewById(R.id.mContentRootView);
        relative_layout_loading = findViewById(R.id.relative_layout_loading);
        imageViewBackground.post(new Runnable() {
            public void run() {
                initBMPNew();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1024) {
            if (eraserResultBmp != null) {
                cutBitmap = eraserResultBmp;
                imageViewBackground.setImageBitmap(cutBitmap);
            }
        }
    }

    public void setStartWings() {
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.crop_progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        final ProgressBar progressBar2 = progressBar;
        new CountDownTimer(5000, 1000) {
            public void onFinish() {

            }

            public void onTick(long j) {
                int unused = count = count + 1;
                if (progressBar2.getProgress() <= 90) {
                    progressBar2.setProgress(count * 5);


                }
            }
        }.start();

        new MLCropAsyncTask(new MLOnCropTaskCompleted() {
            public void onTaskCompleted(Bitmap bitmap, Bitmap bitmap2, int left, int top) {

                try {
                    int[] iArr = {0, 0, selectedBitmap.getWidth(), selectedBitmap.getHeight()};
                    int width = selectedBitmap.getWidth();
                    int height = selectedBitmap.getHeight();
                    int i = width * height;
                    selectedBitmap.getPixels(new int[i], 0, width, 0, 0, width, height);
                    int[] iArr2 = new int[i];
                    Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    createBitmap.setPixels(iArr2, 0, width, 0, 0, width, height);
                    cutBitmap = ImageUtils.getMask(mContext, selectedBitmap, createBitmap, width, height);
                    if (cutBitmap!=null)
                    {
                        Bitmap resizedBitmap = Bitmap.createScaledBitmap(
                                bitmap, cutBitmap.getWidth(), cutBitmap.getHeight(), false);
                        cutBitmap = resizedBitmap;
                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Palette p = Palette.from(cutBitmap).generate();
                            if (p.getDominantSwatch() == null) {
                                Toast.makeText(ImageRemoveBgActivity.this, getString(R.string.txt_not_detect_human), Toast.LENGTH_SHORT).show();
                            }
                            imageViewBackground.setImageBitmap(cutBitmap);
                            done.setVisibility(View.VISIBLE);

                        }
                    });

                }
                catch (Exception e)
                {
                    Toast.makeText(ImageRemoveBgActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    Log.e("exception",e.toString());
                }


            }
        }, this, progressBar).execute(new Void[0]);
    }


    class SaveBitmapWithoutBg extends AsyncTask<Void, String, String> {
        SaveBitmapWithoutBg() {
        }

        public void onPreExecute() {
            mLoading(true);
        }

        public String doInBackground(Void... voids) {
            try {
                BitmapDrawable drawable = (BitmapDrawable) imageViewBackground.getDrawable();
                bitmap = drawable.getBitmap();
                return SaveFileUtils.saveBitmapFileRemove(ImageRemoveBgActivity.this, bitmap, new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date()), null).getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        public void onPostExecute(String string) {
            mLoading(false);
            if (string == null) {
                Toast.makeText(ImageRemoveBgActivity.this.getApplicationContext(), R.string.Something_went_wrong, Toast.LENGTH_LONG).show();
                return;
            }
//            Intent i = new Intent(RemoveActivity.this, ShareActivity.class);
//            i.putExtra("path", string);
//            RemoveActivity.this.startActivity(i);

        }

    }


    public static Bitmap getCacheBitMap(ImageView imageView) {
        try {
            imageView.setDrawingCacheEnabled(true);
            imageView.buildDrawingCache();
            Bitmap createBitmap = Bitmap.createBitmap(imageView.getDrawingCache());
            imageView.destroyDrawingCache();
            imageView.setDrawingCacheEnabled(false);
            return createBitmap;
        } catch (Exception unused) {
            return null;
        }
    }

    public void mLoading(boolean z) {
        if (z) {
            getWindow().setFlags(16, 16);
            this.relative_layout_loading.setVisibility(View.VISIBLE);
            return;
        }
        getWindow().clearFlags(16);
        this.relative_layout_loading.setVisibility(View.GONE);
    }


    enum MODULE {
        COVER,
        BACKGROUND
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
