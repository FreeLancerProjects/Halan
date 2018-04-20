package com.semicolon.Halan.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.iid.FirebaseInstanceId;
import com.semicolon.Halan.Adapters.ViewPagerAdapter;
import com.semicolon.Halan.Fragments.CancelledOrdersFragment;
import com.semicolon.Halan.Fragments.CurrentOrdersFragment;
import com.semicolon.Halan.Fragments.PreviousOrdersFragment;
import com.semicolon.Halan.Models.LocationUpdateModel;
import com.semicolon.Halan.Models.ResponseModel;
import com.semicolon.Halan.Models.TokenModel;
import com.semicolon.Halan.Models.UserModel;
import com.semicolon.Halan.R;
import com.semicolon.Halan.Services.Api;
import com.semicolon.Halan.Services.Preferences;
import com.semicolon.Halan.Services.Services;
import com.semicolon.Halan.Services.Tags;
import com.semicolon.Halan.Services.UpdateDriver_Location;
import com.semicolon.Halan.SingleTone.Users;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import de.hdodenhof.circleimageview.CircleImageView;
import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DriverOrdersActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,Users.UserData{

    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Users users;
    private UserModel userModel;
    private Preferences preferences;
    private AlertDialog.Builder builder,builder2;
    private Target target;
    private CircleImageView userImage;
    private LinearLayout profileContainer;
    private Intent service_intent;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_orders);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "JannaLT-Regular.ttf", true);
        initView();
        EventBus.getDefault().register(this);
        users = Users.getInstance();
        users.getUserData(this);
        preferences = new Preferences(getApplicationContext());
        service_intent = new Intent(this, UpdateDriver_Location.class);
        startService(service_intent);
        UpdateToken();
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        users.getUserData(this);

    }
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        startService(service_intent);

    }
    private void initView()
    {
        toolbar=  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer =  findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        viewPager =  findViewById(R.id.viewpager);
        tabLayout =  findViewById(R.id.tabs);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        ///////////////////////////////////////////////////////
        View view = navigationView.getHeaderView(0);
        userImage = view.findViewById(R.id.userImage);
        profileContainer = view.findViewById(R.id.profileContainer);
        profileContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(DriverOrdersActivity.this,UserProfileActivity.class);
                startActivity(intent);
            }
        });
        ////////////////////////////////////////////////////////

    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CurrentOrdersFragment(), getString(R.string.CurrentOrders));
        adapter.addFragment(new PreviousOrdersFragment(), getString(R.string.PreviousOrders));
        adapter.addFragment(new CancelledOrdersFragment(), getString(R.string.CancelledOrders));
        viewPager.setAdapter(adapter);
    }
    private void CreateAlertDialog()
    {
        builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.logout));
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Logout();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AlertDialog dialog = builder.create();
                dialog.dismiss();
            }
        });

    }
    private void CreateAlertDialog2(String msg)
    {
        builder2 = new AlertDialog.Builder(this);
        builder2.setMessage(msg);
        builder2.setCancelable(false);
        builder2.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AlertDialog dialog = builder2.create();
                dialog.dismiss();
            }
        });


    }
    private void UpdateUI(UserModel userModel)
    {

        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                try {

                    userImage.setImageBitmap(bitmap);

                }catch (NullPointerException e)
                {
                    Log.e("home error",e.getMessage());
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        if (userModel.getUser_photo() != null || !userModel.getUser_photo().equals("0") || TextUtils.isEmpty(userModel.getUser_photo())) {
            Picasso.with(this).load(Uri.parse(Tags.ImgPath + userModel.getUser_photo())).placeholder(R.drawable.user_profile).into(target);
        }
    }
    private void UpdateToken()
    {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("user",MODE_PRIVATE);
        final String user_id = preferences.getString("user_id","");
        final String token   = FirebaseInstanceId.getInstance().getToken();
        if (!TextUtils.isEmpty(token)||token!=null)
        {
            Retrofit retrofit = Api.getClient(Tags.BASE_URL);
            Services services = retrofit.create(Services.class);
            Call<ResponseModel> call = services.Update_token(user_id, token);
            call.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    if (response.isSuccessful())
                    {
                        if (response.body().getSuccess()==1)
                        {
                            Log.e("updated","token updated successfully");
                        }else
                        {
                            Log.e("failed","token updated failed");

                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {
                    Log.e("Error",t.getMessage());

                }
            });
        }


    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Token_Refereshed(TokenModel tokenModel)
    {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("user",MODE_PRIVATE);
        final String user_id = preferences.getString("user_id","");
        final String token   = tokenModel.getToken();
        String session = preferences.getString("session","");

        if (!TextUtils.isEmpty(session)||session!=null)
        {
            if (session.equals(Tags.login))
            {
                Retrofit retrofit = Api.getClient(Tags.BASE_URL);
                Services services = retrofit.create(Services.class);
                Call<ResponseModel> call = services.Update_token(user_id, token);
                call.enqueue(new Callback<ResponseModel>() {
                    @Override
                    public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                        if (response.isSuccessful())
                        {
                            if (response.body().getSuccess()==1)
                            {
                                Log.e("updated","token updated successfully");
                            }else
                            {
                                Log.e("failed","token updated failed");

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel> call, Throwable t) {
                        Log.e("Error",t.getMessage());

                    }
                });
            }
        }


    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void UpdateDriverLocation(LocationUpdateModel locationUpdateModel)
    {
        Log.e("lat",""+locationUpdateModel.getLat());
        UpdateLocation(locationUpdateModel);
    }
    private void UpdateLocation(LocationUpdateModel locationUpdateModel)
    {
        String lat = String.valueOf(locationUpdateModel.getLat());
        String lng = String.valueOf(locationUpdateModel.getLng());

        Retrofit retrofit = Api.getClient(Tags.BASE_URL);
        Services services = retrofit.create(Services.class);
        Call<ResponseModel> call = services.UpdateDriver_Locaion(userModel.getUser_id(), lat, lng);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess()==1)
                    {
                        Log.e("latlng","LatLng Updated successfully");
                    }else
                    {
                        Log.e("latlng","Failed");

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e("Error",t.getMessage());
            }
        });
    }
    private void Logout()
    {
        preferences.ClearPref();
        Intent intent = new Intent(DriverOrdersActivity.this,Activity_Client_Login.class);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        switch (id) {
            case R.id.home:
                break;
            case R.id.register:
                CreateAlertDialog2(getString(R.string.already_driver));
                builder2.show();
               /* if (userModel.getUser_type().equals(Tags.Client)) {
                    Intent intent = new Intent(this, Activity_Driver_Register.class);
                    startActivity(intent);
                } else {

                }
*/
                break;
            case R.id.logout:
                CreateAlertDialog();

                builder.show();
                break;
            case R.id.contact:
                Intent intent1 = new Intent(this, ContactUsActivity.class);
                startActivity(intent1);
                break;
            case R.id.pay:
                Intent intent2 = new Intent(this, PayActivity.class);
                startActivity(intent2);
                break;
            case R.id.notification:
                Intent intent = new Intent(this,DriverNotificationActivity.class);
                startActivity(intent);
                break;
            case R.id.rule:
                Intent intent3 = new Intent(this,RulesActivity.class);
                startActivity(intent3);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void UserDataSuccess(UserModel userModel)
    {
        this.userModel = userModel;
        UpdateUI(userModel);
    }
    @Override
    public void onBackPressed()
    {
        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
