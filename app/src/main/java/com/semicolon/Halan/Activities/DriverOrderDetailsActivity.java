package com.semicolon.Halan.Activities;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.semicolon.Halan.R;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_order_details);
        initView();
        if (isServiceOk())
        {
            CheckPermission();
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
            Toast.makeText(this, "every things ok", Toast.LENGTH_SHORT).show();
            try
            {
                mMap.setMyLocationEnabled(true);
            }catch (SecurityException e)
            {

            }catch (NullPointerException e)
            {

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
}
