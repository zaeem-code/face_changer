package com.apploop.face.changer.app.views.cropimage;

import static com.apploop.face.changer.app.utils.UtilsCons.originalBitmap;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.apploop.face.changer.app.R;
import com.apploop.face.changer.app.manager.AnalyticsManager;
import com.apploop.face.changer.app.utils.UtilsCons;
import com.apploop.face.changer.app.views.FaceChangeScreen.FaceChangeActivity;
import com.apploop.face.changer.app.views.MenPhotoScreen.MenPhotoActivity;
import com.apploop.face.changer.app.views.removeBackground.ImageRemoveBgActivity;
import com.apploop.face.changer.app.views.removeBackground.StoreManager;
import com.isseiaoki.simplecropview.CropImageView;
import com.isseiaoki.simplecropview.callback.CropCallback;
import com.isseiaoki.simplecropview.callback.LoadCallback;
import com.isseiaoki.simplecropview.callback.SaveCallback;
import com.isseiaoki.simplecropview.util.Logger;
import com.isseiaoki.simplecropview.util.Utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BasicFragment extends Fragment {
    private static final String TAG = BasicFragment.class.getSimpleName();

    private static final int REQUEST_PICK_IMAGE = 10011;
    private static final int REQUEST_SAF_PICK_IMAGE = 10012;
    private static final String PROGRESS_DIALOG = "ProgressDialog";
    private static final String KEY_FRAME_RECT = "FrameRect";
    private static final String KEY_SOURCE_URI = "SourceUri";

    // Views ///////////////////////////////////////////////////////////////////////////////////////
    private CropImageView mCropView;
    private Bitmap.CompressFormat mCompressFormat = Bitmap.CompressFormat.JPEG;
    private RectF mFrameRect = null;
    private Uri mSourceUri = null;
    FrameLayout frameLayout;

    // Note: only the system can call this constructor by reflection.
    public BasicFragment() {
    }

    public static BasicFragment newInstance() {
        BasicFragment fragment = new BasicFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_basic, null, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // bind Views
        bindViews(view);
        frameLayout = view.findViewById(R.id.framlayout);

//        mCropView.setDebug(true);

        if (getArguments() != null) {
            mSourceUri = Uri.parse(getArguments().getString("path"));
        }

//        AdsService.getInstance().showNativeAd(frameLayout, R.layout.admob_native_custom, AdsService.NativeAdType.NATIVE_AD_TYPE_MEDIUM);
//        AdsManager.getInstance().loadNativeAd(frameLayout, getLayoutInflater(), R.layout.ad_app_install);

        if (savedInstanceState != null) {
            // restore data
            mFrameRect = savedInstanceState.getParcelable(KEY_FRAME_RECT);
            mSourceUri = savedInstanceState.getParcelable(KEY_SOURCE_URI);
        }

        if (mSourceUri == null) {
//      // default data
//      mSourceUri = getUriFromDrawableResId(getContext(), R.drawable.bb);
            Log.e("aoki", "mSourceUri = " + mSourceUri);
        }
        // load image
        mCropView.load(mSourceUri)
                .initialFrameRect(mFrameRect)
                .useThumbnail(true)
                .execute(mLoadCallback);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save data
        outState.putParcelable(KEY_FRAME_RECT, mCropView.getActualCropRect());
        outState.putParcelable(KEY_SOURCE_URI, mCropView.getSourceUri());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        if (resultCode == Activity.RESULT_OK) {
            // reset frame rect
            mFrameRect = null;
            switch (requestCode) {
                case REQUEST_PICK_IMAGE:
                    mSourceUri = result.getData();
                    mCropView.load(mSourceUri)
                            .initialFrameRect(mFrameRect)
                            .useThumbnail(true)
                            .execute(mLoadCallback);
                    break;
                case REQUEST_SAF_PICK_IMAGE:
                    mSourceUri = Utils.ensureUriPermission(getContext(), result);
                    mCropView.load(mSourceUri)
                            .initialFrameRect(mFrameRect)
                            .useThumbnail(true)
                            .execute(mLoadCallback);
                    break;
            }
        }
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
////    BasicFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
//    }


    private void bindViews(View view) {
        mCropView = (CropImageView) view.findViewById(R.id.cropImageView);
        view.findViewById(R.id.buttonDone).setOnClickListener(btnListener);
        view.findViewById(R.id.buttonPickImage).setOnClickListener(btnListener);
        view.findViewById(R.id.buttonRotateLeft).setOnClickListener(btnListener);
        view.findViewById(R.id.buttonRotateRight).setOnClickListener(btnListener);
    }

    public void cropImage() {
        mCropView.crop(mSourceUri).execute(mCropCallback);
    }

    public void dismissProgress() {
        if (!isResumed()) return;
        FragmentManager manager = getFragmentManager();
        if (manager == null) return;
    }

    public Uri createSaveUri() {
        return createNewUri(getContext(), mCompressFormat);
    }

    public static String getDirPath() {
        String dirPath = "";
        File imageDir = null;
        File extStorageDir = Environment.getExternalStorageDirectory();
        if (extStorageDir.canWrite()) {
            imageDir = new File(extStorageDir.getPath() + "/simplecropview");
        }
        if (imageDir != null) {
            if (!imageDir.exists()) {
                imageDir.mkdirs();
            }
            if (imageDir.canWrite()) {
                dirPath = imageDir.getPath();
            }
        }
        return dirPath;
    }

    public static Uri getUriFromDrawableResId(Context context, int drawableResId) {
        StringBuilder builder = new StringBuilder().append(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .append("://")
                .append(context.getResources().getResourcePackageName(drawableResId))
                .append("/")
                .append(context.getResources().getResourceTypeName(drawableResId))
                .append("/")
                .append(context.getResources().getResourceEntryName(drawableResId));
        return Uri.parse(builder.toString());
    }

    public static Uri createNewUri(Context context, Bitmap.CompressFormat format) {
        long currentTimeMillis = System.currentTimeMillis();
        Date today = new Date(currentTimeMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String title = dateFormat.format(today);
        String dirPath = getDirPath();
        String fileName = "scv" + title + "." + getMimeType(format);
        String path = dirPath + "/" + fileName;
        File file = new File(path);
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/" + getMimeType(format));
        values.put(MediaStore.Images.Media.DATA, path);
        long time = currentTimeMillis / 1000;
        values.put(MediaStore.MediaColumns.DATE_ADDED, time);
        values.put(MediaStore.MediaColumns.DATE_MODIFIED, time);
        if (file.exists()) {
            values.put(MediaStore.Images.Media.SIZE, file.length());
        }

        ContentResolver resolver = context.getContentResolver();
        Uri uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Logger.i("SaveUri = " + uri);
        return uri;
    }

    public static String getMimeType(Bitmap.CompressFormat format) {
        Logger.i("getMimeType CompressFormat = " + format);
        switch (format) {
            case JPEG:
                return "jpeg";
            case PNG:
                return "png";
        }
        return "png";
    }

    public static Uri createTempUri(Context context) {
        return Uri.fromFile(new File(context.getCacheDir(), "cropped"));
    }

    // Handle button event /////////////////////////////////////////////////////////////////////////

    private final View.OnClickListener btnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonDone:
                    AnalyticsManager.getInstance().sendAnalytics("BasicFragment", "Done Clicked");
//                        Constants.BITMAP_TO_PROCESS = mCropView.getCroppedBitmap();

                    UtilsCons.originalBitmap = mCropView.getCroppedBitmap();

                    if (UtilsCons.originalBitmap != null) {
                        if (UtilsCons.chooseLayout.contains("PHOTO_REMOVE_BG")) {
                            call();
                        } else if (UtilsCons.chooseLayout.contains("PHOTO_MEN")) {
                            Intent intent = new Intent(requireActivity(), MenPhotoActivity.class);
                            startActivity(intent);
                            requireActivity().finish();
                        } else {
                            Intent intent = new Intent(requireActivity(), FaceChangeActivity.class);
                            startActivity(intent);
                            requireActivity().finish();
                        }
                    }

//                        if (MyConstants.DoingPhotoEditing) {
//                            Bitmap bitmap = null;
//                            try {
//                                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), MyConstants.resultedUri);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//
//                            Intent intent = new Intent(getContext(), EditImageActivity.class);
//                            createImageFromBitmap(bitmap);
//                            startActivity(intent);
//                            getActivity().finish();
//                        } else {
//                            MyConstants.resultedUri = null;
//                            startActivity(new Intent(getContext(), RemoveObjActivity.class));
//                        }
                    break;
                case R.id.buttonRotateLeft:
                    AnalyticsManager.getInstance().sendAnalytics("BasicFragment", "Left Clicked");
                    mCropView.rotateImage(CropImageView.RotateDegrees.ROTATE_M90D);
                    break;
                case R.id.buttonRotateRight:
                    AnalyticsManager.getInstance().sendAnalytics("BasicFragment", "Right Clicked");
                    mCropView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
                    break;
                case R.id.buttonPickImage:
                    AnalyticsManager.getInstance().sendAnalytics("BasicFragment", "Back Clicked");
                    getActivity().onBackPressed();
                    break;
            }
        }
    };

    public void call() {
        try {
            StoreManager.setCurrentCropedBitmap(requireActivity(), (Bitmap) null);
            StoreManager.setCurrentCroppedMaskBitmap(requireActivity(), (Bitmap) null);
            ImageRemoveBgActivity.setFaceBitmap(originalBitmap);
            StoreManager.setCurrentOriginalBitmap(requireActivity(), originalBitmap);
            startActivity(new Intent(requireActivity(), ImageRemoveBgActivity.class));
            requireActivity().finish();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public String createImageFromBitmap(Bitmap bitmap) {
        String fileName = "myImage";//no .png or .jpg needed
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            FileOutputStream fo = getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
            fo.write(bytes.toByteArray());
            // remember close file output
            fo.close();
        } catch (Exception e) {
            e.printStackTrace();
            fileName = null;
        }
        return fileName;
    }

    // Callbacks ///////////////////////////////////////////////////////////////////////////////////

    private final LoadCallback mLoadCallback = new LoadCallback() {
        @Override
        public void onSuccess() {
        }

        @Override
        public void onError(Throwable e) {
        }
    };

    private final CropCallback mCropCallback = new CropCallback() {
        @Override
        public void onSuccess(Bitmap cropped) {
            mCropView.save(cropped)
                    .compressFormat(mCompressFormat)
                    .execute(createSaveUri(), mSaveCallback);
        }

        @Override
        public void onError(Throwable e) {
        }
    };

    private final SaveCallback mSaveCallback = new SaveCallback() {
        @Override
        public void onSuccess(Uri outputUri) {
            dismissProgress();
            ((BasicActivity) getActivity()).startResultActivity(outputUri);
        }

        @Override
        public void onError(Throwable e) {
            dismissProgress();
        }
    };
}