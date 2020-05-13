package com.fammeo.app.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.fammeo.app.Retrofit.FileUploadService;
import com.fammeo.app.Retrofit.UserFileUpload;
import com.fammeo.app.activity.ProjectFilterActivity;
import com.fammeo.app.adapter.Company.CompanyServiceListAdapter;
import com.fammeo.app.app.App;
import com.fammeo.app.common.DataGlobal;
import com.fammeo.app.common.DataText;
import com.fammeo.app.common.SnakebarCustom;
import com.fammeo.app.constants.Constants;
import com.fammeo.app.model.CompanyRelationJ;
import com.fammeo.app.model.CompanyRelationSubServiceJ;
import com.fammeo.app.util.CustomAuthRequest;
import com.fammeo.app.util.GenericFileProvider;
import com.fammeo.app.util.Helper;
import com.fammeo.app.util.RealPathUtil;
import com.fammeo.app.util.Utility;
import com.google.gson.GsonBuilder;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
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
public class CompanyProfileFragment extends BaseExampleFragment implements Constants,
        CompanyServiceListAdapter.AdapterListener {

    private static String TAG = CompanyProfileFragment.class.getSimpleName();
    private Preference logoutPreference, aboutPreference, itemTerms, itemPrivacy;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private int REQUEST_CODE_PERMISSIONS = 101;
    private ProgressDialog pDialog;
    private Activity CurrentActivity;
    private CompanyProfileFragment _this;
    private CompanyServiceListAdapter mSearchResultsAdapter;
    private Context mContext;
    public View mView;
    Retrofit retrofit;
    public final String APP_TAG = "MyCustomApp";
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    public String photoFileName = "photo.jpg";
    File photoFile;

    LinearLayout aboutDialogContent;
    TextView aboutDialogAppName, aboutDialogAppVersion, aboutDialogAppCopyright;

    private Boolean loading = false;
    private  CompanyRelationJ CR;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        initpDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_companyprofile, container, false);
        CurrentActivity = this.getActivity();
        mView = rootView;

        mContext = CurrentActivity.getApplicationContext();
        Bundle intentBundle =   CurrentActivity.getIntent().getExtras();
        if(intentBundle != null)
        {
            CR =  (CompanyRelationJ)intentBundle.getSerializable("CR");
        }
        return rootView;
    }
    TextView company_name,company_image_text,company_sub_detail,company_service_count,company_project_count,mMessage;
    ImageView company_image;
    RecyclerView mSearchResultsList;
    ArrayList<CompanyRelationSubServiceJ> list;
    LinearLayout company_project_container;
    LinearLayoutManager mlinearLayoutManager;
    ProgressBar pr_imageLoder;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        _this = this;
        Context mContext = CurrentActivity;
        super.onViewCreated(view, savedInstanceState);
        //mSearchResultsList = (RecyclerView) view.findViewById(R.id.recycler_view);

        if(allPermissionsGranted()){
            //cameraIntent(); //start camera if permission has been granted by user
        } else{
            ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS);
        }

       /* public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
            switch (requestCode) {
                case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if(userChoosenTask.equals("Take Photo"))
                            cameraIntent();
                        else if(userChoosenTask.equals("Choose from Library"))
                            galleryIntent();
                    } else {
                        //code for deny
                    }
                    break;
            }
        }*/

        company_name = (TextView)view.findViewById(R.id.company_name);
        company_image_text = (TextView)view.findViewById(R.id.company_image_text);
        company_image = (ImageView)view.findViewById(R.id.company_image);
        company_sub_detail = (TextView)view.findViewById(R.id.company_sub_detail);
        company_service_count = (TextView)view.findViewById(R.id.company_service_count);
        company_project_count = (TextView)view.findViewById(R.id.company_project_count);
        mMessage = (TextView) view.findViewById(R.id.no_service_message);
        pr_imageLoder = view.findViewById(R.id.pr_imageLoder);
        company_project_container = (LinearLayout) view.findViewById(R.id.company_project_container);


        company_name.setText(CR.Name);
        company_sub_detail.setText(CR.LS + " / " + CR.Ind);
        company_service_count.setText(String.valueOf(CR.SC));
        if (!TextUtils.isEmpty(CR.I)) {
            Glide.with(mContext).load(DataText.GetImagePath(CR.I))
                    .thumbnail(0.5f)
                    .transition(withCrossFade())
                    .apply( new RequestOptions().transform(new RoundedCorners(20)).diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(company_image);
              company_image.setColorFilter(null);
              company_image_text.setVisibility(View.GONE);
        } else {
              company_image.setImageResource(R.drawable.bg_square);
              company_image.setColorFilter(CR.getColor());
            company_image_text.setVisibility(View.VISIBLE);
            company_image_text.setText(Helper.getShortName(CR.Name));
        }

        company_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        mlinearLayoutManager = new LinearLayoutManager(CurrentActivity);
        mSearchResultsList = (RecyclerView) CurrentActivity.findViewById(R.id.recycler_view_services);

        list = new ArrayList<CompanyRelationSubServiceJ>();
        mSearchResultsAdapter = new CompanyServiceListAdapter(mContext, list, this);
        mSearchResultsList.setAdapter(mSearchResultsAdapter);
        mSearchResultsList.setLayoutManager(mlinearLayoutManager);
        mSearchResultsList.setHasFixedSize(false);
        company_project_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CurrentActivity, ProjectFilterActivity.class);
                i.putExtra("CRIds",new long[]{ CR.CRId});
                CurrentActivity.startActivity(i);
            }
        });

        getCompanyDetail(true,0,"");
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
                String realPath= RealPathUtil.getRealPath(getActivity(),data.getData());
                Log.e("TEST","RealPath :"+realPath);
                uploadImage(parsedurl,realPath,Token);
            }
            else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
                    String realPath=photoFile.getAbsolutePath();
                    uploadImage(parsedurl,realPath,Token);
            }
        }
    }

    private File getPhotoFileUri(String photoFileName) {
        File mediaStorageDir = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }
        File file = new File(mediaStorageDir.getPath() + File.separator + photoFileName);
        Log.e("TEST","File NAme :"+file);
        return file;
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Gallary",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(getActivity());
                if (items[item].equals("Take Photo")) {
                    //userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Gallary")) {
                   // userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),111);
    }

    private void cameraIntent() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFileName= Calendar.getInstance().getTimeInMillis()+".jpg";
        photoFile = getPhotoFileUri(photoFileName);
        Uri fileProvider = GenericFileProvider.getUriForFile(getActivity(), "com.easycloudbooks.easycloudbooks.util.GenericFileProvider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

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
        okhttp3.RequestBody companyimage =  okhttp3.RequestBody.create(okhttp3.MediaType.parse("text/plain"), "companyimage");
        okhttp3.RequestBody filepath =  okhttp3.RequestBody.create(okhttp3.MediaType.parse("text/plain"), "");
        okhttp3.RequestBody crid =  okhttp3.RequestBody.create(okhttp3.MediaType.parse("text/plain"), String.valueOf(CR.CRId));
//        MultipartBody.Part UploadedFolder = MultipartBody.Part.createFormData("mode", "companyimage");
//        MultipartBody.Part UploadedFolder = MultipartBody.Part.createFormData("filepath", "");
//        MultipartBody.Part UploadedFolder = MultipartBody.Part.createFormData("crid", "92343");
        FileUploadService apiService = retrofit.create(FileUploadService.class);
        Call<UserFileUpload> memberCall = apiService.uploadImage(parsedurl,"Bearer "+token,fileToUpload, companyimage,filepath,crid);
        memberCall.enqueue(new Callback<UserFileUpload>() {

            @Override
            public void onResponse(Call<UserFileUpload> call, retrofit2.Response<UserFileUpload> response) {
                if(response.body() != null && response.body().toString().length() > 0){
                    if(response.body().getMessage().equalsIgnoreCase("OK")){
                        pr_imageLoder.setVisibility(View.GONE);
                        Log.e("TEST","Message :"+response.body().getMessage());
                        Log.e("TEST","Message Type :"+response.body().getMessageType());
                       // uploadImage(parsedurl,realPath,token);
                        Log.e("TEST","Capture ");
                        Glide.with(mContext).load(realPath)
                                .thumbnail(0.5f)
                                .transition(withCrossFade())
                                .apply( new RequestOptions().transform(new RoundedCorners(20)).diskCacheStrategy(DiskCacheStrategy.ALL))
                                .into(company_image);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserFileUpload> call, Throwable t) {
                pr_imageLoder.setVisibility(View.VISIBLE);
                Log.e(">>>", "onFailure: " + t.getMessage());
            }
        });

    }


    public boolean IsLoading = false;
    public  CustomAuthRequest request;
    private List<Long> ExistingServices = new ArrayList<Long>();
    private void getCompanyDetail(final boolean isAppend, final int offset, final String CurrentSearctText) {
        Log.e("TEST","Call Again");
        ExistingServices.clear();
        IsLoading = true;
        request = new CustomAuthRequest(Request.Method.POST, METHOD_COMPANY_GETDETAIL, null,0,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showMessage(getText(R.string.label_empty_list).toString());
                        Log.w(TAG, response.toString() );
                        if (App.getInstance().authorizeSimple(response)) {
                            try {
                                //JSONObject jsonDataRole = response.getJSONObject("obj1");

                                CompanyRelationJ CRN =CompanyRelationJ.getJSON(response.getString("obj"));
                                List<CompanyRelationSubServiceJ> CRList = new ArrayList<>();
                                if(CRN != null)
                                {
                                    CR = CRN;
                                }
                                CRList = CR.Services;
                                company_project_count.setText(String.valueOf(CR.PP));
                                if (!isAppend)
                                    mSearchResultsAdapter.ClearData();
                                if (CRList != null) {
                                    for (CompanyRelationSubServiceJ message : CRList) {
                                        message.setColor(getRandomMaterialColor("400"));
                                        message.isExpanded = false;
                                        if (ExistingServices.indexOf(message.Id) == -1) {
                                            ExistingServices.add(message.Id);
                                            mSearchResultsAdapter.Add(message, false);
                                        }
                                    }
                                }
                                if (mSearchResultsAdapter.getItemCount() != 0) {
                                    hideMessage();
                                }
                                mSearchResultsAdapter.notifyDataSetChanged();
                            } catch (JSONException ex) {
                                DataGlobal.SaveLog(TAG, ex);
                                SnakebarCustom.danger(mContext, mView, "Unable to fetch Services", 5000);
                            }
                            IsLoading = false;
                        } else {
                            IsLoading = false;
                            try {
                                if (response.getString("Message") != null)
                                    SnakebarCustom.danger(mContext, mView, response.getString("Message"), 5000);
                            } catch (JSONException ex) {
                            }
                        }
                    }
                }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                SnakebarCustom.danger(mContext, mView, "Unable to fetch Companies: " + error.getMessage(), 5000);
                IsLoading = false;
            }
        }) {

            @Override
            protected JSONObject getParams() {
                try {
                    JSONObject params = new JSONObject();
                    params.put("CRId", CR.CRId);
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

    @Override
    public void onMessageRowClicked(int position, CompanyServiceListAdapter.ViewHolder holder) {
        // verify whether action mode is enabled or not
        // if enabled, change the row state to activated

            // read the message which removes bold from the row
            CompanyRelationSubServiceJ message = mSearchResultsAdapter.getItem(position);
            message.setRead(true);
            mSearchResultsAdapter.set(position, message);
            mSearchResultsAdapter.notifyDataSetChanged();


    }

    public void showMessage(String message) {

        mMessage.setText(message);
        mMessage.setVisibility(View.VISIBLE);
    }

    public void hideMessage() {

        mMessage.setVisibility(View.GONE);
    }

    private boolean supportsViewElevation() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
    }

    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {

            loading = savedInstanceState.getBoolean("loading");

        } else {

            loading = false;
        }

        if (loading) {

            showpDialog();
        }
    }

    public void onDestroyView() {
        if(request != null) {
            request.destroy();
        }
        request = null;
        if (mlinearLayoutManager != null) {
            mlinearLayoutManager.removeAllViews();
            mlinearLayoutManager = null;
        }

        if (mSearchResultsList != null) {
            mSearchResultsList.setItemAnimator(null);
            mSearchResultsList.setAdapter(null);
            mSearchResultsList = null;
        }

        mlinearLayoutManager = null;
        super.onDestroyView();
        Log.w(TAG, "onDestroyView: " );
        hidepDialog();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putBoolean("loading", loading);
    }

    private int getRandomMaterialColor(String typeColor) {
        int returnColor = Color.GRAY;
        int arrayId = getResources().getIdentifier("mdcolor_" + typeColor, "array", CurrentActivity.getPackageName());

        if (arrayId != 0) {
            TypedArray colors = getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }

    protected void initpDialog() {

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.msg_loading));
        pDialog.setCancelable(false);
    }

    protected void showpDialog() {

        if (!pDialog.isShowing())
            pDialog.show();
    }

    protected void hidepDialog() {

        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public boolean onActivityBackPress() {
        return true;
    }

    private boolean allPermissionsGranted() {
        for(String permission : REQUIRED_PERMISSIONS){
            if(ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

}