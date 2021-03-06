package com.semicolon.Halan.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.semicolon.Halan.Models.Finishied_Order_Model;
import com.semicolon.Halan.Models.MessageModel;
import com.semicolon.Halan.Models.ResponseModel;
import com.semicolon.Halan.R;
import com.semicolon.Halan.Services.Api;
import com.semicolon.Halan.Services.Common;
import com.semicolon.Halan.Services.Services;
import com.semicolon.Halan.Services.Tags;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import de.hdodenhof.circleimageview.CircleImageView;
import me.anwarshahriar.calligrapher.Calligrapher;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class IssueAbillActivity extends AppCompatActivity {
    private ImageView back,bill_img,select_photo;
    private EditText issue_price;
    private Button issueBtn;
    private final int IMG_REQ= 2352;
    private final int CAM_REQ= 2353;
    private Bitmap bitmap;
    private String encoded_image;
    private String order_id,curr_id,chat_id,curr_type,chat_type,curr_img,chat_img,order_cost,order_details,room_id;
    private ProgressDialog dialog;
    byte [] image_bytes;
    private AlertDialog.Builder builder;
    private TextView cost;
    private AlertDialog alertDialog;
    private Uri uri=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_abill);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "JannaLT-Regular.ttf", true);
        EventBus.getDefault().register(this);
        initView();
        getDataFromIntent();
        CreateProgressDialog();
        CreateAlertDialog();

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

        Picasso.with(IssueAbillActivity.this).load(Uri.parse(Tags.ImgPath+finishied_order_model.getDriver_image())).into(driver_img);
        driver_name.setText(finishied_order_model.getDriver_name());
        order_details.setText(finishied_order_model.getOrder_details());
        Button addRateBtn = view.findViewById(R.id.add_rate);
        final AlertDialog alertDialog = new AlertDialog.Builder(IssueAbillActivity.this)
                .setCancelable(false)
                .setView(view)
                .create();

        alertDialog.show();
        addRateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IssueAbillActivity.this,AddRateActivity.class);
                intent.putExtra("driver_id",finishied_order_model.getDriver_id());
                intent.putExtra("order_id",finishied_order_model.getOrder_id());
                intent.putExtra("driver_name",finishied_order_model.getDriver_name());
                intent.putExtra("driver_image",finishied_order_model.getDriver_image());
                startActivity(intent);
                alertDialog.dismiss();


            }
        });

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
            curr_id =intent.getStringExtra("curr_id");
            chat_id =intent.getStringExtra("chat_id");
            curr_type =intent.getStringExtra("curr_type");
            chat_type =intent.getStringExtra("chat_type");
            curr_img=intent.getStringExtra("curr_image");
            chat_img =intent.getStringExtra("chat_image");
            order_cost = intent.getStringExtra("order_cost");
            order_details = intent.getStringExtra("order_details");
            room_id = intent.getStringExtra("room_id");
            cost.setText(order_cost);


        }

    }

    private void initView() {
        back = findViewById(R.id.back);
        bill_img = findViewById(R.id.bill_img);
        select_photo = findViewById(R.id.select_img);
        cost = findViewById(R.id.cost);
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

                builder.show();
            }
        });

        issueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bitmap!=null && !TextUtils.isEmpty(issue_price.getText().toString()))
                {
                    View view1 = LayoutInflater.from(IssueAbillActivity.this).inflate(R.layout.custom_bill_dialog,null);
                    final TextView bill_cost = view1.findViewById(R.id.bill_cost);
                    final TextView way_cost = view1.findViewById(R.id.way_cost);
                    final TextView total_cost = view1.findViewById(R.id.total_cost);
                    bill_cost.setText(issue_price.getText().toString());
                    way_cost.setText(order_cost +" ريال");
                    final double total = Double.parseDouble(issue_price.getText().toString().trim())+Double.parseDouble(order_cost.trim());
                    total_cost.setText(String.valueOf(total)+" ريال");

                    Log.e("costs",issue_price.getText().toString());
                    Log.e("costs",order_cost);
                    Log.e("costs",String.valueOf(total));

                    Button name_updateBtn = view1.findViewById(R.id.updateBtn);
                    Button name_cancelBtn = view1.findViewById(R.id.cancelBtn);
                    name_updateBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            alertDialog.dismiss();

                            Send_Bill(uri,issue_price.getText().toString(),order_cost,String.valueOf(total),room_id);
                            issue_price.setError(null);
                        }
                    });
                    name_cancelBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alertDialog.dismiss();

                        }
                    });
                    alertDialog = new AlertDialog.Builder(IssueAbillActivity.this)
                            .setCancelable(true)
                            .setView(view1)
                            .create();
                    alertDialog.show();




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

    private void CreateAlertDialog()
    {
        String [] items = new String[]{"Open Camera","Choose from gallery"};
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Select option");
        builder.setCancelable(true);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AlertDialog alertDialog = builder.create();
                alertDialog.dismiss();
                switch (i)
                {
                    case 0:
                        Select_Image(CAM_REQ);
                        alertDialog.dismiss();
                        break;
                    case 1:
                        Select_Image(IMG_REQ);
                        alertDialog.dismiss();
                        break;
                }
            }
        });

    }
    private void Send_Bill(final Uri uri, final String price, final String way_cost, final String total_cost, final String room_id) {
        dialog.show();

        Retrofit retrofit = Api.getClient(Tags.BASE_URL);
        Services services = retrofit.create(Services.class);
        Log.e("pr",price);
        Call<ResponseModel> call = services.SendOrderBill(order_id, encoded_image, price);

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                if (response.isSuccessful())
                {
                    if (response.body().getSuccess()==1)
                    {
                        DataToSend(uri,price,way_cost,total_cost,room_id);
                        //DataToSend("");

                    }else
                        {
                            Toast.makeText(IssueAbillActivity.this, R.string.something_haywire, Toast.LENGTH_LONG).show();

                            dialog.dismiss();

                        }
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                Toast.makeText(IssueAbillActivity.this, R.string.something_haywire, Toast.LENGTH_LONG).show();
                Log.e("error",t.getMessage());
                dialog.dismiss();

            }
        });
    }

    private void UploadImage(Uri uri, final String price, final String way_cost, final String total_cost,String room_id) {

    }

    private void Select_Image(int req) {
        if (req== CAM_REQ)
        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent.createChooser(intent,"select photo"),req);
        }else if (req == IMG_REQ)
        {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent.createChooser(intent,"select photo"),req);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQ && resultCode == RESULT_OK && data !=null)
        {
            Uri uri = data.getData();
            this.uri = uri;
            try {
                bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                bill_img.setImageBitmap(bitmap);
               // encodeImage(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == CAM_REQ && resultCode == RESULT_OK && data !=null)
        {
            try {
                bitmap = (Bitmap) data.getExtras().get("data");
                bill_img.setImageBitmap(bitmap);
                encodeImage(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void encodeImage(Bitmap bitmap)
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,90,outputStream);
        image_bytes = outputStream.toByteArray();
        encoded_image = Base64.encodeToString(image_bytes,Base64.DEFAULT);

    }

    private void DataToSend(Uri uri,String price,String way_cost,String total_cost,String room_id)
    {
        String MSG ="الطلب= "+order_details+"\n" +"تلكفة الفاتورة= "+price+"\n"+"تكلفة الطريق= "+way_cost+"\n"+"إجمالي الفاتورة= "+total_cost;

        RequestBody from_id_part = Common.getRequestBodyFromData(curr_id, "text/plain");
        RequestBody to_id_part = Common.getRequestBodyFromData(chat_id, "text/plain");
        RequestBody msg_part = Common.getRequestBodyFromData(MSG, "text/plain");

        RequestBody msg_type_part = Common.getRequestBodyFromData(Tags.img_content_type, "text/plain");
        MultipartBody.Part image_part = Common.getMultipart(this,uri);

        Api.getClient(Tags.BASE_URL)
                .create(Services.class)
                .sendMessage_image(room_id,from_id_part,to_id_part,msg_part,msg_type_part,image_part)
                .enqueue(new Callback<MessageModel>() {
                    @Override
                    public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                        if (response.isSuccessful())
                        {
                            if (response.body().getSuccess_send()==1)
                            {
                                dialog.dismiss();
                                finish();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageModel> call, Throwable t) {
                        Log.e("error",t.getMessage());
                        Toast.makeText(IssueAbillActivity.this, R.string.msg_not_sent, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
