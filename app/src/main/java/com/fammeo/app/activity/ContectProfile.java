package com.fammeo.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.fammeo.app.R;
import com.fammeo.app.Retrofit.ContectExample;
import com.fammeo.app.Retrofit.FileUploadService;
import com.fammeo.app.Retrofit.ViewProfileBGChangeExample;
import com.fammeo.app.adapter.BottumListAdapter;
import com.fammeo.app.app.App;
import com.fammeo.app.common.DataGlobal;
import com.fammeo.app.fragment.CompanyFragment;
import com.fammeo.app.model.ContactDetails;
import com.fammeo.app.util.CustomAuthRequest;
import com.fammeo.app.util.DataText;
import com.fammeo.app.util.GenericFileProvider;
import com.fammeo.app.util.RealPathUtil;
import com.fammeo.app.util.Utility;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.fammeo.app.constants.Constants.METHOD_CONTACT_PROFILE;
import static com.fammeo.app.constants.Constants.METHOD_UPLOAD_COMPANY_PROFILE;
import static com.fammeo.app.fragment.CompanyProfileFragment.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE;

public class ContectProfile extends AppCompatActivity {
    private static final String TAG = ContectProfile.class.getSimpleName();

    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    private View bottom_sheet;
    private RecyclerView bottumList_rv;
    ProgressBar pbHeaderProgress;
    public CustomAuthRequest request;
    List<ContactDetails> bottumList = new ArrayList<>();
    ContactDetails cd;
    ImageView company_image;
    public String photoFileName = "photo.jpg";
    File photoFile;
    public final String APP_TAG = "MyCustomAppContect";
    ProgressBar pr_imageLoder;
    Retrofit retrofit;
    String contectId;

    public  long[] Filter_CRIds ;
    public boolean IsFiltered()
    {
        if(Filter_CRIds != null && Filter_CRIds.length > 0)
        {
            return  true;
        }
        return  false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contect_profile);

        pr_imageLoder = findViewById(R.id.pr_imageLoder);
        bottom_sheet = findViewById(R.id.bottom_sheet);
        company_image = findViewById(R.id.company_image);
        pbHeaderProgress = findViewById(R.id.pbHeaderProgress);
        pbHeaderProgress.setVisibility(View.GONE);
        ((ImageView) findViewById(R.id.img_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String userId = bundle.getString("D");
            String name = bundle.getString("N");
            getcontactDetail(userId);
        }
        company_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TEST","Click On Contect Image :"+cd.userId);
                contectId  = cd.userId;
                selectImage();
            }
        });
        ((LinearLayout) findViewById(R.id.lin_companyList)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showBottomSheetDialog();
                ((NestedScrollView)findViewById(R.id.nested_content)).setVisibility(View.GONE);
                Bundle bundle = new Bundle();
                bundle.putString("params", cd.userId);
                Fragment fragment = new CompanyFragment();
                fragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_frame, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
                /*Intent intent = new Intent(getApplicationContext(), CompanyFragment.class);
                startActivity(intent);*/
            }
        });
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Gallary",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(ContectProfile.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(ContectProfile.this);
                if (items[item].equals("Take Photo")) {
                    //userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Gallary")) {
                     //userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFileName= Calendar.getInstance().getTimeInMillis()+".jpg";
        photoFile = getPhotoFileUri(photoFileName);
        Uri fileProvider = GenericFileProvider.getUriForFile(ContectProfile.this, "com.easycloudbooks.easycloudbooks.util.GenericFileProvider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(ContectProfile.this.getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

        }
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),111);
    }

    private File getPhotoFileUri(String photoFileName) {
        File mediaStorageDir = new File(ContectProfile.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }
        File file = new File(mediaStorageDir.getPath() + File.separator + photoFileName);
        Log.e("TEST","File NAme :"+file);
        return file;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String  Token = App.getInstance().getAccessToken();
        long ParseACId = App.getInstance().getCurrentACId();
        final String parsedurl = App.getInstance().GetCustonDomain(ParseACId,METHOD_UPLOAD_COMPANY_PROFILE);
        Log.e("TEST","Parse URL :"+parsedurl);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 111){
                String realPath= RealPathUtil.getRealPath(ContectProfile.this,data.getData());
                Log.e("TEST","RealPath :"+realPath);
                uploadImage(parsedurl,realPath,Token);
            }
            else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
                String realPath=photoFile.getAbsolutePath();
                uploadImage(parsedurl,realPath,Token);
            }
        }
    }

    private void uploadImage(final String parsedurl, final String realPath, final String token) {
        pr_imageLoder.setVisibility(View.VISIBLE);
        File N_file = new File(realPath);
        StringBuilder sb3 = new StringBuilder();
        // sb3.append(getPackageName());
        sb3.append(".provider");
        // image_path = String.valueOf(FileProvider.getUriForFile(this, sb3.toString(), new File(file_name)));
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        final OkHttpClient client = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS).writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).addInterceptor(interceptor).build();
        retrofit = new Retrofit.Builder()
                .baseUrl(parsedurl+"/").client(client).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().serializeNulls().create())).build();
