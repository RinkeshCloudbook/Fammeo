package com.fammeo.app.activity.EditActivity;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.fammeo.app.FragmentDrawer;
import com.fammeo.app.R;
import com.fammeo.app.activity.SettingsActivity;
import com.fammeo.app.app.App;
import com.fammeo.app.fragment.CompanyFragment;
import com.fammeo.app.fragment.FammeoFragment.ConversationsFragment;
import com.fammeo.app.fragment.FinishedProjectFragment;
import com.fammeo.app.fragment.HomeFragment;
import com.fammeo.app.fragment.NoCompanyFragment;
import com.fammeo.app.fragment.NotificationFragment;
import com.fammeo.app.fragment.ProjectFragment;
import com.fammeo.app.fragment.VewProfileFragment;
import com.fammeo.app.model.DummyModel;

public class SecondMainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private static String TAG = SecondMainActivity.class.getSimpleName();
    Toolbar mToolbar;

    private FragmentDrawer drawerFragment;
    // used to store app title
    private CharSequence mTitle;

    Fragment fragment;
    Boolean action = false;
    int page = 0;

    private Boolean restore = false;
    Context mContext;
    protected DrawerLayout mDrawerLayout;
    private Bundle intentBundle ;
    private String Mode ;
    String doc = "01";

    String navTitles[];
    SecondMainActivity _this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mContext = getApplicationContext();
        _this = this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //if(App.getInstance().getCurrentACId() > 0){
            navTitles = getResources().getStringArray(R.array.navDrawerItems);
            Log.e("TEST","Get Array :"+navTitles.length);
       // }
       // else
           // navTitles = getResources().getStringArray(R.array.NoCompanynavDrawerItems);
        if (savedInstanceState != null ) {
            //Restore the fragment's instance
            fragment = getSupportFragmentManager().getFragment(savedInstanceState, "currentFragment");

            restore = savedInstanceState.getBoolean("restore");
            mTitle = savedInstanceState.getString("mTitle");
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container_body, fragment).commit();
            }
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle(mTitle);


        } else {
            intentBundle =   getIntent().getExtras();
            Mode = "";
            if (intentBundle != null) {
                Mode = intentBundle.getString("mode");
                Log.w(TAG, "onCreate Mode:-"+Mode );
                if(Mode.equals("notification"))
                {
                    displayView(1);
                    restore = true;
                }
                else if (intentBundle.getBoolean("reload") == true) {
                    if (intentBundle.getString("Fragment").equals("Home")) {
                        displayView(1);
                        restore = true;
                    }
                }
            }
            if (!restore) {
                restore = true;
                mTitle = getString(R.string.app_name);
                displayView(-1);
            }
        }
        drawerFragment = (FragmentDrawer) getFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListenerSecond(this);

        if (!restore) {

            displayView(1);
        }
        drawerFragment.ChangeContext();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int permissionReadStorage = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            int permissionWrireStorage = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permissionReadStorage != PackageManager.PERMISSION_GRANTED || permissionWrireStorage != PackageManager.PERMISSION_GRANTED ) {
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1001) {
            boolean granted = true;
            for (int i=0; i<grantResults.length; i++)
            {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED)
                    granted = false;
            }

            if (!granted)
            {
                Toast.makeText(mContext, "Storage permission required to use this application.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("restore", true);
        outState.putBoolean("restore", true);
        outState.putString("mTitle", getSupportActionBar().getTitle().toString());
        getSupportFragmentManager().putFragment(outState, "currentFragment", fragment);
    }

    private ActionBarDrawerToggle mDrawerToggle;


    public void onDrawerItemSelected(View view, int position) {
        if (position > 0)
            displayView(position);
    }

    private void displayView(int RPosition) {

        action = false;
        String title = "";
        ClearCurrentFragment();
        int position = RPosition ;
        if(position < 0) position = 1;

        if (navTitles != null && navTitles.length >= position) {
            title = navTitles[position - 1];
        }

        if (page != position) {
            Log.e("TEST", "Display View");
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle(mTitle);
            getSupportActionBar().show();
            drawerFragment = (FragmentDrawer) getFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
            drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
            drawerFragment.setDrawerListenerSecond(this);
        }

        Log.e("TEST","Get Title :"+title);
            switch (title) {
                case "": {

                    break;
                }
                case "View Profile": {

                    page = 1;
                    fragment = new VewProfileFragment();
                    doc = "01";
                    Log.e("TEST","Title :"+title);
                    getSupportActionBar().setTitle(title);
                    /*if(intentBundle != null)
                        ((NotificationFragment)fragment).SetFilterBundle(intentBundle.getBundle("bundle"));*/
                    action = true;

                    break;
                }
                case "Conversations": {

                    page = 2;
                    fragment = new ConversationsFragment();
                    doc = "02";
                    getSupportActionBar().setTitle("Conversations");
                    action = true;

                    break;
                }
                case "Connections": {

                    page = 3;
                    fragment = new ProjectFragment();
                    doc = "03";
                    getSupportActionBar().setTitle(title);
                    action = true;

                    break;
                }
                case "Followers": {

                    page = 4;
                    fragment = new FinishedProjectFragment();
                    doc = "04";
                    getSupportActionBar().setTitle(title);
                    action = true;

                    break;
                }
                case "Following": {

                    page = 5;
                    fragment = new CompanyFragment();
                    doc = "05";
                    getSupportActionBar().setTitle(title);
                    action = true;

                    break;
                }
                case "Education":{
                    page = 6;
                    /*fragment = new DocumentUpload();
                    doc = "06";
                    //mToolbar.setTitle(title);
                    mToolbar.inflateMenu(R.menu.menu_document_upload);
                    //setSupportActionBar(mToolbar);
                    getSupportActionBar().setTitle(title);
                    action = true;*/

                    break;
                }
                case "Experience":{

                    page = 7;
                    /*fragment = new PaymentFragment();
                    doc = "07";
                    getSupportActionBar().setTitle("Paying to "+ App.getInstance().getACName(App.getInstance().getCurrentACId()));

                    action = true;*/

                    break;
                }

                case "Tweets":{

                    page = 7;
                    /*fragment = new PaymentFragment();
                    doc = "07";
                    getSupportActionBar().setTitle("Paying to "+ App.getInstance().getACName(App.getInstance().getCurrentACId()));

                    action = true;*/

                    break;
                }
                default: {
                    page = 8;
                    Intent i = new Intent(SecondMainActivity.this, SettingsActivity.class);
                    startActivity(i);
                }
            }

            if (action) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container_body, fragment)
                        .commit();
            }


        drawerFragment.ChangeContext();
    }

    private void ClearCurrentFragment() {
        if(fragment != null)
        {
            Log.w(TAG, "ClearCurrentFragment: " +fragment.getClass().getName());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        //inflater.inflate(R.menu.menu_document_upload, menu);

        if (page == DummyModel.DRAWER_ITEM_TAG_SearchContact) {
         /*   MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.searchmenu, menu);

            getSupportActionBar().setTitle("Contacts");
            */
            return true; // false
        } else
            return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home: {
                return true;
            }
           /* case R.id.menu_search: {
                if (page == DummyModel.DRAWER_ITEM_TAG_SearchContact) {
                    Log.i(TAG, "DRAWER_ITEM_TAG_SearchContact");
                    if (mSearchView == null)
                        mSearchView = ((ContactSearchFragment) fragment).GetSearchView();
                    switch (item.getItemId()) {
                        case R.id.menu_search:
                            mSearchView.open(true, item);
                            return true;
            *//* case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START); finish();
                return true;*//*
                        default:
                            return super.onOptionsItemSelected(item);
                    }
                }
            }*/
            default: {

                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerFragment.isDrawerOpen()) {
            drawerFragment.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }
}
