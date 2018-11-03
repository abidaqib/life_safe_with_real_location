package com.example.life_safe_with_real_location;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    EditText et_number;
    EditText et_password;

    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        et_number = findViewById(R.id.et_number);
        et_password = findViewById(R.id.et_password);

        requestQueue = Volley.newRequestQueue(this);

        new IdHulder().setU_id("-1"); // it because static variable memorise valo soo fist we give it garvage variable....
        Toast.makeText(this, new IdHulder().getU_id(), Toast.LENGTH_SHORT).show();3
    }

    public void onclickLoginIn(View view) {

        final String number = et_number.getText().toString();
        final String password = et_password.getText().toString();
        String url = new UrlHolder().getURL()+"LogIn.php";


        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject root = new JSONObject(response);
                    JSONArray result = root.getJSONArray("result");
                    JSONObject user = result.getJSONObject(0);



                    if (user.getString("error").equalsIgnoreCase("no")) {

                        new IdHulder(user.getString("u_id"));
                        Toast.makeText(Login.this, "You are in.."+user.getString("u_id"), Toast.LENGTH_SHORT).show();
                       // startActivity(new Intent(Login.this,Home.class));
                        Intent intent = new Intent(Login.this, Home.class);
                       // intent.putExtra("u_id" ,user.getString("u_id"));
                        startActivity(intent);

                    }

                } catch (JSONException e) {
                    Toast.makeText(Login.this, "" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this, "Error Detected", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put("number", number);
                params.put("password", password);

                return params;
            }
        };

        requestQueue.add(request);


      //  startActivity(new Intent(this, Home.class));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
    }
}
