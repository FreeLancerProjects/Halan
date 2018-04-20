package com.semicolon.Halan.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.semicolon.Halan.Models.AboutUsModel;
import com.semicolon.Halan.R;
import com.semicolon.Halan.Services.Api;
import com.semicolon.Halan.Services.Services;
import com.semicolon.Halan.Services.Tags;

import java.util.List;

import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RulesActivity extends AppCompatActivity {
    private TextView title, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "JannaLT-Regular.ttf", true);

        initView();
        GetDataFromServer();
    }

    private void initView() {
        title = findViewById(R.id.txt_title);
        content = findViewById(R.id.txt_content);

    }

    private void GetDataFromServer() {

        Services services = Api.getClient(Tags.BASE_URL).create(Services.class);
        Call<List<AboutUsModel>> call = services.GetAboutUs();
        call.enqueue(new Callback<List<AboutUsModel>>() {
            @Override
            public void onResponse(Call<List<AboutUsModel>> call, Response<List<AboutUsModel>> response) {

                if (response.isSuccessful()) {
                    title.setText(response.body().get(2).getTitle());
                    content.setText(response.body().get(2).getContent());
                }
            }

            @Override
            public void onFailure(Call<List<AboutUsModel>> call, Throwable t) {
                Log.e("error",t.getMessage());
            }
        });
    }

}
