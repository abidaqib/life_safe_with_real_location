package com.example.life_safe_with_real_location;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Map extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;

    double lat;
    double lng;


    LocationManager locationManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        myLocation();



    }



    private  void  myLocation(){

        //LocationManager locationManager; should be aa global variable ........

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    lat = location.getLatitude();
                    lng = location.getLongitude();


                    //insert latitude and longitude in database...

                    try {
                        Thread.sleep(1000);
                        //insert latitude and longitude in database...
                        //insertLocation(String.valueOf(lat),String.valueOf(lng));

                        Toast.makeText(Map.this, "Net: "+String.valueOf(lat)+","+String.valueOf(lng), Toast.LENGTH_SHORT).show();

                        putLocationToShow(lat, lng);

                        //free location test with out net......
                        //putLocationToShow(lat, lng);


                        // get last value of latitude and longitude from database......
                        //showLocation();

                        //      Toast.makeText(Map.this, String.valueOf(id[0])+","+String.valueOf(lat1[0])+","+String.valueOf(lng1[0]), Toast.LENGTH_SHORT).show();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        }
        else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    lat = location.getLatitude();
                    lng = location.getLongitude();


                    //insert latitude and longitude in database...

                    try {
                        Thread.sleep(1000);
                        //insert latitude and longitude in database...
                       // insertLocation(String.valueOf(lat),String.valueOf(lng));

                        Toast.makeText(Map.this, "GPS: "+String.valueOf(lat)+","+String.valueOf(lng), Toast.LENGTH_SHORT).show();

                        putLocationToShow(lat, lng);

                        //free location test with out net......
                        //putLocationToShow(lat, lng);

                       // showLocation();
                        //      Toast.makeText(Map.this, String.valueOf(id[0])+","+String.valueOf(lat1[0])+","+String.valueOf(lng1[0]), Toast.LENGTH_SHORT).show();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onStatusChanged(String s, int i, Bundle bundle) {

                }

                @Override
                public void onProviderEnabled(String s) {

                }

                @Override
                public void onProviderDisabled(String s) {

                }
            });
        }
    }

    private void showLocation() {
        RequestQueue requestQueue = Volley.newRequestQueue(Map.this);

        String BASE_URL = new UrlHolder().getURL()+"showLocation.php";

        StringRequest request = new StringRequest(Request.Method.GET, BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray root = new JSONArray(response);

                    for (int i = 0; i < root.length(); i++) {
                        JSONObject farmer = root.getJSONObject(i);

                        Toast.makeText(Map.this, String.valueOf(farmer.getDouble("id"))+","+String.valueOf(farmer.getDouble("lat"))+","+String.valueOf(farmer.getDouble("lng")), Toast.LENGTH_SHORT).show();

                        // this method have two argoment and it take the valus and show in to map.......
                        putLocationToShow(farmer.getDouble("lat"), farmer.getDouble("lng"));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Map.this, ""+e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });


        requestQueue.add(request);

        // data fetch end here


    }

    private void putLocationToShow(double lat, double lng) {
        LatLng latLng = new LatLng(lat, lng);

        Geocoder geocoder = new Geocoder(getApplicationContext());
        try {
            mMap.clear();

            List<Address> addressList =  geocoder.getFromLocation(lat ,lng, 1);
//            String str = addressList.get(0).getLocality()+","+addressList.get(0).getCountryName()+","+String.valueOf(lat)+","+String.valueOf(lng);
            String str = String.valueOf(lat)+","+String.valueOf(lng);

            mMap.addMarker(new MarkerOptions().position(latLng).title(str));
           // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.3f));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertLocation(final String lat, final String lng) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url = new UrlHolder().getURL()+"locationInsert.php";

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(Map.this, response, Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }

        }) {
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {
                java.util.Map<String, String> params = new HashMap<>();

                params.put("lat", lat);
                params.put("lng", lng);
                params.put("u_id", new IdHulder().getU_id());

                return params;
            }
        };

        requestQueue.add(request);
    }




    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

      /*  // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    */
    }

    public void goToLocation(View view) {

        LatLng latLng = new LatLng(lat, lng);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.3f));
    }
}
