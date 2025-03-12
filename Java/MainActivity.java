package com.example.capstone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button recommendButton = findViewById(R.id.recommend);

        recommendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // '추천 여행지' 버튼을 클릭했을 때 처리할 코드
                Intent intent = new Intent(MainActivity.this, RecommendActivity.class);
                startActivity(intent);
            }
        });

        Button mapbutton = findViewById(R.id.map);
        mapbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // '지도' 버튼을 클릭했을 때 처리할 코드
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        Button schedulebutton = findViewById(R.id.schedule);
        schedulebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // '일정' 버튼을 클릭했을 때 처리할 코드
                Intent intent = new Intent(MainActivity.this, ScheduleActivity.class);
                startActivity(intent);
            }
        });

        Button costtimebutton = findViewById(R.id.costtime);
        costtimebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // '비용/시간' 버튼을 클릭했을 때 처리할 코드
                Intent intent = new Intent(MainActivity.this, CosttimeActivity.class);
                startActivity(intent);
            }
        });

        Button reviewbutton = findViewById(R.id.review);
        reviewbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // '후기' 버튼을 클릭했을 때 처리할 코드
                Intent intent = new Intent(MainActivity.this, ReviewActivity.class);
                startActivity(intent);
            }
        });

        Button myinfobutton = findViewById(R.id.myinfo);
        myinfobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // '내정보' 버튼을 클릭했을 때 처리할 코드
                Intent intent = new Intent(MainActivity.this, MyinfoActivity.class);
                startActivity(intent);
            }
        });
    }
}