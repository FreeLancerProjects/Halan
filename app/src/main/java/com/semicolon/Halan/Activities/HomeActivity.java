package com.semicolon.Halan.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.maps.android.PolyUtil;
import com.semicolon.Halan.Adapters.PlaceAutocompleteAdapter;
import com.semicolon.Halan.Models.AvailableDriversModel;
import com.semicolon.Halan.Models.ClientLastOrderModel;
import com.semicolon.Halan.Models.DriverAcceptModel;
import com.semicolon.Halan.Models.Finishied_Order_Model;
import com.semicolon.Halan.Models.PlaceModel;
import com.semicolon.Halan.Models.ResponseModel;
import com.semicolon.Halan.Models.TokenModel;
import com.semicolon.Halan.Models.TotalCostModel;
import com.semicolon.Halan.Models.UserModel;
import com.semicolon.Halan.R;
import com.semicolon.Halan.Services.Api;
import com.semicolon.Halan.Services.NetworkConnection;
import com.semicolon.Halan.Services.Preferences;
import com.semicolon.Halan.Services.Services;
import com.semicolon.Halan.Services.Tags;
import com.semicolon.Halan.SingleTone.Users;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import it.beppi.tristatetogglebutton_library.TriStateToggleButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, Users.UserData, OnMapReadyCallback ,GoogleApiClient.OnConnectionFailedListener{

    private AutoCompleteTextView search_view;
    private NavigationView nav_view;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private UserModel userModel;
    private CircleImageView userImage;
    private Target target;
    private GoogleMap mMap;
    private boolean permission_granted = false;
    private String fine_location = Manifest.permission.ACCESS_FINE_LOCATION;
    private String coarse_location = Manifest.permission.ACCESS_COARSE_LOCATION;
    private final int ErrorDialog = 9001;
    private final int per_req = 1200;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LatLngBounds latLngBounds = new LatLngBounds(
            new LatLng(-33.880490, 151.184363),
            new LatLng(-33.858754, 151.229596));
    private PlaceAutocompleteAdapter adapter;
    private GoogleApiClient apiClient;
    private LatLng mylatLng,latLng;
    private FrameLayout costContainer;
    private LinearLayout locContainer;
    private RelativeLayout search_container;
    private Button nextBtn,sendBtn;
    private EditText txt_order;
    private TextView txt_order_from,txt_order_to,cost,distance;
    private double dist;
    private Preferences preferences;
    private AlertDialog.Builder builder,builder2,gpsBuilder;
    private Users users;
    private LinearLayout profileContainer;
    private ProgressDialog dialog;
    private List<String> drivers_ids;
    private String from,to;
    private String distn,order_details;
    private AlertDialog notalertDialog;
    private NetworkConnection connection;
    private TriStateToggleButton toggleBtn;
    private String provider;
    private LocationManager locationManager;
    private final int GPS_REQ=21258;
    private CardView not_data_cardview;
    private LinearLayout bottom_sheet;
    private BottomSheetBehavior behavior;
    private RatingBar not_rateBar,not_rateBar_details;
    private CircleImageView not_driver_img,not_driver_img_details;
    private TextView not_driver_name,not_driver_name_details,not_time,not_time_details,not_driver_rate,not_driver_rate_details,not_order_details,not_date_details,not_cost_details;
    private Button not_accept_btn,not_refuse_btn;
    private ClientLastOrderModel clientLastOrderModel;
    private ProgressDialog dialog_logout;
    private String country_code="sa";
    private AutocompleteFilter filter=null;
    //private CheckBox checkBox;
    private final int NEARBY_REQ=8525;
    private Button nearbyBtn;
    private String myLocality="";
    private NotificationManager manager;
    private TextView alert_txt;
    private static boolean isDeleted = false;
    private LinearLayout l_more,l_myorder,l_notification,l_me;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
       /* Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "JannaLT-Regular.ttf", true);*/
        connection = NetworkConnection.getInstance();
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        preferences = new Preferences(this);
        users = Users.getInstance();
        users.getUserData(this);
        initView();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (userModel!=null)
        {
            EventBus.getDefault().register(this);
            UpdateToken();
        }

        OpenGps();
        CreateProgressDialog();


    }

    private void OpenGps() {
        provider = LocationManager.GPS_PROVIDER;
        locationManager= (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(provider))
        {
            gpsBuilder = new AlertDialog.Builder(this);
            gpsBuilder.setTitle("Gps");
            gpsBuilder.setMessage("قم بفتح gps لتتمكن من إستخدام التطبيق....");
            gpsBuilder.setCancelable(false);
            gpsBuilder.setPositiveButton("Gps Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    AlertDialog dialog = gpsBuilder.create();
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    dialog.dismiss();
                    startActivityForResult(intent,GPS_REQ);
                }
            });

            gpsBuilder.show();
        }else
            {
                if (IsServicesOk()) {
                    checkPermission();
                }
            }
    }

    private void  initFilter(String country_code)
    {
        GeoDataClient geoDataClient = Places.getGeoDataClient(this,null);

        filter = new AutocompleteFilter.Builder()
                .setCountry(country_code)
                .build();
        if (connection.getConnection(this))
        {

            adapter = new PlaceAutocompleteAdapter(this,geoDataClient,latLngBounds,filter);
            search_view.setAdapter(adapter);

        }else
        {
            Toast.makeText(this, R.string.connectiom, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPS_REQ )
        {
            if (IsServicesOk()) {
                checkPermission();
            }
        }else if (requestCode==NEARBY_REQ)
        {
            if (resultCode==RESULT_OK && data !=null)
            {
                try {
                    search_view.setText(null);
                    double lat = data.getDoubleExtra("lat",0.0);
                    double lng = data.getDoubleExtra("lng",0.0);
                    String place_name = data.getStringExtra("name");
                    latLng = new LatLng(lat,lng);
                    from = place_name;
                    txt_order_from.setText(from);
                    Geocoder geocoder = new Geocoder(HomeActivity.this);
                    List<Address> addressList = null;

                    addressList = geocoder.getFromLocation(mylatLng.latitude,mylatLng.longitude,1);

                    if (addressList.size()>0)
                    {
                        to = addressList.get(0).getAddressLine(0);
                        txt_order_to.setText(to);
                    }
                    CreateProgDialog(getString(R.string.locating));

                    dialog.show();

                    getDirection(mylatLng,latLng);
                    //dist = distance(mylatLng.latitude,mylatLng.longitude,latLng.latitude,latLng.longitude);

                } catch (IOException e) {
                    e.printStackTrace();
                }catch (NullPointerException e){}

            }else
                {

                }

        }
    }

    private void CreateNotAlertDialog()
    {
        notalertDialog = new AlertDialog.Builder(this)
                .setMessage(R.string.request_accepted)
                .setPositiveButton(R.string.accepttxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Logout();
                        notalertDialog.dismiss();
                    }
                })
                .setCancelable(false)
                .create();

    }
    private void CreateProgressDialog()
    {
        ProgressBar bar = new ProgressBar(this);
        Drawable drawable = bar.getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        dialog_logout = new ProgressDialog(this);
        dialog_logout.setIndeterminate(true);
        dialog_logout.setMessage(getString(R.string.log_out));
        dialog_logout.setCancelable(true);
        dialog_logout.setCanceledOnTouchOutside(false);
        dialog_logout.setIndeterminateDrawable(drawable);
    }
    @Override
    protected void onStart()
    {
        super.onStart();

        users = Users.getInstance();
        users.getUserData(this);

        if (userModel!=null)
        {
            try {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getLastOrder(userModel.getUser_id());

                    }
                },1500);
            }catch (NullPointerException e)
            {}
        }







    }

    @Override
    protected void onResume() {
        super.onResume();
        if (userModel!=null)
        {
            String user_state = getSharedPreferences("user",MODE_PRIVATE).getString("state","");
            if (user_state.equals(Tags.Driver))
            {
                try
                {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            CreateNotAlertDialog();
                            notalertDialog.show();

                        }
                    },500);
                }catch (NullPointerException e)
                {

                }

            }

        }

    }

    @Override
    public void onBackPressed()
    {
        int vis1=locContainer.getVisibility();
        int vis2=costContainer.getVisibility();
        DrawerLayout drawer =findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (vis1==0){
            locContainer.setVisibility(View.GONE);
            search_view.setText("");
            mMap.clear();
            AddMarker(mylatLng,"");

        }else if (vis2==0)
        {
            costContainer.setVisibility(View.GONE);
            locContainer.setVisibility(View.VISIBLE);
        }else if (vis1==8)
        {

            super.onBackPressed();

        }
        else
        {
            super.onBackPressed();



        }
    }
    private void initView()
    {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        nav_view = findViewById(R.id.nav_view);

        ///////////////////////client///////////////////////////////////

        not_data_cardview = findViewById(R.id.not_data_cardview);
        not_data_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                not_data_cardview.setVisibility(View.GONE);
                manager.cancel(1995);
            }
        });
        ///////////////////////////////////////////////////////////////
        bottom_sheet = findViewById(R.id.bottom_sheet);
        bottom_sheet.setEnabled(false);
        not_rateBar = findViewById(R.id.not_rateBar);
        LayerDrawable drawable = (LayerDrawable) not_rateBar.getProgressDrawable();
        drawable.getDrawable(0).setColorFilter(ContextCompat.getColor(this,R.color.gray3), PorterDuff.Mode.SRC_ATOP);

        drawable.getDrawable(1).setColorFilter(ContextCompat.getColor(this,R.color.rate), PorterDuff.Mode.SRC_ATOP);
        drawable.getDrawable(2).setColorFilter(ContextCompat.getColor(this,R.color.rate), PorterDuff.Mode.SRC_ATOP);
        behavior = BottomSheetBehavior.from(bottom_sheet);

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState)
                {
                    case BottomSheetBehavior.STATE_EXPANDED:
                       // not_data_cardview.setVisibility(View.GONE);
                        manager.cancel(1995);
                        break;
                     case BottomSheetBehavior.STATE_HIDDEN:

                         //not_data_cardview.setVisibility(View.VISIBLE);
                         break;
                     case BottomSheetBehavior.STATE_DRAGGING:
                         behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        // not_data_cardview.setVisibility(View.VISIBLE);
                         manager.cancel(1995);

                         break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        alert_txt = findViewById(R.id.alert_txt);
        not_rateBar_details = findViewById(R.id.not_rateBar_detail);
        LayerDrawable drawable2 = (LayerDrawable) not_rateBar_details.getProgressDrawable();
        drawable2.getDrawable(0).setColorFilter(ContextCompat.getColor(this,R.color.gray3), PorterDuff.Mode.SRC_ATOP);

        drawable2.getDrawable(1).setColorFilter(ContextCompat.getColor(this,R.color.rate), PorterDuff.Mode.SRC_ATOP);
        drawable2.getDrawable(2).setColorFilter(ContextCompat.getColor(this,R.color.rate), PorterDuff.Mode.SRC_ATOP);

        not_driver_img = findViewById(R.id.not_driver_img);
        not_driver_img_details = findViewById(R.id.not_driver_img_detail);
        not_driver_name= findViewById(R.id.not_driver_name);
        not_driver_name_details = findViewById(R.id.not_driver_name_detail);
        not_time = findViewById(R.id.not_time);
        not_time_details = findViewById(R.id.not_time_details);
        not_driver_rate = findViewById(R.id.not_driver_rate);
        not_driver_rate_details = findViewById(R.id.not_driver_rate_detail);
        not_order_details = findViewById(R.id.not_order_details);
        not_date_details = findViewById(R.id.not_date_details);
        not_cost_details = findViewById(R.id.not_cost_details);
        not_accept_btn = findViewById(R.id.not_accept);
        not_refuse_btn = findViewById(R.id.not_refuse);
        l_more = findViewById(R.id.more);
        l_myorder = findViewById(R.id.myorder);
        l_notification = findViewById(R.id.notification);
        l_me = findViewById(R.id.me);


        l_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this,AppContactsActivity.class);
                startActivity(intent);

            }
        });

        l_myorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userModel!=null)
                {
                    Intent intent2=new Intent(HomeActivity.this,MyOrdersActivity.class);
                    startActivity(intent2);
                }else
                    {
                        CreateAlertDialogUserLoginOrRegister(getString(R.string.pl_lgn));
                    }

            }
        });
        l_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userModel!=null)
                {
                    if (userModel.getUser_type().equals(Tags.Client))
                    {
                        Intent intent = new Intent(HomeActivity.this,ClientNotificationActivity.class);
                        startActivity(intent);
                    }else
                    {
                        Intent intent = new Intent(HomeActivity.this,DriverNotificationActivity.class);
                        startActivity(intent);
                    }
                }else
                    {
                        CreateAlertDialogUserLoginOrRegister(getString(R.string.pl_lgn));

                    }


            }
        });
        l_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userModel!=null)
                {
                    Intent intent = new Intent(HomeActivity.this,MyAccountActivity.class);
                    startActivity(intent);
                }else
                    {
                        CreateAlertDialogUserLoginOrRegister(getString(R.string.pl_lgn));

                    }

            }
        });


        not_accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userModel!=null)
                {
                    getLastOrder2(userModel.getUser_id(),"accept");

                }
            }
        });

        not_refuse_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userModel!=null)
                {
                    getLastOrder2(userModel.getUser_id(),"refuse");
                    Log.e("isdeleted2",isDeleted+"");
                }


            }
        });


        ////////////////////////////////////////////////////////////////
        search_container = findViewById(R.id.search_container);
        costContainer = findViewById(R.id.costContainer);
        locContainer  = findViewById(R.id.locContainer);
        txt_order     = findViewById(R.id.txt_order);
        txt_order_from= findViewById(R.id.txt_order_from);
        txt_order_to  = findViewById(R.id.txt_order_to);
        cost          = findViewById(R.id.cost);
        distance      = findViewById(R.id.distance);
        nextBtn       = findViewById(R.id.nextBtn);
        sendBtn       = findViewById(R.id.sendBtn);
        locContainer.setVisibility(View.GONE);
        costContainer.setVisibility(View.GONE);

        /*notf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userModel.getUser_type().equals(Tags.Client))
                {
                    Intent intent = new Intent(HomeActivity.this,ClientNotificationActivity.class);
                    startActivity(intent);
                }else
                {
                    Intent intent = new Intent(HomeActivity.this,DriverNotificationActivity.class);
                    startActivity(intent);
                }
            }
        });*/
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (TextUtils.isEmpty(txt_order.getText().toString()))
                {
                    txt_order.setError(getString(R.string.ent_ored_det));
                }else
                    {


                        String[] dis = String.valueOf(dist).split(" ");
                        distn = String.valueOf(Math.round(Double.parseDouble(dis[0])));
                        distance.setText(distn+" "+getString(R.string.km));
                        order_details = txt_order.getText().toString();
                        CreateProgDialog(getString(R.string.detr_cost_road));

                        dialog.show();
                        getCostByDistance(distn);

                    }


            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (userModel!=null)
                {
                    Log.e("from",from);
                    Log.e("to",to);
                    Log.e("my",mylatLng.latitude+"");
                    Log.e("to lat",latLng.latitude+"");
                    Log.e("ids",drivers_ids.size()+"");
                    Log.e("dis",distn);
                    Log.e("id",userModel.getUser_id());
                    Log.e("details",order_details);
                    Log.e("Cost",cost.getText().toString());

                    Map<String,String> map =new HashMap<>();
                    map.put("user_id",userModel.getUser_id());
                    map.put("client_location",to);
                    map.put("market_location",from);
                    map.put("client_google_lat",String.valueOf(mylatLng.latitude));
                    map.put("client_google_lang",String.valueOf(mylatLng.longitude));
                    map.put("market_google_lat",String.valueOf(latLng.latitude));
                    map.put("market_google_lang",String.valueOf(latLng.longitude));
                    map.put("distance",distn);
                    map.put("order_details",order_details);
                    map.put("total_cost",cost.getText().toString());

                    sendOrders(map,drivers_ids);

                }else
                    {
                        CreateAlertDialogUserLoginOrRegister(getString(R.string.pl_lgn));

                    }








            }
        });

        //////////////////////////////////////////////////////////
        View view = nav_view.getHeaderView(0);
        profileContainer = view.findViewById(R.id.profileContainer);
        userImage = view.findViewById(R.id.userImage);
        toggleBtn = view.findViewById(R.id.toggleBtn);

        String state = preferences.getSoundState();

        if (state.equals(""))
        {
            preferences.UpdateSoundPref("on");
        }

        String state2 = preferences.getSoundState();

        if (state2.equals("on"))
        {
            toggleBtn.setToggleOn();
        }else
        {
            toggleBtn.setToggleOff();
        }

        toggleBtn.setOnToggleChanged(new TriStateToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(TriStateToggleButton.ToggleStatus toggleStatus, boolean b, int i) {
                if (toggleStatus== TriStateToggleButton.ToggleStatus.on)
                {
                    preferences.UpdateSoundPref("on");
                    Toast.makeText(HomeActivity.this, "Sound on", Toast.LENGTH_SHORT).show();

                }else if (toggleStatus == TriStateToggleButton.ToggleStatus.off)
                {
                    preferences.UpdateSoundPref("off");
                    Toast.makeText(HomeActivity.this, "Sound off", Toast.LENGTH_SHORT).show();


                }
            }
        });
        nav_view.setNavigationItemSelectedListener(this);

        profileContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userModel!=null)
                {
                    Intent intent=new Intent(HomeActivity.this,UserProfileActivity.class);
                    startActivity(intent);
                    drawer.closeDrawer(GravityCompat.START);
                }else
                    {
                        CreateAlertDialogUserLoginOrRegister(getString(R.string.pl_lgn));

                    }


            }
        });
        search_view = findViewById(R.id.search);
        search_view.setOnItemClickListener(itemClickListener);
        nearbyBtn = findViewById(R.id.nearbyBtn);
        initFilter(country_code);

        search_view.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i== EditorInfo.IME_ACTION_DONE
                        ||i==EditorInfo.IME_ACTION_SEARCH
                        ||keyEvent.getAction()==KeyEvent.ACTION_DOWN
                        ||keyEvent.getAction()==KeyEvent.KEYCODE_ENTER)

                {

                }
                return false;
            }
        });

        nearbyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userModel!=null)
                {
                    if (mylatLng != null) {
                        if (TextUtils.isEmpty(search_view.getText().toString())) {
                            Toast.makeText(HomeActivity.this, R.string.enter_stroe, Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(HomeActivity.this, NearbyPlacesActivity.class);
                            intent.putExtra("query", search_view.getText().toString());
                            intent.putExtra("lat", mylatLng.latitude);
                            intent.putExtra("lng", mylatLng.longitude);
                            startActivityForResult(intent, NEARBY_REQ);
                        }

                    }
                }else
                    {
                        CreateAlertDialogUserLoginOrRegister(getString(R.string.pl_lgn));

                    }

                //Toast.makeText(HomeActivity.this, ""+search_view.getText().toString(), Toast.LENGTH_SHORT).show();


            }

        });



        apiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this,this)
                .build();


    }

    private void Refuse_order(ClientLastOrderModel clientLastOrderModel) {
        Map<String,String> map = new HashMap<>();
        map.put("action","2");
        map.put("message_id",clientLastOrderModel.getMessage_id());
        map.put("order_id_fk",clientLastOrderModel.getOrder_id());
        map.put("driver_id_fk",clientLastOrderModel.getDriver_id_fk());
        map.put("user_id",userModel.getUser_id());

        Retrofit retrofit = Api.getClient(Tags.BASE_URL);
        Services services = retrofit.create(Services.class);
        Call<ResponseModel> call = services.sendClientRequest_Refuse(map);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess()==1)
                    {
                        Toast.makeText(HomeActivity.this, R.string.respons_send_todriver, Toast.LENGTH_LONG).show();
                        not_data_cardview.setVisibility(View.GONE);
                        bottom_sheet.setVisibility(View.GONE);
                        search_view.setEnabled(true);
                        nearbyBtn.setEnabled(true);
                        alert_txt.setVisibility(View.INVISIBLE);

                    }else
                    {
                        not_accept_btn.setEnabled(true);
                        not_refuse_btn.setEnabled(true);
                        Toast.makeText(HomeActivity.this, R.string.respons_not_send, Toast.LENGTH_LONG).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                not_accept_btn.setEnabled(true);
                not_refuse_btn.setEnabled(true);
                Log.e("Error",t.getMessage());
                Toast.makeText(HomeActivity.this, getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void Accept_order(ClientLastOrderModel clientLastOrderModel) {
        Map<String,String> map = new HashMap<>();
        map.put("action","1");
        map.put("message_id",clientLastOrderModel.getMessage_id());
        map.put("order_id_fk",clientLastOrderModel.getOrder_id());
        map.put("driver_id_fk",clientLastOrderModel.getDriver_id_fk());
        map.put("user_id",userModel.getUser_id());

        Retrofit retrofit = Api.getClient(Tags.BASE_URL);
        Services services = retrofit.create(Services.class);
        Call<ResponseModel> call = services.sendClientRequest_Accept(map);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful())
                {
                    Log.e("response",response.body().getMessage()+"");

                    if (response.body().getSuccess()==1)
                    {
                        Log.e("response",response.body().getMessage()+"");
                        Toast.makeText(HomeActivity.this, R.string.respons_send_todriver, Toast.LENGTH_LONG).show();
                        not_data_cardview.setVisibility(View.GONE);
                        bottom_sheet.setVisibility(View.GONE);
                        search_view.setEnabled(true);
                        nearbyBtn.setEnabled(true);
                        alert_txt.setVisibility(View.INVISIBLE);
                        String room_id = response.body().getRoom_id();
                        CreateChat(room_id);
                        //finish();
                    }else
                    {
                        not_accept_btn.setEnabled(true);
                        not_refuse_btn.setEnabled(true);
                        Toast.makeText(HomeActivity.this, R.string.respons_not_send, Toast.LENGTH_LONG).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e("Error",t.getMessage());
                not_accept_btn.setEnabled(true);
                not_refuse_btn.setEnabled(true);
                Toast.makeText(HomeActivity.this, getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void CreateChat(String room_id) {
        Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
        intent.putExtra("curr_id", userModel.getUser_id());
        intent.putExtra("chat_id", clientLastOrderModel.getDriver_id_fk());
        intent.putExtra("curr_type", userModel.getUser_type());
        intent.putExtra("chat_type", Tags.Driver);
        intent.putExtra("curr_photo", userModel.getUser_photo());
        intent.putExtra("chat_photo", clientLastOrderModel.getDriver_image());
        intent.putExtra("order_id", clientLastOrderModel.getOrder_id());
        intent.putExtra("room_id",room_id);
        startActivity(intent);
/*
        dRef.child("typing").child(userModel.getUser_id()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.hasChild(clientLastOrderModel.getDriver_id_fk())) {
                    String path1 = "/" + userModel.getUser_id() + "/" + clientLastOrderModel.getDriver_id_fk();
                    String path2 = "/" + clientLastOrderModel.getDriver_id_fk() + "/" + userModel.getUser_id();

                    Map dataMap = new HashMap();
                    dataMap.put("type", false);

                    Map map = new HashMap();
                    map.put(path1, dataMap);
                    map.put(path2, dataMap);

                    dRef.child("typing").updateChildren(map).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
                            intent.putExtra("curr_id", userModel.getUser_id());
                            intent.putExtra("chat_id", clientLastOrderModel.getDriver_id_fk());
                            intent.putExtra("curr_type", userModel.getUser_type());
                            intent.putExtra("chat_type", Tags.Driver);
                            intent.putExtra("curr_photo", userModel.getUser_photo());
                            intent.putExtra("chat_photo", clientLastOrderModel.getDriver_image());
                            intent.putExtra("order_id", clientLastOrderModel.getOrder_id());
                            startActivity(intent);
                        }
                    });
                }else
                {
                    Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
                    intent.putExtra("curr_id", userModel.getUser_id());
                    intent.putExtra("chat_id", clientLastOrderModel.getDriver_id_fk());
                    intent.putExtra("curr_type", userModel.getUser_type());
                    intent.putExtra("chat_type", Tags.Driver);
                    intent.putExtra("curr_photo", userModel.getUser_photo());
                    intent.putExtra("chat_photo", clientLastOrderModel.getDriver_image());
                    intent.putExtra("order_id", clientLastOrderModel.getOrder_id());
                    startActivity(intent);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/
        /*Intent intent = new Intent(OrderDeliveryActivity.this, ChatActivity.class);
        intent.putExtra("curr_id", curr_id);
        intent.putExtra("chat_id", chat_id);
        intent.putExtra("curr_type", curr_type);
        intent.putExtra("chat_type", chat_type);
        intent.putExtra("curr_photo", curr_img);
        intent.putExtra("chat_photo", chat_img);
        intent.putExtra("order_id", myOrderModel.getOrder_id());
        startActivity(intent);*/
    }

    private void getLastOrder(String user_id)
    {
        Retrofit retrofit = Api.getClient(Tags.BASE_URL);
        Services services = retrofit.create(Services.class);
        Call<List<ClientLastOrderModel>> call = services.getClient_LastOrder(user_id);
        call.enqueue(new Callback<List<ClientLastOrderModel>>() {
            @Override
            public void onResponse(Call<List<ClientLastOrderModel>> call, Response<List<ClientLastOrderModel>> response) {

                if (response.isSuccessful())
                {
                    if (response.body().size()>0)
                    {
                        ClientLastOrderModel lastOrderModel = response.body().get(0);
                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        UpdateLast_order_ui(lastOrderModel);
                        isDeleted = false;
                        Log.e("isdeleted3",isDeleted+"");


                    }else
                        {
                            isDeleted =true;
                            Log.e("isdeleted4",isDeleted+"");

                        }

                }
            }

            @Override
            public void onFailure(Call<List<ClientLastOrderModel>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("error",t.getMessage());
                Toast.makeText(HomeActivity.this, R.string.something_haywire, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getLastOrder2(String user_id, final String type)
    {
        Retrofit retrofit = Api.getClient(Tags.BASE_URL);
        Services services = retrofit.create(Services.class);
        Call<List<ClientLastOrderModel>> call = services.getClient_LastOrder(user_id);
        call.enqueue(new Callback<List<ClientLastOrderModel>>() {
            @Override
            public void onResponse(Call<List<ClientLastOrderModel>> call, Response<List<ClientLastOrderModel>> response) {

                if (response.isSuccessful())
                {
                    if (response.body().size()>0)
                    {
                        if (type.equals("accept"))
                        {
                            not_accept_btn.setEnabled(false);
                            not_refuse_btn.setEnabled(false);
                            Accept_order(clientLastOrderModel);

                        }else if (type.equals("refuse"))
                        {
                            not_accept_btn.setEnabled(false);
                            not_refuse_btn.setEnabled(false);

                            Refuse_order(clientLastOrderModel);
                        }


                    }else
                    {
                        bottom_sheet.setVisibility(View.GONE);
                        search_view.setEnabled(true);
                        nearbyBtn.setEnabled(true);
                        alert_txt.setVisibility(View.INVISIBLE);
                        Toast.makeText(HomeActivity.this, R.string.or_no_ex, Toast.LENGTH_LONG).show();
                        return;

                    }

                }
            }

            @Override
            public void onFailure(Call<List<ClientLastOrderModel>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("error",t.getMessage());
                Toast.makeText(HomeActivity.this, R.string.something_haywire, Toast.LENGTH_LONG).show();
            }
        });
    }
    private void UpdateLast_order_ui(final ClientLastOrderModel lastOrderModel)
    {

        Picasso.with(this).load(Uri.parse(Tags.ImgPath+lastOrderModel.getDriver_image())).into(not_driver_img);
        Picasso.with(this).load(Uri.parse(Tags.ImgPath+lastOrderModel.getDriver_image())).into(not_driver_img_details);
        not_driver_name.setText(lastOrderModel.getDriver_name());
        not_driver_name_details.setText(lastOrderModel.getDriver_name());
        not_time.setText(lastOrderModel.getOrder_replay_from_minute());
        not_time_details.setText(lastOrderModel.getOrder_replay_from_minute());
        not_rateBar.setRating((float)lastOrderModel.getStars_evaluation());
        not_rateBar_details.setRating((float)lastOrderModel.getStars_evaluation());
        not_driver_rate.setText(String.valueOf(lastOrderModel.getRate_evaluation()));
        not_driver_rate_details.setText(String.valueOf(lastOrderModel.getRate_evaluation()));
        not_order_details.setText(lastOrderModel.getOrder_details());
        not_date_details.setText(lastOrderModel.getOrder_date());
        not_cost_details.setText(lastOrderModel.getOrder_cost()+" ريال");
        //not_data_cardview.setVisibility(View.VISIBLE);
        bottom_sheet.setVisibility(View.VISIBLE);
        this.clientLastOrderModel = lastOrderModel;
        search_view.setEnabled(false);
        search_view.setFocusable(false);
        nearbyBtn.setEnabled(false);
        alert_txt.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        },3000);





    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getLast_order_not(DriverAcceptModel driverAcceptModel)
    {
        getLastOrder(driverAcceptModel.getUser_id());
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
    public void Driver_delivered_Order(Finishied_Order_Model finishied_order_model)
    {
        CreateCustomAlertDialog(finishied_order_model);
    }


    private void CreateCustomAlertDialog(final Finishied_Order_Model finishied_order_model) {
        View view = LayoutInflater.from(this).inflate(R.layout.custom_alert_dialog,null);
        CircleImageView driver_img = view.findViewById(R.id.driver_image);
        TextView driver_name = view.findViewById(R.id.driver_name);
        TextView order_details = view.findViewById(R.id.order_details);

        Picasso.with(HomeActivity.this).load(Uri.parse(Tags.ImgPath+finishied_order_model.getDriver_image())).into(driver_img);
        driver_name.setText(finishied_order_model.getDriver_name());
        order_details.setText(finishied_order_model.getOrder_details());
        Button addRateBtn = view.findViewById(R.id.add_rate);
        final AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this)
                .setCancelable(false)
                .setView(view)
                .create();

        alertDialog.show();
        addRateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,AddRateActivity.class);
                intent.putExtra("driver_id",finishied_order_model.getDriver_id());
                intent.putExtra("order_id",finishied_order_model.getOrder_id());
                intent.putExtra("driver_name",finishied_order_model.getDriver_name());
                intent.putExtra("driver_image",finishied_order_model.getDriver_image());
                startActivity(intent);
                alertDialog.dismiss();


            }
        });

    }

    private void sendOrders(Map<String, String> map, List<String> drivers_ids) {
        CreateProgDialog(getString(R.string.send_reqtoDriv));
        dialog.show();
        Retrofit retrofit = Api.getClient(Tags.BASE_URL);
        Services services = retrofit.create(Services.class);
        Call<ResponseModel> call = services.sendOrder(map, drivers_ids);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful())
                {
                    Log.e("rs",response.body().getSuccess()+"");
                    if (response.body().getSuccess_order()==1)
                    {
                        dialog.dismiss();
                        Toast.makeText(HomeActivity.this, R.string.order_sent, Toast.LENGTH_LONG).show();
                        costContainer.setVisibility(View.GONE);
                        mMap.clear();
                        AddMarker(mylatLng,"");
                        search_view.setText("");
                        txt_order.setText(null);

                    }else
                        {
                            Toast.makeText(HomeActivity.this, R.string.order_notsent, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(HomeActivity.this,getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();
                Log.e("Error",t.getMessage());
            }
        });

    }

    private void CreateProgDialog(String msg)
    {
        ProgressBar bar = new ProgressBar(this);
        Drawable drawable = bar.getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        dialog = new ProgressDialog(this);
        dialog.setMessage(msg);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.setIndeterminateDrawable(drawable);

    }
    private void CreateAlertDialog()
    {
        builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.logout));
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog_logout.show();
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
    private void CreateAlertDialogUserLoginOrRegister(String msg)
    {
        final AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .create();
        View view = LayoutInflater.from(this).inflate(R.layout.custom_dialog,null);
        TextView tv_msg = view.findViewById(R.id.tv_msg);
        Button done_btn = view.findViewById(R.id.doneBtn);
        tv_msg.setText(msg);
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();

            }
        });

        alertDialog.getWindow().getAttributes().windowAnimations = R.style.custom_dialog;
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setView(view);
        alertDialog.show();
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

                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        try
        {
            if (userModel.getUser_photo() != null || !userModel.getUser_photo().equals("0") || TextUtils.isEmpty(userModel.getUser_photo())) {
                Picasso.with(this).load(Uri.parse(Tags.ImgPath + userModel.getUser_photo())).placeholder(R.drawable.user_profile).into(target);
            }
        }catch (NullPointerException e)
        {

        }

    }
    private boolean IsServicesOk()
    {
        int availablity = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);

        if (availablity == ConnectionResult.SUCCESS) {
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(availablity)) {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(HomeActivity.this, availablity, ErrorDialog);
            dialog.show();
        }
        return false;
    }
    private void checkPermission()
    {
        String[] permissions = new String[]{fine_location, coarse_location};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), fine_location) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), coarse_location) == PackageManager.PERMISSION_GRANTED) {
                permission_granted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(HomeActivity.this, permissions, per_req);

            }
        } else {
            ActivityCompat.requestPermissions(HomeActivity.this, permissions, per_req);

        }
    }
    private void initMap()
    {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permission_granted = false;
        if (requestCode == per_req) {
            if (grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        permission_granted = false;
                        return;
                    }
                }
                permission_granted = true;
                initMap();
            }
        }

    }
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        if (googleMap != null) {
            mMap = googleMap;
            mMap.setIndoorEnabled(true);
            mMap.setTrafficEnabled(false);
            mMap.setBuildingsEnabled(true);
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.maps));
                getDeviceLocation();
        }
    }
    private void getDeviceLocation()
    {
       try
       {
           if (permission_granted)
           {
               mMap.clear();
               mMap.setMyLocationEnabled(true);
               Task<Location> lastLocation = fusedLocationProviderClient.getLastLocation();
               lastLocation.addOnCompleteListener(new OnCompleteListener<Location>() {
                   @Override
                   public void onComplete(@NonNull Task<Location> task) {
                       if (task.isSuccessful())
                       {
                           Location location = task.getResult();
                           if (location!=null)
                           {
                               try {

                                   mylatLng  = new LatLng(location.getLatitude(),location.getLongitude());
                                   AddMarker(mylatLng,"");

                                   Geocoder geocoder = new Geocoder(HomeActivity.this);
                                   List<Address> addressList = geocoder.getFromLocation(mylatLng.latitude,mylatLng.longitude,1);
                                   if (addressList!=null && addressList.size()>0)
                                   {
                                       Address address = addressList.get(0);

                                       if (address!=null)
                                       {
                                           Log.e("country code",address.getCountryCode());
                                           Log.e("address1",address.getLocality());



                                           country_code= address.getCountryCode();
                                           myLocality = address.getLocality();

                                           initFilter(country_code);
                                       }else
                                           {
                                               initFilter(country_code);

                                           }
                                   }else
                                       {
                                           initFilter(country_code);

                                       }

                               }catch (NullPointerException e)
                               {
                                   Toast.makeText(HomeActivity.this, R.string.loc_notfounded, Toast.LENGTH_SHORT).show();

                               } catch (IOException e) {
                                   e.printStackTrace();
                               }
                           }



                       }
                   }
               });
           }

       }catch (SecurityException e)
       {

       }
    }
    private void AddMarker(LatLng latLng,String title)
    {
        if (TextUtils.isEmpty(title))
        {
            mMap.addMarker(
                    new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.g_map)).position(latLng)

            );
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15.6f));
        }else
            {
                mMap.addMarker(
                        new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.g_map)).position(latLng).title(title)

                );
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15.6f));
            }

    }
     @Override
    public boolean onOptionsItemSelected(MenuItem item)
     {
        return super.onOptionsItemSelected(item);
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id)
        {
            case R.id.home:
                break;
            case R.id.register:
                if (userModel!=null)
                {
                    if (userModel.getUser_type().equals(Tags.Client))
                    {
                        Intent intent=new Intent(this,Activity_Driver_Register.class);
                        startActivity(intent);
                    }else
                    {
                        CreateAlertDialog2(getString(R.string.already_driver));
                        builder2.show();
                    }
                }else
                    {
                        CreateAlertDialogUserLoginOrRegister(getString(R.string.pl_lgn));

                    }


                break;
            case R.id.logout:
                if (userModel!=null)
                {
                    CreateAlertDialog();

                    builder.show();
                }else
                    {
                        Intent intent = new Intent(HomeActivity.this,Activity_Client_Login.class);
                        startActivity(intent);
                        finish();
                    }

                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void Logout()
    {
        Retrofit retrofit = Api.getClient(Tags.BASE_URL);
        Services services = retrofit.create(Services.class);
        Call<ResponseModel> call = services.LogOut(userModel.getUser_id());
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                if(response.isSuccessful())
                {
                    if (response.body().getSuccess()==1)
                    {
                        dialog_logout.dismiss();

                        preferences.Update_UserState("");
                        preferences.ClearPref();
                        preferences.UpdateSoundPref("");
                        manager.cancel(0);
                        manager.cancel(1995);
                        Intent intent = new Intent(HomeActivity.this,Activity_Client_Login.class);
                        startActivity(intent);
                        finish();
                    }else
                        {
                            dialog_logout.dismiss();

                        }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e("error",t.getMessage());
                dialog_logout.dismiss();
                Toast.makeText(HomeActivity.this, R.string.something_haywire, Toast.LENGTH_LONG).show();


            }
        });

    }
    @Override
    public void UserDataSuccess(UserModel userModel1)
    {
        this.userModel = userModel1;
        UpdateUI(userModel);

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

    private void HideKeyBoard()
    {
        InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(search_view.getWindowToken(),0);
    }
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            costContainer.setVisibility(View.GONE);
            locContainer.setVisibility(View.GONE);
            mMap.clear();
            AutocompletePrediction item = adapter.getItem(i);
            String place_id = item.getPlaceId();
            PendingResult<PlaceBuffer> bufferPendingResult = Places.GeoDataApi.getPlaceById(apiClient,place_id);
            bufferPendingResult.setResultCallback(resultCallback);

        }
    };

    private ResultCallback<PlaceBuffer> resultCallback = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {

            try {
                if (!places.getStatus().isSuccess())
                {
                    places.release();
                    return;
                }
                Place place = places.get(0);
                try
                {
                    HideKeyBoard();
                    latLng = place.getLatLng();
                    from = place.getName()+","+place.getAddress();
                    txt_order_from.setText(from);
                    Geocoder geocoder = new Geocoder(HomeActivity.this);
                    List<Address> addressList = geocoder.getFromLocation(mylatLng.latitude,mylatLng.longitude,1);
                    if (addressList.size()>0)
                    {
                        to = addressList.get(0).getAddressLine(0);
                        txt_order_to.setText(to);
                    }
                    CreateProgDialog(getString(R.string.locating));

                    dialog.show();

                    getDirection(mylatLng,latLng);
                    //dist = distance(mylatLng.latitude,mylatLng.longitude,latLng.latitude,latLng.longitude);


                }
                catch (NullPointerException e)
                {
                    Toast.makeText(HomeActivity.this, R.string.cfl, Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                places.release();
            }catch (NullPointerException e)
            {

            }

        }
    };

    private void getDirection(final LatLng mylatLng,final LatLng latLng) {
        Retrofit retrofit = Api.getClient(Tags.Map_BaseUrl);
        Services services = retrofit.create(Services.class);
        String Origin = String.valueOf(mylatLng.latitude)+","+String.valueOf(mylatLng.longitude);
        String Dest   = String.valueOf(latLng.latitude)+","+String.valueOf(latLng.longitude);
        Log.e("origin",Origin);
        Log.e("dest",Dest);
        String server_key="AIzaSyArjmbYWTWZhDFFtPOLRLKYwjtBDkOEGrY";
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="+Origin+"&destination="+Dest+"&key="+server_key;
        Call<PlaceModel> call = services.getDirection(url);
        call.enqueue(new Callback<PlaceModel>() {
            @Override
            public void onResponse(Call<PlaceModel> call, Response<PlaceModel> response) {
                if (response.isSuccessful())
                {

                    PlaceModel placeModel = response.body();
                    if (placeModel!=null&&placeModel.getRoutes().size()>0)
                    {
                        try {
                            Log.e("pl",placeModel.getRoutes().get(0).getLegs().get(0).getDistance().getText());
                            Log.e("p2",placeModel.getRoutes().get(0).getLegs().get(0).getDuration().getText());
                            String d = placeModel.getRoutes().get(0).getLegs().get(0).getDistance().getText();
                            String d2="";
                            if (d.contains(","))
                            {
                                d2 = d.replaceAll(",","");
                            }else
                            {
                                d2=d;
                            }
                            String spilit_dist [] =d2.split(" ");
                            dist = Double.parseDouble(spilit_dist[0]);
                        }catch (NullPointerException e)
                        {
                            dist = distance(mylatLng.latitude,mylatLng.longitude,latLng.latitude,latLng.longitude);
                            Toast.makeText(HomeActivity.this, "dist2"+dist, Toast.LENGTH_SHORT).show();

                        }catch (IndexOutOfBoundsException e)
                        {
                            Toast.makeText(HomeActivity.this, "empty data", Toast.LENGTH_SHORT).show();
                        }
                        String durat = placeModel.getRoutes().get(0).getLegs().get(0).getDuration().getText();
                        List<String> polylines = new ArrayList<>();
                        List<PlaceModel.Steps> stepsModelList =placeModel.getRoutes().get(0).getLegs().get(0).getSteps();

                        AddMarker(mylatLng,durat);
                        AddMarker(latLng,durat);
                    /*mMap.addMarker(
                            new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.g_map)).position(new LatLng(mylatLng.latitude,mylatLng.longitude)).title(durat)

                    );*/

                        for (int i=0;i<stepsModelList.size();i++)
                        {
                            polylines.add(stepsModelList.get(i).getPolyline().getPoints());
                        }


                        for (int i=0;i<polylines.size();i++)
                        {
                            PolylineOptions options = new PolylineOptions();
                            options.width(5);
                            options.color(Color.BLACK);
                            options.addAll(PolyUtil.decode(polylines.get(i)));
                            options.geodesic(true);
                            mMap.addPolyline(options);
                            Log.e("polyline",""+PolyUtil.decode(polylines.get(i)));

                        }

                        dialog.dismiss();
                        locContainer.setVisibility(View.VISIBLE);
                        ShowAvailable_Drivers();
                    }

                }
            }

            @Override
            public void onFailure(Call<PlaceModel> call, Throwable t) {
                Toast.makeText(HomeActivity.this, getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();
                dialog.dismiss();

                Log.e("errordirection",t.getMessage());
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,R.string.something_haywire, Toast.LENGTH_SHORT).show();
    }

    private void ShowAvailable_Drivers()
    {
        Retrofit retrofit = Api.getClient(Tags.BASE_URL);
        Services services = retrofit.create(Services.class);
        Call<List<AvailableDriversModel>> call = services.ShowAvailable_Drivers();
        call.enqueue(new Callback<List<AvailableDriversModel>>() {
            @Override
            public void onResponse(Call<List<AvailableDriversModel>> call, Response<List<AvailableDriversModel>> response) {
                if (response.isSuccessful())
                {
                    List<AvailableDriversModel> availableDriversModelList = response.body();
                    getDrivers(availableDriversModelList);
                }
            }

            @Override
            public void onFailure(Call<List<AvailableDriversModel>> call, Throwable t) {
                dialog.dismiss();

                Log.e("Error",t.getMessage());
                Toast.makeText(HomeActivity.this, ""+getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDrivers(List<AvailableDriversModel> availableDriversModelList) {
        try
        {
            Map<String,Double> map = new HashMap<>();
            List<Double> sortedArray = new ArrayList<>();
            drivers_ids = new ArrayList<>();
            for (int i=0;i<availableDriversModelList.size();i++)
            {
                AvailableDriversModel driversModel = availableDriversModelList.get(i);
                if (driversModel.getUser_google_lat()!=null||!TextUtils.isEmpty(driversModel.getUser_google_lat())
                        && driversModel.getUser_google_long()!=null||!TextUtils.isEmpty(driversModel.getUser_google_long()))
                {
                    double dis = distance(mylatLng.latitude,mylatLng.longitude,Double.parseDouble(driversModel.getUser_google_lat()),Double.parseDouble(driversModel.getUser_google_long()));
                    if (dis<=50)
                    {
                        map.put(driversModel.getDriver_id(),dis);

                    }
                }
                /*Log.e("ana",mylatLng.latitude+"_"+mylatLng.longitude);
                Log.e("drivers",driversModel.getUser_google_lat()+"_"+driversModel.getUser_google_long());
*/

            }
            for (String key :map.keySet())
            {
                sortedArray.add(map.get(key));
            }

            Collections.sort(sortedArray);

            if (sortedArray.size()>0)
            {
                if (sortedArray.size()<=6)
                {
                    for (int i =0;i<sortedArray.size();i++)
                    {
                        for (String key :map.keySet())
                        {
                            if (map.get(key)== sortedArray.get(i))
                            {
                                drivers_ids.add(key);
                            }
                        }
                    }
                    Log.e("iffff","<6");
                }else
                {
                    for (int i =0;i<6;i++)
                    {
                        for (String key :map.keySet())
                        {

                            if (map.get(key)== sortedArray.get(i))
                            {
                                drivers_ids.add(key);
                            }
                        }
                    }
                    Log.e("iffff",">6");

                }

            }else if(sortedArray.size()==0)
            {
                Toast.makeText(this, R.string.no_driver_near, Toast.LENGTH_SHORT).show();
            }




        }catch (IndexOutOfBoundsException e)
        {
            Log.e("error drivers","index < 0");
        }
        catch (NumberFormatException e)
        {
            //Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }
    }

    private void getCostByDistance(String distance) {
        Log.e("dist",distance);
        Retrofit retrofit = Api.getClient(Tags.BASE_URL);
        Services services = retrofit.create(Services.class);
        Call<TotalCostModel> call = services.getCostByDistance(distance);
        call.enqueue(new Callback<TotalCostModel>() {
            @Override
            public void onResponse(Call<TotalCostModel> call, Response<TotalCostModel> response) {
                if (response.isSuccessful())
                {
                    String totalcost = response.body().getTotal_cost();
                    Log.e("total cost",totalcost);
                    cost.setText(totalcost);
                    locContainer.setVisibility(View.GONE);
                    costContainer.setVisibility(View.VISIBLE);
                    dialog.dismiss();


                }
            }

            @Override
            public void onFailure(Call<TotalCostModel> call, Throwable t) {
                dialog.dismiss();
                Log.e("Error",t.getMessage());
                Toast.makeText(HomeActivity.this, ""+getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
