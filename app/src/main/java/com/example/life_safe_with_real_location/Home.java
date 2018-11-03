package com.example.life_safe_with_real_location;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    String u_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

       // Toast.makeText(this, new IdHulder().getU_id(), Toast.LENGTH_SHORT).show();

        u_id = new IdHulder().getU_id();
        // u_id = getIntent().getStringExtra("u_id");
        Toast.makeText(this, u_id, Toast.LENGTH_SHORT).show();
    }

    public void onClickDocror(View view) {
        Toast.makeText(this, "Doctor", Toast.LENGTH_SHORT).show();
    }

    public void onClickIn(View view) {
        Toast.makeText(this, "In", Toast.LENGTH_SHORT).show();
    }

    public void onClickEmergency(View view) {
        Toast.makeText(this, "Emergency", Toast.LENGTH_SHORT).show();
    }

    public void onClickFire(View view) {
        Toast.makeText(this, "Fire", Toast.LENGTH_SHORT).show();
    }

    public void onClickPolice(View view) {
        Toast.makeText(this, "Police", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    public void seeMyLocation(View view) {
        Toast.makeText(this, "See location", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, Map.class));
    }
}
