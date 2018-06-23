package com.semicolon.Halan.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lamudi.phonefield.PhoneInputLayout;
import com.semicolon.Halan.Models.Finishied_Order_Model;
import com.semicolon.Halan.Models.UserModel;
import com.semicolon.Halan.R;
import com.semicolon.Halan.Services.Api;
import com.semicolon.Halan.Services.Preferences;
import com.semicolon.Halan.Services.Services;
import com.semicolon.Halan.Services.Tags;
import com.semicolon.Halan.SingleTone.Users;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import de.hdodenhof.circleimageview.CircleImageView;
import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity implements Users.UserData,View.OnClickListener {

    private Users users;
    private UserModel userModel;
    private CircleImageView img_profile;
    private RelativeLayout container;
    private RatingBar rateBar;
    private TextView rate,order_num,txt_name,txt_user_name,txt_email,txt_phone,txt_age,txt_gender,txt_city,txt_country;
    private LinearLayout user_name_container,user_email_container,user_phone_container,user_age_container,user_gender_container,user_city_Container,user_country_Container;
    private Preferences preferences;
    private final int IMG_REQ = 100;
    private String enCodedImage;
    private Bitmap bitmap;
    private ImageView back;
    private AlertDialog alertDialog;
    private String gender="",curr_gender="";
    private ProgressDialog dialog;
    private String my_gender="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "JannaLT-Regular.ttf", true);
        EventBus.getDefault().register(this);
        initView();
        users=Users.getInstance();
        preferences=new Preferences(this);
        users.getUserData(this);

    }

    @Override
    public void UserDataSuccess(UserModel userModel) {

        this.userModel=userModel;
        UpdateUi(userModel);

    }
    private void UpdateUi(UserModel userModel) {
        Log.e("city",userModel.getUser_city()+"");
        Log.e("country",userModel.getUser_country()+"");
        Log.e("age",userModel.getUser_age()+"");
        Log.e("gender",userModel.getUser_gender()+"");
        Log.e("name",userModel.getName()+"");

        if (userModel.getUser_type().equals(Tags.Client))
        {
            container.setVisibility(View.INVISIBLE);
            user_age_container.setVisibility(View.GONE);
            user_gender_container.setVisibility(View.GONE);
            user_city_Container.setVisibility(View.GONE);
            user_country_Container.setVisibility(View.GONE);
        }else if (userModel.getUser_type().equals(Tags.Driver))
        {
            user_city_Container.setVisibility(View.VISIBLE);
            container.setVisibility(View.VISIBLE);
            user_age_container.setVisibility(View.VISIBLE);
            user_gender_container.setVisibility(View.VISIBLE);
            txt_age.setText(userModel.getUser_age());
            user_country_Container.setVisibility(View.VISIBLE);
            txt_country.setText(userModel.getUser_country());
            txt_city.setText(userModel.getUser_city());
            if (userModel.getUser_gender().equals(Tags.gender_male))
            {
                txt_gender.setText(getString(R.string.male));
                my_gender=Tags.gender_male;

            }else if (userModel.getUser_gender().equals(Tags.gender_female))
            {
                txt_gender.setText(getString(R.string.female));
                my_gender=Tags.gender_female;

            }

        }
        rateBar.setEnabled(false);
        rateBar.setFocusable(false);
        rateBar.setRating((float) userModel.getStars_evaluation());
        rate.setText(String.valueOf(userModel.getRate_evaluation()));
        order_num.setText(String.valueOf(userModel.getOrder_count()));
        txt_name.setText(userModel.getName());
        txt_user_name.setText(userModel.getUser_name());
        if (userModel.getUser_email().equals("0"))
        {
            txt_email.setText(R.string.no_email);

        }else
            {
                txt_email.setText(userModel.getUser_email());

            }
        txt_phone.setText(userModel.getUser_phone());

        Picasso.with(this).load(Uri.parse(Tags.ImgPath + userModel.getUser_photo())).placeholder(R.drawable.user_profile).into(img_profile);

    }

    private void initView() {
        container = findViewById(R.id.container);
        rateBar = findViewById(R.id.rateBar);
        LayerDrawable drawable = (LayerDrawable) rateBar.getProgressDrawable();
        drawable.getDrawable(0).setColorFilter(ContextCompat.getColor(this,R.color.gray3), PorterDuff.Mode.SRC_ATOP);
        drawable.getDrawable(1).setColorFilter(ContextCompat.getColor(this,R.color.rate), PorterDuff.Mode.SRC_ATOP);
        drawable.getDrawable(2).setColorFilter(ContextCompat.getColor(this,R.color.rate), PorterDuff.Mode.SRC_ATOP);
        rate = findViewById(R.id.rate);
        order_num = findViewById(R.id.order_num);
        txt_name = findViewById(R.id.txt_name);
        txt_user_name = findViewById(R.id.txt_user_name);
        txt_email = findViewById(R.id.txt_email);
        txt_phone = findViewById(R.id.txt_phone);
        txt_age = findViewById(R.id.txt_age);
        txt_gender = findViewById(R.id.txt_gender);
        back = findViewById(R.id.back);
        img_profile=findViewById(R.id.img_profile);
        txt_city = findViewById(R.id.txt_city);
        txt_country = findViewById(R.id.txt_country);



        user_name_container = findViewById(R.id.user_name_Container);
        user_email_container= findViewById(R.id.user_email_Container);
        user_phone_container= findViewById(R.id.user_phone_Container);
        user_age_container  = findViewById(R.id.user_age_Container);
        user_gender_container = findViewById(R.id.user_gender_Container);
        user_city_Container = findViewById(R.id.user_city_Container);
        user_country_Container = findViewById(R.id.user_country_Container);

        user_name_container.setOnClickListener(this);
        user_email_container.setOnClickListener(this);
        user_phone_container.setOnClickListener(this);
        user_age_container.setOnClickListener(this);
        user_gender_container.setOnClickListener(this);
        user_city_Container.setOnClickListener(this);
        user_country_Container.setOnClickListener(this);

        txt_name.setOnClickListener(this);
        img_profile.setOnClickListener(this);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Driver_delivered_Order(Finishied_Order_Model finishied_order_model)
    {
        CreateCustomAlertDialog(finishied_order_model);
    }

    private void CreateProgressDialog(String msg)
    {
        ProgressBar bar = new ProgressBar(this);
        Drawable drawable = bar.getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setMessage(msg);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setIndeterminateDrawable(drawable);
        dialog.show();
    }
    private void CreateCustomAlertDialog(final Finishied_Order_Model finishied_order_model) {
        View view = LayoutInflater.from(this).inflate(R.layout.custom_alert_dialog,null);
        CircleImageView driver_img = view.findViewById(R.id.driver_image);
        TextView driver_name = view.findViewById(R.id.driver_name);
        TextView order_details = view.findViewById(R.id.order_details);

        Picasso.with(UserProfileActivity.this).load(Uri.parse(Tags.ImgPath+finishied_order_model.getDriver_image())).into(driver_img);
        driver_name.setText(finishied_order_model.getDriver_name());
        order_details.setText(finishied_order_model.getOrder_details());
        Button addRateBtn = view.findViewById(R.id.add_rate);
        final AlertDialog alertDialog = new AlertDialog.Builder(UserProfileActivity.this)
                .setCancelable(false)
                .setView(view)
                .create();

        alertDialog.show();
        addRateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this,AddRateActivity.class);
                intent.putExtra("driver_id",finishied_order_model.getDriver_id());
                intent.putExtra("order_id",finishied_order_model.getOrder_id());
                intent.putExtra("driver_name",finishied_order_model.getDriver_name());
                intent.putExtra("driver_image",finishied_order_model.getDriver_image());
                startActivity(intent);
                alertDialog.dismiss();


            }
        });

    }
    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.img_profile:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent.createChooser(intent,getString(R.string.choose_image)),IMG_REQ);
                break;
            case R.id.txt_name:
                updateUserData(Tags.update_name);
                break;
            case R.id.user_name_Container:
                updateUserData(Tags.update_username);

                break;
            case R.id.user_email_Container:
                updateUserData(Tags.update_email);

                break;
            case R.id.user_phone_Container:
                updateUserData(Tags.update_phone);

                break;
            case R.id.user_age_Container:
                updateUserData(Tags.update_age_);

                break;
            case R.id.user_gender_Container:
                updateUserData(Tags.update_gender_);

                break;
            case R.id.user_city_Container:
                updateUserData(Tags.update_city);
                break;
            case R.id.user_country_Container:
                updateUserData(Tags.update_country);
                break;
            case R.id.tw:
                Intent intent_tw = new Intent(UserProfileActivity.this,WebViewActivity.class);
                intent_tw.putExtra("link","https://twitter.com/halanKSA_");
                startActivity(intent_tw);
                break;
            case R.id.in:
                Intent intent_in = new Intent(UserProfileActivity.this,WebViewActivity.class);
                intent_in.putExtra("link","https://www.instagram.com/halanksa_/");
                startActivity(intent_in);
                break;
            case R.id.fb:
                Intent intent_fb = new Intent(UserProfileActivity.this,WebViewActivity.class);
                intent_fb.putExtra("link","https://www.facebook.com/%D8%AD%D8%A7%D9%84%D8%A7-Halan-2069192519985690/");
                startActivity(intent_fb);
                break;
            case R.id.sn:
                Intent intent_sn = new Intent(UserProfileActivity.this,WebViewActivity.class);
                intent_sn.putExtra("link","https://www.snapchat.com/HalanKSA");
                startActivity(intent_sn);
                break;


        }

    }

    private void sendDataToServer(String id, String photo, String name, String user_name, String email , String phone, final String gender, String age,String city,String country)
    {
        Log.e("id",id);
        Log.e("photo",photo);
        Log.e("name",name);
        Log.e("un",user_name);
        Log.e("email",email);
        Log.e("phone",phone);
        Log.e("gender",gender);
        Log.e("age",age);
        Log.e("city",city);
        Log.e("country",country);

        Services services= Api.getClient(Tags.BASE_URL).create(Services.class);
/*
        Call<UserModel> call=services.UpdateClient(id,user_name,phone,email,photo,name,country,age,city,gender);
*/
        Call<UserModel> call=services.UpdateClient(id,"",user_name,name,phone,email,age,gender,city,country);
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess()==1){

                        Log.e("age2",response.body().getUser_age());
                        Log.e("name",response.body().getName());
                        Log.e("city",response.body().getUser_city());
                        Log.e("country",response.body().getUser_country());

                        preferences.UpdatePref(response.body());
                        users.setUserData(response.body());
                        users.getUserData(UserProfileActivity.this);
                        UpdateUi(response.body());
                        dialog.dismiss();
                     //   Toast.makeText(UserProfileActivity.this, ""+userModel.getUser_name(), Toast.LENGTH_SHORT).show();
                     //   Log.e("name",userModel.getUser_name());
                        Toast.makeText(UserProfileActivity.this, R.string.data_send, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        dialog.dismiss();
                        Toast.makeText(UserProfileActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();

                    }
                }else {
                    dialog.dismiss();
                    Toast.makeText(UserProfileActivity.this, ""+getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                dialog.dismiss();
                Log.e("mmmmm",t.getMessage()+"");
                Toast.makeText(UserProfileActivity.this, ""+getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void sendImageToServer()
    {
        CreateProgressDialog(getString(R.string.upd_photo));

        enCodedImage = enCode(bitmap);

        Services services= Api.getClient(Tags.BASE_URL).create(Services.class);
        Call<UserModel> call=services.UpdateClient(userModel.getUser_id(),userModel.getUser_name(),userModel.getUser_phone(),userModel.getUser_email(),enCodedImage,userModel.getName(),userModel.getUser_country(),userModel.getUser_age(),userModel.getUser_city(),userModel.getUser_gender());
        call.enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess()==1){

                        preferences.UpdatePref(response.body());
                        users.setUserData(response.body());
                        users.getUserData(UserProfileActivity.this);
                        UpdateUi(response.body());

                        // Toast.makeText(UserProfileActivity.this, ""+userModel.getUser_name(), Toast.LENGTH_SHORT).show();
                      //  Log.e("name",userModel.getUser_name());
                        dialog.dismiss();
                        Toast.makeText(UserProfileActivity.this, R.string.data_send, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        dialog.dismiss();

                        Toast.makeText(UserProfileActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();

                    }
                }else {
                    dialog.dismiss();

                    Toast.makeText(UserProfileActivity.this, ""+getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Log.e("mmmmm",t.getMessage()+"");
                dialog.dismiss();

                Toast.makeText(UserProfileActivity.this, ""+getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void updateUserData(String fieldType)
    {
        switch (fieldType)
        {
            case Tags.update_name:
                View view_name = LayoutInflater.from(this).inflate(R.layout.custom_dialog_update_txt,null);
                TextView title_name = view_name.findViewById(R.id.title);
                final EditText et_name = view_name.findViewById(R.id.et);
                et_name.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
                Button name_updateBtn = view_name.findViewById(R.id.updateBtn);
                Button name_cancelBtn = view_name.findViewById(R.id.cancelBtn);
                name_updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(et_name.getText().toString()))
                        {
                            et_name.setError(getString(R.string.enter_name));
                        }else if (txt_name.getText().toString().equals(et_name.getText().toString()))
                        {

                            Toast.makeText(UserProfileActivity.this, R.string.nochange, Toast.LENGTH_LONG).show();
                        }
                        else
                            {
                                CreateProgressDialog(getString(R.string.upd_name));

                                sendDataToServer(userModel.getUser_id(),"",et_name.getText().toString(),txt_user_name.getText().toString(),txt_email.getText().toString(),txt_phone.getText().toString(),my_gender,txt_age.getText().toString(),txt_city.getText().toString(),txt_country.getText().toString());
                                alertDialog.dismiss();
                            }
                    }
                });
                name_cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();

                    }
                });
                title_name.setText(R.string.edit_name);
                alertDialog = new AlertDialog.Builder(this)
                        .setCancelable(true)
                        .setView(view_name)
                        .create();
                alertDialog.show();

                break;
            case Tags.update_username:
                View view_username = LayoutInflater.from(this).inflate(R.layout.custom_dialog_update_txt,null);
                TextView title_username = view_username.findViewById(R.id.title);
                final EditText et_username = view_username.findViewById(R.id.et);
                et_username.setInputType(InputType.TYPE_CLASS_TEXT);
                Button username_updateBtn = view_username.findViewById(R.id.updateBtn);
                Button username_cancelBtn = view_username.findViewById(R.id.cancelBtn);
                username_updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(et_username.getText().toString()))
                        {
                            et_username.setError(getString(R.string.enter_username));
                        }else if (txt_user_name.getText().toString().equals(et_username.getText().toString()))
                        {

                            Toast.makeText(UserProfileActivity.this, R.string.nochange, Toast.LENGTH_LONG).show();
                        }else
                        {
                            CreateProgressDialog(getString(R.string.upd_un));

                            sendDataToServer(userModel.getUser_id(),"",txt_name.getText().toString(),et_username.getText().toString(),txt_email.getText().toString(),txt_phone.getText().toString(),my_gender,txt_age.getText().toString(),txt_city.getText().toString(),txt_country.getText().toString());

                            alertDialog.dismiss();
                        }
                    }
                });
                username_cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();

                    }
                });
                title_username.setText(R.string.edit_un);
                alertDialog = new AlertDialog.Builder(this)
                        .setCancelable(true)
                        .setView(view_username)
                        .create();
                alertDialog.show();
                break;
            case Tags.update_email:
                View view_email = LayoutInflater.from(this).inflate(R.layout.custom_dialog_update_txt,null);
                TextView title_email = view_email.findViewById(R.id.title);
                final EditText et_email = view_email.findViewById(R.id.et);
                et_email.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                Button email_updateBtn = view_email.findViewById(R.id.updateBtn);
                Button email_cancelBtn = view_email.findViewById(R.id.cancelBtn);
                email_updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(et_email.getText().toString()))
                        {
                            et_email.setError(getString(R.string.enter_email));
                        }else if(!Patterns.EMAIL_ADDRESS.matcher(et_email.getText().toString()).matches())
                        {
                            et_email.setError(getString(R.string.inv_email));

                        }else if (txt_email.getText().toString().equals(et_email.getText().toString()))
                        {

                            Toast.makeText(UserProfileActivity.this, R.string.nochange, Toast.LENGTH_LONG).show();
                        }

                        else
                        {
                            CreateProgressDialog(getString(R.string.upd_email));
                            sendDataToServer(userModel.getUser_id(),"",txt_name.getText().toString(),txt_email.getText().toString(),et_email.getText().toString(),txt_phone.getText().toString(),my_gender,txt_age.getText().toString(),txt_city.getText().toString(),txt_country.getText().toString());

                            alertDialog.dismiss();
                        }
                    }
                });
                email_cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();

                    }
                });
                title_email.setText(R.string.edit_email);
                alertDialog = new AlertDialog.Builder(this)
                        .setCancelable(true)
                        .setView(view_email)
                        .create();
                alertDialog.show();
                break;
            case Tags.update_phone:
                View view_phone = LayoutInflater.from(this).inflate(R.layout.custom_dialog_phone,null);
                TextView title_phone = view_phone.findViewById(R.id.title);
                final PhoneInputLayout et_phone = view_phone.findViewById(R.id.phone);
                Button phone_updateBtn = view_phone.findViewById(R.id.updateBtn);
                Button phone_cancelBtn = view_phone.findViewById(R.id.cancelBtn);
                phone_updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(et_phone.getPhoneNumber()))
                        {
                            et_phone.getTextInputLayout().getEditText().setError(getString(R.string.enter_phone));
                        }else if (!et_phone.isValid())
                        {
                            et_phone.getTextInputLayout().getEditText().setError(getString(R.string.inv_phone));

                        }else if (txt_phone.getText().toString().equals(et_phone.getPhoneNumber()))
                        {

                            Toast.makeText(UserProfileActivity.this, R.string.nochange, Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            CreateProgressDialog(getString(R.string.upd_phone));
                            sendDataToServer(userModel.getUser_id(),"",txt_name.getText().toString(),txt_email.getText().toString(),txt_email.getText().toString(),et_phone.getPhoneNumber(),my_gender,txt_age.getText().toString(),txt_city.getText().toString(),txt_country.getText().toString());

                            alertDialog.dismiss();
                        }
                    }
                });
                phone_cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();

                    }
                });
                title_phone.setText(R.string.edit_phone);
                alertDialog = new AlertDialog.Builder(this)
                        .setCancelable(true)
                        .setView(view_phone)
                        .create();
                alertDialog.show();
                break;
            case Tags.update_age_:
                View view_age = LayoutInflater.from(this).inflate(R.layout.custom_dialog_update_txt,null);
                TextView title_age = view_age.findViewById(R.id.title);
                final EditText et_age = view_age.findViewById(R.id.et);
                et_age.setInputType(InputType.TYPE_CLASS_TEXT);

                Button age_updateBtn = view_age.findViewById(R.id.updateBtn);
                Button age_cancelBtn = view_age.findViewById(R.id.cancelBtn);
                age_updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(et_age.getText().toString()))
                        {
                            et_age.setError(getString(R.string.enter_age));
                        }else if (txt_age.getText().toString().equals(et_age.getText().toString()))
                        {

                            Toast.makeText(UserProfileActivity.this, R.string.nochange, Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            CreateProgressDialog(getString(R.string.upd_age));
                            Log.e("age",et_age.getText().toString());
                            sendDataToServer(userModel.getUser_id(),"",userModel.getName(),userModel.getUser_name(),userModel.getUser_email(),userModel.getUser_phone(),userModel.getUser_gender(),et_age.getText().toString(),userModel.getUser_city(),userModel.getUser_country());
                            alertDialog.dismiss();
                        }
                    }
                });
                age_cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();

                    }
                });
                title_age.setText(R.string.edit_age);
                alertDialog = new AlertDialog.Builder(this)
                        .setCancelable(true)
                        .setView(view_age)
                        .create();
                alertDialog.show();
                break;
            case Tags.update_gender_:
                View view_gender = LayoutInflater.from(this).inflate(R.layout.custom_dialog_rb,null);

                RadioButton mail = view_gender.findViewById(R.id.maleBtn);
                RadioButton female =view_gender.findViewById(R.id.femaleBtn);
                TextView gender_title = view_gender.findViewById(R.id.title);

                if (mail.isChecked())
                {
                    gender = Tags.gender_male;
                    Log.e("gender2",gender);

                }else if (female.isChecked())
                {
                    gender = Tags.gender_female;
                    Log.e("gender2",gender);

                }

                mail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gender = Tags.gender_male;
                        Log.e("gender2",gender);


                    }
                });
                female.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gender = Tags.gender_female;
                        Log.e("gender2",gender);


                    }
                });
                Button gender_updateBtn = view_gender.findViewById(R.id.updateBtn);
                Button gender_cancelBtn = view_gender.findViewById(R.id.cancelBtn);
                gender_updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("gender1",userModel.getUser_gender());
                        Log.e("gender2",gender);
                        if (txt_gender.getText().toString().equals(getString(R.string.male)))
                        {
                            curr_gender = Tags.gender_male;
                        }else if (txt_gender.getText().toString().equals(getString(R.string.female)))
                        {
                            curr_gender = Tags.gender_female;

                        }
                        if (curr_gender.equals(gender))
                        {

                            Toast.makeText(UserProfileActivity.this, R.string.nochange, Toast.LENGTH_LONG).show();

                        }else
                            {
                                CreateProgressDialog(getString(R.string.upd_gender));

                                sendDataToServer(userModel.getUser_id(),"",txt_name.getText().toString(),txt_email.getText().toString(),txt_email.getText().toString(),txt_phone.getText().toString(),gender,txt_age.getText().toString(),txt_city.getText().toString(),txt_country.getText().toString());

                                alertDialog.dismiss();
                            }

                    }
                });
                gender_cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();

                    }
                });
                gender_title.setText(R.string.edit_gender);
                alertDialog = new AlertDialog.Builder(this)
                        .setCancelable(true)
                        .setView(view_gender)
                        .create();
                alertDialog.show();
                break;
            case Tags.update_city:
                View view_city = LayoutInflater.from(this).inflate(R.layout.custom_dialog_update_txt,null);
                TextView title_city = view_city.findViewById(R.id.title);
                final EditText et_city = view_city.findViewById(R.id.et);
                et_city.setInputType(InputType.TYPE_CLASS_TEXT);
                Button city_updateBtn = view_city.findViewById(R.id.updateBtn);
                Button city_cancelBtn = view_city.findViewById(R.id.cancelBtn);
                city_updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(et_city.getText().toString()))
                        {
                            et_city.setError(getString(R.string.enter_city));
                        }else if (txt_city.getText().toString().equals(et_city.getText().toString()))
                        {

                            Toast.makeText(UserProfileActivity.this, R.string.nochange, Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            CreateProgressDialog(getString(R.string.upd_city));
                            sendDataToServer(userModel.getUser_id(),"",txt_name.getText().toString(),txt_email.getText().toString(),txt_email.getText().toString(),txt_phone.getText().toString(),my_gender,txt_age.getText().toString(),et_city.getText().toString(),txt_country.getText().toString());

                            alertDialog.dismiss();
                        }
                    }
                });
                city_cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();

                    }
                });
                title_city.setText(R.string.edit_city);
                alertDialog = new AlertDialog.Builder(this)
                        .setCancelable(true)
                        .setView(view_city)
                        .create();
                alertDialog.show();

                break;
            case Tags.update_country:
                View view_country = LayoutInflater.from(this).inflate(R.layout.custom_dialog_update_txt,null);
                TextView title_country = view_country.findViewById(R.id.title);
                final EditText et_country = view_country.findViewById(R.id.et);
                et_country.setInputType(InputType.TYPE_CLASS_TEXT);
                Button country_updateBtn = view_country.findViewById(R.id.updateBtn);
                Button country_cancelBtn = view_country.findViewById(R.id.cancelBtn);
                country_updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (TextUtils.isEmpty(et_country.getText().toString()))
                        {
                            et_country.setError(getString(R.string.enter_country));
                        }else if (txt_country.getText().toString().equals(et_country.getText().toString()))
                        {

                            Toast.makeText(UserProfileActivity.this, R.string.nochange, Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            CreateProgressDialog(getString(R.string.upd_count));
                            sendDataToServer(userModel.getUser_id(),"",txt_name.getText().toString(),txt_email.getText().toString(),txt_email.getText().toString(),txt_phone.getText().toString(),my_gender,txt_age.getText().toString(),txt_city.getText().toString(),et_country.getText().toString());

                            alertDialog.dismiss();
                        }
                    }
                });
                country_cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();

                    }
                });
                title_country.setText(R.string.edit_country);
                alertDialog = new AlertDialog.Builder(this)
                        .setCancelable(true)
                        .setView(view_country)
                        .create();
                alertDialog.show();

                break;
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMG_REQ && resultCode == RESULT_OK && data!=null)
        {
            Uri uri = data.getData();
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                img_profile.setImageBitmap(bitmap);
               //enCode(bitmap);
               sendImageToServer();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    private String enCode(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,90,outputStream);
        byte [] bytes = outputStream.toByteArray();
        return Base64.encodeToString(bytes,Base64.DEFAULT);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
