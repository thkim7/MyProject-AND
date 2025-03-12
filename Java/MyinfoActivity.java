package com.example.capstone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MyinfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);

        Button menu_recommend = findViewById(R.id.menu_recommend);
        Button menu_map = findViewById(R.id.menu_map);
        Button menu_schedule = findViewById(R.id.menu_schedule);
        Button menu_costtime = findViewById(R.id.menu_costtime);
        Button menu_review = findViewById(R.id.menu_review);
        Button menu_myinfo = findViewById(R.id.menu_myinfo);

        menu_myinfo.setTextColor(ContextCompat.getColor(this, R.color.text_blue));

        menu_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // '지도' 버튼을 클릭했을 때 처리할 코드
                Intent intent = new Intent(MyinfoActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        menu_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // '일정' 버튼을 클릭했을 때 처리할 코드
                Intent intent = new Intent(MyinfoActivity.this, ScheduleActivity.class);
                startActivity(intent);
            }
        });

        menu_costtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // '비용/시간' 버튼을 클릭했을 때 처리할 코드
                Intent intent = new Intent(MyinfoActivity.this, CosttimeActivity.class);
                startActivity(intent);
            }
        });

        menu_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // '후기' 버튼을 클릭했을 때 처리할 코드
                Intent intent = new Intent(MyinfoActivity.this, ReviewActivity.class);
                startActivity(intent);
            }
        });

        menu_recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // '내 정보' 버튼을 클릭했을 때 처리할 코드
                Intent intent = new Intent(MyinfoActivity.this, RecommendActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_main) {
            Intent intent = new Intent(MyinfoActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}