package com.fammeo.app.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.fammeo.app.R;
import com.fammeo.app.app.App;
import com.fammeo.app.common.ActivityBase;
import com.fammeo.app.fragment.ProjectFragment;


public class ProjectFilterActivity extends ActivityBase {

    Toolbar mToolbar;

    ProjectFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projectfilter);

        mToolbar = (Toolbar) findViewById(R.id.projectfiltertoolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        if (savedInstanceState != null) {

            fragment =(ProjectFragment) getSupportFragmentManager().getFragment(savedInstanceState, "currentFragment");

        } else {

            fragment =  ProjectFragment.newInstance();
        }
        Bundle intentBundle = getIntent().getExtras();
        if(intentBundle != null)
        {
            Long ACId = intentBundle.getLong("acid");
           // Log.w(TAG, "onCreate: "+ACId  );
            if(App.getInstance().haveACId(ACId)) {
                if(App.getInstance().getCurrentACId() != ACId) {
                    App.getInstance().setCurrentACId(ACId);
                    App.getInstance().BroadcastACIdChanged(this);
                    //Log.w(TAG, "onCreate1: "+ACId  );
                }
            }
            long[] CRIds = intentBundle.getLongArray("CRIds");
            if(CRIds != null)
            {
                fragment.SetFilterCRId(CRIds);
            }
            String NFId = intentBundle.getString("nfid");
            // Log.w(TAG, "onCreate: "+ACId  );
            if(NFId != null && NFId.length() > 0) {
                Log.w(TAG, "onCreate: NFId"+NFId );
                fragment.SetFilterNFId(NFId);
            }
            long[] ProjectIds = intentBundle.getLongArray("ProjectIds");
            if(ProjectIds != null)
            {
                Log.w(TAG, "onCreate: ProjectIds"+ProjectIds.length );
                fragment.SetFilterProjectId(ProjectIds);
            }
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.companyprofile_content, fragment).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        getSupportFragmentManager().putFragment(outState, "currentFragment", fragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.\

        switch (item.getItemId()) {

            case android.R.id.home: {

                finish();
                return true;
            }

            default: {

                return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Log.w(TAG, "onBackPressed: " );
    }
}
