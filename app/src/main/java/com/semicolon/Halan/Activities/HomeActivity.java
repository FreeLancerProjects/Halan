package com.semicolon.Halan.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.maps.android.PolyUtil;
import com.semicolon.Halan.Adapters.PlaceAutocompleteAdapter;
import com.semicolon.Halan.Models.AvailableDriversModel;
import com.semicolon.Halan.Models.PlaceModel;
import com.semicolon.Halan.Models.ResponseModel;
import com.semicolon.Halan.Models.TokenModel;
import com.semicolon.Halan.Models.TotalCostModel;
import com.semicolon.Halan.Models.UserModel;
import com.semicolon.Halan.R;
import com.semicolon.Halan.Services.Api;
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
import me.anwarshahriar.calligrapher.Calligrapher;
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
    private AlertDialog.Builder builder,builder2;
    private Users users;
    private LinearLayout profileContainer;
    private ProgressDialog dialog;
    private List<String> drivers_ids;
    private String from,to;
    private String distn,order_details;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "JannaLT-Regular.ttf", true);
        initView();
        EventBus.getDefault().register(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        users = Users.getInstance();
        users.getUserData(this);
        UpdateToken();
        preferences = new Preferences(this);
        if (IsServicesOk()) {
            checkPermission();
        }
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        users.getUserData(this);

        if (userModel.getUser_type().equals(Tags.Driver))
        {
            Intent intent = new Intent(this,DriverOrdersActivity.class);
            startActivity(intent);
            finish();
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

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (TextUtils.isEmpty(txt_order.getText().toString()))
                {
                    txt_order.setError("أدخل تفاصيل الطلب");
                }else
                    {


                        String[] dis = String.valueOf(dist).split(" ");
                        distn = String.valueOf(Math.round(Double.parseDouble(dis[0])));
                        distance.setText(distn+" "+getString(R.string.km));
                        order_details = txt_order.getText().toString();
                        CreateProgDialog("جار تحديد تكلفة الطريق...");

                        dialog.show();
                        getCostByDistance(distn);

                    }


            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("from",from);
                Log.e("to",to);
                Log.e("my",mylatLng.latitude+"");
                Log.e("to lat",latLng.latitude+"");
                Log.e("ids",drivers_ids.size()+"");
                Log.e("dis",distn);
                Log.e("id",userModel.getUser_id());
                Log.e("details",order_details);
                Log.e("Cost",cost.getText().toString());







            }
        });

        //////////////////////////////////////////////////////////
        View view = nav_view.getHeaderView(0);
        profileContainer = view.findViewById(R.id.profileContainer);
        userImage = view.findViewById(R.id.userImage);
        nav_view.setNavigationItemSelectedListener(this);

        profileContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(HomeActivity.this,UserProfileActivity.class);
                startActivity(intent);
            }
        });
        search_view = findViewById(R.id.search);
        search_view.setOnItemClickListener(itemClickListener);
        GeoDataClient geoDataClient = Places.getGeoDataClient(this,null);
        adapter = new PlaceAutocompleteAdapter(this,geoDataClient,latLngBounds,null);

        search_view.setAdapter(adapter);
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
        apiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this,this)
                .build();


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
                getDeviceLocation();
        }
    }
    private void getDeviceLocation()
    {
       try
       {
           if (permission_granted)
           {
               mMap.setMyLocationEnabled(true);
               Task<Location> lastLocation = fusedLocationProviderClient.getLastLocation();
               lastLocation.addOnCompleteListener(new OnCompleteListener<Location>() {
                   @Override
                   public void onComplete(@NonNull Task<Location> task) {
                       if (task.isSuccessful())
                       {
                           Location location = task.getResult();

                           try {

                               mylatLng  = new LatLng(location.getLatitude(),location.getLongitude());
                               AddMarker(mylatLng,"");
                           }catch (NullPointerException e)
                           {
                               Toast.makeText(HomeActivity.this, R.string.loc_notfounded, Toast.LENGTH_SHORT).show();

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
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,11f));
        }else
            {
                mMap.addMarker(
                        new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.g_map)).position(latLng).title(title)

                );
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,11f));
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
                if (userModel.getUser_type().equals(Tags.Client))
                {
                    Intent intent=new Intent(this,Activity_Driver_Register.class);
                    startActivity(intent);
                }else
                    {
                        CreateAlertDialog2(getString(R.string.already_driver));
                        builder2.show();
                    }

                break;
            case R.id.logout:
                CreateAlertDialog();

                builder.show();
                break;
            case R.id.contact:
                Intent intent1=new Intent(this,ContactUsActivity.class);
                startActivity(intent1);
                break;
            case R.id.pay:
                if (userModel.getUser_type().equals(Tags.Driver))
                {

                    Intent intent2=new Intent(this,PayActivity.class);
                    startActivity(intent2);
                }else
                    {
                        CreateAlertDialog2(getString(R.string.serv_aval_drivers));
                        builder2.show();
                    }

                break;
            case R.id.orders:
                Intent intent2=new Intent(this,MyOrdersActivity.class);
                startActivity(intent2);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private void Logout()
    {
        preferences.ClearPref();
        Intent intent = new Intent(HomeActivity.this,Activity_Client_Login.class);
        startActivity(intent);
        finish();
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
                CreateProgDialog("جار تحديد الموقع...");

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
        }
    };

    private void getDirection(final LatLng mylatLng,final LatLng latLng) {
        Retrofit retrofit = Api.getClient(Tags.Map_BaseUrl);
        Services services = retrofit.create(Services.class);
        String Origin = String.valueOf(mylatLng.latitude)+","+String.valueOf(mylatLng.longitude);
        String Dest   = String.valueOf(latLng.latitude)+","+String.valueOf(latLng.longitude);
        Log.e("origin",Origin);
        Log.e("dest",Dest);
        String server_key=getString(R.string.google_maps_key);
        String url = "https://maps.googleapis.com/maps/api/directions/json?origin="+Origin+"&destination="+Dest+"&key="+server_key;
        Call<PlaceModel> call = services.getDirection(url);
        call.enqueue(new Callback<PlaceModel>() {
            @Override
            public void onResponse(Call<PlaceModel> call, Response<PlaceModel> response) {
                if (response.isSuccessful())
                {
                    PlaceModel placeModel = response.body();
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
                double dis = distance(mylatLng.latitude,mylatLng.longitude,Double.parseDouble(driversModel.getUser_google_lat()),Double.parseDouble(driversModel.getUser_google_long()));
                map.put(driversModel.getDriver_id(),dis);
            }
            for (String key :map.keySet())
            {
                sortedArray.add(map.get(key));
            }

            Collections.sort(sortedArray);

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




        }catch (IndexOutOfBoundsException e)
        {
            Log.e("error drivers","index < 0");
        }
    }

    private void getCostByDistance(String distance) {
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


}
