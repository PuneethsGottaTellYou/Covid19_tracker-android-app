package com.example.covidapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;

public class CovidTrackerActivity extends AppCompatActivity {

    TextView cases, recovered, critical, active, todayCases, totalDeaths, todayDeaths, affectedCountries, progressText;
    ScrollView scrollView;
    PieChart pieChart;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_tracker);

        cases = findViewById(R.id.totalCases);
        recovered = findViewById(R.id.recoverdCases);
        critical = findViewById(R.id.criticalCases);
        active = findViewById(R.id.activeCases);
        todayCases = findViewById(R.id.todayCases);
        todayDeaths = findViewById(R.id.todayDeaths);
        affectedCountries = findViewById(R.id.affectedCountries);
        totalDeaths = findViewById(R.id.totalDeaths);
        scrollView = findViewById(R.id.scrollStats);
        pieChart = findViewById(R.id.piechart);
        progressBar = findViewById(R.id.progressBar);
        progressText = findViewById(R.id.progressText);

        fetchData();
    }

    private void fetchData() {
        String url = "https://corona.lmao.ninja/v2/all";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    cases.setText(jsonObject.getString("cases"));
                    recovered.setText(jsonObject.getString("recovered"));
                    critical.setText(jsonObject.getString("critical"));
                    active.setText(jsonObject.getString("active"));
                    todayCases.setText(jsonObject.getString("todayCases"));
                    todayDeaths.setText(jsonObject.getString("todayDeaths"));
                    totalDeaths.setText(jsonObject.getString("deaths"));
                    affectedCountries.setText(jsonObject.getString("affectedCountries"));

                    pieChart.addPieSlice(new PieModel("Cases", Integer.parseInt(cases.getText().toString()), Color.parseColor("#F3B22F")));
                    pieChart.addPieSlice(new PieModel("Recovered", Integer.parseInt(recovered.getText().toString()), Color.parseColor("#6BDA25")));
                    pieChart.addPieSlice(new PieModel("Deaths", Integer.parseInt(totalDeaths.getText().toString()), Color.parseColor("#D50000")));
                    pieChart.addPieSlice(new PieModel("Active", Integer.parseInt(active.getText().toString()), Color.parseColor("#0091EA")));

                    pieChart.startAnimation();
                    progressBar.setVisibility(View.INVISIBLE);
                    progressText.setVisibility(View.INVISIBLE);
                    scrollView.setVisibility(View.VISIBLE);
                    Toast.makeText(CovidTrackerActivity.this, "Last updated 10 min ago", Toast.LENGTH_SHORT).show();



                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.INVISIBLE);
                    progressText.setVisibility(View.INVISIBLE);
                    scrollView.setVisibility(View.VISIBLE);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
                progressText.setVisibility(View.INVISIBLE);
                scrollView.setVisibility(View.VISIBLE);
                Toast.makeText(CovidTrackerActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    public void TrackCoutries(View view) {
        startActivity(new Intent(getApplicationContext(), AffectedCountries.class));
    }
}