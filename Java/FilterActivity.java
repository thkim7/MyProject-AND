package com.example.capstone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FilterActivity extends AppCompatActivity {

    CheckBox natchk;
    CheckBox expchk;
    CheckBox thechk;
    CheckBox hischk;
    Button btnApplyFilters;

    private ArrayList<String> selectTravelTypes = new ArrayList<>();
    private ArrayList<String> selectTravelBudgets = new ArrayList<>();
    private ArrayList<String> selectTravelCompanions = new ArrayList<>();
    private ArrayList<Item> filteredList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        btnApplyFilters = findViewById(R.id.btnApplyFilters);
        expchk = findViewById(R.id.exp_chk);
        natchk = findViewById(R.id.nat_chk);
        hischk = findViewById(R.id.his_chk);
        thechk = findViewById(R.id.the_chk);

        // 데이터를 초기화하고 어댑터를 설정합니다.


        btnApplyFilters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSelectedFilters();
                openRecommendActivity();

            }
        });
    }


    private void saveSelectedFilters() {
        selectTravelTypes.clear();

        if (expchk.isChecked()) {
            selectTravelTypes.add("체험 여행");
        }
        if (natchk.isChecked()) {
            selectTravelTypes.add("자연 여행");
        }
        if (hischk.isChecked()) {
            selectTravelTypes.add("역사 여행");
        }
        if (thechk.isChecked()) {
            selectTravelTypes.add("테마 여행");
        }

        StringBuilder message = new StringBuilder("선택된 필터:\n");
        if (!selectTravelTypes.isEmpty()) {
            message.append("여행 유형: ").append(selectTravelTypes.toString()).append("\n");
        }

        //showToast(message.toString());
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    private void openRecommendActivity() {
        // RecommendActivity로 데이터를 전달하는 Intent 생성
        Intent resultIntent = new Intent();
        resultIntent.putStringArrayListExtra("selectedTravelTypes", selectTravelTypes);
        // setResult를 통해 결과를 돌려줌
        setResult(RESULT_OK, resultIntent);

        // 현재 액티비티 종료
        finish();
    }
}