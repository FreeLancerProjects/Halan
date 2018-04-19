package com.semicolon.Halan.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.semicolon.Halan.Adapters.Message_Adapter;
import com.semicolon.Halan.Models.ChatModel;
import com.semicolon.Halan.Models.MessageModel;
import com.semicolon.Halan.Models.Typing;
import com.semicolon.Halan.R;
import com.semicolon.Halan.Services.Tags;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
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
import java.util.Random;

public class ChatActivity extends AppCompatActivity {
    private Toolbar toolBar;
    private ImageView back,upload_imageBtn,send_imageBtn;
    private EditText ed_msg;
    private RecyclerView recView;
    private LinearLayoutManager manager;
    private RecyclerView.Adapter adapter;
    private String curr_id,chat_id;
    private String curr_type,chat_type;
    private String curr_img,chat_img;
    private DatabaseReference dRef;
    private final int IMG_REQ=12521;
    private StorageReference storageReference;
    private ChatModel chatModel;
    private List<MessageModel> messageModelList;
    private TextView typing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        dRef = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        getDataFromIntent();
        initView();
        DisplayMessage();
        ListenForTyping();
    }

    private void ListenForTyping() {
        dRef.child("typing").child(curr_id).child(chat_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Typing state = dataSnapshot.getValue(Typing.class);
                if (state.isType())
                {
                    typing.setText(R.string.type);
                    typing.setVisibility(View.VISIBLE);

                }else
                    {
                        typing.setVisibility(View.INVISIBLE);

                    }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void DisplayMessage() {
        final DatabaseReference  reference = dRef.child("messages").child(curr_id).child(chat_id);
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
                        recView.scrollToPosition(recView.getAdapter().getItemCount());

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

            chatModel = new ChatModel(curr_id,chat_id,curr_type,chat_type,curr_img,chat_img);
        }
    }

    private void initView() {
        messageModelList = new ArrayList<>();
        toolBar = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        back = findViewById(R.id.back);
        upload_imageBtn = findViewById(R.id.upload_imageBtn);
        send_imageBtn = findViewById(R.id.send_imageBtn);
        recView = findViewById(R.id.recView);
        ed_msg = findViewById(R.id.ed_msg);
        typing = findViewById(R.id.typing);
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
                sendMsg();
            }
        });

        upload_imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
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
                    changeTypingState(true);

                }else
                    {
                        changeTypingState(false);
                    }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void changeTypingState(boolean state) {
        Map map = new HashMap();
        map.put("type",state);
        dRef.child("typing").child(chat_id).child(curr_id).updateChildren(map);
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
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                encodeImage(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
    //https://firebasestorage.googleapis.com/v0/b/semicolon-3dbe7.appspot.com/o/Upload%2FImages%2F-LAPs_rCFAMJCM_NCRvT?alt=media&token=ad5fea62-ff8d-4461-9ab9-2d290856a1df

    private void encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,90,outputStream);
        byte[] bytes = outputStream.toByteArray();
        DatabaseReference reference = dRef.child("Upload").child("Images").push();
        String push = reference.getKey();
        storageReference.child("Upload").child("Images").child(push).putBytes(bytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                DataToSend(Tags.img_msg_type,downloadUrl.toString());
                Log.e("uri",downloadUrl+"");
            }
        });
    }

    private void sendMsg() {
        if (!TextUtils.isEmpty(ed_msg.getText()))
        {
            final String msg = ed_msg.getText().toString();
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
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.bill:
                break;
            case R.id.refuse:
                break;
            case R.id.done:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void DataToSend(String msg_type,String msg)
    {
        String curr_ref = "messages/"+curr_id+"/"+chat_id;
        String chat_ref = "messages/"+chat_id+"/"+curr_id;
        DatabaseReference reference = dRef.child("messages").child(curr_id).child(chat_id).push();
        String push = reference.getKey();

        if (msg_type.equals(Tags.txt_msg_type))
        {
            Map dataMap = new HashMap();

            dataMap.put("message",msg);
            dataMap.put("image","");
            dataMap.put("from_id",curr_id);
            dataMap.put("to_id",chat_id);
            dataMap.put("from_type",curr_type);
            dataMap.put("to_type",chat_type);
            dataMap.put("from_photo",curr_img);
            dataMap.put("to_photo",chat_img);
            dataMap.put("message_type", msg_type);
            dataMap.put("message_time", ServerValue.TIMESTAMP);

            Map pushMap = new HashMap();
            pushMap.put(curr_ref+"/"+push,dataMap);
            pushMap.put(chat_ref+"/"+push,dataMap);

            dRef.updateChildren(pushMap);
            ed_msg.setText("");
        }else if (msg_type.equals(Tags.img_msg_type))
        {
            Map dataMap = new HashMap();

            dataMap.put("message","");
            dataMap.put("image",msg);
            dataMap.put("from_id",curr_id);
            dataMap.put("to_id",chat_id);
            dataMap.put("from_type",curr_type);
            dataMap.put("to_type",chat_type);
            dataMap.put("from_photo",curr_img);
            dataMap.put("to_photo",chat_img);
            dataMap.put("message_type", msg_type);
            dataMap.put("message_time", ServerValue.TIMESTAMP);

            Map pushMap = new HashMap();
            pushMap.put(curr_ref+"/"+push,dataMap);
            pushMap.put(chat_ref+"/"+push,dataMap);

            dRef.updateChildren(pushMap);
            ed_msg.setText("");
        }

    }

    public void setPosTodownloadImage(String url)
    {

        AsynkTask asynkTask = new AsynkTask();
        asynkTask.execute(url);


    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        changeTypingState(false);
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
                //String push = dRef.push().getKey();
                Random random = new Random();
                int i = random.nextInt(1000000000 - 10) + 10;
                File file = new File(Environment.getExternalStorageDirectory(),"/7alan");
                if (!file.exists())
                {
                    file.mkdirs();
                }
                File file1 = new File(file,i+".png");
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
                Toast.makeText(ChatActivity.this, "failed", Toast.LENGTH_LONG).show();

            }
        }
    }

}
