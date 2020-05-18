package com.fammeo.app.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.ContentFrameLayout;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle ;
import androidx.appcompat.widget.Toolbar ;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.fammeo.app.FragmentDrawer;
import com.fammeo.app.R;
import com.fammeo.app.adapter.fammeoAdapter.SearchListAdapter;
import com.fammeo.app.app.App;
import com.fammeo.app.common.ActivityBase;
import com.fammeo.app.common.DataGlobal;
import com.fammeo.app.common.DataText;
import com.fammeo.app.common.PassDataInterface;
import com.fammeo.app.fragment.CompanyFragment;
import com.fammeo.app.fragment.ContactFragment;
import com.fammeo.app.fragment.Fammeo_Search;
import com.fammeo.app.fragment.FragmentCreate;
import com.fammeo.app.fragment.HomeFragment;
import com.fammeo.app.fragment.NoCompanyFragment;
import com.fammeo.app.fragment.NotificationFragment;
import com.fammeo.app.fragment.ProjectFragment;
import com.fammeo.app.fragment.Search;
import com.fammeo.app.model.DummyModel;
import com.fammeo.app.model.SearchUserModel;
import com.fammeo.app.util.CustomAuthRequest;
import com.fammeo.app.util.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class MainActivity extends ActivityBase implements  FragmentDrawer.FragmentDrawerListener {

    private static String TAG = MainActivity.class.getSimpleName();
    Toolbar mToolbar;
    EditText edt_name;
    public CustomAuthRequest request;
    RecyclerView recycler_view;
    ArrayList<SearchUserModel> searchlist = new ArrayList<SearchUserModel>();
    private FragmentDrawer drawerFragment;
    private ViewPager viewPager;
    // used to store app title
    private CharSequence mTitle;
    private static final int MAX_STEP = 4;
    private MyViewPagerAdapter myViewPagerAdapter;

    private Bundle bundleUserData = null;

    Fragment fragment;
    Boolean action = false;
    int page = 0;

    private Boolean restore = false;
    Context mContext;
    protected DrawerLayout mDrawerLayout;
    private Bundle intentBundle ;
    private String Mode, getEmail, getFN , getLN , getUrl;
    private Button btnNext;

    String navTitles[];
    MainActivity _this;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((CardView) findViewById(R.id.card_search)).setBackgroundResource(R.drawable.search_bg);
        edt_name = findViewById(R.id.edt_name);
        recycler_view = findViewById(R.id.recycler_view);
        viewPager = findViewById(R.id.view_pager);
        btnNext = findViewById(R.id.btn_nextt);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        RecyclerView.LayoutManager recyce = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
        recycler_view.setLayoutManager(recyce);

        bottomProgressDots(0);

        myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myViewPagerAdapter);
       // viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        viewPager.setClipToPadding(false);
        viewPager.setPadding(0, 0, 0, 0);
       // viewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.viewpager_margin_overlap));
        viewPager.setOffscreenPageLimit(myViewPagerAdapter.getCount());
        viewPager.beginFakeDrag();

        /*viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("TEST","position :"+position);
                if (viewPager.getCurrentItem() == about_title_array.length - 1) {
                    btnNext.setText("Get Started");
                } else {
                    btnNext.setText("Next");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });*/

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = viewPager.getCurrentItem() + 1;
                 page += 1;
                Log.e("TEST","current :"+current);
//                if (current < MAX_STEP) {
//                    // move to next screen
//                    if(current == 1){
//
//                    }
                    viewPager.setCurrentItem(page, true);
