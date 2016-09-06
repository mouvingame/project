package ru.startandroid.apartmentrentals;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public class WebActivity extends AppCompatActivity{

    public final static String WEB_KEY = "web_key";

    public static Intent newIntent(Context contex, String webUrl){
        Intent i = new Intent(contex, WebActivity.class);
        i.putExtra(WEB_KEY, webUrl);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment webFragment = fm.findFragmentById(R.id.fragmentContainer);
        if(webFragment == null){
            webFragment = WebFragment.newInstance(getIntent().getStringExtra(WEB_KEY));
            fm.beginTransaction().add(R.id.fragmentContainer, webFragment).commit();
        }
    }
}
