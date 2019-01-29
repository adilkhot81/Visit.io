package com.example.mohdadil.visit_10;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import es.situm.sdk.SitumSdk;

public class MainActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT= 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ImageView logoImage =(ImageView) findViewById(R.id.logoImage);
        int imageResource=getResources().getIdentifier("@drawable/logo",null,this.getPackageName());
        logoImage.setImageResource(imageResource);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mapActivity =new Intent(MainActivity.this,indoorOutdoor.class);
                startActivity(mapActivity);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
