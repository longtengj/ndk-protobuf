package com.xc.platform.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.xc.platform.R;

public class MainActivity extends AppCompatActivity{
    private static final java.lang.String BUNDLE_KEY_SETUP = "s";
    private static final java.lang.String BUNDLE_KEY_PERMISSIONS = "pe";

    private Toolbar toolbar;
    private boolean checkSetup;
    private boolean checkPermission;
    private Context context;

    public static boolean hasFailedChecks(Bundle b) {
        return b.getBoolean(BUNDLE_KEY_SETUP) || b.getBoolean(BUNDLE_KEY_PERMISSIONS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activities_activity_main);
        context = this;


//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(view -> {
//            ServiceProxy.runOrBind(context, () -> {
////                ServiceProxy.getServiceDevice().publishTasksMessage();
//                try {
//                    ServiceProxy.getServiceDevice().requestCmdMixDown();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            });
//
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                    .setAction("Action", null).show();
//        });

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

        Bundle b = getIntent().getExtras();
        if (b == null) {  //first start
//            b = runChecks();
        }
//        if (hasFailedChecks(b)) {
//            Timber.v("one or more earlier failed checks discovered");
//
//            checkSetup = b.getBoolean(BUNDLE_KEY_SETUP);
//            checkPermission = b.getBoolean(BUNDLE_KEY_PERMISSIONS);
////            recoverChecks();
//        } else {
////            startActivityMain();
//        }

//        getSupportFragmentManager().beginTransaction()
//                .replace(R.id.content, new BoardsFragment())
//                .commit();


    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
            super.onBackPressed();
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activities_menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

}
