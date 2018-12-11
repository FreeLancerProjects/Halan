package com.semicolon.Halan.Activities;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
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
import com.semicolon.Halan.Models.MyOrderModel;
import com.semicolon.Halan.Models.PlaceModel;
import com.semicolon.Halan.Models.ResponseModel;
import com.semicolon.Halan.Models.UserModel;
import com.semicolon.Halan.R;
import com.semicolon.Halan.Services.Api;
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

public class OrderDeliveryActivity extends AppCompatActivity implements Users.UserData,OnMapReadyCallback{
    private TextView order_from,order_to,cost;
    private Button cancelBtn,chatBtn,deliveryBtn,yesBtn,noBtn;
    private RadioButton driver_late_Rbtn,noRbtn,otherRbtn;
    private Toolbar toolBar;
    private String fine_loc = Manifest.permission.ACCESS_FINE_LOCATION;
    private String coarse_loc = Manifest.permission.ACCESS_COARSE_LOCATION;
    private final int error_dialog = 9001;
    private final int per_REQ = 2102;
    private boolean isGranted = false;
    private GoogleMap mMap;
    private Users users ;
    private UserModel userModel;
    private MyOrderModel myOrderModel;
    private LatLng client_latLng,market_latLng;
    private double dist;
    private AlertDialog alertDialog,cancelAlertDialog;
    private String curr_id,chat_id;
    private String curr_type,chat_type;
    private String curr_img,chat_img;
    private List<String> drivers_ids;
    private ProgressDialog dialog;
    private String provider;
    private AlertDialog.Builder gpsBuilder;
    private LocationManager locationManager;
    private final int GPS_REQ = 3299;
    private ImageView notf;
    private String order_cost="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_delivery);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "JannaLT-Regular.ttf", true);
        EventBus.getDefault().register(this);
        users = Users.getInstance();
        users.getUserData(this);
        initView();
        getDataFromIntent();
        Create_AlertDialog();
        OpenGps();


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
            if (isServiceOk())
            {
                checkPermission();
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

        Picasso.with(OrderDeliveryActivity.this).load(Uri.parse(Tags.ImgPath+finishied_order_model.getDriver_image())).into(driver_img);
        driver_name.setText(finishied_order_model.getDriver_name());
        order_details.setText(finishied_order_model.getOrder_details());
        Button addRateBtn = view.findViewById(R.id.add_rate);
        final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(OrderDeliveryActivity.this)
                .setCancelable(false)
                .setView(view)
                .create();

        alertDialog.show();
        addRateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderDeliveryActivity.this,AddRateActivity.class);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GPS_REQ )
        {
            if (isServiceOk())
            {
                checkPermission();
            }
        }
    }
    private void initView()
    {
        drivers_ids = new ArrayList<>();
        toolBar    = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);

        order_from = findViewById(R.id.txt_order_from);
        order_to   = findViewById(R.id.txt_order_to);
        cost       = findViewById(R.id.cost);
        cancelBtn  = findViewById(R.id.cancelBtn);
        chatBtn    = findViewById(R.id.chatBtn);
        deliveryBtn= findViewById(R.id.deliveryBtn);
        notf = findViewById(R.id.notf);
        notf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userModel.getUser_type().equals(Tags.Client))
                {
                    Intent intent = new Intent(OrderDeliveryActivity.this,ClientNotificationActivity.class);
                    startActivity(intent);
                }else if (userModel.getUser_type().equals(Tags.Driver))
                {
                    Intent intent = new Intent(OrderDeliveryActivity.this,DriverNotificationActivity.class);
                    startActivity(intent);
                }
            }
        });
        if (userModel.getUser_type().equals(Tags.Client))
        {
            deliveryBtn.setVisibility(View.GONE);

        }
        else
            {
                deliveryBtn.setVisibility(View.VISIBLE);

            }
        deliveryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userModel.getUser_type().equals(Tags.Client))
                {
                    CreateProgDialog(getString(R.string.confirm_delev));
                    dialog.show();
                    OrderDelivered("2");

                }else if (userModel.getUser_type().equals(Tags.Driver))
                {
                    CreateProgDialog(getString(R.string.confirm_delev));
                    dialog.show();
                    OrderDelivered("1");

                }
            }
        });

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Create_Chat(myOrderModel.getRoom_id());

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userModel.getUser_type().equals(Tags.Client))
                {
                    alertDialog.show();


                }else
                    {
                        Create_Cancel_AlertDialog();
                        cancelAlertDialog.show();
                    }
            }
        });

    }



    private void OrderDelivered(final String user_type)
    {
        Retrofit retrofit = Api.getClient(Tags.BASE_URL);
        Services services = retrofit.create(Services.class);
        Call<ResponseModel> call = services.orderDeliverd(myOrderModel.getOrder_id(), user_type);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {

                if (response.isSuccessful())
                {
                    if (response.body().getSuccess()==1)
                    {
                        dialog.dismiss();
                        if (user_type.equals("1"))
                        {
                            finish();
                            //driver
                        }/*else if (user_type.equals("2"))
                        {
                            Intent intent = new Intent(OrderDeliveryActivity.this,AddRateActivity.class);
                            intent.putExtra("order",myOrderModel);
                            startActivity(intent);
                            finish();
                            //client
                        }*/
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {

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
    private void Create_Chat(String room_id) {

        Intent intent = new Intent(OrderDeliveryActivity.this, ChatActivity.class);
        intent.putExtra("curr_id", curr_id);
        intent.putExtra("chat_id", chat_id);
        intent.putExtra("curr_type", curr_type);
        intent.putExtra("chat_type", chat_type);
        intent.putExtra("curr_photo", curr_img);
        intent.putExtra("chat_photo", chat_img);
        intent.putExtra("order_cost",order_cost);
        intent.putExtra("order_id", myOrderModel.getOrder_id());
        intent.putExtra("room_id",room_id);
        intent.putExtra("order_details",myOrderModel.getOrder_details());
        startActivity(intent);
        finish();
/*
        dRef.child("typing").child(curr_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.hasChild(chat_id)) {
                    String path1 = "/" + curr_id + "/" + chat_id;
                    String path2 = "/" + chat_id + "/" + curr_id;

                    Map dataMap = new HashMap();
                    dataMap.put("type", false);

                    Map map = new HashMap();
                    map.put(path1, dataMap);
                    map.put(path2, dataMap);

                    dRef.child("typing").updateChildren(map).addOnSuccessListener(new OnSuccessListener() {
                        @Override
                        public void onSuccess(Object o) {
                            Intent intent = new Intent(OrderDeliveryActivity.this, ChatActivity.class);
                            intent.putExtra("curr_id", curr_id);
                            intent.putExtra("chat_id", chat_id);
                            intent.putExtra("curr_type", curr_type);
                            intent.putExtra("chat_type", chat_type);
                            intent.putExtra("curr_photo", curr_img);
                            intent.putExtra("chat_photo", chat_img);
                            intent.putExtra("order_cost",order_cost);
                            intent.putExtra("order_id", myOrderModel.getOrder_id());
                            intent.putExtra("order_details",myOrderModel.getOrder_details());
                            startActivity(intent);
                            finish();
                        }
                    });
                }else
                    {
                        Intent intent = new Intent(OrderDeliveryActivity.this, ChatActivity.class);
                        intent.putExtra("curr_id", curr_id);
                        intent.putExtra("chat_id", chat_id);
                        intent.putExtra("curr_type", curr_type);
                        intent.putExtra("chat_type", chat_type);
                        intent.putExtra("curr_photo", curr_img);
                        intent.putExtra("chat_photo", chat_img);
                        intent.putExtra("order_cost",order_cost);
                        intent.putExtra("order_id", myOrderModel.getOrder_id());
                        intent.putExtra("order_details",myOrderModel.getOrder_details());

                        startActivity(intent);
                        finish();
                    }


                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/
    }


    private void initMap()
    {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    private void getDataFromIntent()
    {
        Intent intent = getIntent();
        if (intent!=null)
        {
            myOrderModel = (MyOrderModel) intent.getSerializableExtra("order");
            client_latLng = new LatLng(myOrderModel.getClient_google_lat(),myOrderModel.getClient_google_lang());
            market_latLng = new LatLng(myOrderModel.getMarket_google_lat(),myOrderModel.getMarket_google_lang());

            if (userModel.getUser_type().equals(Tags.Client))
            {
                curr_id = userModel.getUser_id();
                chat_id = myOrderModel.getDriver_id();
                curr_type = userModel.getUser_type();
                chat_type = Tags.Driver;
                curr_img = userModel.getUser_photo();
                chat_img = myOrderModel.getDriver_photo();
                order_cost = myOrderModel.getCost();

            }else if (userModel.getUser_type().equals(Tags.Driver))
            {
                curr_id = userModel.getUser_id();
                chat_id = myOrderModel.getClient_id();
                curr_type = userModel.getUser_type();
                chat_type = Tags.Client;
                curr_img = userModel.getUser_photo();
                chat_img = myOrderModel.getClient_photo();
                order_cost = myOrderModel.getCost();

            }
            updateUi(myOrderModel);
        }
    }

    private void Create_Cancel_AlertDialog()
    {
        cancelAlertDialog = new AlertDialog.Builder(this)
                .setMessage(R.string.cancelOrder)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                                cancelAlertDialog.dismiss();
                                CreateProgDialog(getString(R.string.cancelling_ord));
                                dialog.show();
                                DriverCancelOrder();

                    }
                })
                .setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        cancelAlertDialog.dismiss();
                    }
                }).create();
    }
    private void Create_AlertDialog()
    {
        View view = LayoutInflater.from(this).inflate(R.layout.custom_refuse_dialog,null);
        yesBtn = view.findViewById(R.id.yesBtn);
        noBtn  = view.findViewById(R.id.noBtn);
        driver_late_Rbtn = view.findViewById(R.id.driver_lateRBtn);
        noRbtn = view.findViewById(R.id.noRBtn);
        otherRbtn = view.findViewById(R.id.otherRBtn);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (noRbtn.isChecked())
                {
                    CreateProgDialog(getString(R.string.cancelling_ord));
                    dialog.show();
                    ClientCancelOrder("0","2");
                }else if (driver_late_Rbtn.isChecked())
                {
                    CreateProgDialog(getString(R.string.cancelling_ord));
                    dialog.show();
                    ClientCancelOrder("0","1");

                }
                else if (otherRbtn.isChecked())
                {
                    CreateProgDialog(getString(R.string.cancelling_ord));
                    dialog.show();
                    ClientCancelOrder("0","3");

                }
                alertDialog.dismiss();
            }
        });
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (noRbtn.isChecked())
                {
                    CreateProgDialog(getString(R.string.cancel_andSend));
                    dialog.show();
                    ClientCancelOrder("1","2");

                }else if (driver_late_Rbtn.isChecked())
                {
                    CreateProgDialog(getString(R.string.cancel_andSend));
                    dialog.show();
                    ClientCancelOrder("1","1");


                }
                else if (otherRbtn.isChecked())
                {
                    CreateProgDialog(getString(R.string.cancel_andSend));
                    dialog.show();
                    ClientCancelOrder("1","3");


                }
                alertDialog.dismiss();

            }
        });
        alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(true)
                .create();

    }

    private void ClientCancelOrder(final String sendtoAnotherDrivers, String cancel_type)
    {
        Retrofit retrofit = Api.getClient(Tags.BASE_URL);
        Services services = retrofit.create(Services.class);
        Call<ResponseModel> call = services.clientCancelOrder(userModel.getUser_id(), myOrderModel.getOrder_id(), myOrderModel.getDriver_id(), cancel_type);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess()==1)
                    {
                        if (sendtoAnotherDrivers.equals("0"))
                        {
                            Toast.makeText(OrderDeliveryActivity.this, R.string.orded_cancelled, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            finish();
                        }else if (sendtoAnotherDrivers.equals("1"))
                        {
                            sendTo_AnotherDrivers();

                        }
                    }


                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(OrderDeliveryActivity.this,getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();
                Log.e("Error",t.getMessage());
            }
        });

    }
    private void DriverCancelOrder()
    {

        Retrofit retrofit = Api.getClient(Tags.BASE_URL);
        Services services = retrofit.create(Services.class);
        Call<ResponseModel> call = services.driverCancelOrder(userModel.getUser_id(), myOrderModel.getOrder_id(), myOrderModel.getClient_id_fk());
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess()==1)
                    {
                        dialog.dismiss();
                        Toast.makeText(OrderDeliveryActivity.this, R.string.orded_cancelled, Toast.LENGTH_LONG).show();
                        finish();
                    }
                          }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(OrderDeliveryActivity.this, getString(R.string.something_haywire), Toast.LENGTH_LONG).show();
                Log.e("Error",t.getMessage());
            }
        });
    }
    private void sendTo_AnotherDrivers()

    {
        ShowAvailable_Drivers();
    }

    private void updateUi(MyOrderModel myOrderModel)
    {
        order_from.setText(myOrderModel.getMarket_location());
        order_to.setText(myOrderModel.getClient_location());
        cost.setText(myOrderModel.getCost());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (googleMap !=null)
        {
            mMap = googleMap;
            mMap = googleMap;
            mMap.setIndoorEnabled(true);
            mMap.setTrafficEnabled(false);
            mMap.setBuildingsEnabled(true);
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.maps));
            try {
                if (isGranted)
                {
                    getDirection(client_latLng,market_latLng);

                }
            }catch (SecurityException e)
            {

            }
        }
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
                Toast.makeText(OrderDeliveryActivity.this, ""+getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();
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
                if (client_latLng.latitude!=0.0 || client_latLng.longitude!=0.0 &&
                        driversModel.getUser_google_lat()!=null||!TextUtils.isEmpty(driversModel.getUser_google_lat()) ||
                        driversModel.getUser_google_long()!=null||!TextUtils.isEmpty(driversModel.getUser_google_long()) )
                {
                    double dis = distance(client_latLng.latitude,client_latLng.longitude,Double.parseDouble(driversModel.getUser_google_lat()),Double.parseDouble(driversModel.getUser_google_long()));

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
            }else if(sortedArray.size()==0)
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
        Call<ResponseModel> call = services.SendToNewDriver(myOrderModel.getOrder_id(),userModel.getUser_id(), drivers_ids);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess_order()==1)
                    {
                        dialog.dismiss();
                        Toast.makeText(OrderDeliveryActivity.this,R.string.order_sent, Toast.LENGTH_LONG).show();
                        finish();
                    }else
                    {
                        Toast.makeText(OrderDeliveryActivity.this, R.string.order_notsent, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(OrderDeliveryActivity.this,getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();
                Log.e("Error",t.getMessage());
            }
        });

    }


    @Override
    public void UserDataSuccess(UserModel userModel) {
        this.userModel = userModel;
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
    private void checkPermission()
    {
        String [] Permissions = new String[]{fine_loc,coarse_loc};
        if (ContextCompat.checkSelfPermission(this,fine_loc)== PackageManager.PERMISSION_GRANTED)
        {
            if (ContextCompat.checkSelfPermission(this,coarse_loc)== PackageManager.PERMISSION_GRANTED)
            {
                isGranted = true;
                initMap();
            }else
                {
                    ActivityCompat.requestPermissions(this,Permissions,per_REQ);
                }
        }else
        {
            ActivityCompat.requestPermissions(this,Permissions,per_REQ);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        isGranted = false;
        if (requestCode == per_REQ)
        {
            if (grantResults.length>0)
            {
                for (int i=0;i<grantResults.length;i++)
                {
                    if (grantResults[i] !=PackageManager.PERMISSION_GRANTED )
                    {
                        isGranted = false;
                        return;
                    }
                }
                isGranted = true;
                initMap();
            }

        }
    }

    private void getDirection(final LatLng client_latLng, final LatLng market_latLng) {
        Retrofit retrofit = Api.getClient(Tags.Map_BaseUrl);
        Services services = retrofit.create(Services.class);
        String Origin = String.valueOf(client_latLng.latitude)+","+String.valueOf(client_latLng.longitude);
        String Dest   = String.valueOf(market_latLng.latitude)+","+String.valueOf(market_latLng.longitude);
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
                            dist = distance(client_latLng.latitude,client_latLng.longitude,market_latLng.latitude,market_latLng.longitude);
                            //Toast.makeText(OrderDeliveryActivity.this, "dist2"+dist, Toast.LENGTH_SHORT).show();

                        }catch (IndexOutOfBoundsException e)
                        {
                            Toast.makeText(OrderDeliveryActivity.this, "empty data", Toast.LENGTH_SHORT).show();
                        }
                        String durat = placeModel.getRoutes().get(0).getLegs().get(0).getDuration().getText();
                        List<String> polylines = new ArrayList<>();
                        List<PlaceModel.Steps> stepsModelList =placeModel.getRoutes().get(0).getLegs().get(0).getSteps();

                        AddMarker(client_latLng,durat);
                        AddMarker(market_latLng,durat);
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


                    }

                }
            }

            @Override
            public void onFailure(Call<PlaceModel> call, Throwable t) {
                Toast.makeText(OrderDeliveryActivity.this, getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        if (userModel.getUser_type().equals(Tags.Client))
        {
            getMenuInflater().inflate(R.menu.order_details_menu,menu);

        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.details)
        {
            if (userModel.getUser_type().equals(Tags.Client))
            {
                if (userModel.getUser_type().equals(Tags.Client))
                {
                    Intent intent = new Intent(OrderDeliveryActivity.this,ClientOrderDetailsActivity.class);
                    intent.putExtra("order",myOrderModel);
                    startActivity(intent);
                }else if (userModel.getUser_type().equals(Tags.Driver))
                {
                    Intent intent = new Intent(OrderDeliveryActivity.this,DriverOrderDetailActivity.class);
                    intent.putExtra("order",myOrderModel);
                    startActivity(intent);
                }
            }
        }/*else if (id == R.id.addRate)
        {
            Toast.makeText(this, "add rate", Toast.LENGTH_SHORT).show();

        }*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
