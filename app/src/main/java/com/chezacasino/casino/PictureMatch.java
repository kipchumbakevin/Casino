package com.chezacasino.casino;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class PictureMatch extends AppCompatActivity {
    ImageView curView = null;
    private int countPair = 0;
    final int[] drawable = new int[]{
            R.drawable.question,
            R.drawable.car,
            R.drawable.house,
            R.drawable.dog,
            R.drawable.pool,
            R.drawable.fridge,
            R.drawable.plane,
            R.drawable.laptop,
            R.drawable.tv
    };
    int pos[] = {0,1,2,3,4,5,6,7,0,1,2,3,4,5,6,7};
    int currentPos = -1;
    GridView gridView;
    ImageAdapter imageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_match);
        gridView = findViewById(R.id.gridView);
        imageAdapter = new ImageAdapter(this);
        gridView.setAdapter(imageAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentPos<0){
                    currentPos = position;
                    curView = (ImageView) view;
                    ((ImageView)view).setImageResource(drawable[pos[position]]);
                }else {
                    if (currentPos == position){
                        ((ImageView)view).setImageResource(R.drawable.question);
                    }else if (pos[currentPos] != pos[position]){
                        curView.setImageResource(R.drawable.question);
                        Toast.makeText(PictureMatch.this,"Not match",Toast.LENGTH_SHORT).show();
                    }else {
                        ((ImageView)view).setImageResource(drawable[pos[position]]);
                        countPair++;
                        if (countPair == 0){
                            Toast.makeText(PictureMatch.this,"You win",Toast.LENGTH_SHORT).show();
                        }
                    }
                    currentPos = -1;
                }
            }
        });
    }
}