//        RequestBody open_time = RequestBody.create(MediaType.parse("text/plain"), "pass any data like id,name ");
        okhttp3.RequestBody requestBodyx = okhttp3.RequestBody.create(okhttp3.MediaType.parse("image/*"), N_file);

        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", new File(realPath).getName(), requestBodyx);
        okhttp3.RequestBody companyimage =  okhttp3.RequestBody.create(okhttp3.MediaType.parse("text/plain"), "contactimage");
        okhttp3.RequestBody filepath =  okhttp3.RequestBody.create(okhttp3.MediaType.parse("text/plain"), "");
        okhttp3.RequestBody coid =  okhttp3.RequestBody.create(okhttp3.MediaType.parse("text/plain"), String.valueOf(cd.userId));

        FileUploadService apiService = retrofit.create(FileUploadService.class);
        Call<ViewProfileBGChangeExample> memberCall = apiService.contectuploadImage(parsedurl,"Bearer "+token,fileToUpload, companyimage,filepath,coid);
        memberCall.enqueue(new Callback<ViewProfileBGChangeExample>() {

            @Override
            public void onResponse(Call<ViewProfileBGChangeExample> call, retrofit2.Response<ViewProfileBGChangeExample> response) {
                if(response.body() != null && response.body().toString().length() > 0){
                    if(response.body().getMessage().equalsIgnoreCase("OK")){
                        pr_imageLoder.setVisibility(View.GONE);
                        Glide.with(ContectProfile.this).load(realPath)
                                .thumbnail(0.5f)
                                .transition(withCrossFade())
                                .apply( new RequestOptions().transform(new RoundedCorners(20)).diskCacheStrategy(DiskCacheStrategy.ALL))
                                .into(company_image);
                    }
                }
            }
            @Override
            public void onFailure(Call<ViewProfileBGChangeExample> call, Throwable t) {
                pr_imageLoder.setVisibility(View.VISIBLE);
                Log.e(">>>", "onFailure: " + t.getMessage());
            }
        });
    }

    private void showBottomSheetDialog() {

            mBehavior = BottomSheetBehavior.from(bottom_sheet);
            if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            final View view = getLayoutInflater().inflate(R.layout.sheet_list, null);
            bottumList_rv = view.findViewById(R.id.bottumList_rv);

            RecyclerView.LayoutManager recyce = new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false);
            bottumList_rv.setLayoutManager(recyce);

            BottumListAdapter adapter = new BottumListAdapter(getApplicationContext(),bottumList);
            bottumList_rv.setAdapter(adapter);

            mBottomSheetDialog = new BottomSheetDialog(ContectProfile.this);
            mBottomSheetDialog.setContentView(view);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }

            mBottomSheetDialog.show();
            mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    mBottomSheetDialog = null;
                }
            });
    }

    public void getcontactDetail(final String id){
        pbHeaderProgress.setVisibility(View.VISIBLE);
        request = new CustomAuthRequest(Request.Method.POST, METHOD_CONTACT_PROFILE, null,0,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //showMessage(getText(R.string.label_empty_list).toString());
                        Log.w(TAG, response.toString() );
                        pbHeaderProgress.setVisibility(View.VISIBLE);
                        Log.e("TEST","Profile Response :"+response.toString());
                        String strResponse = response.toString();
                        if(strResponse != null){
                            try {
                                pbHeaderProgress.setVisibility(View.GONE);
                                cd = new ContactDetails();
                                JSONObject obj = new JSONObject(response.toString());
                                JSONObject sys  = obj.getJSONObject("obj");
                                String name = sys.getString("FN")+" "+sys.getString("LN");
                                cd.FN = sys.getString("FN")+" "+sys.getString("LN");
                                cd.imageUrl = sys.getString("I");
                                String imageURL = sys.getString("I");
                                String uniqueId = sys.getString("UId");
                                cd.userId = sys.getString("Id");

                                ((TextView) findViewById(R.id.txt_uniqueId)).setText(uniqueId);
                                ((TextView) findViewById(R.id.txt_profile_name)).setText(name);

                                String firstLater = sys.getString("FN").substring(0,1).toUpperCase();
                                String secondLater = sys.getString("LN").substring(0,1).toUpperCase();
                                String combinstion = firstLater+secondLater;
                                if(imageURL == "null"){
                                    ((ImageView) findViewById(R.id.company_image)).setImageResource(R.drawable.bg_square);
                                    ((ImageView) findViewById(R.id.company_image)).setColorFilter(null);
                                    ((TextView) findViewById(R.id.company_image_text)).setText(combinstion);
                                    ((TextView) findViewById(R.id.company_image_text)).setVisibility(View.VISIBLE);
                                }else{
                                   // Glide.with(context).load(DataText.GetImagePath(imageURL)).into(holder.imgCardImage);
                                    Glide.with(ContectProfile.this).load(DataText.GetImagePath(imageURL)).into((ImageView) findViewById(R.id.company_image));
                                    ((TextView) findViewById(R.id.company_image_text)).setVisibility(View.GONE);
                                }
                              //  Glide.with(ContectProfile.this).load(DataText.GetImagePath(imageURL)).into((ImageView) findViewById(R.id.image));

                                JSONArray jsonArray = sys.getJSONArray("Es");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject emailobj = jsonArray.getJSONObject(i);
                                    String email = emailobj.getString("E");
                                    ((TextView) findViewById(R.id.txt_email)).setText(email);
                                }

                                JSONArray phoneArray = sys.getJSONArray("PHs");
                                for (int k = 0; k < phoneArray.length(); k++) {
                                    JSONObject phoneobj = phoneArray.getJSONObject(k);
                                    String phone = phoneobj.getString("Ph");
                                    ((TextView) findViewById(R.id.txt_phone)).setText(phone);
                                }
                                JSONArray addsArray = sys.getJSONArray("Adds");
                                for (int m = 0; m < addsArray.length(); m++) {
                                    JSONObject addsobj = addsArray.getJSONObject(m);
                                    String L1 = addsobj.getString("L1");
                                    String T = addsobj.getString("T");
                                    String L2 = addsobj.getString("L2");
                                    String city = addsobj.getString("C");
                                    String landMark = addsobj.getString("A");
                                    String state = addsobj.getString("S");
                                    String country = addsobj.getString("CR");
                                    String zip = addsobj.getString("Zip");
                                    String address = L1+","+L2+","+","+landMark+","+zip+","+city+","+state+","+country;
                                    ((TextView) findViewById(R.id.txt_address)).setText(address);
                                   // ((TextView) findViewById(R.id.txt_postal)).setText(zip);
                                }
                                JSONArray campanyArr = sys.getJSONArray("CRUs");
                                for (int n = 0; n < campanyArr.length(); n++) {
                                    Log.e("TEST","Total Company :"+campanyArr.length());
                                    String count = String.valueOf(campanyArr.length());
                                    ((TextView) findViewById(R.id.txt_count)).setText(count);
                                    JSONObject companyobj = campanyArr.getJSONObject(n);
                                    //cd.userId = companyobj.getString("CRUId");
                                    JSONArray posArr = companyobj.getJSONArray("Ps");
                                    for (int i = 0; i < posArr.length(); i++) {
                                        JSONObject posObj = posArr.getJSONObject(i);
                                        String position = posObj.getString("P");
                                        cd.position = posObj.getString("P");
                                        cd.jdate = posObj.getString("CD");
                                        String posJdate = posObj.getString("CD");
                                        String dt = posJdate.replace("T", " ");
                                        SimpleDateFormat changeDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                                        try {
                                            Date dateFrom = changeDate.parse(dt);
                                            android.text.format.DateFormat df = new android.text.format.DateFormat();
                                            cd.jdate = df.format("MMM dd,yyyy HH:mm", dateFrom).toString();
                                            //((TextView) findViewById(R.id.txt_dob)).setText(df.format("dd MMM yyyy HH:mm", dateFrom).toString());
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    JSONObject jobjcO = companyobj.getJSONObject("CO");
                                    cd.industryType = jobjcO.getString("Ind");
                                    cd.cId = jobjcO.getString("CRId");
                                    cd.cName = jobjcO.getString("Name");

                                    bottumList.add(cd);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("TEST","GET Exception :"+e);
                            }
                        }else {
                            pbHeaderProgress.setVisibility(View.VISIBLE);
                        }


                    }
                }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbHeaderProgress.setVisibility(View.VISIBLE);
                /*SnakebarCustom.danger(mContext, mView, "Unable to fetch Companies: " + error.getMessage(), 5000);
                swipeRefreshLayout.setRefreshing(false);*/
            }
        }) {

            @Override
            protected JSONObject getParams() {
                try {
                    JSONObject params = new JSONObject();
                    params.put("ContactId", id);
                    JSONObject filterExpression = new JSONObject();
                    try {
                        filterExpression.put("Status","All");
                        if(Filter_CRIds != null)
                        {
                            JSONArray CRIds = new JSONArray();
                            for (Long CRId: Filter_CRIds) {
                                CRIds.put(CRId);
                            }
                            if(CRIds.length() > 0)
                            {
                                filterExpression.put("CRIds",CRIds);
                            }
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    params.put("filterExpression", filterExpression);
                    return params;
                } catch (JSONException ex) {
                    DataGlobal.SaveLog(TAG, ex);
                    return null;
                }
            }

            @Override
            protected void onCreateFinished(CustomAuthRequest request) {
                int socketTimeout = 300000;//0 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                request.customRequest.setRetryPolicy(policy);
                App.getInstance().addToRequestQueue(request);
            }
        };

    }
}
