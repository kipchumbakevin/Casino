package com.chezacasino.casino;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chezacasino.casino.utils.SharedPreferencesConfig;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.hbb20.CountryCodePicker;

public class Landing extends AppCompatActivity {
    TextView casino, cards, candy, submit, share, rate, policy;
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
        enter_phone_no = findViewById(R.id.enterPhone);
        ccp = findViewById(R.id.ccp);
        share = findViewById(R.id.share);
        rate = findViewById(R.id.rate);
        policy = findViewById(R.id.policy);
        enterPhoneBottom = findViewById(R.id.enterPhoneBottom);
        startintent = 0;
        enterPSheet = BottomSheetBehavior.from(enterPhoneBottom);
        sharedPreferencesConfig = new SharedPreferencesConfig(this);
        ccp.registerCarrierNumberEditText(enter_phone_no);
        casino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startintent = 1;
                if (sharedPreferencesConfig.readClientsPhone().isEmpty()) {
                    enterPSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    Intent intent = new Intent(Landing.this, Casino.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        candy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startintent = 2;
                if (sharedPreferencesConfig.readClientsPhone().isEmpty()) {
                    enterPSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    Intent intent = new Intent(Landing.this, CandyCrush.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        cards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startintent = 3;
                if (sharedPreferencesConfig.readClientsPhone().isEmpty()) {
                    enterPSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    Intent intent = new Intent(Landing.this, Cards.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enter_phone_no.getText().toString().isEmpty()) {
                    Toast.makeText(Landing.this, "Enter your phone number", Toast.LENGTH_SHORT).show();
                }
                if (!ccp.isValidFullNumber()) {
                    Toast.makeText(Landing.this, "Enter a valid phone number", Toast.LENGTH_SHORT).show();
                } else {
                    String phone = ccp.getFullNumberWithPlus();
                    sharedPreferencesConfig.saveAuthenticationInformation(phone);
                    enterPSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    if (startintent == 1) {
                        Intent intent = new Intent(Landing.this, Casino.class);
                        startActivity(intent);
                    } else if (startintent == 2) {
                        Intent intent = new Intent(Landing.this, CandyCrush.class);
                        startActivity(intent);
                    } else if (startintent == 3) {
                        Intent intent = new Intent(Landing.this, Cards.class);
                        startActivity(intent);
                    }
                }
            }
        });
        policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.lovidovi.co.ke/ealoanspolicy");
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.lovidovi.co.ke/ealoanspolicy")));
                }
            }
        });
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + Landing.this.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=" + Landing.this.getPackageName())));
                }
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBody =
                        "Find loan Apps to help Build your capital,business and other financial needs. Download EA Loans App now at https://play.google.com/store/apps/details?id="
                                + Landing.this.getPackageName();
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                intent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(intent, "Share via"));
            }
        });
    }
}
