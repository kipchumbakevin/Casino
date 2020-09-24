package com.chezacasino.casino;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chezacasino.casino.models.MessageModel;
import com.chezacasino.casino.networking.RetrofitClient;
import com.chezacasino.casino.utils.SharedPreferencesConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CandyCrush extends AppCompatActivity {
    int candyToBeDragged, candyToBeReplaced;
    int notCandy = R.drawable.transparent;
    Handler mHandler;
    int interval = 100;
    int startScore;
    int[] candies = {
            R.drawable.redcandy,
            R.drawable.yellowcandy,
            R.drawable.purplecandy,
            R.drawable.orangecandy,
            R.drawable.greencandy,
            R.drawable.bluecandy
    };
    MediaPlayer mediaPlayerSwipe;
    SharedPreferencesConfig sharedPreferencesConfig;
    MediaPlayer mediaPlayerCrush;
    int score = 0;
    int widthOfBlock, noOfBlocks = 8, widthOfScreen;
    ArrayList<ImageView> candy = new ArrayList<>();
    TextView scoreR, timer;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candy_crush);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        startScore = 0;
        widthOfScreen = displayMetrics.widthPixels;
        sharedPreferencesConfig = new SharedPreferencesConfig(this);
        timer = findViewById(R.id.timer);
        mediaPlayerSwipe = MediaPlayer.create(this, R.raw.swipe);
        mediaPlayerCrush = MediaPlayer.create(this, R.raw.crush);
        int heightOfScreen = displayMetrics.heightPixels;
        scoreR = findViewById(R.id.score);
        widthOfBlock = widthOfScreen / noOfBlocks;
        createBoard();
        for (final ImageView imageView : candy) {
            imageView.setOnTouchListener(new OnSwipeListener(this) {
                @Override
                void onSwipeRight() {
                    super.onSwipeRight();
                    if (!timer.isShown()) {
                        new CountDownTimer(30000, 1000) { // 60 seconds, in 1 second intervals
                            public void onTick(long millisUntilFinished) {
                                timer.setVisibility(View.VISIBLE);
                                timer.setText("Get a score of 200 in " + millisUntilFinished / 1000 + " secs" + " to get 3 Cup Game with Cards trials for free");
                            }

                            public void onFinish() {
                                alertD();
                                timer.setVisibility(View.GONE);
                                score = 0;
                                scoreR.setText("");
                            }
                        }.start();
                    }
                    mediaPlayerSwipe.start();
                    startScore = 1;
                    candyToBeDragged = imageView.getId();
                    candyToBeReplaced = candyToBeDragged + 1;
                    candyInterchange();
                }

                @Override
                void onSwipeLeft() {
                    super.onSwipeLeft();
                    if (!timer.isShown()) {
                        new CountDownTimer(30000, 1000) { // 60 seconds, in 1 second intervals
                            public void onTick(long millisUntilFinished) {
                                timer.setVisibility(View.VISIBLE);
                                timer.setText("Get a score of 200 in " + millisUntilFinished / 1000 + " secs" + " to get 3 Cup game with cards trials for free");
                            }

                            public void onFinish() {
                                alertD();
                                timer.setVisibility(View.GONE);
                                score = 0;
                                scoreR.setText("");
                            }
                        }.start();
                    }
                    mediaPlayerSwipe.start();
                    startScore = 1;
                    candyToBeDragged = imageView.getId();
                    candyToBeReplaced = candyToBeDragged - 1;
                    candyInterchange();
                }

                @Override
                void onSwipeTop() {
                    super.onSwipeTop();
                    if (!timer.isShown()) {
                        new CountDownTimer(30000, 1000) { // 60 seconds, in 1 second intervals
                            public void onTick(long millisUntilFinished) {
                                timer.setVisibility(View.VISIBLE);
                                timer.setText("Get a score of 200 in " + millisUntilFinished / 1000 + " secs" + " to get 3 Cup game with cards trials for free");
                            }

                            public void onFinish() {
                                alertD();
                                timer.setVisibility(View.GONE);
                                score = 0;
                                scoreR.setText("");
                            }
                        }.start();
                    }
                    mediaPlayerSwipe.start();
                    startScore = 1;
                    candyToBeDragged = imageView.getId();
                    candyToBeReplaced = candyToBeDragged - noOfBlocks;
                    candyInterchange();
                }

                @Override
                void onSwipeBottom() {
                    super.onSwipeBottom();
                    if (!timer.isShown()) {
                        new CountDownTimer(30000, 1000) { // 60 seconds, in 1 second intervals
                            public void onTick(long millisUntilFinished) {
                                timer.setVisibility(View.VISIBLE);
                                timer.setText("Get a score of 200 in " + millisUntilFinished / 1000 + " secs" + " to get 3 Cup game with cards trials for free");
                            }

                            public void onFinish() {
                                alertD();
                                timer.setVisibility(View.GONE);
                                score = 0;
                                scoreR.setText("");
                            }
                        }.start();
                    }
                    mediaPlayerSwipe.start();
                    startScore = 1;
                    candyToBeDragged = imageView.getId();
                    candyToBeReplaced = candyToBeDragged + noOfBlocks;
                    candyInterchange();
                }
            });
        }
        mHandler = new Handler();
        startRepeat();
    }

    private void checkRowForThree() {
        if (timer.isShown()) {
            for (int i = 0; i < 62; i++) {
                int choosedCandy = (int) candy.get(i).getTag();
                boolean isBlank = (int) candy.get(i).getTag() == notCandy;
                Integer[] notValid = {6, 7, 14, 15, 22, 23, 30, 31, 38, 39, 46, 47, 54, 55};
                List<Integer> list = Arrays.asList(notValid);
                if (!list.contains(i)) {
                    int x = i;
                    if ((int) candy.get(x++).getTag() == choosedCandy && !isBlank && (int) candy.get(x++).getTag() == choosedCandy &&
                            (int) candy.get(x).getTag() == choosedCandy) {
                        mediaPlayerCrush.start();
                        if (startScore == 1) {
                            score = score + 6;
                            scoreR.setText(String.valueOf(score));
                        }
                        candy.get(x).setImageResource(notCandy);
                        candy.get(x).setTag(notCandy);
                        x--;
                        candy.get(x).setImageResource(notCandy);
                        candy.get(x).setTag(notCandy);
                        x--;
                        candy.get(x).setImageResource(notCandy);
                        candy.get(x).setTag(notCandy);
                    }
                }
            }
            moveDownCandies();
        }
    }

    private void checkColumnForThree() {
        if (timer.isShown()) {
            for (int i = 0; i < 47; i++) {
                int choosedCandy = (int) candy.get(i).getTag();
                boolean isBlank = (int) candy.get(i).getTag() == notCandy;
                int x = i;
                if ((int) candy.get(x).getTag() == choosedCandy && !isBlank &&
                        (int) candy.get(x + noOfBlocks).getTag() == choosedCandy &&
                        (int) candy.get(x + 2 * noOfBlocks).getTag() == choosedCandy) {
                    mediaPlayerCrush.start();
                    if (startScore == 1) {
                        score = score + 6;
                        scoreR.setText(String.valueOf(score));
                    }
                    candy.get(x).setImageResource(notCandy);
                    candy.get(x).setTag(notCandy);
                    x = x + noOfBlocks;
                    candy.get(x).setImageResource(notCandy);
                    candy.get(x).setTag(notCandy);
                    x = x + noOfBlocks;
                    candy.get(x).setImageResource(notCandy);
                    candy.get(x).setTag(notCandy);
                }
            }
            moveDownCandies();
        }
    }

    private void moveDownCandies() {
        Integer[] firstRow = {0, 1, 2, 3, 4, 5, 6, 7};
        List<Integer> list = Arrays.asList(firstRow);
        for (int i = 55; i >= 0; i--) {
            if ((int) candy.get(i + noOfBlocks).getTag() == notCandy) {
                candy.get(i + noOfBlocks).setImageResource((int) candy.get(i).getTag());
                candy.get(i + noOfBlocks).setTag(candy.get(i).getTag());
                candy.get(i).setImageResource(notCandy);
                candy.get(i).setTag(notCandy);
                if (list.contains(i) && (int) candy.get(i).getTag() == notCandy) {
                    int randomCandy = (int) Math.floor(Math.random() * candies.length);
                    candy.get(i).setImageResource(candies[randomCandy]);
                    candy.get(i).setTag(candies[randomCandy]);
                }
            }
        }
        for (int i = 0; i < 8; i++) {
            if ((int) candy.get(i).getTag() == notCandy) {
                int randomCandy = (int) Math.floor(Math.random() * candies.length);
                candy.get(i).setImageResource(candies[randomCandy]);
                candy.get(i).setTag(candies[randomCandy]);
            }
        }
    }

    Runnable repeatChecker = new Runnable() {
        @Override
        public void run() {
            try {
                checkRowForThree();
                checkColumnForThree();
                moveDownCandies();
            } finally {
                mHandler.postDelayed(repeatChecker, interval);
            }
        }
    };

    void startRepeat() {
        repeatChecker.run();
    }

    private void alertD() {
        String failed, title;
        if (score > 100) {
            title = "Hooray! You have earned your 3 trials.";
            failed = "Congrats! You have reached the target, your score is " + score + "\nYou have been awarded 3 free trials to play Cup Game with Cards.";
        } else {
            title = "Ooops! Time is up.";
            failed = "Start a new game\nYou have not reached the target. Your score is " + score;
        }
        AlertDialog.Builder alert = new AlertDialog.Builder(CandyCrush.this);
        final int ss = score;
        alert.setTitle(title)
                .setMessage(failed);
        if (ss > 100) {
            alert.setPositiveButton("Claim", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialogInterface, int i) {
                }
            });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        } else {
            alert.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        }

        final AlertDialog alertDialog = alert.create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ss > 100) {
                    alertDialog.dismiss();
                    final Toast toast = new Toast(CandyCrush.this);
                    toast.makeText(CandyCrush.this, "Loading...", Toast.LENGTH_LONG).show();
                    String phone = sharedPreferencesConfig.readClientsPhone();
                    Call<MessageModel> call = RetrofitClient.getInstance(CandyCrush.this)
                            .getApiConnector()
                            .createB(phone);
                    call.enqueue(new Callback<MessageModel>() {
                        @Override
                        public void onResponse(Call<MessageModel> call, Response<MessageModel> response) {
                            if (response.isSuccessful()) {
                                toast.cancel();
                                Toast.makeText(CandyCrush.this, "You have received your bonus to play Cup Game with Cards", Toast.LENGTH_LONG).show();
                            } else {
                                toast.cancel();
                                Toast.makeText(CandyCrush.this, "Server error", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<MessageModel> call, Throwable t) {
                            toast.cancel();
                            Toast.makeText(CandyCrush.this, "Network error. You need to be connected to get your bonus", Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    alertDialog.dismiss();
                }
            }
        });
    }

    private void candyInterchange() {
        int background = (int) candy.get(candyToBeReplaced).getTag();
        int background1 = (int) candy.get(candyToBeDragged).getTag();
        candy.get(candyToBeDragged).setImageResource(background);
        candy.get(candyToBeReplaced).setImageResource(background1);
        candy.get(candyToBeDragged).setTag(background);
        candy.get(candyToBeReplaced).setTag(background1);
    }

    private void createBoard() {
        GridLayout gridLayout = findViewById(R.id.board);
        gridLayout.setRowCount(noOfBlocks);
        gridLayout.setColumnCount(noOfBlocks);
        gridLayout.getLayoutParams().width = widthOfScreen;
        gridLayout.getLayoutParams().height = widthOfScreen;
        for (int i = 0; i < noOfBlocks * noOfBlocks; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setLayoutParams(new android.view.ViewGroup.LayoutParams(widthOfBlock, widthOfBlock));
            imageView.setMaxHeight(widthOfBlock);
            imageView.setMaxWidth(widthOfBlock);
            int randomCandy = (int) Math.floor(Math.random() * candies.length);//generates random index from cadies arrasy
            imageView.setImageResource(candies[randomCandy]);
            imageView.setTag(candies[randomCandy]);
            candy.add(imageView);
            gridLayout.addView(imageView);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CandyCrush.this, Landing.class);
        startActivity(intent);
        finish();
    }
}
