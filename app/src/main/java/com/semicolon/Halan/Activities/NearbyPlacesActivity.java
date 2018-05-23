package com.semicolon.Halan.Activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.semicolon.Halan.Adapters.NearbyAdapter;
import com.semicolon.Halan.Models.NearbyItem;
import com.semicolon.Halan.Models.NearbyModel;
import com.semicolon.Halan.R;
import com.semicolon.Halan.Services.Api;
import com.semicolon.Halan.Services.Services;
import com.semicolon.Halan.Services.Tags;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NearbyPlacesActivity extends AppCompatActivity {
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private ImageView back;
    private NearbyAdapter adapter;
    private List<NearbyItem> nearbyItemList;
    private String query="";
    private double lat,lng;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_places);
        initView();
        getDataFromIntent();
    }



    private void initView() {
        nearbyItemList = new ArrayList<>();
        progressBar = findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        adapter = new NearbyAdapter(this,nearbyItemList);
        recView = findViewById(R.id.recView);
        manager = new LinearLayoutManager(this);
        recView.setHasFixedSize(true);
        recView.setLayoutManager(manager);
        recView.setAdapter(adapter);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null)
        {
            if (intent.hasExtra("query"))
            {
                query = intent.getStringExtra("query");
                lat = intent.getDoubleExtra("lat",0.0);
                lng = intent.getDoubleExtra("lng",0.0);
                Log.e("q",query);
                Log.e("l",lat+"");
                Log.e("l",lng+"");
                getNearbyPlaces(query,lat,lng);

            }
        }
    }

    private void getNearbyPlaces(String query, double lat, double lng) {
        String Url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+lat+","+lng+"&radius=50000&name="+query+"&key=AIzaSyAxeE8tdOORtoR8pHwSi_EozkgvYu0Q220";
        Retrofit retrofit = Api.getClient(Tags.PLACE_URL);
        Services services = retrofit.create(Services.class);
        Call<NearbyModel> call = services.getNearbyPlaces(Url);
        call.enqueue(new Callback<NearbyModel>() {
            @Override
            public void onResponse(Call<NearbyModel> call, Response<NearbyModel> response) {
                if (response.isSuccessful())
                {
                    Log.e("staatus",response.body().getStatus());
                    if (response.body().getResults()!=null && response.body().getResults().size()>0)
                    {

                        OrderData( response.body().getResults());


                    }
                }
            }

            @Override
            public void onFailure(Call<NearbyModel> call, Throwable t) {
                progressBar.setVisibility(View.GONE);

                Log.e("error",t.getMessage());
                Toast.makeText(NearbyPlacesActivity.this,R.string.something_haywire, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void OrderData(List<NearbyModel.ResultObject> resultObjects) {
        try {
            Map<String,NearbyItem> MainMap = new HashMap<>();
            Map<String,Double> key_dis = new HashMap<>();
            List<Double> disList = new ArrayList<>();
            //List<String> keys = new ArrayList<>();
            List<NearbyItem> resultObjectList = new ArrayList<>();

            for (int i=0;i<resultObjects.size();i++)
            {
                double dis = distance(lat,lng,resultObjects.get(i).getGeometryObject().getLocationObject().getLat(),resultObjects.get(i).getGeometryObject().getLocationObject().getLng());
                NearbyItem nearbyItem = new NearbyItem(resultObjects.get(i).getIcon(),resultObjects.get(i).getName(),dis,resultObjects.get(i).getGeometryObject().getLocationObject().getLat(),resultObjects.get(i).getGeometryObject().getLocationObject().getLng());
                MainMap.put(resultObjects.get(i).getPlace_id(),nearbyItem);

                key_dis.put(resultObjects.get(i).getPlace_id(),dis);
                disList.add(dis);

            }


            Collections.sort(disList);

            for (Double aDouble :disList)
            {

                for (String key:key_dis.keySet())
                {
                    if (aDouble.equals(key_dis.get(key)))
                    {
                        resultObjectList.add(MainMap.get(key));
                    }
                }
            }



            progressBar.setVisibility(View.GONE);
            this.nearbyItemList.addAll(resultObjectList);
            adapter.notifyDataSetChanged();


        }catch (NullPointerException e)
        {
            progressBar.setVisibility(View.GONE);

        }catch (NumberFormatException e)
        {
            progressBar.setVisibility(View.GONE);

        }catch (Exception e)
        {
            progressBar.setVisibility(View.GONE);

        }









    }
    public void setItem(NearbyItem nearbyItem)
    {
        Intent intent = getIntent();
        intent.putExtra("lat",nearbyItem.getLat());
        intent.putExtra("lng",nearbyItem.getLng());
        intent.putExtra("name",nearbyItem.getName());
        setResult(RESULT_OK,intent);
        finish();
        //Toast.makeText(this, ""+nearbyItem.getName(), Toast.LENGTH_SHORT).show();
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
