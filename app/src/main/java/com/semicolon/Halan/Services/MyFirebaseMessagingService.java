package com.semicolon.Halan.Services;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.semicolon.Halan.Activities.ClientNotificationActivity;
import com.semicolon.Halan.Activities.DriverNotificationActivity;
import com.semicolon.Halan.Activities.DriverOrdersActivity;
import com.semicolon.Halan.Activities.HomeActivity;
import com.semicolon.Halan.Activities.MyOrdersActivity;
import com.semicolon.Halan.Models.DriverAcceptModel;
import com.semicolon.Halan.Models.Finishied_Order_Model;
import com.semicolon.Halan.Models.MessageModel;
import com.semicolon.Halan.Models.TypingModel;
import com.semicolon.Halan.Models.UserModel;
import com.semicolon.Halan.R;
import com.semicolon.Halan.SingleTone.Users;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Delta on 25/03/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        final Map<String,String> map = remoteMessage.getData();
        for (String key:map.keySet())
        {
            Log.e(key+"_",map.get(key));

        }
        long not_time = remoteMessage.getSentTime();
        manageNotifications(map,not_time);


    }

    private void manageNotifications(Map<String, String> map, long not_time) {

        Preferences pref = new Preferences(this);
        SharedPreferences preferences = getSharedPreferences("user",MODE_PRIVATE);
        Users users = Users.getInstance();
        users.setUserData(pref.getUserData());
        String session = preferences.getString("session","");
        Log.e("seeeeee","___"+session);
        String not_type = map.get("notification_type");

        if (not_type!=null||!TextUtils.isEmpty(not_type))
        {
            if (not_type.equals(Tags.nottype_driverAccept))
            {
                String not_user_id = map.get("user_id");
                if (session!=null || !TextUtils.isEmpty(session))
                {
                    if (session.equals(Tags.login))
                    {
                        String user_id = preferences.getString("user_id","");
                        String user_type = preferences.getString("user_type","");

                        if (user_type.equals(Tags.Client)) {
                            if (user_id.equals(not_user_id)) {
                                Update_User_state();
                                CreateNotification(user_id,map,not_time,pref);
                            }
                        }

                    }
                }

            }else if (not_type.equals(Tags.notclient_send_request))
                {
                    //Log.e("to_ids",map.get("to_id"));
                    Log.e("222222222222222222","222222222");
                    if (session!=null || !TextUtils.isEmpty(session))
                    {
                        if (session.equals(Tags.login))
                        {
                            Log.e("fffffffff","ffffffffffff");
                            String user_id = preferences.getString("user_id","");
                            String user_type = preferences.getString("user_type","");
                            Log.e("my_id",user_id+"    ---"+"type  "+user_type);
                            if (user_type.equals(Tags.Driver))
                            {

                                try {
                                    List<String> myId = new ArrayList<>();
                                    JSONArray jsonArray = new JSONArray(map.get("to_id"));
                                    if (jsonArray.length()>0)
                                    {
                                        for (int i =0;i<jsonArray.length();i++)
                                        {
                                            if (jsonArray.get(i).toString().equals(user_id))
                                            {
                                                myId.add(jsonArray.get(i).toString());
                                            }
                                        }
                                        if (myId.size()==1)
                                        {
                                            CreateNotification(user_id,map,not_time, pref);
                                            Log.e("size",""+myId.get(0));
                                        }
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }

                        }
                    }
                }
            else if (not_type.equals(Tags.notdriver_accept_order))
            {
                if (session!=null || !TextUtils.isEmpty(session))
                {
                    if (session.equals(Tags.login))
                    {
                        String user_id = preferences.getString("user_id","");
                        String user_type = preferences.getString("user_type","");

                        if (user_type.equals(Tags.Client))
                        {
                            if (map.get("to_id").toString().equals(user_id))
                            {
                                CreateNotification(user_id,map,not_time, pref);

                            }
                        }

                    }
                }
            }
            else if (not_type.equals(Tags.notdriver_refuse_order))
            {
                if (session!=null || !TextUtils.isEmpty(session))
                {
                    if (session.equals(Tags.login))
                    {
                        String user_id = preferences.getString("user_id","");
                        String user_type = preferences.getString("user_type","");

                        if (user_type.equals(Tags.Client))
                        {
                            if (map.get("to_id").toString().equals(user_id))
                            {
                                CreateNotification(user_id,map,not_time, pref);

                            }
                        }
                    }
                }
            }
            else if (not_type.equals(Tags.notclient_cancel_order))
            {
                if (session!=null || !TextUtils.isEmpty(session))
                {
                    if (session.equals(Tags.login))
                    {
                        String user_id = preferences.getString("user_id","");
                        String user_type = preferences.getString("user_type","");

                        if (user_type.equals(Tags.Driver))
                        {
                            if (map.get("to_id").toString().equals(user_id))
                            {
                                CreateNotification(user_id,map,not_time, pref);

                            }
                        }
                    }
                }
            }
            else if (not_type.equals(Tags.notdriver_cancel_order))
            {
                if (session!=null || !TextUtils.isEmpty(session))
                {
                    if (session.equals(Tags.login))
                    {
                        String user_id = preferences.getString("user_id","");
                        String user_type = preferences.getString("user_type","");

                        if (user_type.equals(Tags.Client))
                        {
                            if (map.get("to_id").toString().equals(user_id))
                            {
                                CreateNotification(user_id,map,not_time, pref);

                            }
                        }
                    }
                }
            }else if (not_type.equals(Tags.chat))
            {
                if (session!=null || !TextUtils.isEmpty(session))
                {
                    if (session.equals(Tags.login))
                    {
                        SharedPreferences spRef = this.getSharedPreferences("chat_id",MODE_PRIVATE);
                        String chat_id = spRef.getString("chat_id","");
                        Log.e("chat_id",chat_id);
                        //com.semicolon.Halan.Activities.ChatActivity
                        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
                        String curr_class = am.getRunningTasks(1).get(0).topActivity.getClassName();
                        String user_id = preferences.getString("user_id","");
                        String user_type = preferences.getString("user_type","");
                        Log.e("curclass",curr_class);
                        String from_id = map.get("from_user_id");

                        if (curr_class.equals("com.semicolon.Halan.Activities.ChatActivity"))
                        {
                            if (!user_id.equals(from_id))
                            {
                                Log.e("77","77");
                                if (!TextUtils.isEmpty(chat_id))
                                {
                                    if (map.get("from_user_id").equals(chat_id))
                                    {
                                        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/mm/dd");

                                        String time = timeFormat.format(new Date(not_time));
                                        String date = dateFormat.format(new Date(not_time));
                                        Log.e("mm","mmmmm");
                                        String room_id = map.get("room_id");
                                        String image = map.get("image");
                                        String from_name = map.get("from_name");
                                        String frm_id = map.get("from_user_id");
                                        String from_type = map.get("from_type");
                                        String from_image = map.get("from_photo");
                                        String to_name = map.get("to_name");
                                        String to_id = map.get("'to_user_id");
                                        String to_type = map.get("to_type");
                                        String to_image = map.get("to_photo");
                                        String msg = map.get("message");
                                        String msg_type = map.get("name_message_type");
                                        MessageModel messageModel = new MessageModel(room_id,image,from_name,frm_id,from_type,from_image,to_name,to_id,to_type,to_image,date,time,msg_type,msg);
                                        EventBus.getDefault().post(messageModel);
                                        //CreateNotification(user_id,map,not_time,pref);
                                    }else
                                    {
                                        if (!curr_class.equals("com.semicolon.Halan.Activities.ChatActivity"))
                                        {
                                            CreateNotification(user_id,map,not_time,pref);

                                        }
                                    }

                                }

                            }else
                                {
                                    Log.e("88","88");

                                    if (!TextUtils.isEmpty(chat_id))
                                    {
                                        Log.e("99","99");

                                        if (map.get("from_user_id").equals(chat_id))
                                        {
                                            Log.e("100","100");

                                            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
                                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/mm/dd");

                                            String time = timeFormat.format(new Date(not_time));
                                            String date = dateFormat.format(new Date(not_time));
                                            Log.e("mm","mmmmm");
                                            String room_id = map.get("room_id");
                                            String image = map.get("image");
                                            String from_name = map.get("from_name");
                                            String frm_id = map.get("from_user_id");
                                            String from_type = map.get("from_type");
                                            String from_image = map.get("from_photo");
                                            String to_name = map.get("to_name");
                                            String to_id = map.get("'to_user_id");
                                            String to_type = map.get("to_type");
                                            String to_image = map.get("to_photo");
                                            String msg = map.get("message");
                                            String msg_type = map.get("message_type");
                                            MessageModel messageModel = new MessageModel(room_id,image,from_name,frm_id,from_type,from_image,to_name,to_id,to_type,to_image,date,time,msg_type,msg);

                                            EventBus.getDefault().post(messageModel);
                                            //CreateNotification(user_id,map,not_time,pref);
                                        }else {
                                            if (!curr_class.equals("com.semicolon.Halan.Activities.ChatActivity")) {
                                                CreateNotification(user_id, map, not_time, pref);

                                            }

                                        }
                                    }
                                    }

                        }else
                            {
                                if (!user_id.equals(from_id))
                                {
                                    Log.e("tt","tttt");

                                    CreateNotification(user_id,map,not_time,pref);
                                }
                            }

                    }
                }

            }else if (not_type.equals(Tags.not_typing))
            {
                if (session!=null || !TextUtils.isEmpty(session))
                {
                    if (session.equals(Tags.login))
                    {
                        SharedPreferences spRef = this.getSharedPreferences("chat_id",MODE_PRIVATE);
                        String chat_id = spRef.getString("chat_id","");
                        Log.e("chat_id",chat_id);
                        //com.semicolon.Halan.Activities.ChatActivity
                        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
                        String curr_class = am.getRunningTasks(1).get(0).topActivity.getClassName();
                        String user_id = preferences.getString("user_id","");
                        String user_type = preferences.getString("user_type","");
                        Log.e("curclass",curr_class);
                        String from_id = map.get("from_user_id");

                        Log.e("curr_id",user_id);
                        Log.e("chat_id",from_id);

                        if (curr_class.equals("com.semicolon.Halan.Activities.ChatActivity"))
                        {
                            if (!user_id.equals(from_id))
                            {
                                Log.e("77","77");
                                if (!TextUtils.isEmpty(chat_id))
                                {
                                    if (map.get("from_user_id").equals(chat_id))
                                    {
                                        //String room_id = map.get("room_id");
                                        String typing_value = map.get("typing_value");
                                        TypingModel typingModel = new TypingModel(typing_value);
                                        EventBus.getDefault().post(typingModel);
                                    }

                                }

                            }else
                            {
                                Log.e("88","88");

                                if (!TextUtils.isEmpty(chat_id))
                                {
                                    Log.e("99","99");

                                    if (map.get("from_user_id").equals(chat_id))
                                    {
                                        Log.e("100","100");

                                        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/mm/dd");

                                        String time = timeFormat.format(new Date(not_time));
                                        String date = dateFormat.format(new Date(not_time));
                                        Log.e("mm","mmmmm");
                                        String room_id = map.get("room_id");
                                        String image = map.get("image");
                                        String from_name = map.get("from_name");
                                        String frm_id = map.get("from_user_id");
                                        String from_type = map.get("from_type");
                                        String from_image = map.get("from_photo");
                                        String to_name = map.get("to_name");
                                        String to_id = map.get("'to_user_id");
                                        String to_type = map.get("to_type");
                                        String to_image = map.get("to_photo");
                                        String msg = map.get("message");
                                        String msg_type = map.get("message_type");
                                        MessageModel messageModel = new MessageModel(room_id,image,from_name,frm_id,from_type,from_image,to_name,to_id,to_type,to_image,date,time,msg_type,msg);

                                        EventBus.getDefault().post(messageModel);
                                        //CreateNotification(user_id,map,not_time,pref);
                                    }else {
                                        if (!curr_class.equals("com.semicolon.Halan.Activities.ChatActivity")) {
                                            CreateNotification(user_id, map, not_time, pref);

                                        }

                                    }
                                }
                            }

                        }else
                        {
                            if (!user_id.equals(from_id))
                            {
                                Log.e("tt","tttt");

                                CreateNotification(user_id,map,not_time,pref);
                            }
                        }

                    }
                }
            }
            else if (not_type.equals(Tags.driver_finish_order))
            {
                if (session!=null || !TextUtils.isEmpty(session))
                {
                    if (session.equals(Tags.login))
                    {
                        String user_id = preferences.getString("user_id","");
                        String user_type = preferences.getString("user_type","");

                        if (map.get("to_id").toString().equals(user_id))
                        {
                            CreateNotification(user_id,map,not_time, pref);

                        }

                    }
                }
            }
        }

    }

    private void CreateNotification(final String user_id , final Map<String, String> map, final long not_time, final Preferences pref)
    {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
                final String date = dateFormat.format(new Date(not_time));
                Preferences preferences = new Preferences(MyFirebaseMessagingService.this);
                final NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                final NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
                builder.setVibrate(new long[]{1000,1000,1000});
                builder.setLights(Color.WHITE,3000,3000);
                String state = pref.getSoundState();
                if (state.equals("on"))
                {
                    builder.setSound(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.not));


                }

                UserModel userModel = preferences.getUserData();

                Users users =Users.getInstance();
                users.setUserData(userModel);

                switch (map.get("notification_type"))
                {
                    case Tags.nottype_driverAccept:

                        Intent intent = new Intent(MyFirebaseMessagingService.this, HomeActivity.class);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        //startActivity(intent);
                        PendingIntent pendingIntent = PendingIntent.getActivity(MyFirebaseMessagingService.this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                        builder.setContentIntent(pendingIntent);
                        builder.setSmallIcon(R.mipmap.ic_launcher2);
                        builder.setAutoCancel(true);
                        builder.setContentTitle(map.get("title"));
                        builder.setContentText(map.get("message"));
                        manager.notify(0,builder.build());
                        break;
                    case Tags.notclient_send_request:

                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                final RemoteViews remoteViews1 = new RemoteViews(getPackageName(),R.layout.notification_layout);

                                remoteViews1.setTextViewText(R.id.title,map.get("title"));
                                remoteViews1.setTextViewText(R.id.content,map.get("message")+" "+map.get("from_name"));
                                remoteViews1.setTextViewText(R.id.time,date);
                                final Target target = new Target() {
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                        remoteViews1.setImageViewBitmap(R.id.user_image,bitmap);
                                        Intent intent2 = new Intent(MyFirebaseMessagingService.this, DriverNotificationActivity.class);
                                        //startActivity(intent2);
                                        PendingIntent pendingIntent2 = PendingIntent.getActivity(MyFirebaseMessagingService.this,0,intent2,PendingIntent.FLAG_UPDATE_CURRENT);
                                        builder.setContentIntent(pendingIntent2);
                                        builder.setSmallIcon(R.mipmap.ic_launcher2);
                                        builder.setAutoCancel(true);
                                        builder.setContent(remoteViews1);
                                        builder.setContentTitle(map.get("title"));
                                        builder.setContentText(map.get("message")+map.get("from_name"));
                                        manager.notify(0,builder.build());
                                        Log.e("imaaaaaaaaaagt","sssssssssssssss");
                                    }

                                    @Override
                                    public void onBitmapFailed(Drawable errorDrawable) {

                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                                    }
                                };
                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Log.e("url",Tags.ImgPath+map.get("from_image"));
                                        Picasso.with(MyFirebaseMessagingService.this).load(Uri.parse(Tags.ImgPath+map.get("from_image"))).into(target);

                                    }
                                },1);


                            }
                        },1000);


                        break;
                    case Tags.notdriver_accept_order:
                        final RemoteViews remoteViews2 = new RemoteViews(getPackageName(),R.layout.notification_layout);

                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                remoteViews2.setTextViewText(R.id.title,map.get("title"));
                                remoteViews2.setTextViewText(R.id.time,date);
                                remoteViews2.setTextViewText(R.id.content,map.get("message"));
                                final Target target = new Target() {
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                        remoteViews2.setImageViewBitmap(R.id.user_image,bitmap);
                                        Intent intent3 = new Intent(MyFirebaseMessagingService.this, HomeActivity.class);
                                        //startActivity(intent3);
                                        PendingIntent pendingIntent3 = PendingIntent.getActivity(MyFirebaseMessagingService.this,0,intent3,PendingIntent.FLAG_UPDATE_CURRENT);
                                        builder.setContentIntent(pendingIntent3);
                                        builder.setSmallIcon(R.mipmap.ic_launcher2);
                                        builder.setAutoCancel(false);
                                        builder.setContent(remoteViews2);
                                        builder.setOngoing(true);
                                        manager.notify(1995,builder.build());
                                        EventBus.getDefault().post(new DriverAcceptModel(user_id));
                                    }

                                    @Override
                                    public void onBitmapFailed(Drawable errorDrawable) {

                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                                    }
                                };
                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Picasso.with(MyFirebaseMessagingService.this).load(Uri.parse(Tags.ImgPath+map.get("from_image" ))).placeholder(R.drawable.user_profile).into(target);

                                    }
                                },1);

                            }
                        },1000);

                        break;
                    case Tags.notdriver_refuse_order:
                        final RemoteViews remoteViews3 = new RemoteViews(getPackageName(),R.layout.notification_layout);

                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                remoteViews3.setTextViewText(R.id.title,map.get("title"));
                                remoteViews3.setTextViewText(R.id.content,map.get("message"));
                                remoteViews3.setTextViewText(R.id.time,date);
                                final Target target = new Target() {
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                        remoteViews3.setImageViewBitmap(R.id.user_image,bitmap);
                                        Intent intent4 = new Intent(MyFirebaseMessagingService.this, ClientNotificationActivity.class);
                                        //startActivity(intent4);
                                        PendingIntent pendingIntent4 = PendingIntent.getActivity(MyFirebaseMessagingService.this,0,intent4,PendingIntent.FLAG_UPDATE_CURRENT);
                                        builder.setContentIntent(pendingIntent4);
                                        builder.setSmallIcon(R.mipmap.ic_launcher2);
                                        builder.setAutoCancel(true);
                                        builder.setContent(remoteViews3);
                                        builder.setContentTitle(map.get("title"));
                                        builder.setContentText(map.get("message"));
                                        Log.e("name",map.get("from_name"));
                                        manager.notify(0,builder.build());
                                    }

                                    @Override
                                    public void onBitmapFailed(Drawable errorDrawable) {

                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                                    }
                                };
                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Picasso.with(MyFirebaseMessagingService.this).load(Uri.parse(Tags.ImgPath+map.get("from_image" ))).placeholder(R.drawable.user_profile).into(target);

                                    }
                                },1);

                            }
                        },1000);

                        break;

                    case Tags.notclient_cancel_order:
                        final RemoteViews remoteViews4 = new RemoteViews(getPackageName(),R.layout.notification_layout);

                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                remoteViews4.setTextViewText(R.id.title,map.get("title"));
                                remoteViews4.setTextViewText(R.id.time,date);
                                remoteViews4.setTextViewText(R.id.content,map.get("message"));
                                final Target target = new Target() {
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                        remoteViews4.setImageViewBitmap(R.id.user_image,bitmap);
                                        Intent intent5 = new Intent(MyFirebaseMessagingService.this,DriverOrdersActivity.class);
                                        //startActivity(intent3);
                                        PendingIntent pendingIntent5 = PendingIntent.getActivity(MyFirebaseMessagingService.this,0,intent5,PendingIntent.FLAG_UPDATE_CURRENT);
                                        builder.setContentIntent(pendingIntent5);
                                        builder.setSmallIcon(R.mipmap.ic_launcher2);
                                        builder.setAutoCancel(true);
                                        builder.setContent(remoteViews4);
                                        builder.setContentTitle(map.get("title"));
                                        builder.setContentText(map.get("message"));
                                        manager.notify(0,builder.build());
                                    }

                                    @Override
                                    public void onBitmapFailed(Drawable errorDrawable) {

                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                                    }
                                };
                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Picasso.with(MyFirebaseMessagingService.this).load(Uri.parse(Tags.ImgPath+map.get("from_image" ))).placeholder(R.drawable.user_profile).into(target);

                                    }
                                },1);

                            }
                        },1000);

                        break;
                    case Tags.notdriver_cancel_order:
                        final RemoteViews remoteViews5 = new RemoteViews(getPackageName(),R.layout.notification_layout);

                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                remoteViews5.setTextViewText(R.id.title,map.get("title"));
                                remoteViews5.setTextViewText(R.id.time,date);
                                remoteViews5.setTextViewText(R.id.content,map.get("message"));
                                final Target target = new Target() {
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                        remoteViews5.setImageViewBitmap(R.id.user_image,bitmap);
                                        Intent intent6 = new Intent(MyFirebaseMessagingService.this,MyOrdersActivity.class);
                                        //startActivity(intent3);
                                        PendingIntent pendingIntent5 = PendingIntent.getActivity(MyFirebaseMessagingService.this,0,intent6,PendingIntent.FLAG_UPDATE_CURRENT);
                                        builder.setContentIntent(pendingIntent5);
                                        builder.setSmallIcon(R.mipmap.ic_launcher2);
                                        builder.setAutoCancel(true);
                                        builder.setContent(remoteViews5);
                                        builder.setContentTitle(map.get("title"));
                                        builder.setContentText(map.get("message"));
                                        manager.notify(0,builder.build());
                                    }

                                    @Override
                                    public void onBitmapFailed(Drawable errorDrawable) {

                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                                    }
                                };
                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Picasso.with(MyFirebaseMessagingService.this).load(Uri.parse(Tags.ImgPath+map.get("from_image" ))).placeholder(R.drawable.user_profile).into(target);

                                    }
                                },1);

                            }
                        },1000);

                        break;

                    case Tags.chat:
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                final RemoteViews remoteViews6 = new RemoteViews(getPackageName(),R.layout.notification_layout);
                                String title = String.valueOf(map.get("from_type"));
                                String content_type = map.get("name_message_type");
                                Log.e("chaaaaaaaaaaaaaaat","chaaaat");
                                if (title.equals(Tags.Driver))
                                {
                                    remoteViews6.setTextViewText(R.id.title,getString(R.string.driver));

                                }else
                                    {
                                        remoteViews6.setTextViewText(R.id.title,getString(R.string.client));

                                    }
                                if (content_type.equals(Tags.txt_content_type))
                                {
                                    remoteViews6.setTextViewText(R.id.content,map.get("message"));

                                }else
                                    {
                                        remoteViews6.setTextViewText(R.id.content,getString(R.string.photo_sent));
                                    }
                                remoteViews6.setTextViewText(R.id.time,date);
                                final Target target = new Target() {
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                        remoteViews6.setImageViewBitmap(R.id.user_image,bitmap);
                                        builder.setSmallIcon(R.mipmap.ic_launcher2);
                                        builder.setAutoCancel(true);
                                        builder.setContent(remoteViews6);
                                        manager.notify(0,builder.build());
                                        Log.e("imaaaaaaaaaagt","sssssssssssssss");
                                    }

                                    @Override
                                    public void onBitmapFailed(Drawable errorDrawable) {

                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                                    }
                                };
                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Picasso.with(MyFirebaseMessagingService.this).load(Uri.parse(Tags.ImgPath+map.get("from_photo"))).into(target);

                                    }
                                },1);


                            }
                        },1000);
                        break;
                    case Tags.driver_finish_order:
                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                final RemoteViews remoteViews7 = new RemoteViews(getPackageName(), R.layout.notification_layout);

                                remoteViews7.setTextViewText(R.id.title,"طلب موافقة");
                                remoteViews7.setTextViewText(R.id.content,"تم قبول طلبك من "+map.get("from_name"));

                                final Target target = new Target() {
                                    @Override
                                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                        remoteViews7.setImageViewBitmap(R.id.user_image,bitmap);
                                        Intent intent8 = new Intent(MyFirebaseMessagingService.this, HomeActivity.class);
                                        //startActivity(intent2);
                                        PendingIntent pendingIntent12 = PendingIntent.getActivity(getApplicationContext(),0,intent8,PendingIntent.FLAG_UPDATE_CURRENT);
                                        builder.setContentIntent(pendingIntent12);
                                        builder.setSmallIcon(R.mipmap.ic_launcher2);
                                        builder.setAutoCancel(false);
                                        builder.setOngoing(true);
                                        builder.setContent(remoteViews7);
                                        manager.notify(1995,builder.build());
                                        Log.e("imaaaaaaaaaagt","sssssssssssssss");
                                    }

                                    @Override
                                    public void onBitmapFailed(Drawable errorDrawable) {

                                    }

                                    @Override
                                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                                    }
                                };
                                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //Log.e("url",Tags.ImgPath+lastOrderModel.getDriver_image());
                                        Picasso.with(getApplicationContext()).load(Uri.parse(Tags.ImgPath+map.get("from_image"))).into(target);

                                    }
                                },1);


                            }
                        },1000);


                        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                EventBus.getDefault().post(new Finishied_Order_Model(map.get("from_id").toString(),map.get("from_name").toString(),map.get("from_image").toString(),map.get("order_id").toString(),map.get("order_detals").toString()));

                            }
                        },1000);
                }
            }
        },1000);


    }

    private void Update_User_state()
    {
       Preferences preferences = new Preferences(this);
       preferences.Update_UserState(Tags.Driver);
    }

}
