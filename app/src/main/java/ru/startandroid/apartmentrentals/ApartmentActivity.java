package ru.startandroid.apartmentrentals;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class ApartmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apartment);
        //Log.i("ZHEKA", "Activity - onCreate");
        FragmentManager fm = getSupportFragmentManager();
        Fragment apartmentFragment = fm.findFragmentById(R.id.fragmentContainer);
        if(apartmentFragment == null){
            apartmentFragment = ApartmentFragment.newInstance();
            fm.beginTransaction().add(R.id.fragmentContainer, apartmentFragment).commit();
        }
    }
}
