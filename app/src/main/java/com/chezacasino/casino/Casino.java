package com.chezacasino.casino;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.chezacasino.casino.models.MessageModel;
import com.chezacasino.casino.models.TrialsModel;
import com.chezacasino.casino.networking.RetrofitClient;
import com.chezacasino.casino.utils.SharedPreferencesConfig;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.hbb20.CountryCodePicker;

import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Casino extends AppCompatActivity {

    int degree = 0,degree_old = 0;
    Random r;
    Button start,button_deposit;
    EditText deposit_amount,enter_phone_no;
    LinearLayoutCompat linear_deposit,enterPhoneBottom;
    TextView textView,chances,submit;
    ImageView selected,imageRoul,reload;
    Spinner spinner;
    BottomSheetBehavior enterPSheet;
    ProgressBar pr;
    CountryCodePicker ccp;
    SharedPreferencesConfig sharedPreferencesConfig;
    private static final float FACTOR = 4.86f;
    MediaPlayer mediaPlayerSwipe,mediaPlayerCrush;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start = findViewById(R.id.start);
        imageRoul = findViewById(R.id.roul);
        textView = findViewById(R.id.textview);
        chances = findViewById(R.id.chances);
        mediaPlayerSwipe = MediaPlayer.create(this,R.raw.swipe);
        mediaPlayerCrush = MediaPlayer.create(this,R.raw.crush);
        pr = findViewById(R.id.pr);
        submit = findViewById(R.id.submit);
        ccp = findViewById(R.id.ccp);
        enterPhoneBottom = findViewById(R.id.enterPhoneBottom);
        enter_phone_no  =findViewById(R.id.enterPhone);
        reload = findViewById(R.id.reload);
        selected = findViewById(R.id.nav);
        enterPSheet = BottomSheetBehavior.from(enterPhoneBottom);
        sharedPreferencesConfig = new SharedPreferencesConfig(this);
        linear_deposit = findViewById(R.id.linear_deposit);
        deposit_amount = findViewById(R.id.deposit_amount);
        button_deposit = findViewById(R.id.deposit_button);
        spinner = findViewById(R.id.spinner);

        if (sharedPreferencesConfig.readClientsPhone().isEmpty()){
            enterPSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
        }else {
            populatetrials();
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (enter_phone_no.getText().toString().isEmpty()){
                    Toast.makeText(Casino.this,"Enter your phone number",Toast.LENGTH_SHORT).show();
                }if (!ccp.isValidFullNumber()){
                    Toast.makeText(Casino.this,"Enter a valid phone number",Toast.LENGTH_SHORT).show();
                }else {
                    String phone = ccp.getFullNumberWithPlus();
                    sharedPreferencesConfig.saveAuthenticationInformation(phone);
                    populatetrials();
                    enterPSheet.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    Toast.makeText(Casino.this,"Number saved, please proceed",Toast.LENGTH_SHORT).show();
                }
            }
        });
        ccp.registerCarrierNumberEditText(enter_phone_no);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.numbers,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                populatetrials();
            }
        });
        r = new Random();

        button_deposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deposit_amount.getText().toString().isEmpty()){
                    Toast.makeText(Casino.this,"The minimum amount to deposit is Ksh.20",Toast.LENGTH_SHORT).show();
                }
                else {
                    int ii = Integer.parseInt(deposit_amount.getText().toString());
                    String phone = sharedPreferencesConfig.readClientsPhone();
                    String am = deposit_amount.getText().toString();
                    if (ii >= 20) {
                        pr.setVisibility(View.VISIBLE);
                        Call<MessageModel> call = RetrofitClient.getInstance(Casino.this)
                                .getApiConnector()
                                .casiN(phone,am);
                        call.enqueue(new Callback<MessageModel>() {
                            @Override
                            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                                pr.setVisibility(View.GONE);
                                if (response.isSuccessful()) {
                                    populatetrials();
                                    View viewv = Casino.this.getCurrentFocus();
                                    if (viewv != null) {
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(viewv.getWindowToken(), 0);
                                    }
                                    linear_deposit.setVisibility(View.GONE);
                                    deposit_amount.getText().clear();
                                } else {
                                    Toast.makeText(Casino.this, "Server error", Toast.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<MessageModel> call, Throwable t) {
                                pr.setVisibility(View.GONE);
                                Toast.makeText(Casino.this, "Network error", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Toast.makeText(Casino.this, "The minimum amount to deposit is Ksh.20", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferencesConfig.readClientsPhone().isEmpty()){
                    enterPSheet.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else {
                    final int num = Integer.parseInt(chances.getText().toString());
                    if (num > 0) {
                        mediaPlayerSwipe.start();
                        linear_deposit.setVisibility(View.GONE);
                        degree_old = degree % 360;
                        degree = r.nextInt(3600) + 720;
                        RotateAnimation rotate = new RotateAnimation(degree_old, degree,
                                RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
                        rotate.setDuration(3600);
                        rotate.setFillAfter(true);
                        rotate.setInterpolator(new DecelerateInterpolator());
                        rotate.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                                reduceTrials();
                                textView.setText("-");
                                start.setVisibility(View.GONE);
                                chances.setText(String.valueOf(num - 1));
                                spinner.setEnabled(false);

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                if (spinner.getSelectedItem().toString().equals(textView.getText().toString())){
                                    mediaPlayerCrush.start();
                                }
                                textView.setText(currentNumber(360 - (degree % 360)));
                                start.setVisibility(View.VISIBLE);
                                spinner.setEnabled(true);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                        imageRoul.startAnimation(rotate);
                    } else {
                        linear_deposit.setVisibility(View.VISIBLE);
                        Toast.makeText(Casino.this, "You have no trials, deposit to continue", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });
    }

    private void reduceTrials() {
        String phone = sharedPreferencesConfig.readClientsPhone();
        Call<MessageModel> call = RetrofitClient.getInstance(Casino.this)
                .getApiConnector()
                .reduceT(phone);
        call.enqueue(new Callback<MessageModel>() {
            @Override
            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                pr.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                } else {
                    //Toast.makeText(Casino.this, "Server error", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<MessageModel> call, Throwable t) {
                pr.setVisibility(View.GONE);
                //Toast.makeText(Casino.this, "Network error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void populatetrials() {
        pr.setVisibility(View.VISIBLE);
        start.setEnabled(false);
        reload.setVisibility(View.GONE);
        String phone = sharedPreferencesConfig.readClientsPhone();
        Call<TrialsModel> call = RetrofitClient.getInstance(Casino.this)
                .getApiConnector()
                .pTrials(phone);
        call.enqueue(new Callback<TrialsModel>() {
            @Override
            public void onResponse(Call<TrialsModel> call, Response<TrialsModel> response) {
                pr.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getNum()>=0){
                        chances.setText(response.body().getNum()+"");
                        start.setEnabled(true);
                        linear_deposit.setVisibility(View.GONE);
                    }
                    else{
                        chances.setText("-");
                        linear_deposit.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    Toast.makeText(Casino.this,"Server error",Toast.LENGTH_SHORT).show();
                    reload.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<TrialsModel> call, Throwable t) {
                pr.setVisibility(View.GONE);
                Toast.makeText(Casino.this,"Network error"+t.getMessage(),Toast.LENGTH_SHORT).show();
                reload.setVisibility(View.VISIBLE);
            }

        });
    }

    private String currentNumber(int degrees){
        String text = "";
        if (degrees >=(FACTOR * 1)&& degrees < (FACTOR * 3)){
            text = "32 Red";
        }
        if (degrees >=(FACTOR * 3)&& degrees < (FACTOR * 5)){
            text = "15 Black";
        }
        if (degrees >=(FACTOR * 5)&& degrees < (FACTOR * 7)){
            text = "19 Red";
        }
        if (degrees >=(FACTOR * 7)&& degrees < (FACTOR * 9)){
            text = "4 Black";
        }
        if (degrees >=(FACTOR * 9)&& degrees < (FACTOR * 11)){
            text = "21 Red";
        }
        if (degrees >=(FACTOR * 11)&& degrees < (FACTOR * 13)){
            text = "2 Black";
        }
        if (degrees >=(FACTOR * 13)&& degrees < (FACTOR * 15)){
            text = "25 Red";
        }
        if (degrees >=(FACTOR * 15)&& degrees < (FACTOR * 17)){
            text = "17 Black";
        }
        if (degrees >=(FACTOR * 17)&& degrees < (FACTOR * 19)){
            text = "34 Red";
        }
        if (degrees >=(FACTOR * 19)&& degrees < (FACTOR * 21)){
            text = "6 Black";
        }
        if (degrees >=(FACTOR * 21)&& degrees < (FACTOR * 23)){
            text = "27 Red";
        }
        if (degrees >=(FACTOR * 23)&& degrees < (FACTOR * 25)){
            text = "13 Black";
        }
        if (degrees >=(FACTOR * 25)&& degrees < (FACTOR * 27)){
            text = "36 Red";
        }
        if (degrees >=(FACTOR * 27)&& degrees < (FACTOR * 29)){
            text = "11 Black";
        }
        if (degrees >=(FACTOR * 29)&& degrees < (FACTOR * 31)){
            text = "30 Red";
        }
        if (degrees >=(FACTOR * 31)&& degrees < (FACTOR * 33)){
            text = "8 Black";
        }
        if (degrees >=(FACTOR * 33)&& degrees < (FACTOR * 35)){
            text = "23 Red";
        }
        if (degrees >=(FACTOR * 35)&& degrees < (FACTOR * 37)){
            text = "10 Black";
        }
        if (degrees >=(FACTOR * 37)&& degrees < (FACTOR * 39)){
            text = "5 Red";
        }
        if (degrees >=(FACTOR * 39)&& degrees < (FACTOR * 41)){
            text = "24 Black";
        }
        if (degrees >=(FACTOR * 41)&& degrees < (FACTOR * 43)){
            text = "16 Red";
        }
        if (degrees >=(FACTOR * 43)&& degrees < (FACTOR * 45)){
            text = "33 Black";
        }
        if (degrees >=(FACTOR * 45)&& degrees < (FACTOR * 47)){
            text = "1 Red";
        }
        if (degrees >=(FACTOR * 47)&& degrees < (FACTOR * 49)){
            text = "20 Black";
        }
        if (degrees >=(FACTOR * 49)&& degrees < (FACTOR * 51)){
            text = "14 Red";
        }
        if (degrees >=(FACTOR * 51)&& degrees < (FACTOR * 53)){
            text = "31 Black";
        }
        if (degrees >=(FACTOR * 53)&& degrees < (FACTOR * 55)){
            text = "9 Red";
        }
        if (degrees >=(FACTOR * 55)&& degrees < (FACTOR * 57)){
            text = "22 Black";
        }
        if (degrees >=(FACTOR * 57)&& degrees < (FACTOR * 59)){
            text = "18 Red";
        }
        if (degrees >=(FACTOR * 59)&& degrees < (FACTOR * 61)){
            text = "29 Black";
        }
        if (degrees >=(FACTOR * 61)&& degrees < (FACTOR * 63)){
            text = "7 Red";
        }
        if (degrees >=(FACTOR * 63)&& degrees < (FACTOR * 65)){
            text = "28 Black";
        }
        if (degrees >=(FACTOR * 65)&& degrees < (FACTOR * 67)){
            text = "12 Red";
        }
        if (degrees >=(FACTOR * 67)&& degrees < (FACTOR * 69)){
            text = "35 Black";
        }
        if (degrees >=(FACTOR * 69)&& degrees < (FACTOR * 71)){
            text = "3 Red";
        }
        if (degrees >=(FACTOR * 71)&& degrees < (FACTOR * 73)){
            text = "26 Black";
        }
        if ((degrees >= (FACTOR * 73) && degrees<360) || (degrees>=0 && degrees<(FACTOR * 1))){
            text = "0";
        }

        return text;
    }
}
