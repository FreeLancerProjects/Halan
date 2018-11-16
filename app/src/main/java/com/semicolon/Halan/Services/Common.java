package com.semicolon.Halan.Services;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Common {

    public static MultipartBody.Part getMultipart(Context context ,Uri uri)
    {
        String fullImagePath = getfullImagePath(context,uri);
        File file = new File(fullImagePath);
        MultipartBody.Part part = getMultipartData(file);
        return part;
    }

    private static String getfullImagePath(Context context ,Uri uri)
    {
        String [] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri,projection,null,null,null);
        if (cursor!=null&&cursor.moveToFirst())
        {
            String path = cursor.getString(cursor.getColumnIndexOrThrow(projection[0]));
            return path;
        }

        return null;
    }

    public static RequestBody getRequestBodyFromData(String content,String mediaType)
    {
        RequestBody requestBody = RequestBody.create(MediaType.parse(mediaType),content);
        return requestBody;
    }

    private static MultipartBody.Part getMultipartData(File file)
    {
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"),file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("image",file.getName(),requestBody);
        return part;
    }
}
