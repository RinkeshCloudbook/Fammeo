package com.fammeo.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fammeo.app.R;
import com.fammeo.app.app.App;
import com.fammeo.app.common.DataText;
import com.fammeo.app.view.siv.CircularImageView;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class EditProfile extends AppCompatActivity {
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle("Edit Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sp = getSharedPreferences("uId", MODE_PRIVATE);
        String userId = sp.getString("u","");
        String un = sp.getString("un","");
        Log.e("TEST","Get User Id :"+userId);
        Log.e("TEST","Full Name :"+App.getInstance().getFullname());
        Log.e("TEST","FN :"+App.getInstance().getFirstName());
        Log.e("TEST","LN :"+App.getInstance().getLastName());
        Log.e("TEST","User Id :"+App.getInstance().getUserId());
        Log.e("TEST","Email :"+App.getInstance().GetEmailName());
        Log.e("Text","Image Url :"+App.getInstance().GetUrl());
        Log.e("Text","User Name :"+App.getInstance().getUser());
        ((EditText) findViewById(R.id.edt_fName)).setText(App.getInstance().getFirstName());
        ((EditText) findViewById(R.id.edt_lName)).setText(App.getInstance().getLastName());
        ((EditText) findViewById(R.id.edt_email)).setText(App.getInstance().GetEmailName());
        ((EditText) findViewById(R.id.edt_uname)).setText(un);

        if(App.getInstance().GetUrl() != null){
            Glide.with(getApplicationContext()).load(DataText.GetImagePath(App.getInstance().GetUrl()))
                    .thumbnail(0.5f)
                    .transition(withCrossFade())
                    .apply(RequestOptions.circleCropTransform())
                    .into(((CircularImageView) findViewById(R.id.search_image)));
        }else {
            String firstLater = App.getInstance().getFirstName().substring(0,1).toUpperCase();
            Log.e("TEST","Get Image Url NULL:");
            ((CircularImageView) findViewById(R.id.search_image)).setImageResource(R.drawable.bg_search_circle);
            ((CircularImageView) findViewById(R.id.search_image)).setColorFilter(null);
            ((TextView) findViewById(R.id.search_image_text)).setText(firstLater);
        }


        /*if(imgUrl != null){


        }else {

        }*/

        ((EditText) findViewById(R.id.edt_gender)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showGender(v);
            }
        });
        ((EditText) findViewById(R.id.edt_contury_code)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCountryCode(v);
            }
        });

        ((EditText) findViewById(R.id.edt_phone_type)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMobileType(v);
            }
        });
        ((EditText) findViewById(R.id.edt_locationType)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLocationType(v);
            }
        });
    }

    private void showLocationType(final View v) {
        final String[] location_array = new String[]{
                "Home", "Work", "Others"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Type");
        builder.setSingleChoiceItems(location_array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(location_array[i]);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void showMobileType(final View v) {
        final String[] code_array = new String[]{
                "Mobile", "Office", "Home", "Main", "Work Fax", "Home Fax", "Others"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Type");
        builder.setSingleChoiceItems(code_array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(code_array[i]);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void showCountryCode(final View v) {
        final String[] code_array = new String[]{
                "91 (India)", "98 (Iran)", "353 (Ireland)", "39 (Italy)"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Country Code");
        builder.setSingleChoiceItems(code_array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(code_array[i]);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
    private void showGender(final View v) {
        final String[] gender_array = new String[]{
                "Male", "Female", "Other"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setTitle("Select");
        builder.setSingleChoiceItems(gender_array, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((EditText) v).setText(gender_array[i]);
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
