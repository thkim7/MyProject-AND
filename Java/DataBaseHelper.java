package com.example.capstone;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Imagetest.db";
    private static final int DB_VERSION = 1;
    private final Context mContext;

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.mContext = context;
        copyDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 데이터베이스가 최초로 생성될 때 호출되는 메서드
        // 여기에 테이블 생성 등의 초기화 로직을 작성
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 데이터베이스의 버전이 올라갈 때 호출되는 메서드
        // 여기에 데이터베이스 스키마 변경 등의 업그레이드 로직을 작성
    }

    private void copyDatabase() {
        try {
            InputStream inputStream = mContext.getAssets().open(DB_NAME);
            String outFileName = mContext.getDatabasePath(DB_NAME).getPath();
            OutputStream outputStream = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;

            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("DataBaseHelper", "데이터베이스 복사 중 오류 발생");
        }
    }

    public byte[] getImageFromDatabase(String tableName, String imageName) {
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT image_data FROM " + tableName + " WHERE image_name = ?";
        Cursor cursor = db.rawQuery(query, new String[]{imageName});

        if (cursor.moveToFirst()) {
            return cursor.getBlob(0); // 이미지 데이터는 BLOB 형식으로 저장됨
        }

        cursor.close();
        return null;
    }
}
