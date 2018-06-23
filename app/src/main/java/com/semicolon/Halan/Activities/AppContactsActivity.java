package com.semicolon.Halan.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.semicolon.Halan.Adapters.AccountsAdapter;
import com.semicolon.Halan.Models.BankAccountModel;
import com.semicolon.Halan.Models.ContactModel;
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

public class AppContactsActivity extends AppCompatActivity implements View.OnClickListener{
    private ProgressBar progressBar;
    private RecyclerView recView;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter adapter;
    private List<BankAccountModel> bankAccountModelList;
    private LinearLayout fb,tw,in,sn;
    private TextView txt_fb,txt_tw,txt_in,txt_sn,txt_ph;
    private ContactModel contactModel;
    private ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_contacts);
        initView();
        getAccounts();
        Contacts();

    }



    private void initView() {
        bankAccountModelList = new ArrayList<>();
        progressBar = findViewById(R.id.progBar);
        recView = findViewById(R.id.recView);
        manager = new LinearLayoutManager(this);
        recView.setLayoutManager(manager);
        adapter = new AccountsAdapter(this,bankAccountModelList);
        recView.setAdapter(adapter);
        back = findViewById(R.id.back);
        fb =findViewById(R.id.fb);
        tw = findViewById(R.id.tw);
        in = findViewById(R.id.in);
        sn = findViewById(R.id.sn);

        txt_fb =findViewById(R.id .txt_fb);
        txt_tw = findViewById(R.id.txt_tw);
        txt_in = findViewById(R.id.txt_in);
        txt_sn = findViewById(R.id.txt_sn);
        txt_ph = findViewById(R.id.txt_ph);

        fb.setOnClickListener(this);
        tw.setOnClickListener(this);
        in.setOnClickListener(this);
        sn.setOnClickListener(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getAccounts() {
        Retrofit retrofit = Api.getClient(Tags.BASE_URL);
        Services services = retrofit.create(Services.class);
        Call<List<BankAccountModel>> call = services.getBankAccounts();
        call.enqueue(new Callback<List<BankAccountModel>>() {
            @Override
            public void onResponse(Call<List<BankAccountModel>> call, Response<List<BankAccountModel>> response) {
                if (response.isSuccessful())
                {
                    progressBar.setVisibility(View.GONE);
                    bankAccountModelList.clear();
                    bankAccountModelList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<BankAccountModel>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(AppContactsActivity.this,R.string.something_haywire, Toast.LENGTH_SHORT).show();
                Log.e("Error",t.getMessage());
            }
        });

    }

    private void Contacts (){

        Services services = Api.getClient(Tags.BASE_URL).create(Services.class);
        Call<ContactModel> call = services.getContacts();
        call.enqueue(new Callback<ContactModel>() {
            @Override
            public void onResponse(Call<ContactModel> call, Response<ContactModel> response) {
                if (response.isSuccessful()) {
                    contactModel = response.body();

                    if (contactModel.getOur_phone_number()==null||TextUtils.isEmpty(contactModel.getOur_phone_number()))
                    {
                        txt_ph.setText("لايوجد رقم جوال");
                    }else if (contactModel.getOur_phone_number()!=null||!TextUtils.isEmpty(contactModel.getOur_phone_number()))
                    {
                        txt_ph.setText(contactModel.getOur_phone_number());

                    }
                    else if (contactModel.getTwitter_url()==null||TextUtils.isEmpty(contactModel.getTwitter_url()))
                    {
                        txt_tw.setText("لايوجد حساب تويتر ");

                    }else if (contactModel.getInstgram_url()==null||TextUtils.isEmpty(contactModel.getInstgram_url()))
                    {
                        txt_in.setText("لايوجد حساب انستقرام ");

                    }else if (contactModel.getFacebook_url()==null||TextUtils.isEmpty(contactModel.getFacebook_url()))
                    {
                        txt_fb.setText("لايوجد حساب فيس بوك ");

                    }else if (contactModel.getSnapchat_url()!=null||!TextUtils.isEmpty(contactModel.getSnapchat_url()))
                    {
                        txt_sn.setText("لايوجد حساب اسناب شات");

                    }
                } else {

                    Toast.makeText(AppContactsActivity.this, "" + getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ContactModel> call, Throwable t) {
                Log.e("mmmmm", t.getMessage() + "");
                Toast.makeText(AppContactsActivity.this, "" + getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {

            case R.id.tw:
                Intent intent_tw = new Intent(AppContactsActivity.this,WebViewActivity.class);
                intent_tw.putExtra("link",contactModel.getTwitter_url());
                startActivity(intent_tw);
                break;
            case R.id.in:
                Intent intent_in = new Intent(AppContactsActivity.this,WebViewActivity.class);
                intent_in.putExtra("link",contactModel.getInstgram_url());
                startActivity(intent_in);

                break;
            case R.id.fb:
                Intent intent_fb = new Intent(AppContactsActivity.this,WebViewActivity.class);
                intent_fb.putExtra("link",contactModel.getFacebook_url());
                startActivity(intent_fb);

                break;
            case R.id.sn:

                Intent intent_sn = new Intent(AppContactsActivity.this,WebViewActivity.class);
                intent_sn.putExtra("link",contactModel.getSnapchat_url());
                startActivity(intent_sn);

                break;


        }

    }
}
