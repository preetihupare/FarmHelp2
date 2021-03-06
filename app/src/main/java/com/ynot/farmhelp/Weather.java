package com.ynot.farmhelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;

public class Weather extends AppCompatActivity {

    private ScrollView home;
    private ProgressBar loading;
    private TextView cityName, temperature, condition, humidity, sunrise, sunset, cloud;
    private TextView windSpeed, maxWindSpeed, maxTemp, isRain;
    private TextInputEditText editCity;
    private ImageView background, search, tempStatus;
    private RecyclerView weather, weather1;
    private ArrayList<WeatherModel> weatherModelArrayList;
    private ArrayList<WeatherModel1> weatherModelArrayList1;
    private WeatherAdapter weatherAdapter;
    private WeatherAdapter1 weatherAdapter1;
    private LocationManager locationManager;
    private int PERMISSION_CODE = 1;
    private String CityName;
    DecimalFormat df = new DecimalFormat("#.#");

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    FirebaseUser user;
    StorageReference storageReference;
    public Weather() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS); // for full screen with no status bar
        home = findViewById(R.id.Home);
        loading = findViewById(R.id.Loading);
        cityName = findViewById(R.id.cityName);
        temperature = findViewById(R.id.temperature);
        condition = findViewById(R.id.condition);
        humidity = findViewById(R.id.humidity);
        cloud = findViewById(R.id.cloud);
        sunrise = findViewById(R.id.sunrise);
        sunrise = findViewById(R.id.sunset);
        windSpeed = findViewById(R.id.windSpeed);
        maxWindSpeed = findViewById(R.id.maxWindSpeed);
        maxTemp = findViewById(R.id.maxTemp);
        isRain = findViewById(R.id.isRain);
        weather = findViewById(R.id.recycler);
        weather1 = findViewById(R.id.recycler1);
        editCity = findViewById(R.id.editCity);
        background = findViewById(R.id.background);
        tempStatus = findViewById(R.id.tempStatus);
        search = findViewById(R.id.search);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();

        weatherModelArrayList = new ArrayList<>();
        weatherAdapter = new WeatherAdapter(this, weatherModelArrayList);
        //set adapter to recycleView
        weather.setAdapter(weatherAdapter);

        weatherModelArrayList1 = new ArrayList<>();
        weatherAdapter1 = new WeatherAdapter1(this, weatherModelArrayList1);
        //set adapter to recycleView
        weather1.setAdapter(weatherAdapter1);


        loading.setVisibility(View.VISIBLE);
        home.setVisibility(View.GONE);


        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    cityName.setText(documentSnapshot.getString("city"));
                    CityName = documentSnapshot.getString("city");
                    editCity.setText(CityName);

                    try {
                        getWeatherInfo(CityName);
                        getWeatherInfo2(CityName);
                    } catch ( Exception e1 ) {
                        Toast.makeText(Weather.this, "Please Enter Valid City Name", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Log.d("tag", "onEvent: Data Not Found");
                }
            }
        });

        //to fetch live location and to check location access permissions
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Weather.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }

        //Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        //Location location = getLastKnownLocation();

//        if (location != null){CityName = getCityName(location.getLongitude(),location.getLatitude());
//            getWeatherInfo(CityName);
//        } else {
//            CityName = "Kolhapur";
//
//        }

//        CityName = "Sangli";
//        getWeatherInfo(CityName);

        //CityName = getCityName(location.getLongitude(), location.getLatitude());
        //getWeatherInfo(CityName);




        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = editCity.getText().toString();
                    if (city.isEmpty()) {
                        Toast.makeText(Weather.this, "Please Enter City Name", Toast.LENGTH_SHORT).show();
                    } else {
                        cityName.setText(CityName);
                        getWeatherInfo(city);
                        getWeatherInfo2(city);
                    }
                }
        });
    }

