package com.semicolon.Halan.Services;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.semicolon.Halan.Models.TokenModel;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Delta on 08/04/2018.
 */

public class MyFirebaseInstanceIdServices extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        TokenModel tokenModel = new TokenModel(FirebaseInstanceId.getInstance().getToken());
        EventBus.getDefault().post(tokenModel);
    }


}
