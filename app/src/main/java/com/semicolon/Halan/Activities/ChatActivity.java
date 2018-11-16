package com.semicolon.Halan.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.semicolon.Halan.Adapters.Message_Adapter;
import com.semicolon.Halan.Models.ChatModel;
import com.semicolon.Halan.Models.Finishied_Order_Model;
import com.semicolon.Halan.Models.MessageModel;
import com.semicolon.Halan.Models.TypingModel;
import com.semicolon.Halan.Models.UserModel;
import com.semicolon.Halan.R;
import com.semicolon.Halan.Services.Api;
import com.semicolon.Halan.Services.Common;
import com.semicolon.Halan.Services.Preferences;
import com.semicolon.Halan.Services.Services;
import com.semicolon.Halan.Services.Tags;
import com.semicolon.Halan.SingleTone.Users;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import me.anwarshahriar.calligrapher.Calligrapher;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity implements Users.UserData{
    private Toolbar toolBar;
    private ImageView back,upload_imageBtn,send_imageBtn;
    private EditText ed_msg;
    private RecyclerView recView;
    private LinearLayoutManager manager;
    private RecyclerView.Adapter adapter;
    private String curr_id,chat_id;
    private String curr_type,chat_type;
    private String curr_img,chat_img,order_id,room_id;
    private final int IMG_REQ=12521;
    private List<MessageModel> messageModelList;
    private LinearLayout ll_typing;
    private CircleImageView chat_image_circle;
    private UserModel userModel;
    private Users users;
    private Preferences preferences;
    private String order_cost,order_details;
    private ProgressBar progBar;
    private ChatModel chatModel;
    private String ReadPermission = Manifest.permission.READ_EXTERNAL_STORAGE;
    private String WritePermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private String url;
    private final int WRITE_REQ= 1995,READ_REQ=1996,PER_WRITE_REQ=1223;
    private MediaPlayer mediaPlayer;
    private Uri uri;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Calligrapher calligrapher = new Calligrapher(this);
        calligrapher.setFont(this, "JannaLT-Regular.ttf", true);
        preferences = new Preferences(this);
        EventBus.getDefault().register(this);
        users = Users.getInstance();
        users.getUserData(this);
        chat_image_circle = findViewById(R.id.chat_image_circle);
        getDataFromIntent();
        initView();

    }
    private void initView() {
        messageModelList = new ArrayList<>();
        toolBar = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        progBar = findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        back = findViewById(R.id.back);
        upload_imageBtn = findViewById(R.id.upload_imageBtn);
        send_imageBtn = findViewById(R.id.send_imageBtn);
        recView = findViewById(R.id.recView);
        ed_msg = findViewById(R.id.ed_msg);
        ll_typing = findViewById(R.id.ll_typing);
        manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        manager.setStackFromEnd(true);
        manager.setReverseLayout(false);
        recView.setLayoutManager(manager);
        recView.setHasFixedSize(true);
        adapter = new Message_Adapter(this,messageModelList,chatModel);
        recView.setAdapter(adapter);

        send_imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(ed_msg.getText().toString()))
                {
                    sendMsg(Tags.txt_content_type,null);
                }
            }
        });

        upload_imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CheckReadPermission();
                //selectImage();
            }
        });
        ed_msg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (ed_msg.getText().toString().length()>0)
                {
                    changeTypingState(Tags.typing);

                }else
                {
                    changeTypingState(Tags.end_typing);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        mediaPlayer = new MediaPlayer();
        mediaPlayer.setLooping(true);
        uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.typing);
        try {
            mediaPlayer.setDataSource(this,uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    private void getDataFromIntent() {

        Intent intent = getIntent();
        if (intent!=null)
        {
            curr_id =intent.getStringExtra("curr_id");
            chat_id =intent.getStringExtra("chat_id");
            curr_type =intent.getStringExtra("curr_type");
            chat_type =intent.getStringExtra("chat_type");
            curr_img=intent.getStringExtra("curr_photo");
            chat_img =intent.getStringExtra("chat_photo");
            order_id = intent.getStringExtra("order_id");
            order_cost = intent.getStringExtra("order_cost");
            order_details = intent.getStringExtra("order_details");
            room_id = intent.getStringExtra("room_id");
            Log.e("room_id",room_id);
            //chatModel = new ChatModel(curr_id,chat_id,curr_type,chat_type,curr_img,chat_img);
            chatModel = new ChatModel(curr_id,curr_type,curr_img,chat_type,chat_img);
            preferences.Createpref_chat_user_id(chat_id);
            Picasso.with(this).load(Uri.parse(Tags.ImgPath+chat_img)).into(chat_image_circle);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        messageModelList.clear();
        adapter.notifyDataSetChanged();
        DisplayMessage(room_id);

    }

    private void DisplayMessage(String room_id) {
        Api.getClient(Tags.BASE_URL)
                .create(Services.class)
                .getAllMessage(room_id)
                .enqueue(new Callback<List<MessageModel>>() {
                    @Override
                    public void onResponse(Call<List<MessageModel>> call, Response<List<MessageModel>> response) {
                        if (response.isSuccessful())
                        {
                            progBar.setVisibility(View.GONE);
                            messageModelList.clear();
                            messageModelList.addAll(response.body());
                            adapter.notifyItemInserted(0);
                            recView.scrollToPosition(messageModelList.size()-1);

                        }
                    }

                    @Override
                    public void onFailure(Call<List<MessageModel>> call, Throwable t) {
                        progBar.setVisibility(View.GONE);
                        Toast.makeText(ChatActivity.this, R.string.something_haywire, Toast.LENGTH_SHORT).show();
                        Log.e("Error",t.getMessage());
                    }
                });

       /* final DatabaseReference  reference = dRef.child("messages").child(curr_id).child(chat_id);
        reference.keepSynced(true);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot!=null)
                {
                    if (dataSnapshot.getValue()!=null)
                    {
                        messageModelList.clear();
                        for (DataSnapshot ds:dataSnapshot.getChildren())
                        {
                            MessageModel messageModel = ds.getValue(MessageModel.class);
                            messageModelList.add(messageModel);
                            adapter.notifyDataSetChanged();
                        }
                        recView.scrollToPosition(messageModelList.size()-1);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ListenForTyping(TypingModel typingModel) {
        if (typingModel.getTyping_value().equals(Tags.typing))
        {
            if (mediaPlayer!=null)
            {
                if (!mediaPlayer.isPlaying())
                {

                    try {
                        mediaPlayer.prepare();
                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
                                mediaPlayer.start();
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            ll_typing.setVisibility(View.VISIBLE);

        }else
        {
            if (mediaPlayer!=null)
            {
                if (mediaPlayer.isPlaying())
                {
                    mediaPlayer.stop();
                }
            }
            ll_typing.setVisibility(View.GONE);

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Driver_delivered_Order(Finishied_Order_Model finishied_order_model)
    {
        CreateCustomAlertDialog(finishied_order_model);
    }
    @Subscribe(threadMode =ThreadMode.MAIN)
    public void AddNewMessage(MessageModel messageModel)
    {
        messageModelList.add(messageModel);
        adapter.notifyItemInserted(messageModelList.size()-1);
        recView.scrollToPosition(messageModelList.size()-1);
    }

    private void CreateCustomAlertDialog(final Finishied_Order_Model finishied_order_model) {
        View view = LayoutInflater.from(this).inflate(R.layout.custom_alert_dialog,null);
        CircleImageView driver_img = view.findViewById(R.id.driver_image);
        TextView driver_name = view.findViewById(R.id.driver_name);
        TextView order_details = view.findViewById(R.id.order_details);

        Picasso.with(ChatActivity.this).load(Uri.parse(Tags.ImgPath+finishied_order_model.getDriver_image())).into(driver_img);
        driver_name.setText(finishied_order_model.getDriver_name());
        order_details.setText(finishied_order_model.getOrder_details());
        Button addRateBtn = view.findViewById(R.id.add_rate);
        final AlertDialog alertDialog = new AlertDialog.Builder(ChatActivity.this)
                .setCancelable(false)
                .setView(view)
                .create();

        alertDialog.show();
        addRateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this,AddRateActivity.class);
                intent.putExtra("driver_id",finishied_order_model.getDriver_id());
                intent.putExtra("order_id",finishied_order_model.getOrder_id());
                intent.putExtra("driver_name",finishied_order_model.getDriver_name());
                intent.putExtra("driver_image",finishied_order_model.getDriver_image());
                startActivity(intent);
                alertDialog.dismiss();


            }
        });

    }

    private void changeTypingState(String state) {
        Map<String,String> map = new HashMap<>();
        map.put("from_id",curr_id);
        map.put("to_id",chat_id);
        map.put("typing_value",state);

        Api.getClient(Tags.BASE_URL)
                .create(Services.class)
                .typing(room_id,map)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful())
                        {
                            Log.e("typing","success");
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.e("Error_typing",t.getMessage());

                    }
                });
    }

    private void selectImage() {


        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent,"Select Photo"),IMG_REQ);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQ && resultCode == RESULT_OK && data!=null)
        {

            Uri uri = data.getData();
            sendMsg(Tags.img_content_type,uri);
        }
    }

    private void sendMsg(String message_type,Uri uri) {

        if (message_type.equals(Tags.txt_content_type))
        {
            String msg = ed_msg.getText().toString();
            Map<String,String> map = new HashMap<>();
            map.put("from_id",curr_id);
            map.put("to_id",chat_id);
            map.put("message",msg);
            map.put("message_type",Tags.txt_content_type);

            Api.getClient(Tags.BASE_URL)
                    .create(Services.class)
                    .sendMessage_text(room_id,map)
                    .enqueue(new Callback<MessageModel>() {
                        @Override
                        public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                            if (response.isSuccessful())
                            {
                                Log.e("not_type",response.body().getNotification_type());
                                Log.e("success",response.body().getSuccess_send()+"");
                                if (response.body().getSuccess_send()==1)
                                {

                                    messageModelList.add(response.body());
                                    adapter.notifyItemInserted(messageModelList.size()-1);
                                    recView.scrollToPosition(messageModelList.size()-1);

                                    ed_msg.setText(null);
                                }
                                else
                                    {
                                        Toast.makeText(ChatActivity.this, R.string.msg_not_sent, Toast.LENGTH_SHORT).show();

                                    }
                            }
                        }

                        @Override
                        public void onFailure(Call<MessageModel> call, Throwable t) {
                            Log.e("error",t.getMessage());
                            Toast.makeText(ChatActivity.this, R.string.msg_not_sent, Toast.LENGTH_SHORT).show();
                        }
                    });
        }else if (message_type.equals(Tags.img_msg_type))
        {
            RequestBody from_id_part = Common.getRequestBodyFromData(curr_id, "text/plain");
            RequestBody to_id_part = Common.getRequestBodyFromData(chat_id, "text/plain");
            RequestBody msg_part = Common.getRequestBodyFromData("0", "text/plain");
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
                                    messageModelList.add(response.body());
                                    adapter.notifyItemInserted(messageModelList.size()-1);
                                    recView.scrollToPosition(messageModelList.size()-1);
                                    ed_msg.setText(null);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MessageModel> call, Throwable t) {
                            Log.e("error",t.getMessage());
                            Toast.makeText(ChatActivity.this, R.string.msg_not_sent, Toast.LENGTH_SHORT).show();
                        }
                    });
        }

            /* String msg = ed_msg.getText().toString();
            dRef.child("Messages").child(curr_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.hasChild(chat_id))
                    {
                        DataToSend(Tags.txt_msg_type,msg);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });*/

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (userModel.getUser_type().equals(Tags.Driver))
        {
            getMenuInflater().inflate(R.menu.chat_menu,menu);

        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.bill:
                Intent intent = new Intent(ChatActivity.this,IssueAbillActivity.class);
                intent.putExtra("order_id",order_id);
                intent.putExtra("curr_id",curr_id);
                intent.putExtra("chat_id",chat_id);
                intent.putExtra("curr_type",curr_type);
                intent.putExtra("chat_type",chat_type);
                intent.putExtra("curr_image",curr_img);
                intent.putExtra("chat_image",chat_img);
                intent.putExtra("order_cost",order_cost);
                intent.putExtra("order_details",order_details);
                intent.putExtra("room_id",room_id);


                startActivity(intent);
                break;
            /*case R.id.refuse:
                break;
            case R.id.done:
                break;*/
        }
        return super.onOptionsItemSelected(item);
    }

    public void setPosTodownloadImage(String url)
    {
        this.url = url;

        CheckRead_WritePermission();



    }

    private void StartDownload(String url)
    {
        Log.e("4","4");

        Toast.makeText(this, "Start Download...", Toast.LENGTH_LONG).show();
        AsynkTask asynkTask = new AsynkTask();
        asynkTask.execute(url);
    }
    private void CheckRead_WritePermission() {
        if (ContextCompat.checkSelfPermission(this,WritePermission)== PackageManager.PERMISSION_GRANTED)
        {
            Log.e("1","1");
            StartDownload(url);
        }else
            {
                Log.e("2","2");

                String [] permissions = {WritePermission};
                ActivityCompat.requestPermissions(this,permissions,WRITE_REQ);
                StartDownload(url);

            }
    }
    private void CheckReadPermission() {
        if (ContextCompat.checkSelfPermission(this,ReadPermission)== PackageManager.PERMISSION_GRANTED)
        {
            selectImage();
        }else
        {

            String [] permissions = {ReadPermission};
            ActivityCompat.requestPermissions(this,permissions,READ_REQ);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==WRITE_REQ)
        {
            if (grantResults.length>0)
            {
                if (grantResults[0]==PackageManager.PERMISSION_GRANTED)
                {
                    StartDownload(url);
                }else{
                    return;
                }


            }else
                {
                    String [] per = {WritePermission};
                    ActivityCompat.requestPermissions(this,per,WRITE_REQ);

                }
        }else if (requestCode==READ_REQ)
        {
            if (grantResults.length>0)
            {
                if (grantResults[0]!=PackageManager.PERMISSION_GRANTED)
                {
                    return;
                }

                selectImage();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ll_typing.getVisibility()==View.VISIBLE)
        {
            changeTypingState(Tags.end_typing);

        }
        EventBus.getDefault().unregister(this);
        if (mediaPlayer!=null)
        {
            mediaPlayer.release();
        }
    }

    @Override
    public void UserDataSuccess(UserModel userModel) {
        this.userModel = userModel;
    }

    public class AsynkTask extends AsyncTask<String,Void,String>{
        private InputStream inputStream=null;
        private OutputStream outputStream =null;
        private URL url = null;
        int c;
        @Override
        protected String doInBackground(String... strings) {
            try {

                url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                inputStream = urlConnection.getInputStream();
                File file = new File(Environment.getExternalStorageDirectory(),"/7alan");
                if (!file.exists())
                {
                    file.mkdirs();
                }
                File file1 = new File(file,System.currentTimeMillis()+".png");
                outputStream = new FileOutputStream(file1);
                while ((c=inputStream.read())!=-1)
                {
                    outputStream.write(c);
                }

                outputStream.flush();
                return "1";


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (inputStream!=null)
                {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (outputStream!=null)
                {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return "0";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s.equals("1"))
            {
                Toast.makeText(ChatActivity.this, R.string.img_sav, Toast.LENGTH_LONG).show();
            }else
            {
                Toast.makeText(ChatActivity.this, R.string.failed, Toast.LENGTH_LONG).show();

            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer!=null)
        mediaPlayer.stop();
    }


}