//    private Location getLastKnownLocation() {
//        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
//        List<String> providers = locationManager.getProviders(true);
//        Location bestLocation = null;
//        for (String provider : providers) {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details
//            }
//            Location l = locationManager.getLastKnownLocation(provider);
//            if( l == null ) {
//                continue;
//            }
//            if ( bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
//                bestLocation = l;
//            }
//        }
//
//        return bestLocation;
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if( requestCode == PERMISSION_CODE) {
            if ( grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                Toast.makeText(this,"Permission Granted..", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this,"Please provide the permissions", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private String getCityName(double longitude, double latitude) {
        String cityname = "Not Found";
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latitude,longitude,10);
            for ( Address adr : addresses ) {
                if ( adr != null ) {
                    String city = adr.getLocality();
                    if(city!=null && !city.equals("")) {
                        cityname = city;
                    }
                    else {
                        Log.d("TAG","CITY NOT FOUND");
                        Toast.makeText(this,"User City Not Found..", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        }

        return cityname;
    }

    private void getWeatherInfo(String CityName) {
        String url = "https://api.weatherapi.com/v1/forecast.json?key=7ac5bee01cf749cbb34191911221706&q=" + CityName + "&days=1&aqi=yes&alerts=yes";
        cityName.setText(CityName);
        RequestQueue requestQueue = Volley.newRequestQueue(Weather.this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                loading.setVisibility(View.GONE);
                home.setVisibility(View.VISIBLE);
                weatherModelArrayList.clear();

                try {
                    String temper = response.getJSONObject("current").getString("temp_c");
                    temperature.setText(temper+ "???");
                    int isDay = response.getJSONObject("current").getInt("is_day");
                    String condi = response.getJSONObject("current").getJSONObject(("condition")).getString("text");
                    String conditionIcon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Picasso.get().load("https:".concat(conditionIcon)).into(tempStatus);
                    condition.setText(condi);

                    if( isDay == 1 ) {
                        Picasso.get().load("https://img.freepik.com/free-photo/clouds-sky_53876-33576.jpg?t=st=1655051939~exp=1655052539~hmac=1086a960522da315a87faa944dde58a6425f4b313e5d19784da923a27c4d0df3&w=996.jpg").into(background);
                    }
                    else {
                        Picasso.get().load("https://images.pexels.com/photos/3812773/pexels-photo-3812773.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1.jpg").into(background);
                    }
                    String humid = response.getJSONObject("current").getString("humidity");
                    humidity.setText((humid+ "%"));

                    String clou = response.getJSONObject("current").getString("cloud");
                    cloud.setText((clou+ "%"));

                    String windSp = response.getJSONObject("current").getString("wind_kph");
                    windSpeed.setText((windSp + "kph"));

                    //to get forecast
                    JSONObject forecastObj = response.getJSONObject("forecast");
                    JSONObject forcastO = forecastObj.getJSONArray("forecastday").getJSONObject(0);
                    JSONObject astro = forcastO.getJSONObject("astro");
                    JSONObject day = forcastO.getJSONObject("day");
                    JSONArray hourArray = forcastO.getJSONArray("hour");

                    String sunri = astro.getString("sunrise");
                    sunrise.setText(sunri);

                    String sunse = astro.getString("sunset");
                    sunrise.setText(sunse);

                    String maxWindSp = day.getString("maxwind_kph");
                    maxWindSpeed.setText((maxWindSp + "kph"));

                    String maxTem = day.getString("maxtemp_c");
                    maxTemp.setText((maxTem+"???"));

                    int isRaining = day.getInt("daily_will_it_rain");
                    if ( isRaining == 1 ) {
                        isRain.setText("YES");
                    }
                    else {
                        isRain.setText("NO");
                    }

                    for ( int i=0; i<hourArray.length(); i++ ) {
                        JSONObject hourObj = hourArray.getJSONObject(i);
                        String ti = hourObj.getString("time");
                        String te = hourObj.getString("temp_c");
                        String img = hourObj.getJSONObject("condition").getString("icon");
                        String win = hourObj.getString("wind_kph");
                        weatherModelArrayList.add(new WeatherModel(ti,te,img,win));
                    }

                    weatherAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Weather.this, "Please Enter Valid City Name", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public void getWeatherInfo2(String city) {
        String tempUrl = "https://api.openweathermap.org/data/2.5/forecast?q=" + city + "&appid=592ed9132bfe8caa35a4d9b7e44c105b";

            RequestQueue requestQueue1 = Volley.newRequestQueue(getApplicationContext());
            JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, tempUrl, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    weatherModelArrayList1.clear();
                    try {
                        JSONArray jsonArray = response.getJSONArray("list");

                        for ( int i=2; i<jsonArray.length(); i++ ) {
                            JSONObject weatherObj = jsonArray.getJSONObject(i);
                            String date = weatherObj.getString("dt_txt");
                            JSONObject jsonObjectMain = weatherObj.getJSONObject("main");
                            String time = date;
                            double t = jsonObjectMain.getDouble("temp") - 273.15;
                            String temp = Double.toString(Double.parseDouble(df.format(t)));
                            JSONArray weatherArray = weatherObj.getJSONArray("weather");
                            JSONObject jsonObjectWeather = weatherArray.getJSONObject(0);
                            String description = jsonObjectWeather.getString("description");
                            String humidity = jsonObjectMain.getString("humidity");
                            JSONObject jsonObjectClouds = weatherObj.getJSONObject("clouds");
                            String clouds = jsonObjectClouds.getString("all");
                            JSONObject jsonObjectWind = weatherObj.getJSONObject("wind");
                            double s = jsonObjectWind.getDouble("speed");
                            s *= 3.6;
                            String wind = Double.toString(Double.parseDouble(df.format(s)));
                            String main = jsonObjectWeather.getString("main");
                            String isRain;
                            if ( main.contains("Rain")) {
                                isRain = "YES";
                            }
                            else {
                                isRain = "NO";
                            }
//                            JSONObject jsonObjectSys = weatherObj.getJSONObject("sys");
//                            String dayNight = jsonObjectClouds.getString("pod");
                            weatherModelArrayList1.add(new WeatherModel1(date,time,temp,description,humidity,clouds,wind,isRain));
                        }

                        weatherAdapter1.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener(){

                @Override
                public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(getApplicationContext(), "Please Enter Valid City Name", Toast.LENGTH_SHORT).show();
                }
            });

        requestQueue1.add(jsonObjectRequest1);
    }
}
