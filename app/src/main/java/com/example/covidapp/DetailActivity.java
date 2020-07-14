package com.example.covidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private  int positionCountry;
    TextView tvCountry,tvCases,tvRecovered,tvCritical,tvActive,tvTodayCases,tvTotalDeaths,tvTodayDeaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        int positionCountry;
        TextView tvCountry,tvCases,tvRecovered,tvCritical,tvActive,tvTodayCases,tvTotalDeaths,tvTodayDeaths;

            Intent intent = getIntent();
            positionCountry = intent.getIntExtra("position",0);

            tvCountry = findViewById(R.id.tvCountry);
            tvCases = findViewById(R.id.tvCases);
            tvRecovered = findViewById(R.id.tvRecovered);
            tvCritical = findViewById(R.id.tvCritical);
            tvTodayCases = findViewById(R.id.tvTodayCases);
            tvTotalDeaths = findViewById(R.id.tvDeaths);
            tvTodayDeaths = findViewById(R.id.tvTodayDeaths);
            tvActive = findViewById(R.id.tvActive);

            tvCountry.setText(AffectedCountries.countryModelsList.get(positionCountry).getCountries());
            tvCases.setText(AffectedCountries.countryModelsList.get(positionCountry).getCases());
            tvRecovered.setText(AffectedCountries.countryModelsList.get(positionCountry).getRecovered());
            tvCritical.setText(AffectedCountries.countryModelsList.get(positionCountry).getCritical());
            tvTodayCases.setText(AffectedCountries.countryModelsList.get(positionCountry).getTodayCases());
            tvTotalDeaths.setText(AffectedCountries.countryModelsList.get(positionCountry).getDeaths());
            tvTodayDeaths.setText(AffectedCountries.countryModelsList.get(positionCountry).getTodayDeaths());
            tvActive.setText(AffectedCountries.countryModelsList.get(positionCountry).getActive());


        }
        @Override
        public boolean onOptionsItemSelected(@NonNull MenuItem item) {
            if(item.getItemId()==android.R.id.home)
                finish();
            return super.onOptionsItemSelected(item);
        }
    }
