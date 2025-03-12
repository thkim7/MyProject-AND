package com.example.capstone;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlaceType;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
ActivityCompat.OnRequestPermissionsResultCallback,PlacesListener {

    private GoogleMap mMap;
    private PlacesClient placesClient;
    private EditText edit_destmap;
    private CustomDialog customDialog;
    RecyclerView recyclerView;
    List<Marker> previous_marker = null;
    LatLng currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button menu_recommend = findViewById(R.id.menu_recommend);
        Button menu_map = findViewById(R.id.menu_map);
        Button menu_schedule = findViewById(R.id.menu_schedule);
        Button menu_costtime = findViewById(R.id.menu_costtime);
        Button menu_review = findViewById(R.id.menu_review);
        Button menu_myinfo = findViewById(R.id.menu_myinfo);
        Button destsearchButton = findViewById(R.id.destsearchbutton);

        Button nomal = findViewById(R.id.nomal);
        Button cafe = findViewById(R.id.cafe);
        Button food = findViewById(R.id.food);
        Button hotel = findViewById(R.id.hotel);

        edit_destmap = findViewById(R.id.editTextTravelDestination);

        previous_marker = new ArrayList<Marker>();
        //mMap.setOnMarkerClickListener(this);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerViewPlaces);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);


        menu_map.setTextColor(ContextCompat.getColor(this, R.color.text_blue));

        Places.initialize(getApplicationContext(), "AIzaSyCuGUBEYkJ4vCfp0d5kEVGONSEVSEpiGKU");
        placesClient = Places.createClient(this);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }

        // 위치 서비스 활성화
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastKnownLocation != null) {
                double latitude = lastKnownLocation.getLatitude();
                double longitude = lastKnownLocation.getLongitude();
            }
        }

        nomal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String destination = edit_destmap.getText().toString().trim();

                // 입력한 주소로 지도 이동 및 마커 표시
                if (!TextUtils.isEmpty(destination)) {
                    moveMapToLocation(destination);
                }
            }
        });

        cafe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String destination = edit_destmap.getText().toString().trim();
                if (!TextUtils.isEmpty(destination)) {
                    // 사용자가 입력한 목적지의 주소를 사용하여 음식점을 검색
                    showCafeInformation(destination);

                } else {
                    // 목적지 주소가 비어 있을 때의 처리
                    Toast.makeText(MapActivity.this, "목적지를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String destination = edit_destmap.getText().toString().trim();
                if (!TextUtils.isEmpty(destination)) {
                    // 사용자가 입력한 목적지의 주소를 사용하여 음식점을 검색
                    showFoodInformation(destination);
                } else {
                    // 목적지 주소가 비어 있을 때의 처리
                    Toast.makeText(MapActivity.this, "목적지를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String destination = edit_destmap.getText().toString().trim();
                if (!TextUtils.isEmpty(destination)) {
                    // 사용자가 입력한 목적지의 주소를 사용하여 음식점을 검색
                    showHotelInformation(destination);
                } else {
                    // 목적지 주소가 비어 있을 때의 처리
                    Toast.makeText(MapActivity.this, "목적지를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        menu_recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // '지도' 버튼을 클릭했을 때 처리할 코드
                Intent intent = new Intent(MapActivity.this, RecommendActivity.class);
                startActivity(intent);
            }
        });

        menu_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // '일정' 버튼을 클릭했을 때 처리할 코드
                Intent intent = new Intent(MapActivity.this, ScheduleActivity.class);
                startActivity(intent);
            }
        });

        menu_costtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // '비용/시간' 버튼을 클릭했을 때 처리할 코드
                Intent intent = new Intent(MapActivity.this, CosttimeActivity.class);
                startActivity(intent);
            }
        });

        menu_review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // '후기' 버튼을 클릭했을 때 처리할 코드
                Intent intent = new Intent(MapActivity.this, ReviewActivity.class);
                startActivity(intent);
            }
        });

        menu_myinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // '내 정보' 버튼을 클릭했을 때 처리할 코드
                Intent intent = new Intent(MapActivity.this, MyinfoActivity.class);
                startActivity(intent);
            }
        });

        destsearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String destination = edit_destmap.getText().toString().trim();

                // 입력한 주소로 지도 이동 및 마커 표시
                if (!TextUtils.isEmpty(destination)) {
                    moveMapToLocation(destination);
                }
            }
        });

    }

    private void moveMapToLocation(String locationName) {
        if (mMap == null) {
            return;
        }

        // 지오코더를 사용하여 주소를 좌표로 변환
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addressList = geocoder.getFromLocationName(locationName, 1);
            if (addressList != null && !addressList.isEmpty()) {
                Address address = addressList.get(0);
                LatLng destinationLatLng = new LatLng(address.getLatitude(), address.getLongitude());

                // 기존 마커 삭제
                mMap.clear();

                // 새로운 마커 추가
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(destinationLatLng);
                markerOptions.title(locationName);
                mMap.addMarker(markerOptions);

                // 지도 이동
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destinationLatLng, 15));
                currentPosition = destinationLatLng;
            } else {
                Toast.makeText(this, "주소를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "주소를 검색하는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        LatLng SEOUL = new LatLng(37.556, 126.97);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title("서울");
        markerOptions.snippet("한국 수도");

        mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 10));
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
            Intent intent = new Intent(MapActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPlacesFailure(PlacesException e) {

    }

    @Override
    public void onPlacesStart() {

    }

    @Override
    public void onPlacesSuccess(final List<Place> places) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MapAdapter mapAdapter = new MapAdapter(MapActivity.this, new ArrayList<MapItem>());
                recyclerView.setAdapter(mapAdapter);

                for (noman.googleplaces.Place place : places) {

                    LatLng latLng
                            = new LatLng(place.getLatitude()
                            , place.getLongitude());

                    String markerSnippet = getCurrentAddress(latLng);

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title(place.getName());
                    markerOptions.snippet(markerSnippet);
                    Marker item = mMap.addMarker(markerOptions);
                    previous_marker.add(item);

                    MapItem mapItem = new MapItem();
                    mapItem.setPlaceName(place.getName());
                    mapItem.setPlaceAddress(markerSnippet);
                    mapAdapter.addMarker(mapItem);
                }

                //중복 마커 제거
                HashSet<Marker> hashSet = new HashSet<Marker>();
                hashSet.addAll(previous_marker);
                previous_marker.clear();
                previous_marker.addAll(hashSet);

            }
        });
    }

    @Override
    public void onPlacesFinished() {

    }
    public void showCafeInformation(String destination)
    {
        mMap.clear();//지도 클리어

        if (previous_marker != null)
            previous_marker.clear();//지역정보 마커 클리어

        new NRPlaces.Builder()
                .listener(MapActivity.this)
                .key("AIzaSyCuGUBEYkJ4vCfp0d5kEVGONSEVSEpiGKU")
                .latlng(currentPosition.latitude, currentPosition.longitude)//현재 위치
                .radius(500) //500 미터 내에서 검색
                .type(PlaceType.CAFE) //음식점
                .build()
                .execute();
    }

    public void showFoodInformation(String destination)
    {
        mMap.clear();//지도 클리어

        if (previous_marker != null)
            previous_marker.clear();//지역정보 마커 클리어

        new NRPlaces.Builder()
                .listener(MapActivity.this)
                .key("AIzaSyCuGUBEYkJ4vCfp0d5kEVGONSEVSEpiGKU")
                .latlng(currentPosition.latitude, currentPosition.longitude)//현재 위치
                .radius(500) //500 미터 내에서 검색
                .type(PlaceType.RESTAURANT) //음식점
                .build()
                .execute();
    }
    public void showHotelInformation(String destination)
    {
        mMap.clear();//지도 클리어

        if (previous_marker != null)
            previous_marker.clear();//지역정보 마커 클리어

        new NRPlaces.Builder()
                .listener(MapActivity.this)
                .key("AIzaSyCuGUBEYkJ4vCfp0d5kEVGONSEVSEpiGKU")
                .latlng(currentPosition.latitude, currentPosition.longitude)//현재 위치
                .radius(500) //500 미터 내에서 검색
                .type(PlaceType.LODGING) //음식점
                .build()
                .execute();
    }

    public String getCurrentAddress(LatLng latlng) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latlng.latitude,
                    latlng.longitude,
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }


        if (addresses == null || addresses.size() == 0) {
            //Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }

    }
}