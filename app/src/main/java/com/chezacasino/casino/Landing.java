package com.chezacasino.casino;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chezacasino.casino.utils.SharedPreferencesConfig;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.hbb20.CountryCodePicker;

public class Landing extends AppCompatActivity {
    TextView casino,cards,candy,submit;
    SharedPreferencesConfig sharedPreferencesConfig;
    BottomSheetBehavior enterPSheet;
    CountryCodePicker ccp;
    EditText enter_phone_no;
    LinearLayoutCompat enterPhoneBottom;
    int startintent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        casino = findViewById(R.id.casino);
        cards = findViewById(R.id.cards);
        candy = findViewById(R.id.candy);
        submit = findViewById(R.id.submit);
        enter_phone_no  =findViewById(R.id.enterPhone);
        ccp = findViewById(R.id.ccp);
        enterPhoneBottom = findViewById(R.id.enterPhoneBottom);
        startintent = 0;
        enterPSheet = BottomSheetBehavior.from(enterPhoneBottom);
        sharedPreferencesConfig = new SharedPreferencesConfig(this);
        ccp.registerCarrierNumberEditText(enter_phone_no);
        casino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startintent = 1;
                if (sharedPreferencesConfig.readClientsPhone().isEmpty()){
                    enterPSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else {
                    Intent intent = new Intent(Landing.this,Casino.class);
                    startActivity(intent);
                }
            }
        });
        candy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startintent = 2;
                if (sharedPreferencesConfig.readClientsPhone().isEmpty()){
                    enterPSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else {
                    Intent intent = new Intent(Landing.this,CandyCrush.class);
                    startActivity(intent);
                }
            }
        });
        cards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startintent = 3;
                if (sharedPreferencesConfig.readClientsPhone().isEmpty()){
                    enterPSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else {
                    Intent intent = new Intent(Landing.this,Cards.class);
                    startActivity(intent);
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enter_phone_no.getText().toString().isEmpty()){
                    Toast.makeText(Landing.this,"Enter your phone number",Toast.LENGTH_SHORT).show();
                }if (!ccp.isValidFullNumber()){
                    Toast.makeText(Landing.this,"Enter a valid phone number",Toast.LENGTH_SHORT).show();
                }else {
                    String phone = ccp.getFullNumberWithPlus();
                    sharedPreferencesConfig.saveAuthenticationInformation(phone);
                    enterPSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    if (startintent == 1){
                        Intent intent = new Intent(Landing.this,Casino.class);
                        startActivity(intent);
                    }else if (startintent == 2){
                        Intent intent = new Intent(Landing.this,CandyCrush.class);
                        startActivity(intent);
                    }else if (startintent == 3){
                        Intent intent = new Intent(Landing.this,Cards.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }
}