//                } else {
//                    finish();
//                }
            }
        });

        //showKeyboard(MainActivity.this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mContext = getApplicationContext();
        _this = this;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        Log.e("TEST","Get Current Acid :"+App.getInstance().getCurrentACId());

            navTitles = getResources().getStringArray(R.array.navDrawerItems);

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
            intentBundle = getIntent().getExtras();
            Mode = "";
            if (intentBundle != null) {
                Mode = intentBundle.getString("mode");
                Log.w(TAG, "onCreate Mode:-"+Mode );
               /* if(Mode.equals("notification"))
                {
                    displayView(1);
                    restore = true;
                }
                else if (intentBundle.getBoolean("reload") == true) {
                    if (intentBundle.getString("Fragment").equals("Home")) {
                        displayView(1);
                        restore = true;
                    }
                }*/
            }
            if (!restore) {
                restore = true;
                mTitle = getString(R.string.app_name);
                displayView(-1);
            }
        }


        Intent intent = getIntent();
        getEmail = intent.getStringExtra("E");
        getFN = intent.getStringExtra("FN");
        getLN = intent.getStringExtra("LN");
        getUrl = intent.getStringExtra("U");


       /* String fullName = App.getInstance().getFirstName()+" "+App.getInstance().getLastName();
        ((TextView) findViewById(R.id.txt_name)).setText(fullName);
        ((TextView) findViewById(R.id.txt_email)).setText(App.getInstance().setEmailName());

        String firstLater = App.getInstance().getFullname().substring(0,1).toUpperCase();

        ((ImageView) findViewById(R.id.search_image)).setImageResource(R.drawable.bg_search_circle);
        ((ImageView) findViewById(R.id.search_image)).setColorFilter(null);
        ((TextView) findViewById(R.id.search_image_text)).setText(firstLater);*/

        drawerFragment = (FragmentDrawer) getFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        if (!restore) {

            displayView(1);
        }
        drawerFragment.ChangeContext();

    }

    private void bottomProgressDots(int current_index) {
        LinearLayout dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        ImageView[] dots = new ImageView[MAX_STEP];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle);
            dots[i].setColorFilter(getResources().getColor(R.color.grey_20), PorterDuff.Mode.SRC_IN);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current_index].setImageResource(R.drawable.shape_circle);
            dots[current_index].setColorFilter(getResources().getColor(R.color.light_green_600), PorterDuff.Mode.SRC_IN);
        }
    }

    //  viewpager change listener

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("restore", true);
        outState.putString("mTitle", getSupportActionBar().getTitle().toString());
      //  getSupportFragmentManager().putFragment(outState, "currentFragment", fragment);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        if (position > 0)
            displayView(position);
    }

    public  void ClearCurrentFragment()
    {
        if(fragment != null)
        {
            Log.w(TAG, "ClearCurrentFragment: " +fragment.getClass().getName());
        }
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
                setSupportActionBar(mToolbar);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setTitle(mTitle);
                getSupportActionBar().show();
                drawerFragment = (FragmentDrawer) getFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
                drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
                drawerFragment.setDrawerListener(this);
           /* if (position == 1) {
                getSupportActionBar().hide();
            }*/
            }


            Log.e("TEST","Title :"+title);
            switch (title) {

                case "": {

                    break;
                }




            }

            /*if(title.equals("Settings"))
            {
                Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(i);

            }*/

        drawerFragment.ChangeContext();
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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


    private class MyViewPagerAdapter extends FragmentPagerAdapter implements PassDataInterface{
        public Fragment[] tfragments;
        String fullName,  FN,  LN, email, imgUrl;
        public MyViewPagerAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Search search= new Search();
                    search.setListener(this);
//                    tfragments[position] = search;
                    return search;
                case 1:
                    FragmentCreate createFragment= new FragmentCreate();
                    Log.e("TEST","Get Bundle");
                    Bundle bundle=new Bundle();
                    bundle.putString("fullName",fullName);
                    bundle.putString("FN",FN);
                    bundle.putString("LN",LN);
                    bundle.putString("email",email);
                    bundle.putString("imgUrl",imgUrl);
                    createFragment.setArguments(bundle);
//                    tfragments[position] = createFragment;
                    return createFragment;
                case 2:
                    Search searchFrag= new Search();
//                    tfragments[position] = searchFrag;
                    return searchFrag;
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public void userData(String fullName, String FN, String LN, String email, String imgUrl) {
            this.fullName=fullName;
            this.FN=FN;
            this.LN=LN;
            this.email=email;
            this.imgUrl=imgUrl;

            bundleUserData = new Bundle();
            bundleUserData.putString("fullName", fullName);
            bundleUserData.putString("FN", FN);
            bundleUserData.putString("LN", LN);
            bundleUserData.putString("email", email);
            bundleUserData.putString("imgUrl", imgUrl);


            Log.e("TEST","Event :"+fullName);
            Log.e("TEST","Event :"+FN);
            Log.e("TEST","Event :"+LN);

            int current = viewPager.getCurrentItem() + 1;
            page += 1;
            viewPager.setCurrentItem(page, true);
            Fragment pageFragm = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.view_pager + ":" + viewPager.getCurrentItem());
            // based on the current position you can then cast the page to the correct
            // class and call the method:
            //if (viewPager.getCurrentItem() == 1 && pageFragm != null) {
                ((FragmentCreate)pageFragm).userData(bundleUserData);
            //}
            Log.e("TEST","current :"+current);
//                if (current < MAX_STEP) {
//                    // move to next screen
//                    if(current == 1){
//
//                    }

        }
    }

}
