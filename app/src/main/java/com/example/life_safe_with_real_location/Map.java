package com.example.life_safe_with_real_location;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.support.v7.app.AlertDialog;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

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
            Toast.makeText(this, "Not aces", Toast.LENGTH_SHORT).show();
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
                        insertLocation(String.valueOf(lat),String.valueOf(lng));

                        Toast.makeText(Map.this, "Net: "+String.valueOf(lat)+","+String.valueOf(lng), Toast.LENGTH_SHORT).show();

                        putLocationToMarker(lat, lng);

                        searchRequest();

                       // HelpHulder helpHulder = new HelpHulder();

                       // putLocationMarkerOther(Double.valueOf(helpHulder.getLat()), Double.valueOf(helpHulder.getLng()), helpHulder.getH_id());

                       // point();

                       // Toast.makeText(Map.this, String.valueOf( distance(lat, lng, lat+2, lng+2) ), Toast.LENGTH_SHORT).show();
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
                        insertLocation(String.valueOf(lat),String.valueOf(lng));

                        Toast.makeText(Map.this, "GPS: "+String.valueOf(lat)+","+String.valueOf(lng), Toast.LENGTH_SHORT).show();

                        putLocationToMarker(lat, lng);

                        searchRequest();

                        //free location test with out net......
                       // putLocationMarkerOther(lat+1, lng+1);

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

    private void point(){
        LatLng latLng = new LatLng(lat, lng);
        LatLng latLng2 = new LatLng(lat+1, lng+1);
        mMap.addPolyline(new PolylineOptions().add(latLng).add(latLng2));
    }

    private void searchRequest() {
        RequestQueue requestQueue = Volley.newRequestQueue(Map.this);

        String url = new UrlHolder().getURL()+"SearchRequest.php";


        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject root = new JSONObject(response);
                    JSONArray result = root.getJSONArray("result");
                    JSONObject user = result.getJSONObject(0);
                    if (user.getString("error").equalsIgnoreCase("no")) {

                        Toast.makeText(Map.this, "You are in.. "+user.getString("h_id"), Toast.LENGTH_SHORT).show();
                        new HelpHulder(user.getString("h_id"), user.getString("lat"), user.getString("lng"));

                        Toast.makeText(Map.this, user.getString("h_id")+" "+user.getString("lat")+" "+user.getString("lng"), Toast.LENGTH_SHORT).show();
                        putLocationMarkerOther(Double.valueOf(user.getString("lat")), Double.valueOf(user.getString("lng")), user.getString("h_id"));
                    }

                } catch (JSONException e) {
                    Toast.makeText(Map.this, "" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Map.this, "Srch_Hlp: Error Detected is "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected java.util.Map<String, String> getParams() throws AuthFailureError {

                java.util.Map<String, String> params = new HashMap<>();
                params.put("who_help", new IdHulder().getWho());

                return params;
            }
        };

        requestQueue.add(request);


        // data fetch end here


    }

    private void putLocationToMarker(double lat, double lng) {
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

    void dialogBox(String str, final String who){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        builder.setTitle("Confirm");
        builder.setMessage(str+who);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                Toast.makeText(Map.this, "Yes", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Map.this, "No", Toast.LENGTH_SHORT).show();
                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }



    private void putLocationMarkerOther(double h_lat, double h_lng, String h_id) {
        LatLng latLng = new LatLng(h_lat, h_lng);

        Geocoder geocoder = new Geocoder(getApplicationContext());
        try {
          //  mMap.clear();

            List<Address> addressList = geocoder.getFromLocation(h_lat, h_lng, 1);
//            String str = addressList.get(0).getLocality()+","+addressList.get(0).getCountryName()+","+String.valueOf(lat)+","+String.valueOf(lng);
            String str = String.valueOf(lat) + "," + String.valueOf(lng) + " >>"+h_id +" D: "+distance(lat, lng, h_lat, h_lng);

           Marker marker =  mMap.addMarker(new MarkerOptions().position(latLng).title(str));

           /*
            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker m) {
                    Toast.makeText(Map.this, "marker", Toast.LENGTH_SHORT).show();
                    dialogBox("Do our want to save him?", ""+new IdHulder().getWho());
                    return false;
                }
            });
        */

            // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.3f));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
            dist = dist * 1.609344;

        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private void insertLocation(final String lat, final String lng) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String url = new UrlHolder().getURL()+"InsertLocation.php";

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
