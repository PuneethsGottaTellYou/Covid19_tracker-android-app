package com.example.covidapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, layout_dialog.IntefaceDialogListener {

    private GoogleMap mMap;
    private View mapView;
    private Button findButton, currentLocationButton;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private PlacesClient placesClient;
    private List<AutocompletePrediction> predictionList;
    private Location lastKnowmLocation, yourLocation;
    private LocationCallback locationCallback;
    private final float DEFAULT_ZOOM = 7;
    private SearchView searchView;
    ProgressBar progressBar;
    TextView textView;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        searchView = (SearchView) findViewById(R.id.searchBar);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.progressText);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MapsActivity.this);
        Places.initialize(MapsActivity.this, "AIzaSyCgIuaMfoP_tKxvNVwMfwxjv5IaaufLWfY");
        placesClient = Places.createClient(this);
        final AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;

                if (location != null || !location.equals("")) {

                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (addressList.size() > 0) {

                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(location);
                        mMap.addMarker(markerOptions);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                        Toast.makeText(MapsActivity.this, "Search found ", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MapsActivity.this, "No search results found", Toast.LENGTH_SHORT).show();
                    }
                } else {


                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                FindAutocompletePredictionsRequest predictionsRequest = FindAutocompletePredictionsRequest.builder()
                        .setCountry("IND")
                        .setTypeFilter(TypeFilter.ADDRESS)
                        .setSessionToken(token)
                        .setQuery(s)
                        .build();

                placesClient.findAutocompletePredictions(predictionsRequest).addOnCompleteListener(new OnCompleteListener<FindAutocompletePredictionsResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<FindAutocompletePredictionsResponse> task) {
                        if (task.isSuccessful()) {
                            FindAutocompletePredictionsResponse predictionsResponse = task.getResult();
                            if (predictionsResponse != null) {
                                predictionList = predictionsResponse.getAutocompletePredictions();
                                List<String> suggestionList = new ArrayList<>();
                                for (int i = 0; i < predictionList.size(); i++) {
                                    AutocompletePrediction prediction = predictionList.get(i);
                                    suggestionList.add(prediction.getFullText(null).toString());
                                }
                            }

                        } else {

                        }
                    }
                });
                return false;
            }
        });

        fetchData();

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 40, 180);


            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(5000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

            SettingsClient settingsClient = LocationServices.getSettingsClient(MapsActivity.this);
            Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());
            task.addOnSuccessListener(MapsActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
                @Override
                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    getDeviceLocation();

                }
            });
            task.addOnFailureListener(MapsActivity.this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof ResolvableApiException) {
                        ResolvableApiException resolvableApiException = (ResolvableApiException) e;

                        try {
                            resolvableApiException.startResolutionForResult(MapsActivity.this, 51);
                        } catch (IntentSender.SendIntentException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 51) {
            if (requestCode == RESULT_OK) {
                getDeviceLocation();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {

                    lastKnowmLocation = task.getResult();
                    if (lastKnowmLocation != null) {
                        LatLng presentLocation = new LatLng(lastKnowmLocation.getLatitude(), lastKnowmLocation.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnowmLocation.getLatitude(), lastKnowmLocation.getLongitude()), DEFAULT_ZOOM));
                        MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(lastKnowmLocation.getLatitude(), lastKnowmLocation.getLongitude())).title("Your Location");
                        mMap.addMarker(markerOptions);

                        //generateCircle(presentLocation);
                    } else {
                        final LocationRequest locationRequest = LocationRequest.create();
                        locationRequest.setInterval(10000);
                        locationRequest.setFastestInterval(5000);
                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                        locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                super.onLocationResult(locationResult);
                                if (locationResult != null) {
                                    return;
                                }

                                lastKnowmLocation = locationResult.getLastLocation();
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnowmLocation.getLatitude(), lastKnowmLocation.getLongitude()), 10));
                                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                                MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(lastKnowmLocation.getLatitude(), lastKnowmLocation.getLongitude())).title("Your Location");
                                mMap.addMarker(markerOptions);
                            }
                        };
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
                    }
                } else {
                    Toast.makeText(MapsActivity.this, "Unable to get last location", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void generateMarker(LatLng presentLocation, String location, String total_cases, String recoverd, String active, String deaths) {
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(presentLocation);
        circleOptions.radius(10000);
        circleOptions.fillColor(Color.rgb(247, 111, 111));
        circleOptions.strokeColor(Color.rgb(247, 111, 111));

        MarkerOptions options = new MarkerOptions()
                .position(presentLocation).title(location)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.covid_icon_main2));
        mMap.addMarker(options).setSnippet("Total Cases: " + total_cases + ", Recovred: " + recoverd + ", Deaths: " + deaths);


    }

    private void fetchData() {

        String url = "https://api.covidindiatracker.com/state_data.json";
        progressBar.setVisibility(View.VISIBLE);
        textView.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String state = jsonObject.getString("state");
                        String total_cases = jsonObject.getString("confirmed");
                        String active = jsonObject.getString("active");
                        String recovered = jsonObject.getString("recovered");
                        String deaths = jsonObject.getString("deaths");


                        Geocoder geocoder = new Geocoder(MapsActivity.this);
                        List<Address> locationList = geocoder.getFromLocationName(state, 1);
                        if (locationList.size() > 0) {
                            Address address = locationList.get(0);
                            String locality = address.getLocality();

                            double lat = address.getLatitude();
                            double lng = address.getLongitude();
                            LatLng locationCoordinates = new LatLng(lat, lng);
                            generateMarker(locationCoordinates, state, total_cases, active, recovered, deaths);
                            Toast.makeText(MapsActivity.this, "Map Ready", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            textView.setVisibility(View.INVISIBLE);
                        }

                        progressBar.setVisibility(View.INVISIBLE);
                        textView.setVisibility(View.INVISIBLE);

                    }


                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MapsActivity.this, "error.getMessage", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void fetchDataByDistrict() {

    }


    public void stateWiseDataButton(View view) {
        layout_dialog dialogBox = new layout_dialog();
        dialogBox.show(getSupportFragmentManager(), "DialogBox");
    }

    @Override
    public void applyTexts(final String state, final String District) {

        String url = "https://api.covidindiatracker.com/state_data.json";
        progressBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);



                    for(int i = 0; i< jsonArray.length(); i++){

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String stateName = jsonObject.getString("state");
                        JSONArray jsonArray1 = (JSONArray) jsonObject.get("districtData");
                        if(stateName.equals(state) && stateName != null){
                            //Toast.makeText(MapsActivity.this, stateName, Toast.LENGTH_SHORT).show();
                            for(int j = 0; j< jsonArray1.length(); j++)
                            {

                                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                String districtName = jsonObject1.getString("id");
                                if(districtName.equals("Bengaluru-Urban") || districtName.equals("Bengaluru-Rural"))
                                {
                                    districtName = "Bengaluru";
                                }
                                String totalCases = jsonObject1.getString("confirmed");

                                if(districtName.equals(District) && districtName != null)
                                {

                                    Toast.makeText(MapsActivity.this, districtName, Toast.LENGTH_SHORT).show();
                                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                                    List<Address> locationList = null;
                                    try {
                                        locationList = geocoder.getFromLocationName(districtName, 1);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    if (locationList.size() > 0) {
                                        Address address = locationList.get(0);
                                        String locality = address.getLocality();
                                        double lat = address.getLatitude();
                                        double lng = address.getLongitude();
                                        LatLng locationCoordinates = new LatLng(lat, lng);
                                        markStateData(locationCoordinates, address, totalCases, locality, districtName);
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }else{
                                        Toast.makeText(MapsActivity.this, "Search not found", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }

                                }
                            }

                        }

                    }
                    progressBar.setVisibility(View.INVISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MapsActivity.this, "error.getMessage", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void markStateData(LatLng locationCoordinates, Address address, String totalCases, String locality, String districtName) {
        MarkerOptions markerOptions = new MarkerOptions().title(locality).position(locationCoordinates)
                .snippet("District: "+districtName+", Total cases: "+totalCases);
        mMap.addMarker(markerOptions);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(locationCoordinates, 10));
    }

}



