package com.semicolon.Halan.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;

import com.semicolon.Halan.R;

public class ChatActivity extends AppCompatActivity {
    private Toolbar toolBar;
    private ImageView back,upload_imageBtn,send_imageBtn;
    private EditText ed_msg;
    private RecyclerView recView;
    private LinearLayoutManager manager;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initView();
    }

    private void initView() {
        toolBar = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        back = findViewById(R.id.back);
        upload_imageBtn = findViewById(R.id.upload_imageBtn);
        send_imageBtn = findViewById(R.id.send_imageBtn);
        recView = findViewById(R.id.recView);
        manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        manager.setStackFromEnd(true);
        manager.setReverseLayout(false);
        recView.setLayoutManager(manager);
        recView.setHasFixedSize(true);



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
}
