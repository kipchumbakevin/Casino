package com.chezacasino.casino;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chezacasino.casino.models.MessageModel;
import com.chezacasino.casino.models.TrialsModel;
import com.chezacasino.casino.networking.RetrofitClient;
import com.chezacasino.casino.utils.SharedPreferencesConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cards extends AppCompatActivity {
    ImageView left,middle,right,leftd,middled,rightd,leftf,middlef,rightf,first,second,third,correct
            ,notCorrect,reload;
    List<Integer> cards;
    Button new_trial,deposit_button;
    int corr = 0;
    ProgressBar pr;
    EditText deposit_amount;
    TextView chances;
    int resf,resS,resT;
    LinearLayoutCompat payment;
    SharedPreferencesConfig sharedPreferencesConfig;
    MediaPlayer mediaPlayerSwipe,mediaPlayerCrush;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);
        left = findViewById(R.id.left);
        middle = findViewById(R.id.middle);
        right = findViewById(R.id.right);
        leftd = findViewById(R.id.leftD);
        middled = findViewById(R.id.middleD);
        rightd = findViewById(R.id.rightD);
        mediaPlayerSwipe = MediaPlayer.create(this,R.raw.swipe);
        mediaPlayerCrush = MediaPlayer.create(this,R.raw.crush);
        //eN = 0;
        reload = findViewById(R.id.reload);
        leftf = findViewById(R.id.leftF);
        sharedPreferencesConfig = new SharedPreferencesConfig(this);
        middlef = findViewById(R.id.middleF);
        pr = findViewById(R.id.pr);
        rightf = findViewById(R.id.rightF);
        chances = findViewById(R.id.chances);
        deposit_amount = findViewById(R.id.deposit_amount);
        deposit_button = findViewById(R.id.deposit_button);
        new_trial = findViewById(R.id.new_trial);
        first = findViewById(R.id.first);
        second = findViewById(R.id.second);
        payment = findViewById(R.id.linear_deposit);
        third = findViewById(R.id.third);
        correct = findViewById(R.id.correct);
        notCorrect = findViewById(R.id.not_correct);
        populatetrials();
        disable();
