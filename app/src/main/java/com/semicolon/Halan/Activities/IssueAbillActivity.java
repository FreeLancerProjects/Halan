package com.semicolon.Halan.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.semicolon.Halan.Models.ResponseModel;
import com.semicolon.Halan.R;
import com.semicolon.Halan.Services.Api;
import com.semicolon.Halan.Services.Services;
import com.semicolon.Halan.Services.Tags;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class IssueAbillActivity extends AppCompatActivity {
    private ImageView back,bill_img,select_photo;
    private EditText issue_price;
    private Button issueBtn;
    private final int IMG_REQ= 2352;
    private Bitmap bitmap;
    private String encoded_image;
    private String order_id;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_abill);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "JannaLT-Regular.ttf", true);

        initView();
        getDataFromIntent();
        CreateProgressDialog();

    }

    private void CreateProgressDialog()
    {
        ProgressBar bar = new ProgressBar(this);
        Drawable drawable = bar.getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.sendingbill));
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        dialog.setIndeterminateDrawable(drawable);
    }
    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null)
        {
            order_id = intent.getStringExtra("order_id");
        }
    }

    private void initView() {
        back = findViewById(R.id.back);
        bill_img = findViewById(R.id.bill_img);
        select_photo = findViewById(R.id.select_img);
        issue_price = findViewById(R.id.bill_price);
        issueBtn = findViewById(R.id.issue_bill_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        select_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Select_Image();
            }
        });
        issueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmap!=null && !TextUtils.isEmpty(issue_price.getText().toString()))
                {
                    dialog.show();
                    Send_Bill(issue_price.getText().toString());
                    issue_price.setError(null);
                }else if (bitmap==null)
                {
                    Toast.makeText(IssueAbillActivity.this, R.string.chos_bill_img, Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(issue_price.getText().toString()))
                {
                    issue_price.setError(getString(R.string.bill_price));
                }
            }
        });

    }

    private void Send_Bill(String price) {
        Retrofit retrofit = Api.getClient(Tags.BASE_URL);
        Services services = retrofit.create(Services.class);
        Call<ResponseModel> call = services.SendOrderBill(order_id, encoded_image, price);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess()==1)
                    {
                        dialog.dismiss();
                        Toast.makeText(IssueAbillActivity.this, R.string.sendbill_done, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(IssueAbillActivity.this, R.string.something_haywire, Toast.LENGTH_LONG).show();
                Log.e("error",t.getMessage());
            }
        });
    }

    private void Select_Image() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent,"select photo"),IMG_REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQ && resultCode == RESULT_OK && data !=null)
        {
            Uri uri = data.getData();
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                bill_img.setImageBitmap(bitmap);
                encodeImage(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void encodeImage(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,90,outputStream);
        byte [] bytes = outputStream.toByteArray();
        encoded_image = Base64.encodeToString(bytes,Base64.DEFAULT);

    }
}
