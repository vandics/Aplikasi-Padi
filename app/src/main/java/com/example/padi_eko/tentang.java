package com.example.padi_eko;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class tentang extends AppCompatActivity {
    private Button btn_balik_menu2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tentang);

        btn_balik_menu2 = findViewById(R.id.btn_balik_menu2);

        btn_balik_menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(tentang.this, MainActivity.class));
                finish();
            }
        });
    }
}