//        if (eN == 0){
//            disable();
//        }
        corr = 0;
        resf = 0;
        resS = 0;
        resT = 0;
        cards = new ArrayList<>();
        cards.add(107); //spades
        cards.add(207); //hearts
        cards.add(407); //diamonds
        cards.add(507); //flowers
        cards.add(607); //ninespade
        cards.add(707); //k
        cards.add(807); //j
        cards.add(907); //q
        cards.add(1007); //three
        first.setImageResource(R.drawable.back);
        second.setImageResource(R.drawable.back);
        third.setImageResource(R.drawable.back);
        deposit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (deposit_amount.getText().toString().isEmpty()){
                    Toast.makeText(Cards.this,"The minimum amount to deposit is Ksh.20",Toast.LENGTH_SHORT).show();
                }
                else {
                    int ii = Integer.parseInt(deposit_amount.getText().toString());
                    String phone = sharedPreferencesConfig.readClientsPhone();
                    String am = deposit_amount.getText().toString();
                    if (ii >= 20) {
                        pr.setVisibility(View.VISIBLE);
                        Call<MessageModel> call = RetrofitClient.getInstance(Cards.this)
                                .getApiConnector()
                                .casiN(phone,am);
                        call.enqueue(new Callback<MessageModel>() {
                            @Override
                            public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                                pr.setVisibility(View.GONE);
                                if (response.isSuccessful()) {
                                    populatetrials();
                                    View viewv = Cards.this.getCurrentFocus();
                                    if (viewv != null) {
                                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(viewv.getWindowToken(), 0);
                                    }
                                    payment.setVisibility(View.GONE);
                                    deposit_amount.getText().clear();
                                } else {
                                    Toast.makeText(Cards.this, "Server error", Toast.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<MessageModel> call, Throwable t) {
                                pr.setVisibility(View.GONE);
                                Toast.makeText(Cards.this, "Network error", Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        Toast.makeText(Cards.this, "The minimum amount to deposit is Ksh.20", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        new_trial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ch = Integer.parseInt(chances.getText().toString());
                if (ch>0) {
                    mediaPlayerSwipe.start();
                    reduceTrials();
                    disable();
                    new CountDownTimer(3000, 1000) { // 60 seconds, in 1 second intervals
                        public void onTick(long millisUntilFinished) {
                            Collections.shuffle(cards);
                        }

                        public void onFinish() {
                            enable();
                         //   eN = 1;
                        }
                    }.start();
                    left.setImageResource(R.drawable.back);
                    right.setImageResource(R.drawable.back);
                    middle.setImageResource(R.drawable.back);
                    leftd.setImageResource(R.drawable.back);
                    rightd.setImageResource(R.drawable.back);
                    middled.setImageResource(R.drawable.back);
                    leftf.setImageResource(R.drawable.back);
                    rightf.setImageResource(R.drawable.back);
                    middlef.setImageResource(R.drawable.back);
                    Animation anim_left = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.left);
                    Animation anim_middle = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.middle);
                    Animation anim_right = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.right);
                    Animation anim_leftd = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.leftd);
                    Animation anim_middled = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.middled);
                    Animation anim_rightd = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rightd);
                    left.startAnimation(anim_left);
                    middle.startAnimation(anim_middle);
                    right.startAnimation(anim_right);
                    leftd.startAnimation(anim_leftd);
                    middled.startAnimation(anim_middled);
                    rightd.startAnimation(anim_rightd);
                    leftf.startAnimation(anim_leftd);
                    middlef.startAnimation(anim_middled);
                    rightf.startAnimation(anim_rightd);
                    chances.setText(String.valueOf(ch-1));
                }else {
                    payment.setVisibility(View.VISIBLE);
                    resT=0;
                    resS=0;
                    resf=0;
                    corr=0;
                    setback();
                }
            }
        });
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                populatetrials();
            }
        });
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //assign card
                    if (cards.get(0) == 107) {
                        mediaPlayerCrush.start();
                        left.setImageResource(R.drawable.spade);
                        if (corr == 0) {
                            first.setImageResource(R.drawable.spade);
                            first.setVisibility(View.VISIBLE);
                            corr = 1;
                            resf = 1;
                        } else if (corr == 1) {
                            second.setVisibility(View.VISIBLE);
                            second.setImageResource(R.drawable.spade);
                            corr = 2;
                            resS = 1;
                        } else if (corr == 2) {
                            third.setVisibility(View.VISIBLE);
                            third.setImageResource(R.drawable.spade);
                            corr = 3;
                            resT = 1;
                            if (resf == 1 && resS == 1 && resT == 1) {
                                correct.setVisibility(View.VISIBLE);
                            } else {
                                notCorrect.setVisibility(View.VISIBLE);
                            }
                        }
                        Toast.makeText(Cards.this, "Correct", Toast.LENGTH_SHORT).show();
                    } else if (cards.get(0) == 207) {
                        left.setImageResource(R.drawable.hearts);
                    } else if (cards.get(0) == 407) {
                        left.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(0) == 507) {
                        left.setImageResource(R.drawable.flowers);
                    } else if (cards.get(0) == 607) {
                        left.setImageResource(R.drawable.nine);
                    } else if (cards.get(0) == 707) {
                        left.setImageResource(R.drawable.k);
                    } else if (cards.get(0) == 807) {
                        left.setImageResource(R.drawable.j);
                    } else if (cards.get(0) == 907) {
                        left.setImageResource(R.drawable.q);
                    } else if (cards.get(0) == 1007) {
                        left.setImageResource(R.drawable.three);
                    }


                    if (cards.get(1) == 107) {
                        middle.setImageResource(R.drawable.spade);
                    } else if (cards.get(1) == 207) {
                        middle.setImageResource(R.drawable.hearts);
                    } else if (cards.get(1) == 407) {
                        middle.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(1) == 507) {
                        middle.setImageResource(R.drawable.flowers);
                    } else if (cards.get(1) == 607) {
                        middle.setImageResource(R.drawable.nine);
                    } else if (cards.get(1) == 707) {
                        middle.setImageResource(R.drawable.k);
                    } else if (cards.get(1) == 807) {
                        middle.setImageResource(R.drawable.j);
                    } else if (cards.get(1) == 907) {
                        middle.setImageResource(R.drawable.q);
                    } else if (cards.get(1) == 1007) {
                        middle.setImageResource(R.drawable.three);
                    }


                    if (cards.get(2) == 107) {
                        right.setImageResource(R.drawable.spade);
                    } else if (cards.get(2) == 207) {
                        right.setImageResource(R.drawable.hearts);
                    } else if (cards.get(2) == 407) {
                        right.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(2) == 507) {
                        right.setImageResource(R.drawable.flowers);
                    } else if (cards.get(2) == 607) {
                        right.setImageResource(R.drawable.nine);
                    } else if (cards.get(2) == 707) {
                        right.setImageResource(R.drawable.k);
                    } else if (cards.get(2) == 807) {
                        right.setImageResource(R.drawable.j);
                    } else if (cards.get(2) == 907) {
                        right.setImageResource(R.drawable.q);
                    } else if (cards.get(2) == 1007) {
                        right.setImageResource(R.drawable.three);
                    }


                    if (cards.get(3) == 107) {
                        leftd.setImageResource(R.drawable.spade);
                    } else if (cards.get(3) == 207) {
                        leftd.setImageResource(R.drawable.hearts);
                    } else if (cards.get(3) == 407) {
                        leftd.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(3) == 507) {
                        leftd.setImageResource(R.drawable.flowers);
                    } else if (cards.get(3) == 607) {
                        leftd.setImageResource(R.drawable.nine);
                    } else if (cards.get(3) == 707) {
                        leftd.setImageResource(R.drawable.k);
                    } else if (cards.get(3) == 807) {
                        leftd.setImageResource(R.drawable.j);
                    } else if (cards.get(3) == 907) {
                        leftd.setImageResource(R.drawable.q);
                    } else if (cards.get(3) == 1007) {
                        leftd.setImageResource(R.drawable.three);
                    }


                    if (cards.get(4) == 107) {
                        middled.setImageResource(R.drawable.spade);
                    } else if (cards.get(4) == 207) {
                        middled.setImageResource(R.drawable.hearts);
                    } else if (cards.get(4) == 407) {
                        middled.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(4) == 507) {
                        middled.setImageResource(R.drawable.flowers);
                    } else if (cards.get(4) == 607) {
                        middled.setImageResource(R.drawable.nine);
                    } else if (cards.get(4) == 707) {
                        middled.setImageResource(R.drawable.k);
                    } else if (cards.get(4) == 807) {
                        middled.setImageResource(R.drawable.j);
                    } else if (cards.get(4) == 907) {
                        middled.setImageResource(R.drawable.q);
                    } else if (cards.get(4) == 1007) {
                        middled.setImageResource(R.drawable.three);
                    }


                    if (cards.get(5) == 107) {
                        rightd.setImageResource(R.drawable.spade);
                    } else if (cards.get(5) == 207) {
                        rightd.setImageResource(R.drawable.hearts);
                    } else if (cards.get(5) == 407) {
                        rightd.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(5) == 507) {
                        rightd.setImageResource(R.drawable.flowers);
                    } else if (cards.get(5) == 607) {
                        rightd.setImageResource(R.drawable.nine);
                    } else if (cards.get(5) == 707) {
                        rightd.setImageResource(R.drawable.k);
                    } else if (cards.get(5) == 807) {
                        rightd.setImageResource(R.drawable.j);
                    } else if (cards.get(5) == 907) {
                        rightd.setImageResource(R.drawable.q);
                    } else if (cards.get(5) == 1007) {
                        rightd.setImageResource(R.drawable.three);
                    }


                    if (cards.get(6) == 107) {
                        leftf.setImageResource(R.drawable.spade);
                    } else if (cards.get(6) == 207) {
                        leftf.setImageResource(R.drawable.hearts);
                    } else if (cards.get(6) == 407) {
                        leftf.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(6) == 507) {
                        leftf.setImageResource(R.drawable.flowers);
                    } else if (cards.get(6) == 607) {
                        leftf.setImageResource(R.drawable.nine);
                    } else if (cards.get(6) == 707) {
                        leftf.setImageResource(R.drawable.k);
                    } else if (cards.get(6) == 807) {
                        leftf.setImageResource(R.drawable.j);
                    } else if (cards.get(6) == 907) {
                        leftf.setImageResource(R.drawable.q);
                    } else if (cards.get(6) == 1007) {
                        leftf.setImageResource(R.drawable.three);
                    }
                    if (cards.get(7) == 107) {
                        middlef.setImageResource(R.drawable.spade);
                    } else if (cards.get(7) == 207) {
                        middlef.setImageResource(R.drawable.hearts);
                    } else if (cards.get(7) == 407) {
                        middlef.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(7) == 507) {
                        middlef.setImageResource(R.drawable.flowers);
                    } else if (cards.get(7) == 607) {
                        middlef.setImageResource(R.drawable.nine);
                    } else if (cards.get(7) == 707) {
                        middlef.setImageResource(R.drawable.k);
                    } else if (cards.get(7) == 807) {
                        middlef.setImageResource(R.drawable.j);
                    } else if (cards.get(7) == 907) {
                        middlef.setImageResource(R.drawable.q);
                    } else if (cards.get(7) == 1007) {
                        middlef.setImageResource(R.drawable.three);
                    }

                    if (cards.get(8) == 107) {
                        rightf.setImageResource(R.drawable.spade);
                    } else if (cards.get(8) == 207) {
                        rightf.setImageResource(R.drawable.hearts);
                    } else if (cards.get(8) == 407) {
                        rightf.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(8) == 507) {
                        rightf.setImageResource(R.drawable.flowers);
                    } else if (cards.get(8) == 607) {
                        rightf.setImageResource(R.drawable.nine);
                    } else if (cards.get(8) == 707) {
                        rightf.setImageResource(R.drawable.k);
                    } else if (cards.get(8) == 807) {
                        rightf.setImageResource(R.drawable.j);
                    } else if (cards.get(8) == 907) {
                        rightf.setImageResource(R.drawable.q);
                    } else if (cards.get(8) == 1007) {
                        rightf.setImageResource(R.drawable.three);
                    }
                disable();

            }
        });
        leftd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //assign card
                    if (cards.get(3) == 107) {
                        mediaPlayerCrush.start();
                        leftd.setImageResource(R.drawable.spade);
                        if (corr == 0) {
                            first.setImageResource(R.drawable.spade);
                            first.setVisibility(View.VISIBLE);
                            corr = 1;
                            resf = 1;
                        } else if (corr == 1) {
                            second.setVisibility(View.VISIBLE);
                            second.setImageResource(R.drawable.spade);
                            corr = 2;
                            resS = 1;
                        } else if (corr == 2) {
                            third.setVisibility(View.VISIBLE);
                            third.setImageResource(R.drawable.spade);
                            corr = 3;
                            resT = 1;
                            if (resf == 1 && resS == 1 && resT == 1) {
                                correct.setVisibility(View.VISIBLE);
                            } else {
                                notCorrect.setVisibility(View.VISIBLE);
                            }
                        }
                        Toast.makeText(Cards.this, "Correct", Toast.LENGTH_SHORT).show();
                    } else if (cards.get(3) == 207) {
                        leftd.setImageResource(R.drawable.hearts);
                    } else if (cards.get(3) == 407) {
                        leftd.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(3) == 507) {
                        leftd.setImageResource(R.drawable.flowers);
                    } else if (cards.get(3) == 607) {
                        leftd.setImageResource(R.drawable.nine);
                    } else if (cards.get(3) == 707) {
                        leftd.setImageResource(R.drawable.k);
                    } else if (cards.get(3) == 807) {
                        leftd.setImageResource(R.drawable.j);
                    } else if (cards.get(3) == 907) {
                        leftd.setImageResource(R.drawable.q);
                    } else if (cards.get(3) == 1007) {
                        leftd.setImageResource(R.drawable.three);
                    }


                    if (cards.get(4) == 107) {
                        middled.setImageResource(R.drawable.spade);
                    } else if (cards.get(4) == 207) {
                        middled.setImageResource(R.drawable.hearts);
                    } else if (cards.get(4) == 407) {
                        middled.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(4) == 507) {
                        middled.setImageResource(R.drawable.flowers);
                    } else if (cards.get(4) == 607) {
                        middled.setImageResource(R.drawable.nine);
                    } else if (cards.get(4) == 707) {
                        middled.setImageResource(R.drawable.k);
                    } else if (cards.get(4) == 807) {
                        middled.setImageResource(R.drawable.j);
                    } else if (cards.get(4) == 907) {
                        middled.setImageResource(R.drawable.q);
                    } else if (cards.get(4) == 1007) {
                        middled.setImageResource(R.drawable.three);
                    }

                    if (cards.get(5) == 107) {
                        rightd.setImageResource(R.drawable.spade);
                    } else if (cards.get(5) == 207) {
                        rightd.setImageResource(R.drawable.hearts);
                    } else if (cards.get(5) == 407) {
                        rightd.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(5) == 507) {
                        rightd.setImageResource(R.drawable.flowers);
                    } else if (cards.get(5) == 607) {
                        rightd.setImageResource(R.drawable.nine);
                    } else if (cards.get(5) == 707) {
                        rightd.setImageResource(R.drawable.k);
                    } else if (cards.get(5) == 807) {
                        rightd.setImageResource(R.drawable.j);
                    } else if (cards.get(5) == 907) {
                        rightd.setImageResource(R.drawable.q);
                    } else if (cards.get(5) == 1007) {
                        rightd.setImageResource(R.drawable.three);
                    }

                    if (cards.get(0) == 107) {
                        left.setImageResource(R.drawable.spade);
                    } else if (cards.get(0) == 207) {
                        left.setImageResource(R.drawable.hearts);
                    } else if (cards.get(0) == 407) {
                        left.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(0) == 507) {
                        left.setImageResource(R.drawable.flowers);
                    } else if (cards.get(0) == 607) {
                        left.setImageResource(R.drawable.nine);
                    } else if (cards.get(0) == 707) {
                        left.setImageResource(R.drawable.k);
                    } else if (cards.get(0) == 807) {
                        left.setImageResource(R.drawable.j);
                    } else if (cards.get(0) == 907) {
                        left.setImageResource(R.drawable.q);
                    } else if (cards.get(0) == 1007) {
                        left.setImageResource(R.drawable.three);
                    }

                    if (cards.get(1) == 107) {
                        middle.setImageResource(R.drawable.spade);
                    } else if (cards.get(1) == 207) {
                        middle.setImageResource(R.drawable.hearts);
                    } else if (cards.get(1) == 407) {
                        middle.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(1) == 507) {
                        middle.setImageResource(R.drawable.flowers);
                    } else if (cards.get(1) == 607) {
                        middle.setImageResource(R.drawable.nine);
                    } else if (cards.get(1) == 707) {
                        middle.setImageResource(R.drawable.k);
                    } else if (cards.get(1) == 807) {
                        middle.setImageResource(R.drawable.j);
                    } else if (cards.get(1) == 907) {
                        middle.setImageResource(R.drawable.q);
                    } else if (cards.get(1) == 1007) {
                        middle.setImageResource(R.drawable.three);
                    }

                    if (cards.get(2) == 107) {
                        right.setImageResource(R.drawable.spade);
                    } else if (cards.get(2) == 207) {
                        right.setImageResource(R.drawable.hearts);
                    } else if (cards.get(2) == 407) {
                        right.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(2) == 507) {
                        right.setImageResource(R.drawable.flowers);
                    } else if (cards.get(2) == 607) {
                        right.setImageResource(R.drawable.nine);
                    } else if (cards.get(2) == 707) {
                        right.setImageResource(R.drawable.k);
                    } else if (cards.get(2) == 807) {
                        right.setImageResource(R.drawable.j);
                    } else if (cards.get(2) == 907) {
                        right.setImageResource(R.drawable.q);
                    } else if (cards.get(2) == 1007) {
                        right.setImageResource(R.drawable.three);
                    }

                    if (cards.get(6) == 107) {
                        leftf.setImageResource(R.drawable.spade);
                    } else if (cards.get(6) == 207) {
                        leftf.setImageResource(R.drawable.hearts);
                    } else if (cards.get(6) == 407) {
                        leftf.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(6) == 507) {
                        leftf.setImageResource(R.drawable.flowers);
                    } else if (cards.get(6) == 607) {
                        leftf.setImageResource(R.drawable.nine);
                    } else if (cards.get(6) == 707) {
                        leftf.setImageResource(R.drawable.k);
                    } else if (cards.get(6) == 807) {
                        leftf.setImageResource(R.drawable.j);
                    } else if (cards.get(6) == 907) {
                        leftf.setImageResource(R.drawable.q);
                    } else if (cards.get(6) == 1007) {
                        leftf.setImageResource(R.drawable.three);
                    }
                    if (cards.get(7) == 107) {
                        middlef.setImageResource(R.drawable.spade);
                    } else if (cards.get(7) == 207) {
                        middlef.setImageResource(R.drawable.hearts);
                    } else if (cards.get(7) == 407) {
                        middlef.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(7) == 507) {
                        middlef.setImageResource(R.drawable.flowers);
                    } else if (cards.get(7) == 607) {
                        middlef.setImageResource(R.drawable.nine);
                    } else if (cards.get(7) == 707) {
                        middlef.setImageResource(R.drawable.k);
                    } else if (cards.get(7) == 807) {
                        middlef.setImageResource(R.drawable.j);
                    } else if (cards.get(7) == 907) {
                        middlef.setImageResource(R.drawable.q);
                    } else if (cards.get(7) == 1007) {
                        middlef.setImageResource(R.drawable.three);
                    }

                    if (cards.get(8) == 107) {
                        rightf.setImageResource(R.drawable.spade);
                    } else if (cards.get(8) == 207) {
                        rightf.setImageResource(R.drawable.hearts);
                    } else if (cards.get(8) == 407) {
                        rightf.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(8) == 507) {
                        rightf.setImageResource(R.drawable.flowers);
                    } else if (cards.get(8) == 607) {
                        rightf.setImageResource(R.drawable.nine);
                    } else if (cards.get(8) == 707) {
                        rightf.setImageResource(R.drawable.k);
                    } else if (cards.get(8) == 807) {
                        rightf.setImageResource(R.drawable.j);
                    } else if (cards.get(8) == 907) {
                        rightf.setImageResource(R.drawable.q);
                    } else if (cards.get(8) == 1007) {
                        rightf.setImageResource(R.drawable.three);
                    }
                disable();
            }
        });
        middle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //assign card
                    if (cards.get(1) == 107) {
                        mediaPlayerCrush.start();
                        middle.setImageResource(R.drawable.spade);
                        if (corr == 0) {
                            first.setImageResource(R.drawable.spade);
                            first.setVisibility(View.VISIBLE);
                            corr = 1;
                            resf = 1;
                        } else if (corr == 1) {
                            second.setVisibility(View.VISIBLE);
                            second.setImageResource(R.drawable.spade);
                            corr = 2;
                            resS = 1;
                        } else if (corr == 2) {
                            third.setVisibility(View.VISIBLE);
                            third.setImageResource(R.drawable.spade);
                            corr = 3;
                            resT = 1;
                            if (resf == 1 && resS == 1 && resT == 1) {
                                correct.setVisibility(View.VISIBLE);
                            } else {
                                notCorrect.setVisibility(View.VISIBLE);
                            }
                        }
                        Toast.makeText(Cards.this, "Correct", Toast.LENGTH_SHORT).show();
                    } else if (cards.get(1) == 207) {
                        middle.setImageResource(R.drawable.hearts);
                    } else if (cards.get(1) == 407) {
                        middle.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(1) == 507) {
                        middle.setImageResource(R.drawable.flowers);
                    } else if (cards.get(1) == 607) {
                        middle.setImageResource(R.drawable.nine);
                    } else if (cards.get(1) == 707) {
                        middle.setImageResource(R.drawable.k);
                    } else if (cards.get(1) == 807) {
                        middle.setImageResource(R.drawable.j);
                    } else if (cards.get(1) == 907) {
                        middle.setImageResource(R.drawable.q);
                    } else if (cards.get(1) == 1007) {
                        middle.setImageResource(R.drawable.three);
                    }


                    if (cards.get(0) == 107) {
                        left.setImageResource(R.drawable.spade);
                    } else if (cards.get(0) == 207) {
                        left.setImageResource(R.drawable.hearts);
                    } else if (cards.get(0) == 407) {
                        left.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(0) == 507) {
                        left.setImageResource(R.drawable.flowers);
                    } else if (cards.get(0) == 607) {
                        left.setImageResource(R.drawable.nine);
                    } else if (cards.get(0) == 707) {
                        left.setImageResource(R.drawable.k);
                    } else if (cards.get(0) == 807) {
                        left.setImageResource(R.drawable.j);
                    } else if (cards.get(0) == 907) {
                        left.setImageResource(R.drawable.q);
                    } else if (cards.get(0) == 1007) {
                        left.setImageResource(R.drawable.three);
                    }

                    if (cards.get(2) == 107) {
                        right.setImageResource(R.drawable.spade);
                    } else if (cards.get(2) == 207) {
                        right.setImageResource(R.drawable.hearts);
                    } else if (cards.get(2) == 407) {
                        right.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(2) == 507) {
                        right.setImageResource(R.drawable.flowers);
                    } else if (cards.get(2) == 607) {
                        right.setImageResource(R.drawable.nine);
                    } else if (cards.get(2) == 707) {
                        right.setImageResource(R.drawable.k);
                    } else if (cards.get(2) == 807) {
                        right.setImageResource(R.drawable.j);
                    } else if (cards.get(2) == 907) {
                        right.setImageResource(R.drawable.q);
                    } else if (cards.get(2) == 1007) {
                        right.setImageResource(R.drawable.three);
                    }

                    if (cards.get(3) == 107) {
                        leftd.setImageResource(R.drawable.spade);
                    } else if (cards.get(3) == 207) {
                        leftd.setImageResource(R.drawable.hearts);
                    } else if (cards.get(3) == 407) {
                        leftd.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(3) == 507) {
                        leftd.setImageResource(R.drawable.flowers);
                    } else if (cards.get(3) == 607) {
                        leftd.setImageResource(R.drawable.nine);
                    } else if (cards.get(3) == 707) {
                        leftd.setImageResource(R.drawable.k);
                    } else if (cards.get(3) == 807) {
                        leftd.setImageResource(R.drawable.j);
                    } else if (cards.get(3) == 907) {
                        leftd.setImageResource(R.drawable.q);
                    } else if (cards.get(3) == 1007) {
                        leftd.setImageResource(R.drawable.three);
                    }
                    if (cards.get(4) == 107) {
                        middled.setImageResource(R.drawable.spade);
                    } else if (cards.get(4) == 207) {
                        middled.setImageResource(R.drawable.hearts);
                    } else if (cards.get(4) == 407) {
                        middled.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(4) == 507) {
                        middled.setImageResource(R.drawable.flowers);
                    } else if (cards.get(4) == 607) {
                        middled.setImageResource(R.drawable.nine);
                    } else if (cards.get(4) == 707) {
                        middled.setImageResource(R.drawable.k);
                    } else if (cards.get(4) == 807) {
                        middled.setImageResource(R.drawable.j);
                    } else if (cards.get(4) == 907) {
                        middled.setImageResource(R.drawable.q);
                    } else if (cards.get(4) == 1007) {
                        middled.setImageResource(R.drawable.k);
                    }
                    if (cards.get(5) == 107) {
                        rightd.setImageResource(R.drawable.spade);
                    } else if (cards.get(5) == 207) {
                        rightd.setImageResource(R.drawable.hearts);
                    } else if (cards.get(5) == 407) {
                        rightd.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(5) == 507) {
                        rightd.setImageResource(R.drawable.flowers);
                    } else if (cards.get(5) == 607) {
                        rightd.setImageResource(R.drawable.nine);
                    } else if (cards.get(5) == 707) {
                        rightd.setImageResource(R.drawable.k);
                    } else if (cards.get(5) == 807) {
                        rightd.setImageResource(R.drawable.j);
                    } else if (cards.get(5) == 907) {
                        rightd.setImageResource(R.drawable.q);
                    } else if (cards.get(5) == 1007) {
                        rightd.setImageResource(R.drawable.three);
                    }
                    if (cards.get(6) == 107) {
                        leftf.setImageResource(R.drawable.spade);
                    } else if (cards.get(6) == 207) {
                        leftf.setImageResource(R.drawable.hearts);
                    } else if (cards.get(6) == 407) {
                        leftf.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(6) == 507) {
                        leftf.setImageResource(R.drawable.flowers);
                    } else if (cards.get(6) == 607) {
                        leftf.setImageResource(R.drawable.nine);
                    } else if (cards.get(6) == 707) {
                        leftf.setImageResource(R.drawable.k);
                    } else if (cards.get(6) == 807) {
                        leftf.setImageResource(R.drawable.j);
                    } else if (cards.get(6) == 907) {
                        leftf.setImageResource(R.drawable.q);
                    } else if (cards.get(6) == 1007) {
                        leftf.setImageResource(R.drawable.three);
                    }
                    if (cards.get(7) == 107) {
                        middlef.setImageResource(R.drawable.spade);
                    } else if (cards.get(7) == 207) {
                        middlef.setImageResource(R.drawable.hearts);
                    } else if (cards.get(7) == 407) {
                        middlef.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(7) == 507) {
                        middlef.setImageResource(R.drawable.flowers);
                    } else if (cards.get(7) == 607) {
                        middlef.setImageResource(R.drawable.nine);
                    } else if (cards.get(7) == 707) {
                        middlef.setImageResource(R.drawable.k);
                    } else if (cards.get(7) == 807) {
                        middlef.setImageResource(R.drawable.j);
                    } else if (cards.get(7) == 907) {
                        middlef.setImageResource(R.drawable.q);
                    } else if (cards.get(7) == 1007) {
                        middlef.setImageResource(R.drawable.three);
                    }

                    if (cards.get(8) == 107) {
                        rightf.setImageResource(R.drawable.spade);
                    } else if (cards.get(8) == 207) {
                        rightf.setImageResource(R.drawable.hearts);
                    } else if (cards.get(8) == 407) {
                        rightf.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(8) == 507) {
                        rightf.setImageResource(R.drawable.flowers);
                    } else if (cards.get(8) == 607) {
                        rightf.setImageResource(R.drawable.nine);
                    } else if (cards.get(8) == 707) {
                        rightf.setImageResource(R.drawable.k);
                    } else if (cards.get(8) == 807) {
                        rightf.setImageResource(R.drawable.j);
                    } else if (cards.get(8) == 907) {
                        rightf.setImageResource(R.drawable.q);
                    } else if (cards.get(8) == 1007) {
                        rightf.setImageResource(R.drawable.three);
                    }
                disable();
            }
        });

        middled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //assign card
                    if (cards.get(4) == 107) {
                        mediaPlayerCrush.start();
                        middled.setImageResource(R.drawable.spade);
                        if (corr == 0) {
                            first.setImageResource(R.drawable.spade);
                            first.setVisibility(View.VISIBLE);
                            corr = 1;
                            resf = 1;
                        } else if (corr == 1) {
                            second.setVisibility(View.VISIBLE);
                            second.setImageResource(R.drawable.spade);
                            corr = 2;
                            resS = 1;
                        } else if (corr == 2) {
                            third.setVisibility(View.VISIBLE);
                            third.setImageResource(R.drawable.spade);
                            corr = 3;
                            resT = 1;
                            if (resf == 1 && resS == 1 && resT == 1) {
                                correct.setVisibility(View.VISIBLE);
                            } else {
                                notCorrect.setVisibility(View.VISIBLE);
                            }
                        }
                        Toast.makeText(Cards.this, "Correct", Toast.LENGTH_SHORT).show();
                    } else if (cards.get(4) == 207) {
                        middled.setImageResource(R.drawable.hearts);
                    } else if (cards.get(4) == 407) {
                        middled.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(4) == 507) {
                        middled.setImageResource(R.drawable.flowers);
                    } else if (cards.get(4) == 607) {
                        middled.setImageResource(R.drawable.nine);
                    } else if (cards.get(4) == 707) {
                        middled.setImageResource(R.drawable.k);
                    } else if (cards.get(4) == 807) {
                        middled.setImageResource(R.drawable.j);
                    } else if (cards.get(4) == 907) {
                        middled.setImageResource(R.drawable.q);
                    } else if (cards.get(4) == 1007) {
                        middled.setImageResource(R.drawable.three);
                    }


                    if (cards.get(3) == 107) {
                        leftd.setImageResource(R.drawable.spade);
                    } else if (cards.get(3) == 207) {
                        leftd.setImageResource(R.drawable.hearts);
                    } else if (cards.get(3) == 407) {
                        leftd.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(3) == 507) {
                        leftd.setImageResource(R.drawable.flowers);
                    } else if (cards.get(3) == 607) {
                        leftd.setImageResource(R.drawable.nine);
                    } else if (cards.get(3) == 707) {
                        leftd.setImageResource(R.drawable.k);
                    } else if (cards.get(3) == 807) {
                        leftd.setImageResource(R.drawable.j);
                    } else if (cards.get(3) == 907) {
                        leftd.setImageResource(R.drawable.q);
                    } else if (cards.get(3) == 1007) {
                        leftd.setImageResource(R.drawable.three);
                    }

                    if (cards.get(5) == 107) {
                        rightd.setImageResource(R.drawable.spade);
                    } else if (cards.get(5) == 207) {
                        rightd.setImageResource(R.drawable.hearts);
                    } else if (cards.get(5) == 407) {
                        rightd.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(5) == 507) {
                        rightd.setImageResource(R.drawable.flowers);
                    } else if (cards.get(5) == 607) {
                        rightd.setImageResource(R.drawable.nine);
                    } else if (cards.get(5) == 707) {
                        rightd.setImageResource(R.drawable.k);
                    } else if (cards.get(5) == 807) {
                        rightd.setImageResource(R.drawable.j);
                    } else if (cards.get(5) == 907) {
                        rightd.setImageResource(R.drawable.q);
                    } else if (cards.get(5) == 1007) {
                        rightd.setImageResource(R.drawable.three);
                    }

                    if (cards.get(0) == 107) {
                        left.setImageResource(R.drawable.spade);
                    } else if (cards.get(0) == 207) {
                        left.setImageResource(R.drawable.hearts);
                    } else if (cards.get(0) == 407) {
                        left.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(0) == 507) {
                        left.setImageResource(R.drawable.flowers);
                    } else if (cards.get(0) == 607) {
                        left.setImageResource(R.drawable.nine);
                    } else if (cards.get(0) == 707) {
                        left.setImageResource(R.drawable.k);
                    } else if (cards.get(0) == 807) {
                        left.setImageResource(R.drawable.j);
                    } else if (cards.get(0) == 907) {
                        left.setImageResource(R.drawable.q);
                    } else if (cards.get(0) == 1007) {
                        left.setImageResource(R.drawable.three);
                    }


                    if (cards.get(1) == 107) {
                        middle.setImageResource(R.drawable.spade);
                    } else if (cards.get(1) == 207) {
                        middle.setImageResource(R.drawable.hearts);
                    } else if (cards.get(1) == 407) {
                        middle.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(1) == 507) {
                        middle.setImageResource(R.drawable.flowers);
                    } else if (cards.get(1) == 607) {
                        middle.setImageResource(R.drawable.nine);
                    } else if (cards.get(1) == 707) {
                        middle.setImageResource(R.drawable.k);
                    } else if (cards.get(1) == 807) {
                        middle.setImageResource(R.drawable.j);
                    } else if (cards.get(1) == 907) {
                        middle.setImageResource(R.drawable.q);
                    } else if (cards.get(1) == 1007) {
                        middle.setImageResource(R.drawable.three);
                    }
                    if (cards.get(2) == 107) {
                        right.setImageResource(R.drawable.spade);
                    } else if (cards.get(2) == 207) {
                        right.setImageResource(R.drawable.hearts);
                    } else if (cards.get(2) == 407) {
                        right.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(2) == 507) {
                        right.setImageResource(R.drawable.flowers);
                    } else if (cards.get(2) == 607) {
                        right.setImageResource(R.drawable.nine);
                    } else if (cards.get(2) == 707) {
                        right.setImageResource(R.drawable.k);
                    } else if (cards.get(2) == 807) {
                        right.setImageResource(R.drawable.j);
                    } else if (cards.get(2) == 907) {
                        right.setImageResource(R.drawable.q);
                    } else if (cards.get(2) == 1007) {
                        right.setImageResource(R.drawable.three);
                    }


                    if (cards.get(6) == 107) {
                        leftf.setImageResource(R.drawable.spade);
                    } else if (cards.get(6) == 207) {
                        leftf.setImageResource(R.drawable.hearts);
                    } else if (cards.get(6) == 407) {
                        leftf.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(6) == 507) {
                        leftf.setImageResource(R.drawable.flowers);
                    } else if (cards.get(6) == 607) {
                        leftf.setImageResource(R.drawable.nine);
                    } else if (cards.get(6) == 707) {
                        leftf.setImageResource(R.drawable.k);
                    } else if (cards.get(6) == 807) {
                        leftf.setImageResource(R.drawable.j);
                    } else if (cards.get(6) == 907) {
                        leftf.setImageResource(R.drawable.q);
                    } else if (cards.get(6) == 1007) {
                        leftf.setImageResource(R.drawable.three);
                    }
                    if (cards.get(7) == 107) {
                        middlef.setImageResource(R.drawable.spade);
                    } else if (cards.get(7) == 207) {
                        middlef.setImageResource(R.drawable.hearts);
                    } else if (cards.get(7) == 407) {
                        middlef.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(7) == 507) {
                        middlef.setImageResource(R.drawable.flowers);
                    } else if (cards.get(7) == 607) {
                        middlef.setImageResource(R.drawable.nine);
                    } else if (cards.get(7) == 707) {
                        middlef.setImageResource(R.drawable.k);
                    } else if (cards.get(7) == 807) {
                        middlef.setImageResource(R.drawable.j);
                    } else if (cards.get(7) == 907) {
                        middlef.setImageResource(R.drawable.q);
                    } else if (cards.get(7) == 1007) {
                        middlef.setImageResource(R.drawable.three);
                    }

                    if (cards.get(8) == 107) {
                        rightf.setImageResource(R.drawable.spade);
                    } else if (cards.get(8) == 207) {
                        rightf.setImageResource(R.drawable.hearts);
                    } else if (cards.get(8) == 407) {
                        rightf.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(8) == 507) {
                        rightf.setImageResource(R.drawable.flowers);
                    } else if (cards.get(8) == 607) {
                        rightf.setImageResource(R.drawable.nine);
                    } else if (cards.get(8) == 707) {
                        rightf.setImageResource(R.drawable.k);
                    } else if (cards.get(8) == 807) {
                        rightf.setImageResource(R.drawable.j);
                    } else if (cards.get(8) == 907) {
                        rightf.setImageResource(R.drawable.q);
                    } else if (cards.get(8) == 1007) {
                        rightf.setImageResource(R.drawable.three);
                    }
                disable();
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //assign
                    if (cards.get(2) == 107) {
                        mediaPlayerCrush.start();
                        right.setImageResource(R.drawable.spade);
                        if (corr == 0) {
                            first.setImageResource(R.drawable.spade);
                            first.setVisibility(View.VISIBLE);
                            corr = 1;
                            resf = 1;
                        } else if (corr == 1) {
                            second.setVisibility(View.VISIBLE);
                            second.setImageResource(R.drawable.spade);
                            corr = 2;
                            resS = 1;
                        } else if (corr == 2) {
                            third.setVisibility(View.VISIBLE);
                            third.setImageResource(R.drawable.spade);
                            corr = 3;
                            resT = 1;
                            if (resf == 1 && resS == 1 && resT == 1) {
                                correct.setVisibility(View.VISIBLE);
                            } else {
                                notCorrect.setVisibility(View.VISIBLE);
                            }
                        }
                        Toast.makeText(Cards.this, "Correct", Toast.LENGTH_SHORT).show();
                    } else if (cards.get(2) == 207) {
                        right.setImageResource(R.drawable.hearts);
                    } else if (cards.get(2) == 407) {
                        right.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(2) == 507) {
                        right.setImageResource(R.drawable.flowers);
                    } else if (cards.get(2) == 607) {
                        right.setImageResource(R.drawable.nine);
                    } else if (cards.get(2) == 707) {
                        right.setImageResource(R.drawable.k);
                    } else if (cards.get(2) == 807) {
                        right.setImageResource(R.drawable.j);
                    } else if (cards.get(2) == 907) {
                        right.setImageResource(R.drawable.q);
                    } else if (cards.get(2) == 1007) {
                        right.setImageResource(R.drawable.three);
                    }


                    if (cards.get(1) == 107) {
                        middle.setImageResource(R.drawable.spade);
                    } else if (cards.get(1) == 207) {
                        middle.setImageResource(R.drawable.hearts);
                    } else if (cards.get(1) == 407) {
                        middle.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(1) == 507) {
                        middle.setImageResource(R.drawable.flowers);
                    } else if (cards.get(1) == 607) {
                        middle.setImageResource(R.drawable.nine);
                    } else if (cards.get(1) == 707) {
                        middle.setImageResource(R.drawable.k);
                    } else if (cards.get(1) == 807) {
                        middle.setImageResource(R.drawable.j);
                    } else if (cards.get(1) == 907) {
                        middle.setImageResource(R.drawable.q);
                    } else if (cards.get(1) == 1007) {
                        middle.setImageResource(R.drawable.three);
                    }

                    if (cards.get(0) == 107) {
                        left.setImageResource(R.drawable.spade);
                    } else if (cards.get(0) == 207) {
                        left.setImageResource(R.drawable.hearts);
                    } else if (cards.get(0) == 407) {
                        left.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(0) == 507) {
                        left.setImageResource(R.drawable.flowers);
                    } else if (cards.get(0) == 607) {
                        left.setImageResource(R.drawable.nine);
                    } else if (cards.get(0) == 707) {
                        left.setImageResource(R.drawable.k);
                    } else if (cards.get(0) == 807) {
                        left.setImageResource(R.drawable.j);
                    } else if (cards.get(0) == 907) {
                        left.setImageResource(R.drawable.q);
                    } else if (cards.get(0) == 1007) {
                        left.setImageResource(R.drawable.three);
                    }
                    if (cards.get(3) == 107) {
                        leftd.setImageResource(R.drawable.spade);
                    } else if (cards.get(3) == 207) {
                        leftd.setImageResource(R.drawable.hearts);
                    } else if (cards.get(3) == 407) {
                        leftd.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(3) == 507) {
                        leftd.setImageResource(R.drawable.flowers);
                    } else if (cards.get(3) == 607) {
                        leftd.setImageResource(R.drawable.nine);
                    } else if (cards.get(3) == 707) {
                        leftd.setImageResource(R.drawable.k);
                    } else if (cards.get(3) == 807) {
                        leftd.setImageResource(R.drawable.j);
                    } else if (cards.get(3) == 907) {
                        leftd.setImageResource(R.drawable.q);
                    } else if (cards.get(3) == 1007) {
                        leftd.setImageResource(R.drawable.three);
                    }


                    if (cards.get(4) == 107) {
                        middled.setImageResource(R.drawable.spade);
                    } else if (cards.get(4) == 207) {
                        middled.setImageResource(R.drawable.hearts);
                    } else if (cards.get(4) == 407) {
                        middled.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(4) == 507) {
                        middled.setImageResource(R.drawable.flowers);
                    } else if (cards.get(4) == 607) {
                        middled.setImageResource(R.drawable.nine);
                    } else if (cards.get(4) == 707) {
                        middled.setImageResource(R.drawable.k);
                    } else if (cards.get(4) == 807) {
                        middled.setImageResource(R.drawable.j);
                    } else if (cards.get(4) == 907) {
                        middled.setImageResource(R.drawable.q);
                    } else if (cards.get(4) == 1007) {
                        middled.setImageResource(R.drawable.three);
                    }


                    if (cards.get(5) == 107) {
                        rightd.setImageResource(R.drawable.spade);
                    } else if (cards.get(5) == 207) {
                        rightd.setImageResource(R.drawable.hearts);
                    } else if (cards.get(5) == 407) {
                        rightd.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(5) == 507) {
                        rightd.setImageResource(R.drawable.flowers);
                    } else if (cards.get(5) == 607) {
                        rightd.setImageResource(R.drawable.nine);
                    } else if (cards.get(5) == 707) {
                        rightd.setImageResource(R.drawable.k);
                    } else if (cards.get(5) == 807) {
                        rightd.setImageResource(R.drawable.j);
                    } else if (cards.get(5) == 907) {
                        rightd.setImageResource(R.drawable.q);
                    } else if (cards.get(5) == 1007) {
                        rightd.setImageResource(R.drawable.three);
                    }

                    if (cards.get(6) == 107) {
                        leftf.setImageResource(R.drawable.spade);
                    } else if (cards.get(6) == 207) {
                        leftf.setImageResource(R.drawable.hearts);
                    } else if (cards.get(6) == 407) {
                        leftf.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(6) == 507) {
                        leftf.setImageResource(R.drawable.flowers);
                    } else if (cards.get(6) == 607) {
                        leftf.setImageResource(R.drawable.nine);
                    } else if (cards.get(6) == 707) {
                        leftf.setImageResource(R.drawable.k);
                    } else if (cards.get(6) == 807) {
                        leftf.setImageResource(R.drawable.j);
                    } else if (cards.get(6) == 907) {
                        leftf.setImageResource(R.drawable.q);
                    } else if (cards.get(6) == 1007) {
                        leftf.setImageResource(R.drawable.three);
                    }
                    if (cards.get(7) == 107) {
                        middlef.setImageResource(R.drawable.spade);
                    } else if (cards.get(7) == 207) {
                        middlef.setImageResource(R.drawable.hearts);
                    } else if (cards.get(7) == 407) {
                        middlef.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(7) == 507) {
                        middlef.setImageResource(R.drawable.flowers);
                    } else if (cards.get(7) == 607) {
                        middlef.setImageResource(R.drawable.nine);
                    } else if (cards.get(7) == 707) {
                        middlef.setImageResource(R.drawable.k);
                    } else if (cards.get(7) == 807) {
                        middlef.setImageResource(R.drawable.j);
                    } else if (cards.get(7) == 907) {
                        middlef.setImageResource(R.drawable.q);
                    } else if (cards.get(7) == 1007) {
                        middlef.setImageResource(R.drawable.three);
                    }

                    if (cards.get(8) == 107) {
                        rightf.setImageResource(R.drawable.spade);
                    } else if (cards.get(8) == 207) {
                        rightf.setImageResource(R.drawable.hearts);
                    } else if (cards.get(8) == 407) {
                        rightf.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(8) == 507) {
                        rightf.setImageResource(R.drawable.flowers);
                    } else if (cards.get(8) == 607) {
                        rightf.setImageResource(R.drawable.nine);
                    } else if (cards.get(8) == 707) {
                        rightf.setImageResource(R.drawable.k);
                    } else if (cards.get(8) == 807) {
                        rightf.setImageResource(R.drawable.j);
                    } else if (cards.get(8) == 907) {
                        rightf.setImageResource(R.drawable.q);
                    } else if (cards.get(8) == 1007) {
                        rightf.setImageResource(R.drawable.three);
                    }
                disable();
            }
        });
        rightd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //assign
                    if (cards.get(5) == 107) {
                        mediaPlayerCrush.start();
                        Toast.makeText(Cards.this, "Correct", Toast.LENGTH_SHORT).show();
                        rightd.setImageResource(R.drawable.spade);
                        if (corr == 0) {
                            first.setImageResource(R.drawable.spade);
                            first.setVisibility(View.VISIBLE);
                            corr = 1;
                            resf = 1;
                        } else if (corr == 1) {
                            second.setVisibility(View.VISIBLE);
                            second.setImageResource(R.drawable.spade);
                            corr = 2;
                            resS = 1;
                        } else if (corr == 2) {
                            third.setVisibility(View.VISIBLE);
                            third.setImageResource(R.drawable.spade);
                            corr = 3;
                            resT = 1;
                            if (resf == 1 && resS == 1 && resT == 1) {
                                correct.setVisibility(View.VISIBLE);
                            } else {
                                notCorrect.setVisibility(View.VISIBLE);
                            }
                        }
                    } else if (cards.get(5) == 207) {
                        rightd.setImageResource(R.drawable.hearts);
                    } else if (cards.get(5) == 407) {
                        rightd.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(5) == 507) {
                        rightd.setImageResource(R.drawable.flowers);
                    } else if (cards.get(5) == 607) {
                        rightd.setImageResource(R.drawable.nine);
                    } else if (cards.get(5) == 707) {
                        rightd.setImageResource(R.drawable.k);
                    } else if (cards.get(5) == 807) {
                        rightd.setImageResource(R.drawable.j);
                    } else if (cards.get(5) == 907) {
                        rightd.setImageResource(R.drawable.q);
                    } else if (cards.get(5) == 1007) {
                        rightd.setImageResource(R.drawable.three);
                    }


                    if (cards.get(4) == 107) {
                        middled.setImageResource(R.drawable.spade);
                    } else if (cards.get(4) == 207) {
                        middled.setImageResource(R.drawable.hearts);
                    } else if (cards.get(4) == 407) {
                        middled.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(4) == 507) {
                        middled.setImageResource(R.drawable.flowers);
                    } else if (cards.get(4) == 607) {
                        middled.setImageResource(R.drawable.nine);
                    } else if (cards.get(4) == 707) {
                        middled.setImageResource(R.drawable.k);
                    } else if (cards.get(4) == 807) {
                        middled.setImageResource(R.drawable.j);
                    } else if (cards.get(4) == 907) {
                        middled.setImageResource(R.drawable.q);
                    } else if (cards.get(4) == 1007) {
                        middled.setImageResource(R.drawable.three);
                    }
                    if (cards.get(3) == 107) {
                        leftd.setImageResource(R.drawable.spade);
                    } else if (cards.get(3) == 207) {
                        leftd.setImageResource(R.drawable.hearts);
                    } else if (cards.get(3) == 407) {
                        leftd.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(3) == 507) {
                        leftd.setImageResource(R.drawable.flowers);
                    } else if (cards.get(3) == 607) {
                        leftd.setImageResource(R.drawable.nine);
                    } else if (cards.get(3) == 707) {
                        leftd.setImageResource(R.drawable.k);
                    } else if (cards.get(3) == 807) {
                        leftd.setImageResource(R.drawable.j);
                    } else if (cards.get(3) == 907) {
                        leftd.setImageResource(R.drawable.q);
                    } else if (cards.get(3) == 1007) {
                        leftd.setImageResource(R.drawable.three);
                    }
                    if (cards.get(0) == 107) {
                        left.setImageResource(R.drawable.spade);
                    } else if (cards.get(0) == 207) {
                        left.setImageResource(R.drawable.hearts);
                    } else if (cards.get(0) == 407) {
                        left.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(0) == 507) {
                        left.setImageResource(R.drawable.flowers);
                    } else if (cards.get(0) == 607) {
                        left.setImageResource(R.drawable.nine);
                    } else if (cards.get(0) == 707) {
                        left.setImageResource(R.drawable.k);
                    } else if (cards.get(0) == 807) {
                        left.setImageResource(R.drawable.j);
                    } else if (cards.get(0) == 907) {
                        left.setImageResource(R.drawable.q);
                    } else if (cards.get(0) == 1007) {
                        left.setImageResource(R.drawable.three);
                    }

                    if (cards.get(1) == 107) {
                        middle.setImageResource(R.drawable.spade);
                    } else if (cards.get(1) == 207) {
                        middle.setImageResource(R.drawable.hearts);
                    } else if (cards.get(1) == 407) {
                        middle.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(1) == 507) {
                        middle.setImageResource(R.drawable.flowers);
                    } else if (cards.get(1) == 607) {
                        middle.setImageResource(R.drawable.nine);
                    } else if (cards.get(1) == 707) {
                        middle.setImageResource(R.drawable.k);
                    } else if (cards.get(1) == 807) {
                        middle.setImageResource(R.drawable.j);
                    } else if (cards.get(1) == 907) {
                        middle.setImageResource(R.drawable.q);
                    } else if (cards.get(1) == 1007) {
                        middle.setImageResource(R.drawable.three);
                    }
                    if (cards.get(2) == 107) {
                        right.setImageResource(R.drawable.spade);
                    } else if (cards.get(2) == 207) {
                        right.setImageResource(R.drawable.hearts);
                    } else if (cards.get(2) == 407) {
                        right.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(2) == 507) {
                        right.setImageResource(R.drawable.flowers);
                    } else if (cards.get(2) == 607) {
                        right.setImageResource(R.drawable.nine);
                    } else if (cards.get(2) == 707) {
                        right.setImageResource(R.drawable.k);
                    } else if (cards.get(2) == 807) {
                        right.setImageResource(R.drawable.j);
                    } else if (cards.get(2) == 907) {
                        right.setImageResource(R.drawable.q);
                    } else if (cards.get(2) == 1007) {
                        right.setImageResource(R.drawable.three);
                    }
                    if (cards.get(6) == 107) {
                        leftf.setImageResource(R.drawable.spade);
                    } else if (cards.get(6) == 207) {
                        leftf.setImageResource(R.drawable.hearts);
                    } else if (cards.get(6) == 407) {
                        leftf.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(6) == 507) {
                        leftf.setImageResource(R.drawable.flowers);
                    } else if (cards.get(6) == 607) {
                        leftf.setImageResource(R.drawable.nine);
                    } else if (cards.get(6) == 707) {
                        leftf.setImageResource(R.drawable.k);
                    } else if (cards.get(6) == 807) {
                        leftf.setImageResource(R.drawable.j);
                    } else if (cards.get(6) == 907) {
                        leftf.setImageResource(R.drawable.q);
                    } else if (cards.get(6) == 1007) {
                        leftf.setImageResource(R.drawable.three);
                    }
                    if (cards.get(7) == 107) {
                        middlef.setImageResource(R.drawable.spade);
                    } else if (cards.get(7) == 207) {
                        middlef.setImageResource(R.drawable.hearts);
                    } else if (cards.get(7) == 407) {
                        middlef.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(7) == 507) {
                        middlef.setImageResource(R.drawable.flowers);
                    } else if (cards.get(7) == 607) {
                        middlef.setImageResource(R.drawable.nine);
                    } else if (cards.get(7) == 707) {
                        middlef.setImageResource(R.drawable.k);
                    } else if (cards.get(7) == 807) {
                        middlef.setImageResource(R.drawable.j);
                    } else if (cards.get(7) == 907) {
                        middlef.setImageResource(R.drawable.q);
                    } else if (cards.get(7) == 1007) {
                        middlef.setImageResource(R.drawable.three);
                    }

                    if (cards.get(8) == 107) {
                        rightf.setImageResource(R.drawable.spade);
                    } else if (cards.get(8) == 207) {
                        rightf.setImageResource(R.drawable.hearts);
                    } else if (cards.get(8) == 407) {
                        rightf.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(8) == 507) {
                        rightf.setImageResource(R.drawable.flowers);
                    } else if (cards.get(8) == 607) {
                        rightf.setImageResource(R.drawable.nine);
                    } else if (cards.get(8) == 707) {
                        rightf.setImageResource(R.drawable.k);
                    } else if (cards.get(8) == 807) {
                        rightf.setImageResource(R.drawable.j);
                    } else if (cards.get(8) == 907) {
                        rightf.setImageResource(R.drawable.q);
                    } else if (cards.get(8) == 1007) {
                        rightf.setImageResource(R.drawable.three);
                    }
                disable();
            }
        });
        leftf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (cards.get(5) == 107) {
                        rightd.setImageResource(R.drawable.spade);
                    } else if (cards.get(5) == 207) {
                        rightd.setImageResource(R.drawable.hearts);
                    } else if (cards.get(5) == 407) {
                        rightd.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(5) == 507) {
                        rightd.setImageResource(R.drawable.flowers);
                    } else if (cards.get(5) == 607) {
                        rightd.setImageResource(R.drawable.nine);
                    } else if (cards.get(5) == 707) {
                        rightd.setImageResource(R.drawable.k);
                    } else if (cards.get(5) == 807) {
                        rightd.setImageResource(R.drawable.j);
                    } else if (cards.get(5) == 907) {
                        rightd.setImageResource(R.drawable.q);
                    } else if (cards.get(5) == 1007) {
                        rightd.setImageResource(R.drawable.three);
                    }


                    if (cards.get(4) == 107) {
                        middled.setImageResource(R.drawable.spade);
                    } else if (cards.get(4) == 207) {
                        middled.setImageResource(R.drawable.hearts);
                    } else if (cards.get(4) == 407) {
                        middled.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(4) == 507) {
                        middled.setImageResource(R.drawable.flowers);
                    } else if (cards.get(4) == 607) {
                        middled.setImageResource(R.drawable.nine);
                    } else if (cards.get(4) == 707) {
                        middled.setImageResource(R.drawable.k);
                    } else if (cards.get(4) == 807) {
                        middled.setImageResource(R.drawable.j);
                    } else if (cards.get(4) == 907) {
                        middled.setImageResource(R.drawable.q);
                    } else if (cards.get(4) == 1007) {
                        middled.setImageResource(R.drawable.three);
                    }


                    if (cards.get(3) == 107) {
                        leftd.setImageResource(R.drawable.spade);
                    } else if (cards.get(3) == 207) {
                        leftd.setImageResource(R.drawable.hearts);
                    } else if (cards.get(3) == 407) {
                        leftd.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(3) == 507) {
                        leftd.setImageResource(R.drawable.flowers);
                    } else if (cards.get(3) == 607) {
                        leftd.setImageResource(R.drawable.nine);
                    } else if (cards.get(3) == 707) {
                        leftd.setImageResource(R.drawable.k);
                    } else if (cards.get(3) == 807) {
                        leftd.setImageResource(R.drawable.j);
                    } else if (cards.get(3) == 907) {
                        leftd.setImageResource(R.drawable.q);
                    } else if (cards.get(3) == 1007) {
                        leftd.setImageResource(R.drawable.three);
                    }


                    if (cards.get(0) == 107) {
                        left.setImageResource(R.drawable.spade);
                    } else if (cards.get(0) == 207) {
                        left.setImageResource(R.drawable.hearts);
                    } else if (cards.get(0) == 407) {
                        left.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(0) == 507) {
                        left.setImageResource(R.drawable.flowers);
                    } else if (cards.get(0) == 607) {
                        left.setImageResource(R.drawable.nine);
                    } else if (cards.get(0) == 707) {
                        left.setImageResource(R.drawable.k);
                    } else if (cards.get(0) == 807) {
                        left.setImageResource(R.drawable.j);
                    } else if (cards.get(0) == 907) {
                        left.setImageResource(R.drawable.q);
                    } else if (cards.get(0) == 1007) {
                        left.setImageResource(R.drawable.three);
                    }


                    if (cards.get(1) == 107) {
                        middle.setImageResource(R.drawable.spade);
                    } else if (cards.get(1) == 207) {
                        middle.setImageResource(R.drawable.hearts);
                    } else if (cards.get(1) == 407) {
                        middle.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(1) == 507) {
                        middle.setImageResource(R.drawable.flowers);
                    } else if (cards.get(1) == 607) {
                        middle.setImageResource(R.drawable.nine);
                    } else if (cards.get(1) == 707) {
                        middle.setImageResource(R.drawable.k);
                    } else if (cards.get(1) == 807) {
                        middle.setImageResource(R.drawable.j);
                    } else if (cards.get(1) == 907) {
                        middle.setImageResource(R.drawable.q);
                    } else if (cards.get(1) == 1007) {
                        middle.setImageResource(R.drawable.three);
                    }


                    if (cards.get(2) == 107) {
                        right.setImageResource(R.drawable.spade);
                    } else if (cards.get(2) == 207) {
                        right.setImageResource(R.drawable.hearts);
                    } else if (cards.get(2) == 407) {
                        right.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(2) == 507) {
                        right.setImageResource(R.drawable.flowers);
                    } else if (cards.get(2) == 607) {
                        right.setImageResource(R.drawable.nine);
                    } else if (cards.get(2) == 707) {
                        right.setImageResource(R.drawable.k);
                    } else if (cards.get(2) == 807) {
                        right.setImageResource(R.drawable.j);
                    } else if (cards.get(2) == 907) {
                        right.setImageResource(R.drawable.q);
                    } else if (cards.get(2) == 1007) {
                        right.setImageResource(R.drawable.three);
                    }


                    if (cards.get(6) == 107) {
                        mediaPlayerCrush.start();
                        Toast.makeText(Cards.this, "Correct", Toast.LENGTH_SHORT).show();
                        leftf.setImageResource(R.drawable.spade);
                        if (corr == 0) {
                            first.setImageResource(R.drawable.spade);
                            first.setVisibility(View.VISIBLE);
                            corr = 1;
                            resf = 1;
                        } else if (corr == 1) {
                            second.setVisibility(View.VISIBLE);
                            second.setImageResource(R.drawable.spade);
                            corr = 2;
                            resS = 1;
                        } else if (corr == 2) {
                            third.setVisibility(View.VISIBLE);
                            third.setImageResource(R.drawable.spade);
                            corr = 3;
                            resT = 1;
                            if (resf == 1 && resS == 1 && resT == 1) {
                                correct.setVisibility(View.VISIBLE);
                            } else {
                                notCorrect.setVisibility(View.VISIBLE);
                            }
                        }
                    } else if (cards.get(6) == 207) {
                        leftf.setImageResource(R.drawable.hearts);
                    } else if (cards.get(6) == 407) {
                        leftf.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(6) == 507) {
                        leftf.setImageResource(R.drawable.flowers);
                    } else if (cards.get(6) == 607) {
                        leftf.setImageResource(R.drawable.nine);
                    } else if (cards.get(6) == 707) {
                        leftf.setImageResource(R.drawable.k);
                    } else if (cards.get(6) == 807) {
                        leftf.setImageResource(R.drawable.j);
                    } else if (cards.get(6) == 907) {
                        leftf.setImageResource(R.drawable.q);
                    } else if (cards.get(6) == 1007) {
                        leftf.setImageResource(R.drawable.three);
                    }
                    if (cards.get(7) == 107) {
                        middlef.setImageResource(R.drawable.spade);
                    } else if (cards.get(7) == 207) {
                        middlef.setImageResource(R.drawable.hearts);
                    } else if (cards.get(7) == 407) {
                        middlef.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(7) == 507) {
                        middlef.setImageResource(R.drawable.flowers);
                    } else if (cards.get(7) == 607) {
                        middlef.setImageResource(R.drawable.nine);
                    } else if (cards.get(7) == 707) {
                        middlef.setImageResource(R.drawable.k);
                    } else if (cards.get(7) == 807) {
                        middlef.setImageResource(R.drawable.j);
                    } else if (cards.get(7) == 907) {
                        middlef.setImageResource(R.drawable.q);
                    } else if (cards.get(7) == 1007) {
                        middlef.setImageResource(R.drawable.three);
                    }

                    if (cards.get(8) == 107) {
                        rightf.setImageResource(R.drawable.spade);
                    } else if (cards.get(8) == 207) {
                        rightf.setImageResource(R.drawable.hearts);
                    } else if (cards.get(8) == 407) {
                        rightf.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(8) == 507) {
                        rightf.setImageResource(R.drawable.flowers);
                    } else if (cards.get(8) == 607) {
                        rightf.setImageResource(R.drawable.nine);
                    } else if (cards.get(8) == 707) {
                        rightf.setImageResource(R.drawable.k);
                    } else if (cards.get(8) == 807) {
                        rightf.setImageResource(R.drawable.j);
                    } else if (cards.get(8) == 907) {
                        rightf.setImageResource(R.drawable.q);
                    } else if (cards.get(8) == 1007) {
                        rightf.setImageResource(R.drawable.three);
                    }
                disable();
            }
        });
        middlef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (cards.get(5) == 107) {
                        rightd.setImageResource(R.drawable.spade);
                    } else if (cards.get(5) == 207) {
                        rightd.setImageResource(R.drawable.hearts);
                    } else if (cards.get(5) == 407) {
                        rightd.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(5) == 507) {
                        rightd.setImageResource(R.drawable.flowers);
                    } else if (cards.get(5) == 607) {
                        rightd.setImageResource(R.drawable.nine);
                    } else if (cards.get(5) == 707) {
                        rightd.setImageResource(R.drawable.k);
                    } else if (cards.get(5) == 807) {
                        rightd.setImageResource(R.drawable.j);
                    } else if (cards.get(5) == 907) {
                        rightd.setImageResource(R.drawable.q);
                    } else if (cards.get(5) == 1007) {
                        rightd.setImageResource(R.drawable.three);
                    }


                    if (cards.get(4) == 107) {
                        middled.setImageResource(R.drawable.spade);
                    } else if (cards.get(4) == 207) {
                        middled.setImageResource(R.drawable.hearts);
                    } else if (cards.get(4) == 407) {
                        middled.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(4) == 507) {
                        middled.setImageResource(R.drawable.flowers);
                    } else if (cards.get(4) == 607) {
                        middled.setImageResource(R.drawable.nine);
                    } else if (cards.get(4) == 707) {
                        middled.setImageResource(R.drawable.k);
                    } else if (cards.get(4) == 807) {
                        middled.setImageResource(R.drawable.j);
                    } else if (cards.get(4) == 907) {
                        middled.setImageResource(R.drawable.q);
                    } else if (cards.get(4) == 1007) {
                        middled.setImageResource(R.drawable.three);
                    }


                    if (cards.get(3) == 107) {
                        leftd.setImageResource(R.drawable.spade);
                    } else if (cards.get(3) == 207) {
                        leftd.setImageResource(R.drawable.hearts);
                    } else if (cards.get(3) == 407) {
                        leftd.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(3) == 507) {
                        leftd.setImageResource(R.drawable.flowers);
                    } else if (cards.get(3) == 607) {
                        leftd.setImageResource(R.drawable.nine);
                    } else if (cards.get(3) == 707) {
                        leftd.setImageResource(R.drawable.k);
                    } else if (cards.get(3) == 807) {
                        leftd.setImageResource(R.drawable.j);
                    } else if (cards.get(3) == 907) {
                        leftd.setImageResource(R.drawable.q);
                    } else if (cards.get(3) == 1007) {
                        leftd.setImageResource(R.drawable.three);
                    }


                    if (cards.get(0) == 107) {
                        left.setImageResource(R.drawable.spade);
                    } else if (cards.get(0) == 207) {
                        left.setImageResource(R.drawable.hearts);
                    } else if (cards.get(0) == 407) {
                        left.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(0) == 507) {
                        left.setImageResource(R.drawable.flowers);
                    } else if (cards.get(0) == 607) {
                        left.setImageResource(R.drawable.nine);
                    } else if (cards.get(0) == 707) {
                        left.setImageResource(R.drawable.k);
                    } else if (cards.get(0) == 807) {
                        left.setImageResource(R.drawable.j);
                    } else if (cards.get(0) == 907) {
                        left.setImageResource(R.drawable.q);
                    } else if (cards.get(0) == 1007) {
                        left.setImageResource(R.drawable.three);
                    }


                    if (cards.get(1) == 107) {
                        middle.setImageResource(R.drawable.spade);
                    } else if (cards.get(1) == 207) {
                        middle.setImageResource(R.drawable.hearts);
                    } else if (cards.get(1) == 407) {
                        middle.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(1) == 507) {
                        middle.setImageResource(R.drawable.flowers);
                    } else if (cards.get(1) == 607) {
                        middle.setImageResource(R.drawable.nine);
                    } else if (cards.get(1) == 707) {
                        middle.setImageResource(R.drawable.k);
                    } else if (cards.get(1) == 807) {
                        middle.setImageResource(R.drawable.j);
                    } else if (cards.get(1) == 907) {
                        middle.setImageResource(R.drawable.q);
                    } else if (cards.get(1) == 1007) {
                        middle.setImageResource(R.drawable.three);
                    }


                    if (cards.get(2) == 107) {
                        right.setImageResource(R.drawable.spade);
                    } else if (cards.get(2) == 207) {
                        right.setImageResource(R.drawable.hearts);
                    } else if (cards.get(2) == 407) {
                        right.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(2) == 507) {
                        right.setImageResource(R.drawable.flowers);
                    } else if (cards.get(2) == 607) {
                        right.setImageResource(R.drawable.nine);
                    } else if (cards.get(2) == 707) {
                        right.setImageResource(R.drawable.k);
                    } else if (cards.get(2) == 807) {
                        right.setImageResource(R.drawable.j);
                    } else if (cards.get(2) == 907) {
                        right.setImageResource(R.drawable.q);
                    } else if (cards.get(2) == 1007) {
                        right.setImageResource(R.drawable.three);
                    }

                    if (cards.get(6) == 107) {
                        leftf.setImageResource(R.drawable.spade);
                    } else if (cards.get(6) == 207) {
                        leftf.setImageResource(R.drawable.hearts);
                    } else if (cards.get(6) == 407) {
                        leftf.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(6) == 507) {
                        leftf.setImageResource(R.drawable.flowers);
                    } else if (cards.get(6) == 607) {
                        leftf.setImageResource(R.drawable.nine);
                    } else if (cards.get(6) == 707) {
                        leftf.setImageResource(R.drawable.k);
                    } else if (cards.get(6) == 807) {
                        leftf.setImageResource(R.drawable.j);
                    } else if (cards.get(6) == 907) {
                        leftf.setImageResource(R.drawable.q);
                    } else if (cards.get(6) == 1007) {
                        leftf.setImageResource(R.drawable.three);
                    }
                    if (cards.get(7) == 107) {
                        mediaPlayerCrush.start();
                        Toast.makeText(Cards.this, "Correct", Toast.LENGTH_SHORT).show();
                        middlef.setImageResource(R.drawable.spade);
                        if (corr == 0) {
                            first.setImageResource(R.drawable.spade);
                            first.setVisibility(View.VISIBLE);
                            corr = 1;
                            resf = 1;
                        } else if (corr == 1) {
                            second.setVisibility(View.VISIBLE);
                            second.setImageResource(R.drawable.spade);
                            corr = 2;
                            resS = 1;
                        } else if (corr == 2) {
                            third.setVisibility(View.VISIBLE);
                            third.setImageResource(R.drawable.spade);
                            corr = 3;
                            resT = 1;
                            if (resf == 1 && resS == 1 && resT == 1) {
                                correct.setVisibility(View.VISIBLE);
                            } else {
                                notCorrect.setVisibility(View.VISIBLE);
                            }
                        }
                    } else if (cards.get(7) == 207) {
                        middlef.setImageResource(R.drawable.hearts);
                    } else if (cards.get(7) == 407) {
                        middlef.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(7) == 507) {
                        middlef.setImageResource(R.drawable.flowers);
                    } else if (cards.get(7) == 607) {
                        middlef.setImageResource(R.drawable.nine);
                    } else if (cards.get(7) == 707) {
                        middlef.setImageResource(R.drawable.k);
                    } else if (cards.get(7) == 807) {
                        middlef.setImageResource(R.drawable.j);
                    } else if (cards.get(7) == 907) {
                        middlef.setImageResource(R.drawable.q);
                    } else if (cards.get(7) == 1007) {
                        middlef.setImageResource(R.drawable.three);
                    }

                    if (cards.get(8) == 107) {
                        rightf.setImageResource(R.drawable.spade);
                    } else if (cards.get(8) == 207) {
                        rightf.setImageResource(R.drawable.hearts);
                    } else if (cards.get(8) == 407) {
                        rightf.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(8) == 507) {
                        rightf.setImageResource(R.drawable.flowers);
                    } else if (cards.get(8) == 607) {
                        rightf.setImageResource(R.drawable.nine);
                    } else if (cards.get(8) == 707) {
                        rightf.setImageResource(R.drawable.k);
                    } else if (cards.get(8) == 807) {
                        rightf.setImageResource(R.drawable.j);
                    } else if (cards.get(8) == 907) {
                        rightf.setImageResource(R.drawable.q);
                    } else if (cards.get(8) == 1007) {
                        rightf.setImageResource(R.drawable.three);
                    }
                disable();
            }
        });
        rightf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (cards.get(5) == 107) {
                        rightd.setImageResource(R.drawable.spade);
                    } else if (cards.get(5) == 207) {
                        rightd.setImageResource(R.drawable.hearts);
                    } else if (cards.get(5) == 407) {
                        rightd.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(5) == 507) {
                        rightd.setImageResource(R.drawable.flowers);
                    } else if (cards.get(5) == 607) {
                        rightd.setImageResource(R.drawable.nine);
                    } else if (cards.get(5) == 707) {
                        rightd.setImageResource(R.drawable.k);
                    } else if (cards.get(5) == 807) {
                        rightd.setImageResource(R.drawable.j);
                    } else if (cards.get(5) == 907) {
                        rightd.setImageResource(R.drawable.q);
                    } else if (cards.get(5) == 1007) {
                        rightd.setImageResource(R.drawable.three);
                    }
                    if (cards.get(4) == 107) {
                        middled.setImageResource(R.drawable.spade);
                    } else if (cards.get(4) == 207) {
                        middled.setImageResource(R.drawable.hearts);
                    } else if (cards.get(4) == 407) {
                        middled.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(4) == 507) {
                        middled.setImageResource(R.drawable.flowers);
                    } else if (cards.get(4) == 607) {
                        middled.setImageResource(R.drawable.nine);
                    } else if (cards.get(4) == 707) {
                        middled.setImageResource(R.drawable.k);
                    } else if (cards.get(4) == 807) {
                        middled.setImageResource(R.drawable.j);
                    } else if (cards.get(4) == 907) {
                        middled.setImageResource(R.drawable.q);
                    } else if (cards.get(4) == 1007) {
                        middled.setImageResource(R.drawable.three);
                    }

                    if (cards.get(3) == 107) {
                        leftd.setImageResource(R.drawable.spade);
                    } else if (cards.get(3) == 207) {
                        leftd.setImageResource(R.drawable.hearts);
                    } else if (cards.get(3) == 407) {
                        leftd.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(3) == 507) {
                        leftd.setImageResource(R.drawable.flowers);
                    } else if (cards.get(3) == 607) {
                        leftd.setImageResource(R.drawable.nine);
                    } else if (cards.get(3) == 707) {
                        leftd.setImageResource(R.drawable.k);
                    } else if (cards.get(3) == 807) {
                        leftd.setImageResource(R.drawable.j);
                    } else if (cards.get(3) == 907) {
                        leftd.setImageResource(R.drawable.q);
                    } else if (cards.get(3) == 1007) {
                        leftd.setImageResource(R.drawable.three);
                    }


                    if (cards.get(0) == 107) {
                        left.setImageResource(R.drawable.spade);
                    } else if (cards.get(0) == 207) {
                        left.setImageResource(R.drawable.hearts);
                    } else if (cards.get(0) == 407) {
                        left.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(0) == 507) {
                        left.setImageResource(R.drawable.flowers);
                    } else if (cards.get(0) == 607) {
                        left.setImageResource(R.drawable.nine);
                    } else if (cards.get(0) == 707) {
                        left.setImageResource(R.drawable.k);
                    } else if (cards.get(0) == 807) {
                        left.setImageResource(R.drawable.j);
                    } else if (cards.get(0) == 907) {
                        left.setImageResource(R.drawable.q);
                    } else if (cards.get(0) == 1007) {
                        left.setImageResource(R.drawable.three);
                    }
                    if (cards.get(1) == 107) {
                        middle.setImageResource(R.drawable.spade);
                    } else if (cards.get(1) == 207) {
                        middle.setImageResource(R.drawable.hearts);
                    } else if (cards.get(1) == 407) {
                        middle.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(1) == 507) {
                        middle.setImageResource(R.drawable.flowers);
                    } else if (cards.get(1) == 607) {
                        middle.setImageResource(R.drawable.nine);
                    } else if (cards.get(1) == 707) {
                        middle.setImageResource(R.drawable.k);
                    } else if (cards.get(1) == 807) {
                        middle.setImageResource(R.drawable.j);
                    } else if (cards.get(1) == 907) {
                        middle.setImageResource(R.drawable.q);
                    } else if (cards.get(1) == 1007) {
                        middle.setImageResource(R.drawable.three);
                    }


                    if (cards.get(2) == 107) {
                        right.setImageResource(R.drawable.spade);
                    } else if (cards.get(2) == 207) {
                        right.setImageResource(R.drawable.hearts);
                    } else if (cards.get(2) == 407) {
                        right.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(2) == 507) {
                        right.setImageResource(R.drawable.flowers);
                    } else if (cards.get(2) == 607) {
                        right.setImageResource(R.drawable.nine);
                    } else if (cards.get(2) == 707) {
                        right.setImageResource(R.drawable.k);
                    } else if (cards.get(2) == 807) {
                        right.setImageResource(R.drawable.j);
                    } else if (cards.get(2) == 907) {
                        right.setImageResource(R.drawable.q);
                    } else if (cards.get(2) == 1007) {
                        right.setImageResource(R.drawable.three);
                    }


                    if (cards.get(6) == 107) {
                        leftf.setImageResource(R.drawable.spade);
                    } else if (cards.get(6) == 207) {
                        leftf.setImageResource(R.drawable.hearts);
                    } else if (cards.get(6) == 407) {
                        leftf.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(6) == 507) {
                        leftf.setImageResource(R.drawable.flowers);
                    } else if (cards.get(6) == 607) {
                        leftf.setImageResource(R.drawable.nine);
                    } else if (cards.get(6) == 707) {
                        leftf.setImageResource(R.drawable.k);
                    } else if (cards.get(6) == 807) {
                        leftf.setImageResource(R.drawable.j);
                    } else if (cards.get(6) == 907) {
                        leftf.setImageResource(R.drawable.q);
                    } else if (cards.get(6) == 1007) {
                        leftf.setImageResource(R.drawable.three);
                    }
                    if (cards.get(7) == 107) {
                        middlef.setImageResource(R.drawable.spade);
                    } else if (cards.get(7) == 207) {
                        middlef.setImageResource(R.drawable.hearts);
                    } else if (cards.get(7) == 407) {
                        middlef.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(7) == 507) {
                        middlef.setImageResource(R.drawable.flowers);
                    } else if (cards.get(7) == 607) {
                        middlef.setImageResource(R.drawable.nine);
                    } else if (cards.get(7) == 707) {
                        middlef.setImageResource(R.drawable.k);
                    } else if (cards.get(7) == 807) {
                        middlef.setImageResource(R.drawable.j);
                    } else if (cards.get(7) == 907) {
                        middlef.setImageResource(R.drawable.q);
                    } else if (cards.get(7) == 1007) {
                        middlef.setImageResource(R.drawable.three);
                    }

                    if (cards.get(8) == 107) {
                        mediaPlayerCrush.start();
                        Toast.makeText(Cards.this, "Correct", Toast.LENGTH_SHORT).show();
                        rightf.setImageResource(R.drawable.spade);
                        if (corr == 0) {
                            first.setImageResource(R.drawable.spade);
                            first.setVisibility(View.VISIBLE);
                            corr = 1;
                            resf = 1;
                        } else if (corr == 1) {
                            second.setVisibility(View.VISIBLE);
                            second.setImageResource(R.drawable.spade);
                            corr = 2;
                            resS = 1;
                        } else if (corr == 2) {
                            third.setVisibility(View.VISIBLE);
                            third.setImageResource(R.drawable.spade);
                            corr = 3;
                            resT = 1;
                            if (resf == 1 && resS == 1 && resT == 1) {
                                correct.setVisibility(View.VISIBLE);
                            } else {
                                notCorrect.setVisibility(View.VISIBLE);
                            }
                        }
                    } else if (cards.get(8) == 207) {
                        rightf.setImageResource(R.drawable.hearts);
                    } else if (cards.get(8) == 407) {
                        rightf.setImageResource(R.drawable.diamonds);
                    } else if (cards.get(8) == 507) {
                        rightf.setImageResource(R.drawable.flowers);
                    } else if (cards.get(8) == 607) {
                        rightf.setImageResource(R.drawable.nine);
                    } else if (cards.get(8) == 707) {
                        rightf.setImageResource(R.drawable.k);
                    } else if (cards.get(8) == 807) {
                        rightf.setImageResource(R.drawable.j);
                    } else if (cards.get(8) == 907) {
                        rightf.setImageResource(R.drawable.q);
                    } else if (cards.get(8) == 1007) {
                        rightf.setImageResource(R.drawable.three);
                    }
                disable();
            }
        });
    }

    private void setback() {
        first.setImageResource(R.drawable.back);
        second.setImageResource(R.drawable.back);
        third.setImageResource(R.drawable.back);
        left.setImageResource(R.drawable.back);
        leftd.setImageResource(R.drawable.back);
        leftf.setImageResource(R.drawable.back);
        middle.setImageResource(R.drawable.back);
        middled.setImageResource(R.drawable.back);
        middlef.setImageResource(R.drawable.back);
        right.setImageResource(R.drawable.back);
        rightd.setImageResource(R.drawable.back);
        rightf.setImageResource(R.drawable.back);
    }

    private void reduceTrials() {
        String phone = sharedPreferencesConfig.readClientsPhone();
        Call<MessageModel> call = RetrofitClient.getInstance(Cards.this)
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
        new_trial.setEnabled(false);
        reload.setVisibility(View.GONE);
        String phone = sharedPreferencesConfig.readClientsPhone();
        Call<TrialsModel> call = RetrofitClient.getInstance(Cards.this)
                .getApiConnector()
                .pTrials(phone);
        call.enqueue(new Callback<TrialsModel>() {
            @Override
            public void onResponse(Call<TrialsModel> call, Response<TrialsModel> response) {
                pr.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getNum()>=0){
                        chances.setText(response.body().getNum()+"");
                        payment.setVisibility(View.GONE);
                    }
                    else{
                        chances.setText("-");
                        payment.setVisibility(View.VISIBLE);
                    }
                    new_trial.setEnabled(true);
                }
                else {
                    Toast.makeText(Cards.this,"Server error",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TrialsModel> call, Throwable t) {
                reload.setVisibility(View.VISIBLE);
                pr.setVisibility(View.GONE);
                Toast.makeText(Cards.this,"Network error"+t.getMessage(),Toast.LENGTH_SHORT).show();
            }

        });
    }
    public void enable(){
        left.setEnabled(true);
        leftd.setEnabled(true);
        leftf.setEnabled(true);
        middle.setEnabled(true);
        middled.setEnabled(true);
        middlef.setEnabled(true);
        right.setEnabled(true);
        rightd.setEnabled(true);
        rightf.setEnabled(true);
    }
    public void disable(){
        left.setEnabled(false);
        leftd.setEnabled(false);
        leftf.setEnabled(false);
        middle.setEnabled(false);
        middled.setEnabled(false);
        middlef.setEnabled(false);
        right.setEnabled(false);
        rightd.setEnabled(false);
        rightf.setEnabled(false);
    }
}