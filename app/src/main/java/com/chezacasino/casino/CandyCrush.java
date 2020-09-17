package com.chezacasino.casino;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CandyCrush extends AppCompatActivity {
    int candyToBeDragged,candyToBeReplaced;
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
    MediaPlayer mediaPlayerCrush;
    int score = 0;
    int widthOfBlock,noOfBlocks = 8, widthOfScreen;
    ArrayList<ImageView> candy = new ArrayList<>();
    TextView scoreR;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candy_crush);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        startScore = 0;
        widthOfScreen = displayMetrics.widthPixels;
        mediaPlayerSwipe = MediaPlayer.create(this,R.raw.swipe);
        mediaPlayerCrush = MediaPlayer.create(this,R.raw.crush);
        int heightOfScreen = displayMetrics.heightPixels;
        scoreR = findViewById(R.id.score);
        widthOfBlock = widthOfScreen / noOfBlocks;
        createBoard();
        for (final ImageView imageView : candy){
            imageView.setOnTouchListener(new OnSwipeListener(this){
                @Override
                void onSwipeRight() {
                    super.onSwipeRight();
                    mediaPlayerSwipe.start();
                    startScore = 1;
                    candyToBeDragged = imageView.getId();
                    candyToBeReplaced = candyToBeDragged + 1;
                    candyInterchange();
                }

                @Override
                void onSwipeLeft() {
                    super.onSwipeLeft();
                    mediaPlayerSwipe.start();
                    startScore = 1;
                    candyToBeDragged = imageView.getId();
                    candyToBeReplaced = candyToBeDragged - 1;
                    candyInterchange();
                }

                @Override
                void onSwipeTop() {
                    super.onSwipeTop();
                    mediaPlayerSwipe.start();
                    startScore = 1;
                    candyToBeDragged = imageView.getId();
                    candyToBeReplaced = candyToBeDragged - noOfBlocks;
                    candyInterchange();
                }

                @Override
                void onSwipeBottom() {
                    super.onSwipeBottom();
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
    private void checkRowForThree(){
        for (int i = 0;i<62;i++){
            int choosedCandy = (int) candy.get(i).getTag();
            boolean isBlank = (int) candy.get(i).getTag() == notCandy;
            Integer[] notValid = {6,7,14,15,22,23,30,31,38,39,46,47,54,55};
            List<Integer> list = Arrays.asList(notValid);
            if (!list.contains(i)){
                int x = i;
                if ((int)candy.get(x++).getTag() == choosedCandy && !isBlank && (int)candy.get(x++).getTag() == choosedCandy &&
                        (int)candy.get(x).getTag() == choosedCandy){
                    mediaPlayerCrush.start();
                    if (startScore == 1){
                        score = score + 3;
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
    private void checkColumnForThree(){
        for (int i = 0;i<47;i++){
            int choosedCandy = (int) candy.get(i).getTag();
            boolean isBlank = (int) candy.get(i).getTag() == notCandy;
                int x = i;
                if ((int)candy.get(x).getTag() == choosedCandy && !isBlank &&
                        (int)candy.get(x+noOfBlocks).getTag() == choosedCandy &&
                        (int)candy.get(x+2*noOfBlocks).getTag() == choosedCandy){
                    mediaPlayerCrush.start();
                    if (startScore == 1){
                        score = score + 3;
                        scoreR.setText(String.valueOf(score));
                    }
                    candy.get(x).setImageResource(notCandy);
                    candy.get(x).setTag(notCandy);
                    x =x+noOfBlocks;
                    candy.get(x).setImageResource(notCandy);
                    candy.get(x).setTag(notCandy);
                    x =x+noOfBlocks;
                    candy.get(x).setImageResource(notCandy);
                    candy.get(x).setTag(notCandy);
                }
        }
        moveDownCandies();
    }
    private void moveDownCandies(){
        Integer[] firstRow = {0,1,2,3,4,5,6,7};
        List<Integer>list = Arrays.asList(firstRow);
        for (int i = 55; i>=0;i--){
            if ((int)candy.get(i + noOfBlocks).getTag() == notCandy){
                candy.get(i + noOfBlocks).setImageResource((int) candy.get(i).getTag());
                candy.get(i + noOfBlocks).setTag(candy.get(i).getTag());
                candy.get(i).setImageResource(notCandy);
                candy.get(i).setTag(notCandy);
                if (list.contains(i) && (int) candy.get(i).getTag() == notCandy){
                    int randomCandy = (int) Math.floor(Math.random()*candies.length);
                    candy.get(i).setImageResource(candies[randomCandy]);
                    candy.get(i).setTag(candies[randomCandy]);
                }
            }
        }
        for (int i = 0; i<8; i++){
            if ((int) candy.get(i).getTag() == notCandy){
                int randomCandy = (int) Math.floor(Math.random()*candies.length);
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
            }finally {
                mHandler.postDelayed(repeatChecker,interval);
            }
        }
    };
    void startRepeat(){
        repeatChecker.run();
    }
    private void candyInterchange(){
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
        for (int i = 0; i<noOfBlocks * noOfBlocks; i++){
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setLayoutParams(new android.view.ViewGroup.LayoutParams(widthOfBlock,widthOfBlock));
            imageView.setMaxHeight(widthOfBlock);
            imageView.setMaxWidth(widthOfBlock);
            int randomCandy = (int) Math.floor(Math.random()*  candies.length);//generates random index from cadies arrasy
            imageView.setImageResource(candies[randomCandy]);
            imageView.setTag(candies[randomCandy]);
            candy.add(imageView);
            gridLayout.addView(imageView);
        }
    }
}
