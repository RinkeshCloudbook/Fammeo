package com.fammeo.app.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fammeo.app.R;
import com.fammeo.app.Retrofit.FileUploadService;
import com.fammeo.app.Retrofit.ViewProfileBGChangeExample;
import com.fammeo.app.activity.EditActivity.AboutDetails;
import com.fammeo.app.activity.EditActivity.EditAddress;
import com.fammeo.app.activity.EditActivity.EditHobbies;
import com.fammeo.app.activity.EditActivity.EditLanguage;
import com.fammeo.app.activity.EditActivity.EditPhone;
import com.fammeo.app.activity.EditActivity.Skills;
import com.fammeo.app.activity.EditActivity.SocialLink;
import com.fammeo.app.activity.EditEmail;
import com.fammeo.app.activity.SettingEdit;
import com.fammeo.app.adapter.AddressAdapter;
import com.fammeo.app.adapter.LanuageSettingAdapter;
import com.fammeo.app.adapter.fammeoAdapter.EmailDailogeAdapter;
import com.fammeo.app.adapter.fammeoAdapter.EmailListAdapter;
import com.fammeo.app.adapter.fammeoAdapter.HobbyAdapterSetting;
import com.fammeo.app.adapter.fammeoAdapter.PhoneAdapter;
import com.fammeo.app.adapter.fammeoAdapter.SkillAdapterSetting;
import com.fammeo.app.app.App;
import com.fammeo.app.common.DataGlobal;
import com.fammeo.app.common.DataText;
import com.fammeo.app.model.CommonModel;
import com.fammeo.app.model.EmailModel;
import com.fammeo.app.util.CustomAuthRequest;
import com.fammeo.app.util.GenericFileProvider;
import com.fammeo.app.util.RealPathUtil;
import com.fammeo.app.view.siv.CircularImageView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.Serializable;
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

import static android.content.Context.MODE_PRIVATE;
import static android.media.MediaRecorder.VideoSource.CAMERA;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.fammeo.app.constants.Constants.METHOD_GET_CHANGE_BG_IMAGE_USER;
import static com.fammeo.app.constants.Constants.METHOD_GET_DELETE_ADDRESS_USER;
import static com.fammeo.app.constants.Constants.METHOD_GET_DELETE_Email_USER;
import static com.fammeo.app.constants.Constants.METHOD_GET_DELETE_PHONE_USER;
import static com.fammeo.app.constants.Constants.METHOD_GET_USERDATA_USER;
import static com.fammeo.app.fragment.CompanyProfileFragment.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE;

/**
 * A simple {@link Fragment} subclass.
 */
public class VewProfileFragment extends Fragment {

    private static final String TAG = VewProfileFragment.class.getSimpleName();
    String email_id,getLink,getDec,getTitle,mode;
    LinearLayout pbHeaderProgress;
    public CustomAuthRequest request;
    RecyclerView recycler_view_lang,recycler_view_email,recycler_view_address,recycler_view_phone,recycler_view_hb
                    ,recycler_view_sk;
    ArrayList<CommonModel> lanList = new ArrayList<>();
    ArrayList<CommonModel> mAddressList = new ArrayList<>();
    ArrayList<CommonModel> phoneList = new ArrayList<>();
    List<CommonModel> hobbyList = new ArrayList<>();
    List<CommonModel> skillList = new ArrayList<>();
    ArrayList<CommonModel> getFieldList = new ArrayList<>();
    List<CommonModel> profileLangList;
    ArrayList<EmailModel> emailList;
    View mView;
    String pe, peType,userId;
    TextView txt_title,txt_dec,txt_link;
    int phoneLenght;
    JSONArray arr, arrSetting;
    private Button bt_save, bt_save_address;
    private EmailListAdapter emailadapter = null;
    private EmailDailogeAdapter emailAdapterDialogList = null;
    SharedPreferences sp;
    AddressAdapter addsAdapter;
    PhoneAdapter phoneAdapter;
    ImageView bg_image;
    AlertDialog dialog,modedialog;
    ProgressBar pr_imageLoder;
    public final String APP_TAG = "MyCustomAppContect";
    File photoFile;
    public String photoFileName = "photo.jpg";

