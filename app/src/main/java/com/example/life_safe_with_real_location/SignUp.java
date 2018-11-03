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

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    EditText et_name;
    EditText et_number;
    EditText et_password;


   private RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        et_name=findViewById(R.id.et_name);
        et_number=findViewById(R.id.et_number);
        et_password=findViewById(R.id.et_password);

        requestQueue = Volley.newRequestQueue(this);
    }

    public void onclickSignUpIn(View view) {

        final String name = et_name.getText().toString();
        final String number = et_number.getText().toString();
        final String u_password = et_password.getText().toString();
        final String who = "1";
        String url = new UrlHolder().getURL() + "SignUp.php";

        if (name.trim().isEmpty() || number.trim().isEmpty() || u_password.trim().isEmpty()) {
            Toast.makeText(this, "Fill up all items", Toast.LENGTH_SHORT).show();
        } else {
            StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(SignUp.this, response, Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(SignUp.this, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    params.put("name", name);
                    params.put("number", number);
                    params.put("u_password", u_password);
                    params.put("who", who);

                    return params;
                }
            };

            requestQueue.add(request);

            et_name.setText("");
            et_password.setText("");
            et_number.setText("");


            startActivity(new Intent(this, Verifie.class));
        }
    }
}
