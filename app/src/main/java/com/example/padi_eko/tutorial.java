package com.example.padi_eko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class tutorial extends AppCompatActivity {

    private Button btn_kembali_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        btn_kembali_menu = findViewById(R.id.btn_balik_menu3);

        btn_kembali_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(tutorial.this, MainActivity.class));
                finish();
            }
        });
    }
}