package com.semicolon.Halan.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.semicolon.Halan.Models.PlaceModel;
import com.semicolon.Halan.R;
import com.semicolon.Halan.Services.Api;
import com.semicolon.Halan.Services.Services;
import com.semicolon.Halan.Services.Tags;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DriverOrderDetailsActivity extends AppCompatActivity implements OnMapReadyCallback{

    private FrameLayout driver_orderContainer;
    private TextView driver_txt_order_from,driver_txt_order_to,driver_order_details,driver_client_phone,driver_cost;
    private Button accept,refuse;
    private String Fine_Loc = Manifest.permission.ACCESS_FINE_LOCATION;
    private String Coarse_Loc = Manifest.permission.ACCESS_COARSE_LOCATION;
    private boolean isGranted = false;
    private final int per_req=1550;
    private final int error_dialog=9001;
    private GoogleMap mMap;
    private String client_location,market_location,order_detail,cost,phone;
    private Double market_lat,market_long,client_lat,client_long;
    private double dist;
    private LatLng fromLatLng,toLatLng;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_order_details);
        getDataFromIntent();
        initView();
        if (isServiceOk())
        {
            CheckPermission();
        }

    }

    private void getDataFromIntent() {
        Intent intent=getIntent();

        if (intent!=null)
        {
            client_location=intent.getStringExtra("client_location");
            market_location=intent.getStringExtra("market_location");
            order_detail=intent.getStringExtra("order_detail");
            cost=intent.getStringExtra("cost");
            phone=intent.getStringExtra("phone");
            market_lat=intent.getDoubleExtra("market_lat",1.1);
            market_long=intent.getDoubleExtra("market_long",1.1);
            client_lat=intent.getDoubleExtra("client_lat",1.1);
            client_long=intent.getDoubleExtra("client_long",1.1);

            fromLatLng = new LatLng(market_lat,market_long);
            toLatLng = new LatLng(client_lat,client_long);


        }
    }

    private void initView() {
        driver_orderContainer = findViewById(R.id.driver_orderContainer);
        driver_txt_order_from = findViewById(R.id.driver_txt_order_from);
        driver_txt_order_to   = findViewById(R.id.driver_txt_order_to);
        driver_order_details  = findViewById(R.id.driver_order_details);
        driver_client_phone   = findViewById(R.id.driver_client_phone);
        driver_cost           = findViewById(R.id.driver_cost);
        accept                = findViewById(R.id.accept);
        refuse                = findViewById(R.id.refuse);

        driver_txt_order_from.setText(market_location);
        driver_txt_order_to.setText(client_location);
        driver_order_details.setText(order_detail);
        driver_client_phone.setText(phone);
        driver_cost.setText(cost);

    }
    private void initMap()
    {
        SupportMapFragment map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        map.getMapAsync(this);

    }
    private boolean isServiceOk()
    {
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (available == ConnectionResult.SUCCESS)
        {
            return true;
        }else if (GoogleApiAvailability.getInstance().isUserResolvableError(available))
        {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(this,available,error_dialog);
            dialog.show();
        }
            return false;
    }

    private void CheckPermission()
    {
        String [] permissions = new String[]{Fine_Loc,Coarse_Loc};
        if (ContextCompat.checkSelfPermission(this,Fine_Loc)== PackageManager.PERMISSION_GRANTED)
        {
            if (ContextCompat.checkSelfPermission(this,Coarse_Loc)== PackageManager.PERMISSION_GRANTED)
            {
                isGranted = true;
                initMap();
            }else
                {
                    ActivityCompat.requestPermissions(this,permissions,per_req);
                }
        }else
            {
                ActivityCompat.requestPermissions(this,permissions,per_req);

            }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap!=null)
        {
            mMap = googleMap;
            try
            {
                if (isGranted)
                {
                    Log.e("latlng" +fromLatLng.longitude,"");
                    Log.e("latlng" +toLatLng.latitude,"");

                    getDirection(fromLatLng,toLatLng);
                }
            }catch (SecurityException e)
            {

            }catch (NullPointerException e)
            {

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        isGranted = false;
        if (grantResults.length>0)
        {
            for (int i=0;i<grantResults.length;i++)
            {
                if (grantResults[i]!=PackageManager.PERMISSION_GRANTED)
                {
                    isGranted=false;
                    return;
                }
            }
            isGranted= true;
            initMap();
        }


    }
    private void getDirection(final LatLng fromlatLng, final LatLng tolatLng) {
        Retrofit retrofit = Api.getClient(Tags.Map_BaseUrl);
        Services services = retrofit.create(Services.class);
        String Origin = String.valueOf(fromlatLng.latitude)+","+String.valueOf(fromlatLng.longitude);
        String Dest   = String.valueOf(tolatLng.latitude)+","+String.valueOf(tolatLng.longitude);
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
                    if (placeModel.getRoutes().size()>0)
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

                        }catch (IndexOutOfBoundsException e)
                        {
                            Toast.makeText(DriverOrderDetailsActivity.this, "empty data", Toast.LENGTH_SHORT).show();
                        }
                        String durat = placeModel.getRoutes().get(0).getLegs().get(0).getDuration().getText();
                        List<String> polylines = new ArrayList<>();
                        List<PlaceModel.Steps> stepsModelList =placeModel.getRoutes().get(0).getLegs().get(0).getSteps();

                        AddMarker(fromlatLng,durat);
                        AddMarker(tolatLng,durat);
                    /*mMap.addMarker(
                            new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.g_map)).position(new LatLng(mylatLng.latitude,mylatLng.longitude)).title(durat)

                    );*/

                    if (stepsModelList.size()>0)
                    {
                        for (int i=0;i<stepsModelList.size();i++)
                        {
                            polylines.add(stepsModelList.get(i).getPolyline().getPoints());
                        }

                        if (polylines.size()>0)
                        {
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
                        }

                    }

                    }else
                        {
                            Toast.makeText(DriverOrderDetailsActivity.this, getString(R.string.cfl), Toast.LENGTH_LONG).show();
                        }



                }
            }

            @Override
            public void onFailure(Call<PlaceModel> call, Throwable t) {
                Toast.makeText(DriverOrderDetailsActivity.this, getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();

                Log.e("errordirection",t.getMessage());
            }
        });
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
}
