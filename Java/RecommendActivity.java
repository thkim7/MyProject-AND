package com.example.capstone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class RecommendActivity extends AppCompatActivity {

    private static final int YOUR_FILTER_REQUEST_CODE = 1;
    final String TAG = "RecommendActivity";
    private String requestUrl;
    ArrayList<Item> list = null;
    Item place = null;
    RecyclerView recyclerView;
    private GridLayout imageButtonContainer;
    ArrayList<Item> naturalTourismList = null; // 자연관광지 목록
    ArrayList<Item> experienceTourismList = null; // 체험관광지 목록
    ArrayList<Item> historyTourismList = null; // 역사관광지 목록
    ArrayList<Item> themeTourismList = null; // 테마관광지 목록
    ArrayList<Item> cultureTourismList = null; // 문화관광지 목록
    ArrayList<Item> recommendTourismList = null;
    ArrayList<Item> filteredList = null;

    Item natplace = null;
    Item expplace = null;
    Item hisplace = null;
    Item theplace = null;
    Item culplace = null;
    private Spinner spinner_lo;
    private Spinner spinner_reco;
    private EditText editTextTravelDestination;
    Button filter_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        StrictMode.enableDefaults();

        spinner_lo = findViewById(R.id.spinnerLocation);
        spinner_reco = findViewById(R.id.spinnerRecommendation);

        Button menu_recommend = findViewById(R.id.menu_recommend);
        Button menu_map = findViewById(R.id.menu_map);
        Button menu_schedule = findViewById(R.id.menu_schedule);
        Button menu_costtime = findViewById(R.id.menu_costtime);
        Button menu_review = findViewById(R.id.menu_review);
        Button menu_myinfo = findViewById(R.id.menu_myinfo);
        Button searchButton = findViewById(R.id.search_button);

        editTextTravelDestination = findViewById(R.id.editTextTravelDestination);

        filter_button = findViewById(R.id.filter_button);

        menu_recommend.setTextColor(ContextCompat.getColor(this, R.color.text_blue));

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.execute();

        spinner_lo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedLocation = spinner_lo.getSelectedItem().toString();
                String selectedRecommendation = spinner_reco.getSelectedItem().toString();
                String searchText = editTextTravelDestination.getText().toString();
                ArrayList<Item> filteredList = filterItemsByLocationAndRecommendation(selectedLocation, selectedRecommendation, searchText);
                updateRecyclerView(filteredList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        spinner_reco.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedLocation = spinner_lo.getSelectedItem().toString();
                String selectedRecommendation = spinner_reco.getSelectedItem().toString();
                String searchText = editTextTravelDestination.getText().toString();
                ArrayList<Item> filteredList = filterItemsByLocationAndRecommendation(selectedLocation, selectedRecommendation, searchText);
                updateRecyclerView(filteredList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        menu_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // '지도' 버튼을 클릭했을 때 처리할 코드
                Intent intent = new Intent(RecommendActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        menu_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // '일정' 버튼을 클릭했을 때 처리할 코드
                Intent intent = new Intent(RecommendActivity.this, ScheduleActivity.class);
                startActivity(intent);
            }
        });

        menu_costtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // '비용/시간' 버튼을 클릭했을 때 처리할 코드
                Intent intent = new Intent(RecommendActivity.this, CosttimeActivity.class);
                startActivity(intent);
            }
        });

        menu_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // '후기' 버튼을 클릭했을 때 처리할 코드
                Intent intent = new Intent(RecommendActivity.this, ReviewActivity.class);
                startActivity(intent);
            }
        });

        menu_myinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // '내 정보' 버튼을 클릭했을 때 처리할 코드
                Intent intent = new Intent(RecommendActivity.this, MyinfoActivity.class);
                startActivity(intent);
            }
        });

        filter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecommendActivity.this, FilterActivity.class);
                startActivityForResult(intent, YOUR_FILTER_REQUEST_CODE);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = editTextTravelDestination.getText().toString();
                String selectedLocation = spinner_lo.getSelectedItem().toString();
                String selectedRecommendation = spinner_reco.getSelectedItem().toString();

                ArrayList<Item> searchResults = filterItemsByLocationAndRecommendation(selectedLocation, selectedRecommendation, searchText);
                updateRecyclerView(searchResults);
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
            Intent intent = new Intent(RecommendActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public class MyAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {

            list = new ArrayList<Item>();
            naturalTourismList = new ArrayList<Item>();
            experienceTourismList = new ArrayList<Item>();
            historyTourismList = new ArrayList<Item>();
            themeTourismList = new ArrayList<Item>();
            cultureTourismList = new ArrayList<Item>();
            recommendTourismList = new ArrayList<Item>();

            String[] requestUrls = {
                    "https://openapi.gg.go.kr/CTST?KEY=e15c18ace775486e83c79835fad37825&pIndex=1&pSize=500",
                    "https://openapi.gg.go.kr/ETST?KEY=e15c18ace775486e83c79835fad37825&pIndex=1&pSize=271",
                    "https://openapi.gg.go.kr/HTST?KEY=e15c18ace775486e83c79835fad37825&pIndex=1&pSize=310",
                    "https://openapi.gg.go.kr/TTST?KEY=e15c18ace775486e83c79835fad37825&pIndex=1&pSize=301",
                    "https://openapi.gg.go.kr/CSST?KEY=e15c18ace775486e83c79835fad37825&pIndex=1&pSize=322"

            };
            for (String requestUrl : requestUrls) {
                try {
                    boolean b_tursm_info_nm = false;
                    boolean b_sm_re_addr = false;
                    boolean b_refine_wgs84_logt = false;
                    boolean b_refine_wgs84_lat = false;
                    boolean b_telno = false;
                    boolean b_firstimage = false;
                    boolean b_sigun_nm = false;

                    URL url = new URL(requestUrl);
                    InputStream is = url.openStream();
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = factory.newPullParser();
                    parser.setInput(new InputStreamReader(is, "UTF-8"));

                    String tag;
                    int eventType = parser.getEventType();
                    HashSet<String> uniqueInfo = new HashSet<>();

                    if (requestUrl.contains("CTST")) {
                        while (eventType != XmlPullParser.END_DOCUMENT) {
                            switch (eventType)   {
                                case XmlPullParser.START_DOCUMENT:
                                    break;
                                case XmlPullParser.END_DOCUMENT:
                                    break;
                                case XmlPullParser.END_TAG:
                                    if (parser.getName().equals("row") && natplace != null) {
                                        String uniqueKey = natplace.getTursm_info_nm() + natplace.getSm_re_addr();
                                        if (uniqueInfo.add(uniqueKey)) {
                                            naturalTourismList.add(natplace);
                                        }
                                    }
                                    break;
                                case XmlPullParser.START_TAG:
                                    if (parser.getName().equals("row")) {
                                        natplace = new Item();

                                    }
                                    if (parser.getName().equals("TURSM_INFO_NM"))
                                        b_tursm_info_nm = true;
                                    if (parser.getName().equals("SM_RE_ADDR")) b_sm_re_addr = true;
                                    if (parser.getName().equals("REFINE_WGS84_LOGT"))
                                        b_refine_wgs84_logt = true;
                                    if (parser.getName().equals("REFINE_WGS84_LAT"))
                                        b_refine_wgs84_lat = true;
                                    if (parser.getName().equals("TELNO")) b_telno = true;
                                    if (parser.getName().equals("SIGUN_NM")) b_sigun_nm = true;
                                    break;
                                case XmlPullParser.TEXT:
                                    if (b_tursm_info_nm) {
                                        natplace.setTursm_info_nm(parser.getText());
                                        b_tursm_info_nm = false;
                                    } else if (b_sm_re_addr) {
                                        natplace.setSm_re_addr(parser.getText());
                                        b_sm_re_addr = false;
                                    } else if (b_refine_wgs84_logt) {
                                        natplace.setRefine_wgs84_logt(parser.getText());
                                        b_refine_wgs84_logt = false;
                                    } else if (b_refine_wgs84_lat) {
                                        natplace.setRefine_wgs84_lat(parser.getText());
                                        b_refine_wgs84_lat = false;
                                    } else if (b_telno) {
                                        natplace.setTelno(parser.getText());
                                        b_telno = false;
                                    } else if (b_sigun_nm) {
                                        natplace.setSigun_nm(parser.getText());
                                        b_sigun_nm = false;
                                    }
                                    break;
                            }
                            eventType = parser.next();
                        }
                    } else if (requestUrl.contains("ETST")) {
                        while (eventType != XmlPullParser.END_DOCUMENT) {
                            switch (eventType) {
                                case XmlPullParser.START_DOCUMENT:
                                    break;
                                case XmlPullParser.END_DOCUMENT:
                                    break;
                                case XmlPullParser.END_TAG:
                                    if (parser.getName().equals("row") && expplace != null) {
                                        String uniqueKey = expplace.getTursm_info_nm() + expplace.getSm_re_addr();
                                        if (uniqueInfo.add(uniqueKey)) {
                                            experienceTourismList.add(expplace);
                                        }
                                    }
                                    break;
                                case XmlPullParser.START_TAG:
                                    if (parser.getName().equals("row")) {
                                        expplace = new Item();
                                    }
                                    if (parser.getName().equals("TURSM_INFO_NM"))
                                        b_tursm_info_nm = true;
                                    if (parser.getName().equals("SM_RE_ADDR")) b_sm_re_addr = true;
                                    if (parser.getName().equals("REFINE_WGS84_LOGT"))
                                        b_refine_wgs84_logt = true;
                                    if (parser.getName().equals("REFINE_WGS84_LAT"))
                                        b_refine_wgs84_lat = true;
                                    if (parser.getName().equals("TELNO")) b_telno = true;
                                    if (parser.getName().equals("SIGUN_NM")) b_sigun_nm = true;
                                    break;
                                case XmlPullParser.TEXT:
                                    if (b_tursm_info_nm) {
                                        expplace.setTursm_info_nm(parser.getText());
                                        b_tursm_info_nm = false;
                                    } else if (b_sm_re_addr) {
                                        expplace.setSm_re_addr(parser.getText());
                                        b_sm_re_addr = false;
                                    } else if (b_refine_wgs84_logt) {
                                        expplace.setRefine_wgs84_logt(parser.getText());
                                        b_refine_wgs84_logt = false;
                                    } else if (b_refine_wgs84_lat) {
                                        expplace.setRefine_wgs84_lat(parser.getText());
                                        b_refine_wgs84_lat = false;
                                    } else if (b_telno) {
                                        expplace.setTelno(parser.getText());
                                        b_telno = false;
                                    } else if (b_sigun_nm) {
                                        expplace.setSigun_nm(parser.getText());
                                        b_sigun_nm = false;
                                    }
                                    break;
                            }
                            eventType = parser.next();
                        }
                    } else if (requestUrl.contains("HTST")) {
                        while (eventType != XmlPullParser.END_DOCUMENT) {
                            switch (eventType) {
                                case XmlPullParser.START_DOCUMENT:
                                    break;
                                case XmlPullParser.END_DOCUMENT:
                                    break;
                                case XmlPullParser.END_TAG:
                                    if (parser.getName().equals("row") && hisplace != null) {
                                        String uniqueKey = hisplace.getTursm_info_nm() + hisplace.getSm_re_addr();
                                        if (uniqueInfo.add(uniqueKey)) {
                                            historyTourismList.add(hisplace);
                                        }
                                    }
                                    break;
                                case XmlPullParser.START_TAG:
                                    if (parser.getName().equals("row")) {
                                        hisplace = new Item();
                                    }
                                    if (parser.getName().equals("TURSM_INFO_NM"))
                                        b_tursm_info_nm = true;
                                    if (parser.getName().equals("SM_RE_ADDR")) b_sm_re_addr = true;
                                    if (parser.getName().equals("REFINE_WGS84_LOGT"))
                                        b_refine_wgs84_logt = true;
                                    if (parser.getName().equals("REFINE_WGS84_LAT"))
                                        b_refine_wgs84_lat = true;
                                    if (parser.getName().equals("TELNO")) b_telno = true;
                                    if (parser.getName().equals("SIGUN_NM")) b_sigun_nm = true;
                                    break;
                                case XmlPullParser.TEXT:
                                    if (b_tursm_info_nm) {
                                        hisplace.setTursm_info_nm(parser.getText());
                                        b_tursm_info_nm = false;
                                    } else if (b_sm_re_addr) {
                                        hisplace.setSm_re_addr(parser.getText());
                                        b_sm_re_addr = false;
                                    } else if (b_refine_wgs84_logt) {
                                        hisplace.setRefine_wgs84_logt(parser.getText());
                                        b_refine_wgs84_logt = false;
                                    } else if (b_refine_wgs84_lat) {
                                        hisplace.setRefine_wgs84_lat(parser.getText());
                                        b_refine_wgs84_lat = false;
                                    } else if (b_telno) {
                                        hisplace.setTelno(parser.getText());
                                        b_telno = false;
                                    } else if (b_sigun_nm) {
                                        hisplace.setSigun_nm(parser.getText());
                                        b_sigun_nm = false;
                                    }
                                    break;
                            }
                            eventType = parser.next();
                        }
                    } else if (requestUrl.contains("TTST")) {
                        while (eventType != XmlPullParser.END_DOCUMENT) {
                            switch (eventType) {
                                case XmlPullParser.START_DOCUMENT:
                                    break;
                                case XmlPullParser.END_DOCUMENT:
                                    break;
                                case XmlPullParser.END_TAG:
                                    if (parser.getName().equals("row") && theplace != null) {
                                        String uniqueKey = theplace.getTursm_info_nm() + theplace.getSm_re_addr();
                                        if (uniqueInfo.add(uniqueKey)) {
                                            themeTourismList.add(theplace);
                                        }
                                    }
                                    break;
                                case XmlPullParser.START_TAG:
                                    if (parser.getName().equals("row")) {
                                        theplace = new Item();
                                    }
                                    if (parser.getName().equals("TURSM_INFO_NM"))
                                        b_tursm_info_nm = true;
                                    if (parser.getName().equals("SM_RE_ADDR")) b_sm_re_addr = true;
                                    if (parser.getName().equals("REFINE_WGS84_LOGT"))
                                        b_refine_wgs84_logt = true;
                                    if (parser.getName().equals("REFINE_WGS84_LAT"))
                                        b_refine_wgs84_lat = true;
                                    if (parser.getName().equals("TELNO")) b_telno = true;
                                    if (parser.getName().equals("SIGUN_NM")) b_sigun_nm = true;
                                    break;
                                case XmlPullParser.TEXT:
                                    if (b_tursm_info_nm) {
                                        theplace.setTursm_info_nm(parser.getText());
                                        b_tursm_info_nm = false;
                                    } else if (b_sm_re_addr) {
                                        theplace.setSm_re_addr(parser.getText());
                                        b_sm_re_addr = false;
                                    } else if (b_refine_wgs84_logt) {
                                        theplace.setRefine_wgs84_logt(parser.getText());
                                        b_refine_wgs84_logt = false;
                                    } else if (b_refine_wgs84_lat) {
                                        theplace.setRefine_wgs84_lat(parser.getText());
                                        b_refine_wgs84_lat = false;
                                    } else if (b_telno) {
                                        theplace.setTelno(parser.getText());
                                        b_telno = false;
                                    } else if (b_sigun_nm) {
                                        theplace.setSigun_nm(parser.getText());
                                        b_sigun_nm = false;
                                    }
                                    break;
                            }
                            eventType = parser.next();
                        }
                    }
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
            list.addAll(naturalTourismList);
            list.addAll(experienceTourismList);
            list.addAll(historyTourismList);
            list.addAll(themeTourismList);
            HashSet<Item> uniqueSet1 = new HashSet<>(list);
            list.clear();
            list.addAll(uniqueSet1);
            Collections.sort(list);

            String[] additionalTourismSpots = {
                    "에버랜드",
                    "한국민속촌",
                    "수원화성",
                    "캠프그리브스 DMZ 체험관",
                    "판문점",
                    "서울대공원",
                    "서울랜드",
                    "곤지암",
                    "캐리비안베이",
                    "임진각 평화누리",
                    "원마운트",
                    "시흥갯골생태공원",
                    "일산호수공원",
                    "포천 허브아일랜드",
                    "웅진플레이도시",
                    "용문산관광단지"
            };

            for (String spot : additionalTourismSpots) {
                for (Item item : list) {
                    if (spot.equals(item.getTursm_info_nm())) {
                        recommendTourismList.add(item);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //어답터 연결
            MyAdapter adapter = new MyAdapter(getApplicationContext(), list);
            recyclerView.setAdapter(adapter);
        }
    }
    private ArrayList<Item> filterItemsByLocationAndRecommendation(String selectedLocation, String selectedRecommendation, String searchText) {
        if (list != null) {
            ArrayList<Item> filteredList = new ArrayList<>();
            ArrayList<Item> selectedList = (selectedRecommendation.equals("인기 여행지 추천")) ? recommendTourismList : list;

            for (Item item : selectedList) {
                boolean locationMatches = selectedLocation.isEmpty() || item.getSigun_nm().equals(selectedLocation);
                boolean keywordMatches = searchText.isEmpty() || item.getSigun_nm().contains(searchText) || item.getTursm_info_nm().contains(searchText);

                if (selectedLocation.equals("지역 전체보기") || (locationMatches && keywordMatches)) {
                    if (searchText.isEmpty() || keywordMatches) {
                        filteredList.add(item);
                    }
                }
            }
            return filteredList;
        }
        return new ArrayList<>();
    }

    // RecyclerView를 업데이트
    private void updateRecyclerView(ArrayList<Item> filteredList) {
        if (recyclerView != null) {

            MyAdapter adapter = new MyAdapter(getApplicationContext(), filteredList);
            recyclerView.setAdapter(adapter);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == YOUR_FILTER_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> selectedTravelTypes = data.getStringArrayListExtra("selectedTravelTypes");

            // 여행 유형에 따라 필터링된 목록을 가져옴
            ArrayList<Item> filteredListByTravelTypes = filterItemsByTravelTypes(selectedTravelTypes);

            // 여행 유형에 따라 필터링된 목록을 업데이트
            updateRecyclerView(filteredListByTravelTypes);
            updateFilterButtonText(selectedTravelTypes);
        }
    }

    private ArrayList<Item> filterItemsByTravelTypes(ArrayList<String> selectedTravelTypes) {
        if (list != null) {
            ArrayList<Item> filteredList = new ArrayList<>();

            for (Item item : list) {
                boolean shouldAdd = true;

                for (String travelType : selectedTravelTypes) {
                    // 선택된 여행 유형에 따라 필터링된 목록을 추가
                    switch (travelType) {
                        case "자연 여행":
                            shouldAdd &= naturalTourismList.contains(item);
                            break;
                        case "체험 여행":
                            shouldAdd &= experienceTourismList.contains(item);
                            break;
                        case "역사 여행":
                            shouldAdd &= historyTourismList.contains(item);
                            break;
                        case "테마 여행":
                            shouldAdd &= themeTourismList.contains(item);
                            break;
                    }
                }

                if (shouldAdd) {
                    filteredList.add(item);
                }
            }
            return filteredList;
        }
        return new ArrayList<>();
    }
    private void updateFilterButtonText(ArrayList<String> selectedTravelTypes) {
        // 첫 번째 선택한 여행 유형을 표시하려고 가정합니다.
        if (!selectedTravelTypes.isEmpty()) {
            String selectedType = selectedTravelTypes.get(0);
            String buttonText;

            // 선택한 여행 유형에 따라 버튼 텍스트 설정
            switch (selectedType) {
                case "자연 여행":
                    buttonText = "자연";
                    break;
                case "체험 여행":
                    buttonText = "체험";
                    break;
                case "역사 여행":
                    buttonText = "역사";
                    break;
                case "테마 여행":
                    buttonText = "테마";
                    break;
                default:
                    buttonText = "필터";
                    break;
            }

            filter_button.setText(buttonText);
        } else {
            filter_button.setText("필터");
        }
    }
}

