package com.semicolon.Halan.Activities;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.semicolon.Halan.R;

public class SplachActivity extends AppCompatActivity implements AnimationListener {

    ImageView imgPoster;
    Animation animSlideUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splach);

        imgPoster =  findViewById(R.id.img);
        animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);

        animSlideUp.setAnimationListener(this);
        imgPoster.setVisibility(View. VISIBLE);
        imgPoster.startAnimation(animSlideUp);
        animSlideUp.setDuration(3000);


    }

    @Override
    public void onAnimationEnd(Animation animation) {

        Intent i =new Intent(this,Activity_Client_Login.class);
        startActivity(i);
        finish();

    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub

    }



}
