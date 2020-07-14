package com.example.covidapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AffectedCountries extends AppCompatActivity {

    EditText edtSearch;
    ListView listView;

    public static List<CountryModel> countryModelsList = new ArrayList<>();
    CountryModel countryModel;
    Custom_Adapter myCustomAdapter;
    ProgressBar progressBar;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affected_countries);

        edtSearch = findViewById(R.id.searchCountriesBar);
        listView = findViewById(R.id.searchCountriesListView);
        progressBar = findViewById(R.id.trackCountriesProgressBar);
        textView = findViewById(R.id.trackCountriesProgressBarTextView);



        fetchData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(),DetailActivity.class).putExtra("position",position));
            }
        });


        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                myCustomAdapter.getFilter().filter(s);
                myCustomAdapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void fetchData() {

        String url  = "https://corona.lmao.ninja/v2/countries";
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsonArray = new JSONArray(response);


                            for(int i=0;i<jsonArray.length();i++){

                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String countryName = jsonObject.getString("country");
                                String cases = jsonObject.getString("cases");
                                String todayCases = jsonObject.getString("todayCases");
                                String deaths = jsonObject.getString("deaths");
                                String todayDeaths = jsonObject.getString("todayDeaths");
                                String recovered = jsonObject.getString("recovered");
                                String active = jsonObject.getString("active");
                                String critical = jsonObject.getString("critical");

                                JSONObject object = jsonObject.getJSONObject("countryInfo");
                                String flagUrl = object.getString("flag");

                                countryModel = new CountryModel(flagUrl,countryName,cases,critical,recovered,todayCases,todayDeaths,deaths,active);
                                countryModelsList.add(countryModel);


                            }

                            myCustomAdapter = new Custom_Adapter(AffectedCountries.this,countryModelsList);
                            listView.setAdapter(myCustomAdapter);
                            progressBar.setVisibility(View.INVISIBLE);
                            textView.setVisibility(View.INVISIBLE);
                            listView.setVisibility(View.VISIBLE);







                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.INVISIBLE);
                            textView.setVisibility(View.INVISIBLE);
                            listView.setVisibility(View.VISIBLE);
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
                textView.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.VISIBLE);
                Toast.makeText(AffectedCountries.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);


    }

}
