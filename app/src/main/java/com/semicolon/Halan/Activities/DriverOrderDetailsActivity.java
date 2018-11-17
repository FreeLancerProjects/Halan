package com.semicolon.Halan.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
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
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.semicolon.Halan.Models.AvailableDriversModel;
import com.semicolon.Halan.Models.Finishied_Order_Model;
import com.semicolon.Halan.Models.PlaceModel;
import com.semicolon.Halan.Models.ResponseModel;
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

public class DriverOrderDetailsActivity extends AppCompatActivity implements OnMapReadyCallback, Users.UserData,View.OnClickListener{

    private FrameLayout driver_orderContainer;
    private TextView driver_txt_order_from,driver_txt_order_to,driver_order_details,driver_client_phone,driver_cost;
    private Button accept,refuse;
    private String Fine_Loc = Manifest.permission.ACCESS_FINE_LOCATION;
    private String Coarse_Loc = Manifest.permission.ACCESS_COARSE_LOCATION;
    private boolean isGranted = false;
    private final int per_req=1550;
    private final int error_dialog=9001;
    private GoogleMap mMap;
    private String client_location,market_location,order_detail,cost,phone,client_id,order_id,messege_id;
    private Double market_lat,market_long,client_lat,client_long;
    private double dist;
    private LatLng fromLatLng,toLatLng;
    private Preferences preferences;
    private UserModel userModel;
    private List<String> drivers_ids;
    private ProgressDialog dialog;
    Users users;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_order_details);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "JannaLT-Regular.ttf", true);
        EventBus.getDefault().register(this);
        users = Users.getInstance();
        preferences=new Preferences(this);
        users.getUserData(this);
        getDataFromIntent();
        initView();
        CreateProgDialog();
        if (isServiceOk())
        {
            CheckPermission();
        }

    }

    private void CreateProgDialog()
    {
        ProgressBar bar = new ProgressBar(this);
        Drawable drawable = bar.getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.cancel_andSend));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.setIndeterminateDrawable(drawable);

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

            client_id=intent.getStringExtra("client_id");
            order_id=intent.getStringExtra("order_id");
            messege_id=intent.getStringExtra("message_id");

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
        accept.setOnClickListener(this);
        refuse.setOnClickListener(this);

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

        Picasso.with(DriverOrderDetailsActivity.this).load(Uri.parse(Tags.ImgPath+finishied_order_model.getDriver_image())).into(driver_img);
        driver_name.setText(finishied_order_model.getDriver_name());
        order_details.setText(finishied_order_model.getOrder_details());
        Button addRateBtn = view.findViewById(R.id.add_rate);
        final AlertDialog alertDialog = new AlertDialog.Builder(DriverOrderDetailsActivity.this)
                .setCancelable(false)
                .setView(view)
                .create();

        alertDialog.show();
        addRateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverOrderDetailsActivity.this,AddRateActivity.class);
                intent.putExtra("driver_id",finishied_order_model.getDriver_id());
                intent.putExtra("order_id",finishied_order_model.getOrder_id());
                intent.putExtra("driver_name",finishied_order_model.getDriver_name());
                intent.putExtra("driver_image",finishied_order_model.getDriver_image());
                startActivity(intent);
                alertDialog.dismiss();


            }
        });

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
            mMap = googleMap;
            mMap.setIndoorEnabled(true);
            mMap.setTrafficEnabled(false);
            mMap.setBuildingsEnabled(true);
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.maps));
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
        String server_key="AIzaSyArjmbYWTWZhDFFtPOLRLKYwjtBDkOEGrY";
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
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15.6f));
        }else
        {
            mMap.addMarker(
                    new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.g_map)).position(latLng).title(title)

            );
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15.6f));
        }

    }


    private void SendAccept() {

        Map<String,String> map = new HashMap<>();
        map.put("action","1");
        map.put("message_id",messege_id);
        map.put("order_id_fk",order_id);
        map.put("client_id_fk",client_id);

//        map.put("user_id",userId);

        Retrofit retrofit = Api.getClient(Tags.BASE_URL);
        Services services = retrofit.create(Services.class);
        Call<ResponseModel> call = services.sendDriverRequest_Accept(map);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess()==1)
                    {
                        Toast.makeText(DriverOrderDetailsActivity.this, R.string.respons_send_toclient, Toast.LENGTH_LONG).show();
                        finish();
                    }else
                    {
                        Toast.makeText(DriverOrderDetailsActivity.this, R.string.respons_not_send, Toast.LENGTH_LONG).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e("Error",t.getMessage());
                Toast.makeText(DriverOrderDetailsActivity.this, getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void SendRefuse() {
        dialog.show();
        Map<String,String> map = new HashMap<>();
        map.put("action","2");
        map.put("message_id",messege_id);
        map.put("order_id_fk",order_id);
        map.put("client_id_fk",client_id);
      //  map.put("user_id",userId);

        Retrofit retrofit = Api.getClient(Tags.BASE_URL);
        Services services = retrofit.create(Services.class);
        Call<ResponseModel> call = services.sendDriverRequest_Refuse(map);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess()==1)
                    {
                        ShowAvailable_Drivers();
                        //Toast.makeText(DriverOrderDetailsActivity.this, R.string.respons_send_todriver, Toast.LENGTH_LONG).show();
                        //finish();

                    }else
                    {
                        Toast.makeText(DriverOrderDetailsActivity.this, R.string.respons_not_send, Toast.LENGTH_LONG).show();

                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Log.e("Error",t.getMessage());
                Toast.makeText(DriverOrderDetailsActivity.this, getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();
            }
        });
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
                Toast.makeText(DriverOrderDetailsActivity.this, ""+getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();
                finish();
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
                if (client_lat!=0.0 || client_long!=0.0 &&
                        driversModel.getUser_google_lat()!=null||!TextUtils.isEmpty(driversModel.getUser_google_lat()) ||
                        driversModel.getUser_google_long()!=null||!TextUtils.isEmpty(driversModel.getUser_google_long()) )
                {
                    double dis = distance(client_lat,client_long,Double.parseDouble(driversModel.getUser_google_lat()),Double.parseDouble(driversModel.getUser_google_long()));
                    if (dis<=50)
                    {
                        map.put(driversModel.getDriver_id(),dis);

                    }
                }

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

               /* Map<String,String> datamap =new HashMap<>();
                datamap.put("user_id",userModel.getUser_id());
                datamap.put("client_location",myOrderModel.getClient_location());
                datamap.put("market_location",myOrderModel.getMarket_location());
                datamap.put("client_google_lat",String.valueOf(myOrderModel.getClient_google_lat()));
                datamap.put("client_google_lang",String.valueOf(myOrderModel.getClient_google_lang()));
                datamap.put("market_google_lat",String.valueOf(myOrderModel.getMarket_google_lat()));
                datamap.put("market_google_lang",String.valueOf(myOrderModel.getMarket_google_lang()));
                datamap.put("distance",String.valueOf(dist));
                datamap.put("order_details",myOrderModel.getOrder_details());
                datamap.put("total_cost",myOrderModel.getCost());*/

                    sendOrders(drivers_ids);

                    Log.e("iffff",">6");

                }
            }else if (sortedArray.size()==0)
            {
                Toast.makeText(this, R.string.no_driver_near, Toast.LENGTH_SHORT).show();

            }





        }catch (IndexOutOfBoundsException e)
        {
            Log.e("error drivers","index < 0");
        }
    }
    private void sendOrders(List<String> drivers_ids) {
        Retrofit retrofit = Api.getClient(Tags.BASE_URL);
        Services services = retrofit.create(Services.class);
        Call<ResponseModel> call = services.SendToOtherDriver(order_id,userModel.getUser_id(), drivers_ids);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess_order()==1)
                    {
                        dialog.dismiss();
                        Toast.makeText(DriverOrderDetailsActivity.this, R.string.respons_send_todriver, Toast.LENGTH_LONG).show();                        finish();
                    }else
                    {
                        Toast.makeText(DriverOrderDetailsActivity.this, R.string.order_notsent, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(DriverOrderDetailsActivity.this,getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();
                Log.e("Error",t.getMessage());
                finish();
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
    public void UserDataSuccess(UserModel userModel) {
        this.userModel = userModel;
        userId=userModel.getUser_id();

    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.accept:
                SendAccept();
                break;
            case R.id.refuse:
                SendRefuse();

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
