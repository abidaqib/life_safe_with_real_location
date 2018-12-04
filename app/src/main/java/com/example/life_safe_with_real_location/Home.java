package com.example.life_safe_with_real_location;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import java.util.HashMap;

public class Home extends AppCompatActivity {

    String u_id;

    private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

       // Toast.makeText(this, new IdHulder().getU_id(), Toast.LENGTH_SHORT).show();

        u_id = new IdHulder().getU_id();
        // u_id = getIntent().getStringExtra("u_id");
        Toast.makeText(this, u_id, Toast.LENGTH_SHORT).show();
        requestQueue = Volley.newRequestQueue(this);

    }

    public void onClickDocror(View view) {
        Toast.makeText(this, "Doctor", Toast.LENGTH_SHORT).show();
        dialogBox("Do you want to call the Ambulance?", "Ambulance");
    }

    public void onClickIn(View view) {
        Toast.makeText(this, "In", Toast.LENGTH_SHORT).show();
    }

    public void onClickEmergency(View view) {
        Toast.makeText(this, "Emergency", Toast.LENGTH_SHORT).show();
        dialogBox("Do you want to call the police and Ambulance?", "Emergency");
    }

    public void onClickFire(View view) {
        Toast.makeText(this, "Fire", Toast.LENGTH_SHORT).show();
        dialogBox("Do you want to call the Fire Service?", "Fire Service");
    }

    public void onClickPolice(View view) {
        Toast.makeText(this, "Police", Toast.LENGTH_SHORT).show();
        dialogBox("Do you want to call the police?", "Police");
    }

    void dialogBox(String str, final String who){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);


        builder.setTitle("Confirm");
        builder.setMessage(str);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                Toast.makeText(Home.this, "Yes", Toast.LENGTH_SHORT).show();
                String who_id = null;
                if(who=="Police"){
                    who_id = "2";
                }
                else if(who == "Fire Service"){
                    who_id = "3";
                }
                else if(who == "Ambulance"){
                    who_id = "4";
                }
                else if(who == "Emergency"){
                    who_id = "5";
                }
                help(u_id, who_id, "0", "0");
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Home.this, "No", Toast.LENGTH_SHORT).show();
                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    void help(final String help_u_id, final String who_help, final String who_helped_u_id, final String is_safe ) {
        String url = new UrlHolder().getURL() + "Help.php";
        try {

            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(Home.this, response, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Home.this, Map.class));
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Home.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected java.util.Map<String, String> getParams() throws AuthFailureError {
                    java.util.Map<String, String> params = new HashMap<>();

                    params.put("help_u_id", help_u_id);
                    params.put("who_help", who_help);
                    params.put("who_helped_u_id", who_helped_u_id);
                    params.put("is_safe", is_safe);

                    return params;
                }
            };

            requestQueue.add(request);

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    public void seeMyLocation(View view) {
        Toast.makeText(this, "See location", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Home.this, Map.class));
    }
}
