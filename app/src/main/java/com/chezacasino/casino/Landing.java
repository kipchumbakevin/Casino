package com.chezacasino.casino;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Landing extends AppCompatActivity {
    TextView casino,cards,candy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        casino = findViewById(R.id.casino);
        cards = findViewById(R.id.cards);
        candy = findViewById(R.id.candy);
        casino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Landing.this,Casino.class);
                startActivity(intent);
            }
        });
        candy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Landing.this,CandyCrush.class);
                startActivity(intent);
            }
        });
        cards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Landing.this,Cards.class);
                startActivity(intent);
            }
        });
    }
}
