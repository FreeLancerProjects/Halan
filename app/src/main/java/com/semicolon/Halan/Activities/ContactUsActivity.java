package com.semicolon.Halan.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.semicolon.Halan.Models.ContactModel;
import com.semicolon.Halan.Models.Finishied_Order_Model;
import com.semicolon.Halan.Models.UserModel;
import com.semicolon.Halan.R;
import com.semicolon.Halan.Services.Api;
import com.semicolon.Halan.Services.Services;
import com.semicolon.Halan.Services.Tags;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import me.anwarshahriar.calligrapher.Calligrapher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText name, email, subject, message;
    private Button send;
    private ProgressDialog dialog;
    private String phone;
    private ImageView back,whatsBtn,callBtn;
    private ContactModel contactModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "JannaLT-Regular.ttf", true);
        EventBus.getDefault().register(this);
        initView();
        CreateProgressDialog();
        Contacts();


    }

    private void SaveDataToServer() {

        String cname = name.getText().toString();
        String cemail = email.getText().toString();
        final String csubject = subject.getText().toString();
        final String cmessage = message.getText().toString();
        if (TextUtils.isEmpty(cname) && TextUtils.isEmpty(cemail) && TextUtils.isEmpty(csubject) && TextUtils.isEmpty(cmessage)) {
            name.setError(getString(R.string.enter_username));
            email.setError(getString(R.string.enter_email));
            subject.setError(getString(R.string.enter_subject));
            message.setError(getString(R.string.enter_message));
        } else if (TextUtils.isEmpty(cname)) {
            name.setError(getString(R.string.enter_username));

        } else if (TextUtils.isEmpty(cemail)) {
            email.setError(getString(R.string.enter_email));
            name.setError(null);
        } else if (!Patterns.EMAIL_ADDRESS.matcher(cemail).matches()) {
            email.setError(getString(R.string.inv_email));
            name.setError(null);


        } else if (TextUtils.isEmpty(csubject)) {
            email.setError(null);
            subject.setError(getString(R.string.enter_username));

        } else if (TextUtils.isEmpty(cmessage)) {
            subject.setError(null);
            message.setError(getString(R.string.enter_username));

        } else {

            dialog.show();

            Services services = Api.getClient(Tags.BASE_URL).create(Services.class);
            Call<UserModel> call = services.ContactUs(cname, cemail, csubject, cmessage);
            call.enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    if (response.isSuccessful()) {
                        if (response.body().getSuccess() == 1) {

                            dialog.dismiss();
                            sendMail(csubject,cmessage);
                            Toast.makeText(ContactUsActivity.this, R.string.data_send, Toast.LENGTH_SHORT).show();
                            finish();

                        } else {
                            dialog.dismiss();

                            Toast.makeText(ContactUsActivity.this, R.string.failed, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        dialog.dismiss();

                        Toast.makeText(ContactUsActivity.this, "" + getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {
                    Log.e("mmmmm", t.getMessage() + "");
                    dialog.dismiss();
                    Toast.makeText(ContactUsActivity.this, "" + getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();

                }
            });
        }

    }

    private void sendMail(String csubject, String cmessage) {

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        if (contactModel!=null)
        {
            if (contactModel.getEmail()!=null||TextUtils.isEmpty(contactModel.getEmail()))
            {
                intent.putExtra(Intent.EXTRA_EMAIL,new String[]{contactModel.getEmail()});

            }else
                {
                    intent.putExtra(Intent.EXTRA_EMAIL,new String[]{"info@raqytech.com"});

                }
        }
        intent.putExtra(Intent.EXTRA_SUBJECT,csubject);
        intent.putExtra(Intent.EXTRA_TEXT,cmessage);
        PackageManager pm =getPackageManager();
        List<ResolveInfo> matches = pm.queryIntentActivities(intent, 0);
        ResolveInfo best = null;
        for(ResolveInfo info : matches)
        {
            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
            {
                best = info;

            }
        }

        if (best != null)
        {
            intent.setClassName(best.activityInfo.packageName, best.activityInfo.name);

        }

        startActivity(intent);

    }

    private void initView() {
        name = findViewById(R.id.edt_name);
        email = findViewById(R.id.edt_email);
        subject = findViewById(R.id.edt_subject);
        message = findViewById(R.id.edt_message);
        callBtn = findViewById(R.id.callBtn);
        whatsBtn = findViewById(R.id.whatsBtn);
        send = findViewById(R.id.btn_send);
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        callBtn.setOnClickListener(this);
        send.setOnClickListener(this);
        whatsBtn.setOnClickListener(this);

        if (isWhatsApp_installed())
        {
            whatsBtn.setVisibility(View.VISIBLE);
        }else
            {
                whatsBtn.setVisibility(View.INVISIBLE);

            }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.callBtn:
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
                break;

            case R.id.btn_send:
                SaveDataToServer();

                break;
            case R.id.whatsBtn:
                contact(contactModel.getOur_phone_number());
                break;
        }

    }

    private void contact(String phone) {

        if (TextUtils.isEmpty(phone)||phone.equals("#")||phone==null||!Patterns.PHONE.matcher(phone).matches())
        {
            Toast.makeText(this, R.string.inv_phone, Toast.LENGTH_SHORT).show();
            return;
        }
        if (isWhatsApp_installed())
        {
            Uri uri = Uri.parse("smsto:"+phone);
            Intent intent =new Intent(Intent.ACTION_SENDTO,uri);
            intent.setPackage("com.whatsapp");
            startActivity(intent.createChooser(intent,"via whatsapp"));
        }



}
    private boolean isWhatsApp_installed()
    {

        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo("com.whatsapp",PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void Contacts (){

        Services services = Api.getClient(Tags.BASE_URL).create(Services.class);
        Call<ContactModel> call = services.getContacts();
        call.enqueue(new Callback<ContactModel>() {
            @Override
            public void onResponse(Call<ContactModel> call, Response<ContactModel> response) {
                if (response.isSuccessful()) {
                    contactModel = response.body();
                 phone=response.body().getOur_phone_number();
                 Log.e("phone",phone);
                } else {

                    Toast.makeText(ContactUsActivity.this, "" + getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ContactModel> call, Throwable t) {
                Log.e("mmmmm", t.getMessage() + "");
                dialog.dismiss();
                Toast.makeText(ContactUsActivity.this, "" + getString(R.string.something_haywire), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void CreateProgressDialog() {
        ProgressBar bar = new ProgressBar(this);
        Drawable drawable = bar.getIndeterminateDrawable().mutate();
        drawable.setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.sending));
        dialog.setIndeterminateDrawable(drawable);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
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

        Picasso.with(ContactUsActivity.this).load(Uri.parse(Tags.ImgPath+finishied_order_model.getDriver_image())).into(driver_img);
        driver_name.setText(finishied_order_model.getDriver_name());
        order_details.setText(finishied_order_model.getOrder_details());
        Button addRateBtn = view.findViewById(R.id.add_rate);
        final AlertDialog alertDialog = new AlertDialog.Builder(ContactUsActivity.this)
                .setCancelable(false)
                .setView(view)
                .create();

        alertDialog.show();
        addRateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactUsActivity.this,AddRateActivity.class);
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
