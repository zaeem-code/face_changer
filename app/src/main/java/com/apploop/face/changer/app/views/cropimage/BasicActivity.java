package com.apploop.face.changer.app.views.cropimage;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.apploop.face.changer.app.R;


public class BasicActivity extends AppCompatActivity {
  private static final String TAG = BasicActivity.class.getSimpleName();

  Uri imagePath;

  public static Intent createIntent(Activity activity) {
    return new Intent(activity, BasicActivity.class);
  }

  // Lifecycle Method ////////////////////////////////////////////////////////////////////////////

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_basic);
    Bundle extras = getIntent().getExtras();
    if (extras != null){
      imagePath = extras.getParcelable("image");
    }

    if(savedInstanceState == null){
      FragmentManager fragmentManager = getSupportFragmentManager();
      FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
      BasicFragment footerFragment = new BasicFragment();
      Bundle bundle = new Bundle();
      bundle.putString("path", String.valueOf(imagePath));
      footerFragment.setArguments(bundle);
      fragmentTransaction.replace(R.id.container, footerFragment);
      fragmentTransaction.commit();
//      getSupportFragmentManager().beginTransaction().add(R.id.container, BasicFragment.newInstance(), imagePath).commit();
    }

  }

  @Override public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
  }

  @Override public boolean onSupportNavigateUp() {
    onBackPressed();
    return super.onSupportNavigateUp();
  }

//  private void initToolbar() {
//    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//    setSupportActionBar(toolbar);
//    ActionBar actionBar = getSupportActionBar();
//    actionBar.setDisplayHomeAsUpEnabled(true);
//    actionBar.setHomeButtonEnabled(true);
//  }

  public void startResultActivity(Uri uri) {
    if (isFinishing()) return;
    // Start ResultActivity
    startActivity(ResultActivity.createIntent(this, uri));
  }
}
