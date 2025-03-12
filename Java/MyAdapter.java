package com.example.capstone;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    private ArrayList<Item> mList;
    private LayoutInflater mInflate;
    private Context mContext;
    private DataBaseHelper dbHelper;

    public MyAdapter(Context context, ArrayList<Item> items) {
        this.mList = items;
        this.mInflate = LayoutInflater.from(context);
        this.mContext = context;
        this.dbHelper = new DataBaseHelper(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflate.inflate(R.layout.recycle_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tursm_info_nm.setText(mList.get(position).tursm_info_nm);
        holder.sm_re_addr.setText(mList.get(position).sm_re_addr);
        holder.refine_wgs84_logt.setText(mList.get(position).refine_wgs84_logt);
        holder.refine_wgs84_lat.setText(mList.get(position).refine_wgs84_lat);
        holder.telno.setText(mList.get(position).telno);
        holder.sigun_nm.setText(mList.get(position).sigun_nm);

        //click event
        ImageButton placeinfobutton = holder.itemView.findViewById(R.id.placeinfobutton);

        String imageName = mList.get(position).getImageName();

        byte[] imageData = dbHelper.getImageFromDatabase("images1", imageName);

        if (imageData != null) {
            File imageFile = saveImageToFile(imageData, imageName);

            // Picasso로 이미지 로딩
            Picasso.get().load(imageFile).into(placeinfobutton);
        } else {
            // 이미지 데이터가 없으면 기본 이미지 또는 다른 처리를 수행할 수 있습니다.
            placeinfobutton.setImageResource(R.drawable.ic_noimage);
        }

        placeinfobutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DestInfoActivity.class);
                intent.putExtra("sm_re_addr", mList.get(position).sm_re_addr);
                intent.putExtra("telno", mList.get(position).telno);
                intent.putExtra("refine_wgs84_logt", mList.get(position).refine_wgs84_logt);
                intent.putExtra("refine_wgs84_lat", mList.get(position).refine_wgs84_lat);
                intent.putExtra("imageName", mList.get(position).getImageName());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    private File saveImageToFile(byte[] imageData, String imageName) {
        try {
            File directory = mContext.getDir("images", Context.MODE_PRIVATE);
            File imageFile = new File(directory, imageName);

            FileOutputStream fos = new FileOutputStream(imageFile);
            fos.write(imageData);
            fos.close();

            return imageFile;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tursm_info_nm;
        public TextView sm_re_addr;
        public TextView refine_wgs84_logt;
        public TextView refine_wgs84_lat;
        public TextView telno;
        public ViewGroup buttonContainer;
        public TextView firstimage;
        public TextView sigun_nm;

        public MyViewHolder(View itemView) {
            super(itemView);

            tursm_info_nm = itemView.findViewById(R.id.tv_tursm_info_nm);
            sm_re_addr = itemView.findViewById(R.id.tv_sm_re_addr);
            refine_wgs84_logt = itemView.findViewById(R.id.tv_refine_wgs84_logt);
            refine_wgs84_lat = itemView.findViewById(R.id.tv_refine_wgs84_lat);
            telno = itemView.findViewById(R.id.tv_telno);
            sigun_nm = itemView.findViewById(R.id.tv_sigun_nm);
            //firstimage = itemView.findViewById(R.id.placeinfobutton);
        }
    }
}
