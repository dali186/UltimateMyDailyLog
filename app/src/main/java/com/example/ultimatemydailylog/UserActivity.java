package com.example.ultimatemydailylog;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UserActivity extends AppCompatActivity {
    Button btn_save_user;
    EditText et_name_user, et_mass_user, et_weight_user, et_fat_user, et_gmass_user, et_gweight_user, et_gfat_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        et_name_user = findViewById(R.id.et_title_user);
        et_mass_user = findViewById(R.id.et_mass_user);
        et_weight_user = findViewById(R.id.et_weight_user);
        et_fat_user = findViewById(R.id.et_fat_user);
        et_gmass_user = findViewById(R.id.et_gmass_user);
        et_gweight_user = findViewById(R.id.et_gweight_user);
        et_gfat_user = findViewById(R.id.et_gfat_user);

        btn_save_user = findViewById(R.id.btn_save_user);

        btn_save_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String setName = et_name_user.getText().toString();
                String setMass = et_mass_user.getText().toString();
                String setWeight = et_weight_user.getText().toString();
                String setFat = et_fat_user.getText().toString();
                String setGmass = et_gmass_user.getText().toString();
                String setGweight = et_gweight_user.getText().toString();
                String setGfat = et_fat_user.getText().toString();


                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("setName", setName);
                intent.putExtra("setMass", setMass);
                intent.putExtra("setWeight", setWeight);
                intent.putExtra("setFat", setFat);
                intent.putExtra("setGmass", setGmass);
                intent.putExtra("setGweight", setGweight);
                intent.putExtra("setGfat", setGfat);

                startActivity(intent);
            }
        });
    }
}