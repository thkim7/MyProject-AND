package com.example.capstone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DestInfoActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double mapx;
    private double mapy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dest_info);

        //ImageButton destination1 = findViewById(R.id.destination1);
        TextView destinfo = findViewById(R.id.destinfo);
        TextView tv_getaddr1 = findViewById(R.id.tv_getaddr1);
        TextView tv_gettel = findViewById(R.id.tv_gettel);
        ImageView placeImage = findViewById(R.id.placeImage);

        Intent intent = getIntent();

        String sm_re_addr = intent.getStringExtra("sm_re_addr");
        String telno = intent.getStringExtra("telno");
        mapx = Double.parseDouble(intent.getStringExtra("refine_wgs84_logt"));
        mapy = Double.parseDouble(intent.getStringExtra("refine_wgs84_lat"));
        String imageName = intent.getStringExtra("imageName");

        if (sm_re_addr != null) {
            tv_getaddr1.setText(sm_re_addr);
        }

        if (telno != null) {
            tv_gettel.setText(telno);
        }
        if (imageName != null) {
            DataBaseHelper dbHelper = new DataBaseHelper(this);
            byte[] imageData = dbHelper.getImageFromDatabase("images1", imageName);

            if (imageData != null) {
                File imageFile = saveImageToFile(imageData, imageName);

                // Picasso로 이미지 로딩
                Picasso.get().load(imageFile).into(placeImage);
            } else {
                // 이미지 데이터가 없으면 기본 이미지 또는 다른 처리를 수행할 수 있습니다.
                placeImage.setImageResource(R.drawable.ic_noimage);
            }
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng destination;

        if (mapx != 0.0 && mapy != 0.0) {
            destination = new LatLng(mapy, mapx);
        } else {
            // mapx 또는 mapy가 null 또는 0.0인 경우, 기본 위치로 설정
            destination = new LatLng(37.5, 127.0);
        }
        mMap.addMarker(new MarkerOptions().position(destination).title("Destination"));

        // Move the camera to the destination location
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, 16));

    }

    private File saveImageToFile(byte[] imageData, String imageName) {
        try {
            File directory = getDir("images", MODE_PRIVATE);
            File imageFile = new File(directory, imageName);

            FileOutputStream fos = new FileOutputStream(imageFile);
            fos.write(imageData);
            fos.close();

            return imageFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}