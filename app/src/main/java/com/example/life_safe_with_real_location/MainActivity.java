package com.example.life_safe_with_real_location;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void onClickLogin(View view) {
        startActivity(new Intent(this, Login.class));
    }

    public void onClickSignUp(View view) {
        startActivity(new Intent(this, SignUp.class));
    }
}