    public VewProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_vew_profile, container, false);

        pbHeaderProgress = mView.findViewById(R.id.pbHeaderProgress);
        recycler_view_lang = mView.findViewById(R.id.recycler_view_lang);
        recycler_view_email = mView.findViewById(R.id.recycler_view_email);
        recycler_view_address = mView.findViewById(R.id.recycler_view_address);
        recycler_view_phone = mView.findViewById(R.id.recycler_view_phone);
        recycler_view_hb = mView.findViewById(R.id.recycler_view_hb);
        recycler_view_sk = mView.findViewById(R.id.recycler_view_sk);
        bg_image = mView.findViewById(R.id.bg_image);
        txt_title = mView.findViewById(R.id.txt_title);
        txt_dec = mView.findViewById(R.id.txt_dec);
        txt_link = mView.findViewById(R.id.txt_link);
        pr_imageLoder = mView.findViewById(R.id.pr_imageLoder);


        setRecleyViewManager(recycler_view_email);
        setRecleyViewManager(recycler_view_address);
        setRecleyViewManager(recycler_view_phone);
        //setRecleyViewManager(recycler_view_hb);

        GridLayoutManager skillgridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recycler_view_sk.setLayoutManager(skillgridLayoutManager);

        GridLayoutManager langgridLayoutManager = new GridLayoutManager(getActivity(), 3);
        recycler_view_hb.setLayoutManager(langgridLayoutManager);

        sp = getActivity().getSharedPreferences("uId", MODE_PRIVATE);
        userId = sp.getString("u", "");
        String un = sp.getString("un", "");

        if (userId.equals("") || userId.length() > 0) {
            userId = App.getInstance().getUserId();
        }

        Log.e("TEST","Get User Id :"+userId);
        ((ImageButton) mView.findViewById(R.id.img_btn_about)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showAboutCustomDialog();
                Intent intent = new Intent(getActivity(), AboutDetails.class);
                intent.putExtra("T",getTitle);
                intent.putExtra("D",getDec);
                intent.putExtra("L",getLink);
                startActivity(intent);
            }
        });
        ((ImageButton) mView.findViewById(R.id.img_btn_socLink)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SocialLink.class);
                intent.putExtra("field", (Serializable) getFieldList);
                startActivity(intent);
            }
        });
        ((TextView) mView.findViewById(R.id.txt_addNewAddress)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditAddress.class);
                startActivity(intent);
            }
        });
        ((TextView) mView.findViewById(R.id.txt_addNewEmail)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditEmail.class);
                startActivity(intent);
            }
        });
        ((TextView) mView.findViewById(R.id.txt_addNewPhone)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditPhone.class);
                startActivity(intent);
            }
        });

        ((ImageButton) mView.findViewById(R.id.imgbt_lang)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showAboutCustomDialog();
                ArrayList<String> LangList = new ArrayList<String>();
                Intent intent = new Intent(getActivity(), EditLanguage.class);
                intent.putExtra("list", (Serializable) profileLangList);
                startActivity(intent);
            }
        });

        ((ImageButton) mView.findViewById(R.id.img_edit_hobbies)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TEST","Hlist :"+hobbyList.size());
                Intent intent = new Intent(getActivity(), EditHobbies.class);
                intent.putExtra("Hlist", (Serializable) hobbyList);
                startActivity(intent);
            }
        });
        ((ImageButton) mView.findViewById(R.id.img_edit_skills)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TEST","Click skills:");
                Intent intent = new Intent(getActivity(), Skills.class);
                intent.putExtra("Slist", (Serializable) skillList);
                startActivity(intent);
            }
        });
        ((FloatingActionButton) mView.findViewById(R.id.fab_editImage)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        getUserData();

        return mView;
    }

    private void selectImage() {
        dialog = new AlertDialog.Builder(getActivity()).create();
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.custom_alert_dailogbox, null);

        Button bt_decline = layout.findViewById(R.id.bt_decline);
        TextView txt_profile = layout.findViewById(R.id.txt_profile);
        TextView txt_change_bg = layout.findViewById(R.id.txt_change_bg);
        TextView txt_setting = layout.findViewById(R.id.txt_setting);

        dialog.setView(layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        txt_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = "userimage";
                selectImageMode();
            }
        });
        txt_change_bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = "userbgimage";
                selectImageMode();
            }
        });
        txt_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingDilougeBox();
            }
        });
        bt_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void selectImageMode() {
        modedialog = new AlertDialog.Builder(getActivity()).create();
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.custom_alert_dailogbox_camera, null);

        TextView txt_gallary = layout.findViewById(R.id.txt_gallary);
        Button bt_decline = layout.findViewById(R.id.bt_decline);
        TextView txt_takepic = layout.findViewById(R.id.txt_takepic);

        txt_takepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Log.e("TEST","Camera");
                askForPermission(Manifest.permission.CAMERA,CAMERA);
            }
        });
        txt_gallary.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                    }
                }
                galleryIntent();
            }
        });

        bt_decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modedialog.dismiss();
            }
        });
        modedialog.setView(layout);
        modedialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        modedialog.show();
        dialog.dismiss();
    }

    public void show() {
        dialog.show();
    }
    public void hide() {
        dialog.dismiss();
    }

    public void cameraIntent() {
        Log.e("TEST","Camara mode :"+mode);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFileName= Calendar.getInstance().getTimeInMillis()+".jpg";
        photoFile = getPhotoFileUri(photoFileName);
        Uri fileProvider = GenericFileProvider.getUriForFile(getActivity(), "com.fammeo.app.util.GenericFileProvider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    public void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),111);
    }

    private File getPhotoFileUri(String photoFileName) {
        File mediaStorageDir = new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), APP_TAG);

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(APP_TAG, "failed to create directory");
        }
        File file = new File(mediaStorageDir.getPath() + File.separator + photoFileName);
        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String  Token = App.getInstance().getAccessToken();
        long ParseACId = App.getInstance().getCurrentACId();

        final String parsedurl = App.getInstance().GetCustonDomain(ParseACId,METHOD_GET_CHANGE_BG_IMAGE_USER);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 111){
                Log.e("TEST","Upload");
                String realPath= RealPathUtil.getRealPath(getActivity(),data.getData());
                uploadImage(parsedurl,realPath,Token,mode);
            }else if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
                String realPath=photoFile.getAbsolutePath();
                Log.e("TEST","Capture");
                uploadImage(parsedurl,realPath,Token,mode);
            }
        }
    }

    private void uploadImage(String parsedurl, final String realPath, String token,String mode) {
        Log.e("TEST","Get Mode :"+mode);
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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(parsedurl+"/").client(client).addConverterFactory(GsonConverterFactory.create(new GsonBuilder().serializeNulls().create())).build();
//        RequestBody open_time = RequestBody.create(MediaType.parse("text/plain"), "pass any data like id,name ");
        okhttp3.RequestBody requestBodyx = okhttp3.RequestBody.create(okhttp3.MediaType.parse("image/*"), N_file);

        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", new File(realPath).getName(), requestBodyx);
        okhttp3.RequestBody companyimage =  okhttp3.RequestBody.create(okhttp3.MediaType.parse("text/plain"), mode);
        okhttp3.RequestBody filepath =  okhttp3.RequestBody.create(okhttp3.MediaType.parse("text/plain"), "");
        okhttp3.RequestBody coid =  okhttp3.RequestBody.create(okhttp3.MediaType.parse("text/plain"), userId);


        FileUploadService apiService = retrofit.create(FileUploadService.class);

        Call<ViewProfileBGChangeExample> memberCall = apiService.contectuploadImage(parsedurl,"Bearer "+token,fileToUpload, companyimage,filepath,coid);
        memberCall.enqueue(new Callback<ViewProfileBGChangeExample>() {

            @Override
            public void onResponse(Call<ViewProfileBGChangeExample> call, retrofit2.Response<ViewProfileBGChangeExample> response) {
                if(response.body() != null && response.body().toString().length() > 0){
                    Log.e("TEST", "Getting Response = "+response.body().toString());
                    if(response.body().getMessage().equalsIgnoreCase("OK")){
                        Log.e("TEST", "Success");
                        pr_imageLoder.setVisibility(View.GONE);
                        Glide.with(getActivity()).load(realPath)
                                .thumbnail(0.5f)
                                .transition(withCrossFade())
                                .into(bg_image);
                        getUserData();
                        modedialog.dismiss();
                    }
                    else
                        Toast.makeText(getContext(), "Error while uploading Image", Toast.LENGTH_SHORT).show();
                }
                else
                    Log.e("TEST", "Nothing");
            }
            @Override
            public void onFailure(Call<ViewProfileBGChangeExample> call, Throwable t) {
                pr_imageLoder.setVisibility(View.VISIBLE);
                Log.e(">>>", "onFailure: " + t.getMessage());
            }
        });
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{permission}, requestCode);
            }
        } else {
            cameraIntent();
            // Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }

    private void getUserData() {
        pbHeaderProgress.setVisibility(View.VISIBLE);
        Log.e("TEST", "Get User Data");
        // list.clear();
        request = new CustomAuthRequest(Request.Method.POST, METHOD_GET_USERDATA_USER, null, 0,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onResponse(JSONObject response) {
                        if (App.getInstance().authorizeSimple(response)) {
                            String strResponse = response.toString();
                            Log.e("TEST", "Get User Data Response :" + response.toString());
                            if (strResponse != null) {
                                pbHeaderProgress.setVisibility(View.GONE);

                                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
                                recycler_view_lang.setLayoutManager(gridLayoutManager);

                                lanList.clear();
                                try {

                                    JSONObject object = new JSONObject(strResponse);
                                    String msgType = object.getString("MessageType");
                                    if (msgType.equalsIgnoreCase("success")) ;

                                    JSONObject obj = object.getJSONObject("obj");
                                    /*------------------------------------------------------------------------------------------------------------------*/
                                    JSONArray arr = obj.getJSONArray("Ls");
                                    if(arr.length() == 0){
                                        ((TextView) mView.findViewById(R.id.txt_langContent)).setVisibility(View.VISIBLE);
                                        ((TextView) mView.findViewById(R.id.txt_langContent)).setText("No language to show");
                                    }
                                    String userId = obj.getString("Id");
                                    String userURl = obj.getString("I");
                                    String un = obj.getString("UN");
                                    String fullName = obj.getString("FN") + " " + obj.getString("LN");
                                    ((TextView) mView.findViewById(R.id.txt_name)).setText(fullName);
                                    ((TextView) mView.findViewById(R.id.txt_uname)).setText("@" + un);
                                    if (userURl != null) {
                                        Glide.with(getActivity()).load(DataText.GetImagePath(userURl))
                                                .thumbnail(0.5f)
                                                .transition(withCrossFade())
                                                .apply(RequestOptions.circleCropTransform())
                                                .into(((CircularImageView) mView.findViewById(R.id.search_image)));
                                    } else {
                                        String firstLater = fullName.substring(0, 1).toUpperCase();
                                        ((CircularImageView) mView.findViewById(R.id.search_image)).setImageResource(R.drawable.bg_search_circle);
                                        ((CircularImageView) mView.findViewById(R.id.search_image)).setColorFilter(null);
                                        ((TextView) mView.findViewById(R.id.search_image_text)).setText(firstLater);
                                    }

                                    //emailList.clear();
                                    emailList = new ArrayList<>();
                                    /*------------------------------------------------------------------------------------------------------------------*/
                                    JSONArray arrEs = obj.getJSONArray("Es");
                                    if(arrEs.length() == 0){
                                        ((TextView) mView.findViewById(R.id.txt_emailContent)).setVisibility(View.VISIBLE);
                                        ((TextView) mView.findViewById(R.id.txt_emailContent)).setText("No data found");
                                    }
                                    for (int i = 0; i < arrEs.length(); i++) {
                                        JSONObject objEs = arrEs.getJSONObject(i);
                                        EmailModel em = new EmailModel();
                                        pe = objEs.getString("E");
                                        peType = objEs.getString("T");
                                        String id = objEs.getString("Id");
                                        em.emailAddress = pe;
                                        em.emailType = peType;
                                        em.recordId = id;
                                        emailList.add(em);
                                    }
                                    emailadapter = new EmailListAdapter(VewProfileFragment.this,getContext(), emailList);
                                    recycler_view_email.setAdapter(emailadapter);

                                    mAddressList.clear();
                                    /*------------------------------------------------------------------------------------------------------------------*/
                                    JSONArray arrAdds = obj.getJSONArray("Adds");
                                    if(arrAdds.length() == 0){
                                        ((TextView) mView.findViewById(R.id.txt_addContent)).setVisibility(View.VISIBLE);
                                        ((TextView) mView.findViewById(R.id.txt_addContent)).setText("No Address to show");
                                    }
                                    for (int j = 0; j < arrAdds.length(); j++) {
                                        JSONObject addsObj = arrAdds.getJSONObject(j);
                                        String fullAddress = addsObj.getString("C") + addsObj.getString("S") + addsObj.getString("CR");
                                        CommonModel am = new CommonModel();

                                        am.cType = addsObj.getString("T");
                                        am.cAddress = addsObj.getString("L1");
                                        am.cN = addsObj.getString("C");
                                        am.cState = addsObj.getString("S");
                                        am.cCountry = addsObj.getString("CR");
                                        am.addsId = addsObj.getString("Id");
                                        am.fullAddress = fullAddress;

                                        mAddressList.add(am);
                                    }
                                    addsAdapter = new AddressAdapter(VewProfileFragment.this,getContext(), mAddressList);
                                    recycler_view_address.setAdapter(addsAdapter);
                                    /*------------------------------------------------------------------------------------------------------------------*/
                                    phoneList.clear();
                                    JSONArray phoneAdds = obj.getJSONArray("PHs");
                                    phoneLenght = phoneAdds.length();
                                    if(phoneAdds.length() == 0){
                                        ((TextView) mView.findViewById(R.id.txt_phoneContent)).setVisibility(View.VISIBLE);
                                        ((TextView) mView.findViewById(R.id.txt_phoneContent)).setText("No Phone numbers to show");
                                    }
                                    for (int k = 0; k < phoneAdds.length(); k++) {
                                        JSONObject phoneObj = phoneAdds.getJSONObject(k);
                                        CommonModel am = new CommonModel();
                                        am.phNumber = phoneObj.getString("Ph");
                                        am.phcType = phoneObj.getString("T");
                                        am.phcCode = phoneObj.getString("CC");
                                        am.phId = phoneObj.getString("Id");
                                        phoneList.add(am);
                                    }
                                    phoneAdapter = new PhoneAdapter(VewProfileFragment.this,getContext(), phoneList);
                                    recycler_view_phone.setAdapter(phoneAdapter);
                                    /*------------------------------------------------------------------------------------------------------------------*/

                                    profileLangList = new ArrayList<>();
                                    // LinearLayout lLayout = (LinearLayout) findViewById(R.id.rel_lang);
                                    // lLayout.removeAllViews();
                                    for (int i = 0; i < arr.length(); i++) {
                                        JSONObject arrObj = arr.getJSONObject(i);
                                        CommonModel cm = new CommonModel();
                                        cm.lId = arrObj.getString("Id");
                                        cm.lName = arrObj.getString("N");
                                        profileLangList.add(cm);
                                        String getname = cm.lName;
                                    }

                                    LanuageSettingAdapter listAdapter = new LanuageSettingAdapter(VewProfileFragment.this,getContext(), profileLangList);
                                    recycler_view_lang.setAdapter(listAdapter);
                                    //rowTextView.setText(getname);
                                    /*------------------------------------------------------------------------------------------------------------------*/
                                    JSONArray arrHobbies = obj.getJSONArray("Hs");
                                    if(arrHobbies.length() == 0){
                                        ((TextView) mView.findViewById(R.id.txt_hbContent)).setVisibility(View.VISIBLE);
                                        ((TextView) mView.findViewById(R.id.txt_hbContent)).setText("No Hobby to show");
                                    }
                                    for (int k = 0; k < arrHobbies.length(); k++) {

                                        JSONObject arrObj = arrHobbies.getJSONObject(k);
                                        CommonModel cm = new CommonModel();
                                        cm.lId = arrObj.getString("Id");
                                        cm.lName = arrObj.getString("N");
                                        hobbyList.add(cm);
                                    }

                                    HobbyAdapterSetting adpeter = new HobbyAdapterSetting(VewProfileFragment.this, getContext(), hobbyList);
                                    recycler_view_hb.setAdapter(adpeter);
                                    /*------------------------------------------------------------------------------------------------------------------*/
                                    arrSetting = obj.getJSONArray("Ss");
                                    Log.e("TEST","arrSetting :"+arrSetting.length());
                                    /*------------------------------------------------------------------------------------------------------------------*/
                                    JSONArray arrSkill = obj.getJSONArray("Sks");
                                    if(arrSkill.length() == 0){
                                        ((TextView) mView.findViewById(R.id.txt_skillContent)).setVisibility(View.VISIBLE);
                                        ((TextView) mView.findViewById(R.id.txt_skillContent)).setText("No Skill to show");
                                    }
                                    for (int k = 0; k < arrSkill.length(); k++) {
                                        JSONObject arrObj = arrSkill.getJSONObject(k);
                                        CommonModel cm = new CommonModel();
                                        cm.lId = arrObj.getString("Id");
                                        cm.lName = arrObj.getString("N");
                                        skillList.add(cm);
                                    }

                                    SkillAdapterSetting skadpeter = new SkillAdapterSetting(VewProfileFragment.this, getContext(), skillList);
                                    recycler_view_sk.setAdapter(skadpeter);
                                    /*------------------------------------------------------------------------------------------------------------------*/
                                    JSONArray arrfields = obj.getJSONArray("Fs");
                                    if(arrfields.length() == 0){
                                        ((TextView) mView.findViewById(R.id.txt_socContent)).setVisibility(View.VISIBLE);
                                        ((TextView) mView.findViewById(R.id.txt_socContent)).setText("No Social links to show");
                                    }
                                    for (int k = 0; k < arrfields.length(); k++) {
                                        JSONObject fieldObj = arrfields.getJSONObject(k);
                                        CommonModel cm = new CommonModel();
                                        cm.soc_N = fieldObj.getString("N");
                                        cm.soc_V = fieldObj.getString("V");
                                        getFieldList.add(cm);

                                        if(cm.soc_N.equalsIgnoreCase("facebook")) {
                                            if(cm.soc_V.equalsIgnoreCase("")){
                                                ((FloatingActionButton) mView.findViewById(R.id.flt_fb)).setVisibility(View.GONE);
                                            }else
                                                ((FloatingActionButton) mView.findViewById(R.id.flt_fb)).setVisibility(View.VISIBLE);
                                        }
                                        if(cm.soc_N.equalsIgnoreCase("twitter")) {
                                            if(cm.soc_V.equalsIgnoreCase("")){
                                                ((FloatingActionButton) mView.findViewById(R.id.flt_twitter)).setVisibility(View.GONE);
                                            }else
                                                ((FloatingActionButton) mView.findViewById(R.id.flt_twitter)).setVisibility(View.VISIBLE);
                                        }
                                        if(cm.soc_N.equalsIgnoreCase("linkedin")) {
                                            if(cm.soc_V.equalsIgnoreCase("")){
                                                ((FloatingActionButton) mView.findViewById(R.id.flt_linkdin)).setVisibility(View.GONE);
                                            }else
                                                ((FloatingActionButton) mView.findViewById(R.id.flt_linkdin)).setVisibility(View.VISIBLE);
                                        }
                                        if(cm.soc_N.equalsIgnoreCase("instagram")) {
                                            if(cm.soc_V.equalsIgnoreCase("")){
                                                ((FloatingActionButton) mView.findViewById(R.id.flt_instagram)).setVisibility(View.GONE);
                                            }else
                                                ((FloatingActionButton) mView.findViewById(R.id.flt_instagram)).setVisibility(View.VISIBLE);
                                        }
                                    }
                                    JSONArray arrAbout = obj.getJSONArray("Bls");

                                    JSONObject objLin = arrAbout.getJSONObject(0);
                                    String imgLink = objLin.getString("OV");

                                    Glide.with(VewProfileFragment.this).load(DataText.GetImagePath(imgLink))
                                            .thumbnail(0.5f)
                                            .transition(withCrossFade())
                                            .into(bg_image);

                                    JSONObject objDec = arrAbout.getJSONObject(1);
                                    getLink = objDec.getString("OV");
                                    txt_link.setText(getLink);
                                    //txt_link.setText(getDec);

                                    JSONObject objAbout = arrAbout.getJSONObject(2);
                                    getDec = objAbout.getString("OV");
                                    txt_dec.setText(getDec);
                                    //txt_dec.setText(getTitle);

                                    JSONObject objTitle = arrAbout.getJSONObject(3);
                                    getTitle = objTitle.getString("OV");
                                    txt_title.setText(getTitle);


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.e("TEST","Get User Exception :"+e);
                                }
                            }
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbHeaderProgress.setVisibility(View.VISIBLE);
                //SnakebarCustom.danger(mContext, v, "Unable to fetch Companies: " + error.getMessage(), 5000);
            }
        }) {

            @Override
            protected JSONObject getParams() {
                try {
                    JSONObject params = new JSONObject();
                    JSONArray jsonArray = new JSONArray();

                    //JSONObject jsonObject=new JSONObject();
                    //jsonObject.put("id",null);
                    //jsonObject.put("N",allLangList.get(i));
                    jsonArray.put("email");
                    jsonArray.put("field");
                    jsonArray.put("phone");
                    jsonArray.put("address");
                    jsonArray.put("date");
                    jsonArray.put("setting");
                    jsonArray.put("language");
                    jsonArray.put("hobby");
                    jsonArray.put("skill");
                    jsonArray.put("blob");

                    params.put("scopes", jsonArray);
                    params.put("UserId", userId);

                    Log.e("TEST","Get User Param :"+params);
                    return params;
                } catch (JSONException ex) {
                    Log.e("TEST","Get User Exception :"+ex);
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

    public void deleteEmail(final String emailId){
        Log.e("TEST","Get Delete Id From Email :"+emailId);
        // list.clear();
        request = new CustomAuthRequest(Request.Method.POST, METHOD_GET_DELETE_Email_USER, null, 0,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (App.getInstance().authorizeSimple(response)) {
                            String strResponse = response.toString();
                            Log.e("TEST", "Delete Response :" + response.toString());
                            if (strResponse != null) {
                                lanList.clear();
                                try {
                                    JSONObject object = new JSONObject(strResponse);
                                    String msgType = object.getString("MessageType");
                                    if (msgType.equalsIgnoreCase("success")) {
                                        toastIconSuccess("addsdlete");
                                        getUserData();
                                    }
//
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
//
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //pbHeaderProgress.setVisibility(View.VISIBLE);
                //SnakebarCustom.danger(mContext, v, "Unable to fetch Companies: " + error.getMessage(), 5000);
            }
        }) {

            @Override
            protected JSONObject getParams() {
                try {
                    JSONObject params = new JSONObject();
                    params.put("Id", emailId);
                    params.put("UserId", userId);
                    Log.e("TEST","Delete Address Param :"+params);
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

    private void toastIconSuccess(String msgText) {
        Toast toast = new Toast(getActivity());
        toast.setDuration(Toast.LENGTH_LONG);

        //inflate view
        View custom_view = getLayoutInflater().inflate(R.layout.toast_success_text, null);
        if (msgText.equalsIgnoreCase("Language")) {
            ((TextView) custom_view.findViewById(R.id.message)).setText("Language(s) Saved Successfully!");
        } else if (msgText.equalsIgnoreCase("email")) {
            ((TextView) custom_view.findViewById(R.id.message)).setText("Email(s) Saved Successfully!");
        } else if (msgText.equalsIgnoreCase("address")) {
            ((TextView) custom_view.findViewById(R.id.message)).setText("Address(s) Saved Successfully!");
        } else if (msgText.equalsIgnoreCase("phone")) {
            ((TextView) custom_view.findViewById(R.id.message)).setText("Phone(s) Saved Successfully!");
        }else if (msgText.equalsIgnoreCase("about")) {
            ((TextView) custom_view.findViewById(R.id.message)).setText("About(s) Saved Successfully!");
        }else if (msgText.equalsIgnoreCase("addsdlete")) {
            ((TextView) custom_view.findViewById(R.id.message)).setText("Delete Successfully!");
        }

        ((ImageView) custom_view.findViewById(R.id.icon)).setImageResource(R.drawable.ic_done);
        ((CardView) custom_view.findViewById(R.id.parent_view)).setCardBackgroundColor(getResources().getColor(R.color.green_500));

        toast.setView(custom_view);
        toast.show();
    }

    public void deleteAddress(final String addsId){
        Log.e("TEST","Get Delete Id From Address :"+addsId);
        // list.clear();
        request = new CustomAuthRequest(Request.Method.POST, METHOD_GET_DELETE_ADDRESS_USER, null, 0,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (App.getInstance().authorizeSimple(response)) {
                            String strResponse = response.toString();
                            Log.e("TEST", "Delete Response :" + response.toString());
                            if (strResponse != null) {
                                lanList.clear();
                                try {
                                    JSONObject object = new JSONObject(strResponse);
                                    String msgType = object.getString("MessageType");
                                    if (msgType.equalsIgnoreCase("success")) {
                                        toastIconSuccess("addsdlete");
                                        getUserData();
                                    }
//
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        } else {
//
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //pbHeaderProgress.setVisibility(View.VISIBLE);
                //SnakebarCustom.danger(mContext, v, "Unable to fetch Companies: " + error.getMessage(), 5000);
            }
        }) {

            @Override
            protected JSONObject getParams() {
                try {
                    JSONObject params = new JSONObject();
                    params.put("Id", addsId);
                    params.put("UserId", userId);
                    Log.e("TEST","Delete Address Param :"+params);
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

    public void deletePhone(final String addsId){
        Log.e("TEST","Get Phone Id From :"+addsId);
        // list.clear();
        request = new CustomAuthRequest(Request.Method.POST, METHOD_GET_DELETE_PHONE_USER, null, 0,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (App.getInstance().authorizeSimple(response)) {
                            String strResponse = response.toString();
                            Log.e("TEST", "Delete Phone Response :" + response.toString());
                            if (strResponse != null) {
                                lanList.clear();
                                try {
                                    JSONObject object = new JSONObject(strResponse);
                                    String msgType = object.getString("MessageType");
                                    if (msgType.equalsIgnoreCase("success")) {
                                        toastIconSuccess("addsdlete");
                                        getUserData();
                                    }
//
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
//
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //pbHeaderProgress.setVisibility(View.VISIBLE);
                //SnakebarCustom.danger(mContext, v, "Unable to fetch Companies: " + error.getMessage(), 5000);
            }
        }) {

            @Override
            protected JSONObject getParams() {
                try {
                    JSONObject params = new JSONObject();
                    params.put("Id", addsId);
                    params.put("UserId", userId);
                    Log.e("TEST","Delete Phone Param :"+params);
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

    private void settingDilougeBox() {
        //arrSetting
        Log.e("TEST","Setting Request :"+arrSetting.length());

        final Dialog settindialog = new Dialog(getActivity());
        settindialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        settindialog.setContentView(R.layout.custom_setting_alert_dailogbox);
        settindialog.setCancelable(true);

        String getShowFrnd = ((TextView) settindialog.findViewById(R.id.txt_user_ShowFriends1)).getText().toString();
        String getshowFollowers = ((TextView) settindialog.findViewById(R.id.txt_user_ShowFollowers1)).getText().toString();
        String getAllowRequest = ((TextView) settindialog.findViewById(R.id.txt_user_AllowFriendRequests1)).getText().toString();
        String getNewMessage = ((TextView) settindialog.findViewById(R.id.txt_user_Notify_NewMessage1)).getText().toString();
        String getNewConnection = ((TextView) settindialog.findViewById(R.id.txt_user_Notify_NewConnection1)).getText().toString();
        String getEmailOff = ((TextView) settindialog.findViewById(R.id.txt_user_EmailOff1)).getText().toString();
        Log.e("TEST","Get Value :"+getShowFrnd);

        for (int i = 0; i < arrSetting.length(); i++) {
            try {
                JSONObject objSetting = arrSetting.getJSONObject(i);
                String settingN = objSetting.getString("N");

                if(settingN.equalsIgnoreCase(getShowFrnd)){
                    if(objSetting.getString("IA").equalsIgnoreCase("true")){
                        ((Switch) settindialog.findViewById(R.id.switch_user_ShowFriends)).setChecked(true);
                    }else {
                        ((Switch) settindialog.findViewById(R.id.switch_user_ShowFriends)).setChecked(false);
                    }
                }
                if(settingN.equalsIgnoreCase(getshowFollowers)){
                    if(objSetting.getString("IA").equalsIgnoreCase("true")){
                        ((Switch) settindialog.findViewById(R.id.switch_user_ShowFollowers)).setChecked(true);
                    }else {
                        ((Switch) settindialog.findViewById(R.id.switch_user_ShowFollowers)).setChecked(false);
                    }
                }
                if(settingN.equalsIgnoreCase(getAllowRequest)){
                    if(objSetting.getString("IA").equalsIgnoreCase("true")){
                        ((Switch) settindialog.findViewById(R.id.switch_user_AllowFriendRequests)).setChecked(true);
                    }else {
                        ((Switch) settindialog.findViewById(R.id.switch_user_AllowFriendRequests)).setChecked(false);
                    }
                }
                if(settingN.equalsIgnoreCase(getNewMessage)){
                    if(objSetting.getString("IA").equalsIgnoreCase("true")){
                        ((Switch) settindialog.findViewById(R.id.switch_user_Notify_NewMessage)).setChecked(true);
                    }else {
                        ((Switch) settindialog.findViewById(R.id.switch_user_Notify_NewMessage)).setChecked(false);
                    }
                }
                if(settingN.equalsIgnoreCase(getNewConnection)){
                    if(objSetting.getString("IA").equalsIgnoreCase("true")){
                        ((Switch) settindialog.findViewById(R.id.switch_user_Notify_NewConnection)).setChecked(true);
                    }else {
                        ((Switch) settindialog.findViewById(R.id.switch_user_Notify_NewConnection)).setChecked(false);
                    }
                }
                if(settingN.equalsIgnoreCase(getEmailOff)){
                    if(objSetting.getString("IA").equalsIgnoreCase("true")){
                        ((Switch) settindialog.findViewById(R.id.switch_user_EmailOff)).setChecked(true);
                    }else {
                        ((Switch) settindialog.findViewById(R.id.switch_user_EmailOff)).setChecked(false);
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        //modedialog.setView(lp);
        settindialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        settindialog.show();
        settindialog.getWindow().setAttributes(lp);
        dialog.dismiss();
    }



    private void setRecleyViewManager(RecyclerView recycler_view){
        RecyclerView.LayoutManager addsrecy = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recycler_view.setLayoutManager(addsrecy);
    }
}
