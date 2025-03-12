package com.example.capstone;

import android.app.Dialog;
import android.content.Context;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class CustomDialog extends Dialog {
    private TextView mapdestinfo;
    private TextView mapcategory;
    private TextView mapAddress;
    private Button mapsavePlaceButton;

    public CustomDialog(@NonNull Context context, String placeName, String placeAddress) {
        super(context);
        setContentView(R.layout.activity_custom_dest_info);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(getWindow().getAttributes());
        layoutParams.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9);
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT; // 높이 조정은 필요에 따라 조절하세요
        getWindow().setAttributes(layoutParams);

        mapdestinfo = findViewById(R.id.mapdestinfo);
        mapcategory = findViewById(R.id.mapcategory);
        mapAddress = findViewById(R.id.mapaddress);
        mapsavePlaceButton = findViewById(R.id.mapsavePlaceButton);
        ImageView mapplaceImage = findViewById(R.id.mapplaceImage);
        TableLayout infoTable1 = findViewById(R.id.infoTable1);

        mapdestinfo.setText(placeName);
        mapAddress.setText(placeAddress);
    }
    public void setCategory(String category) {
        mapcategory.setText(category);
    }
}
