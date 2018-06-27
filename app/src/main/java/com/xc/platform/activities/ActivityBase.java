package com.xc.platform.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;


import com.xc.platform.R;



@Deprecated
public abstract class ActivityBase extends AppCompatActivity {
    private static final String TAG = "ActivityBase";
    protected static final java.lang.String DISABLES_ANIMATION = "disablesAnimation";
    Toolbar toolbar;
    private boolean disablesAnimation = false;

    boolean hasIntentExtras() {
        return getIntent() != null && getIntent().getExtras() != null;
    }

    private static final int[] PERMISSION_GRANTED = new int[]{PackageManager.PERMISSION_GRANTED};
    private static final String[] PERMISSIONS_PLACEHOLDER = new String[]{""};

    void runActionWithLocationPermissionCheck(int action) {
        runActionWithPermissionCheck(action, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void runActionWithPermissionCheck(int action, String permission) {
        Log.v(TAG, "runActionWithPermissionCheck() " + permission);
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            onRequestPermissionsResult(action, PERMISSIONS_PLACEHOLDER, PERMISSION_GRANTED);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{permission}, action);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            App.getEventBus().postSticky(new Events.PermissionGranted(permissions[0]));

            onRunActionWithPermissionCheck(requestCode, true);
        } else {
            onRunActionWithPermissionCheck(requestCode, false);

        }
    }

    void onRunActionWithPermissionCheck(int action, boolean granted) {

    }
    public void hideNavigationBar() {
        int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;

        if (android.os.Build.VERSION.SDK_INT >= 19) {
            uiFlags |= 0x00001000;
        } else {
            uiFlags |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }

        getWindow().getDecorView().setSystemUiVisibility(uiFlags);
    }

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        if(getIntent() != null && getIntent().getExtras() !=  null)
            disablesAnimation = getIntent().getExtras().getBoolean(DISABLES_ANIMATION);
        hideNavigationBar();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideNavigationBar();
        }
    }

    public static void launchActivityFromDrawer(@NonNull ActivityBase source, @NonNull Class<ActivityBase> target) {
        Intent i = new Intent(source, target);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );

        source.startActivity(i);

    }
    
    public View getToolbar() {
        return toolbar; 
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onStart() {

        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(disablesAnimation)
            overridePendingTransition(0, 0);
        else
            overridePendingTransition(R.anim.push_up_in, R.anim.none);

    }

    void setSupportToolbar() {
        setSupportToolbar(false);
    }

    void setSupportToolbar(boolean hideTitle) {
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(!hideTitle);
    }

}
