package com.example.capstone;

import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class CosttimeActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap2;

//    final LatLng[] startLatLng = new LatLng[1];
//    final LatLng[] destinationLatLng = new LatLng[1];

    //private MarkerOptions startMarkerOptions;
    //private MarkerOptions destinationMarkerOptions;

    private LatLng startLatLng;
    private LatLng destinationLatLng;

    private Polyline polyline;

    private TextView elapsedTimeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_costtime);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        Button menu_recommend = findViewById(R.id.menu_recommend);
        Button menu_map = findViewById(R.id.menu_map);
        Button menu_schedule = findViewById(R.id.menu_schedule);
        Button menu_costtime = findViewById(R.id.menu_costtime);
        Button menu_review = findViewById(R.id.menu_review);
        Button menu_myinfo = findViewById(R.id.menu_myinfo);

        Button startbutton = findViewById(R.id.startsearch);
        Button destinationbutton = findViewById(R.id.destinationsearch);

        EditText coststart = findViewById(R.id.coststart);
        EditText costdest = findViewById(R.id.costdest);

        Button train = findViewById(R.id.train);
        Button car = findViewById(R.id.car);
        Button walk = findViewById(R.id.walk);
        Button bike = findViewById(R.id.bike);

        elapsedTimeTextView = findViewById(R.id.elapsedTimeTextView);


        menu_costtime.setTextColor(ContextCompat.getColor(this, R.color.text_blue));


        train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        bike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        menu_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // '지도' 버튼을 클릭했을 때 처리할 코드
                Intent intent = new Intent(CosttimeActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });

        menu_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // '일정' 버튼을 클릭했을 때 처리할 코드
                Intent intent = new Intent(CosttimeActivity.this, ScheduleActivity.class);
                startActivity(intent);
            }
        });

        menu_recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // '비용/시간' 버튼을 클릭했을 때 처리할 코드
                Intent intent = new Intent(CosttimeActivity.this, RecommendActivity.class);
                startActivity(intent);
            }
        });

        menu_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // '후기' 버튼을 클릭했을 때 처리할 코드
                Intent intent = new Intent(CosttimeActivity.this, ReviewActivity.class);
                startActivity(intent);
            }
        });

        menu_myinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // '내 정보' 버튼을 클릭했을 때 처리할 코드
                Intent intent = new Intent(CosttimeActivity.this, MyinfoActivity.class);
                startActivity(intent);
            }
        });

        startbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String startLocation = coststart.getText().toString().trim();

                // 입력한 주소로 지도 이동 및 마커 표시
                if (!TextUtils.isEmpty(startLocation)) {
//                    startMarkerOptions =
                            moveMapToLocation(startLocation, true);
                }
            }
        });

        destinationbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String destinationLocation = costdest.getText().toString().trim();

                // 입력한 주소로 지도 이동 및 마커 표시
                if (!TextUtils.isEmpty(destinationLocation)) {
//                    destinationMarkerOptions =
                            moveMapToLocation(destinationLocation, true);
                }

                if (startLatLng != null && destinationLatLng != null) {
                    drawRoute(startLatLng, destinationLatLng);
                    calculateAndDisplayElapsedTime(startLatLng, destinationLatLng);
                }
            }
        });
    }

    private void moveMapToLocation(String locationName, boolean isStartLocation) {
        if (mMap2 == null) {
            return;
        }

        // 지오코더를 사용하여 주소를 좌표로 변환
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addressList = geocoder.getFromLocationName(locationName, 1);
            if (addressList != null && !addressList.isEmpty()) {
                Address address = addressList.get(0);
                LatLng locationLatLng = new LatLng(address.getLatitude(), address.getLongitude());

                // 기존 마커 삭제
                //mMap2.clear();

                // 새로운 마커 추가
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(locationLatLng);
                markerOptions.title(locationName);
                mMap2.addMarker(markerOptions);

                // 지도 이동
                mMap2.moveCamera(CameraUpdateFactory.newLatLngZoom(locationLatLng, 15));

            } else {
                Toast.makeText(this, "주소를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "주소를 검색하는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
        }
    }
    private void drawPolyline(List<LatLng> points) {
        if (polyline != null) {
            polyline.remove();
        }

        PolylineOptions polylineOptions = new PolylineOptions().width(5).color(Color.RED).addAll(points);

        polyline = mMap2.addPolyline(polylineOptions);
    }
    private void drawRoute(LatLng start, LatLng end) {
        String apiUrl = "https://maps.googleapis.com/maps/api/directions/json" +
                "?origin=" + start.latitude + "," + start.longitude +
                "&destination=" + end.latitude + "," + end.longitude;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(apiUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        // JSON 응답 파싱
                        parseDirectionsResponse(response.toString());
                    } else {
                        // 오류 처리
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CosttimeActivity.this, "Directions API 오류", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CosttimeActivity.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }
    private void calculateAndDisplayElapsedTime(LatLng start, LatLng end) {
        String apiUrl = "https://maps.googleapis.com/maps/api/directions/json" +
                "?origin=" + start.latitude + "," + start.longitude +
                "&destination=" + end.latitude + "," + end.longitude;

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        // JSON 응답 파싱
                        return parseDirectionsResponse(response.toString());
                    } else {
                        // 오류 처리
                        return "Directions API 오류";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return "오류";
                }
            }
            @Override
            protected void onPostExecute(String elapsedTime) {
                elapsedTimeTextView.setText("걸리는 시간: " + elapsedTime);
            }
        }.execute(apiUrl);
    }

    private String parseDirectionsResponse(String jsonResponse) {
        try {
            Log.d("DirectionsResponse", jsonResponse);
            JSONObject jsonObject = new JSONObject(jsonResponse);
            if (jsonObject.has("routes")) {
                JSONArray routesArray = jsonObject.getJSONArray("routes");
                if (routesArray.length() > 0) {
                    JSONObject route = routesArray.getJSONObject(0);
                    JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
                    String points = overviewPolyline.getString("points");
                    Log.d("OverViewPolyline", points);

                    List<LatLng> decodedPath = PolyUtil.decode(points); // PolyUtil 라이브러리 사용

                    JSONObject legs = route.getJSONArray("legs").getJSONObject(0);
                    JSONObject duration = legs.getJSONObject("duration");
                    String time = duration.getString("text");
                    Log.d("Duration", time);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            drawPolyline(decodedPath);
                            elapsedTimeTextView.setText("걸리는 시간: " + time);
                        }
                    });
                    return time;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(CosttimeActivity.this, "응답 데이터를 파싱하는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
        return "";
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap2 = googleMap;

        LatLng SEOUL = new LatLng(37.556, 126.97);

//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(SEOUL);
//        markerOptions.title("서울");
//        markerOptions.snippet("한국 수도");
//
//        mMap2.addMarker(markerOptions);
        Log.d("MapReady", "Map is ready");

        mMap2.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 10));
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
            Intent intent = new Intent(CosttimeActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}