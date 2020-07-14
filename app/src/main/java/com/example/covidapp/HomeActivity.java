package com.example.covidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


    }

    public void CovidTracker(View view) {
        startActivity(new Intent(HomeActivity.this, CovidTrackerActivity.class));
    }

    public void CovidNews(View view) {
        startActivity(new Intent(HomeActivity.this, BingMaps.class));
    }

    public void CovidMaps(View view) {
        startActivity(new Intent(HomeActivity.this, MapsActivity.class));
    }
}