package com.example.capstone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MapAdapter extends RecyclerView.Adapter<MapAdapter.MapViewHolder> {
    private ArrayList<MapItem> mapItemList;
    private LayoutInflater mInflate;
    private Context mContext;

    public MapAdapter(Context context, ArrayList<MapItem> mapitems) {
        this.mapItemList = mapitems;
        this.mInflate = LayoutInflater.from(context);
        this.mContext = context;
    }


    public void addMarker(MapItem mapItem) {
        mapItemList.add(mapItem);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MapViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflate.inflate(R.layout.recycle_item2, parent, false);
        MapViewHolder viewHolder = new MapViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    MapItem clickedItem = mapItemList.get(position);
                    openCustomDialog(clickedItem.getPlaceName(), clickedItem.getPlaceAddress(), clickedItem.getCategory());
                }
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MapViewHolder holder, int position) {
        holder.placeName.setText(mapItemList.get(position).placeName);
        holder.placeAddress.setText(mapItemList.get(position).placeAddress);
    }

    @Override
    public int getItemCount() {
        return mapItemList.size();
    }
    private void openCustomDialog(String placeName, String placeAddress, String category) {
        CustomDialog customDialog = new CustomDialog(mContext, placeName, placeAddress);
        customDialog.setCategory(category);
        customDialog.show();
    }
    public ArrayList<MapItem> getMapItems() {
        return mapItemList;
    }
    public static class MapViewHolder extends RecyclerView.ViewHolder {
        public TextView placeName;
        public TextView placeAddress;


        public MapViewHolder(View itemView) {
            super(itemView);

            placeName = itemView.findViewById(R.id.tv_placeName);
            placeAddress = itemView.findViewById(R.id.tv_address);
        }
    }

}
