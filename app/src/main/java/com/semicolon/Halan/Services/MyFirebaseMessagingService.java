package com.semicolon.Halan.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
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
import com.semicolon.Halan.Models.UserModel;
import com.semicolon.Halan.R;
import com.semicolon.Halan.SingleTone.Users;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

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
            Log.e("data",map.get(key));

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
                                CreateNotification(map,not_time);
                            }
                        }

                    }
                }

            }else if (not_type.equals(Tags.notclient_send_request))
                {
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
                                    for (int i =0;i<jsonArray.length();i++)
                                    {
                                        if (jsonArray.get(i).toString().equals(user_id))
                                        {
                                            myId.add(jsonArray.get(i).toString());
                                        }
                                    }
                                    if (myId.size()==1)
                                    {
                                        CreateNotification(map,not_time);
                                        Log.e("size",""+myId.get(0));
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
                                CreateNotification(map,not_time);

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
                                CreateNotification(map,not_time);

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
                                CreateNotification(map,not_time);

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
                                CreateNotification(map,not_time);

                            }
                        }
                    }
                }
            }
        }

    }

    private void CreateNotification(final Map<String, String> map, final long not_time) {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
                final String date = dateFormat.format(new Date(not_time));
                Preferences preferences = new Preferences(MyFirebaseMessagingService.this);
                final NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                final NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
                builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

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
                        builder.setSmallIcon(R.mipmap.ic_launcher_round);
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
                                        builder.setSmallIcon(R.mipmap.ic_launcher_round);
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
                                        Intent intent3 = new Intent(MyFirebaseMessagingService.this, ClientNotificationActivity.class);
                                        //startActivity(intent3);
                                        PendingIntent pendingIntent3 = PendingIntent.getActivity(MyFirebaseMessagingService.this,0,intent3,PendingIntent.FLAG_UPDATE_CURRENT);
                                        builder.setContentIntent(pendingIntent3);
                                        builder.setSmallIcon(R.mipmap.ic_launcher_round);
                                        builder.setAutoCancel(true);
                                        builder.setContent(remoteViews2);
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
                                        builder.setSmallIcon(R.mipmap.ic_launcher_round);
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
                                        builder.setSmallIcon(R.mipmap.ic_launcher_round);
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
                                        builder.setSmallIcon(R.mipmap.ic_launcher_round);
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
                }
            }
        },1000);


    }

    private void Update_User_state() {
       Preferences preferences = new Preferences(this);
       preferences.Update_UserState(Tags.Driver);
    }


}
