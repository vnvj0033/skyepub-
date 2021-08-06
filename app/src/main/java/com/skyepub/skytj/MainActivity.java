package com.skyepub.skytj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    final String TAG = "SKYEPUB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        check();
        setup();
    }

    void check() {
        String path = getStorageDirectory() + "/books/" + "Alice.epub";
        File file = new File(path);
        if (file.exists()) {
            Log.w(TAG, "File installed");
        } else {
            Log.w(TAG, "File not installed");
        }
    }

    void setup() {
        if (isSetup()) return;
        makeDirectory("books");
        copyBookFromAssetsToDevice("Alice.epub");

        SharedPreferences pref = getSharedPreferences("SkyMiniJ", 0);
        SharedPreferences.Editor edit = pref.edit();
        edit.putBoolean("isSetup", true);
        edit.commit();

    }

    private boolean isSetup() {
        SharedPreferences pref = getSharedPreferences("SkyMiniJ", 0);
        return pref.getBoolean("isSetup", false);
    }


    public void copyBookFromAssetsToDevice(String fileName) {
        try {
            String path = getStorageDirectory() + "/books/" + fileName;
            File file = new File(path);
            if (file.exists()) return;
            InputStream localInputStream = getAssets().open("books/" + fileName);
            FileOutputStream localFileOutputStream = new FileOutputStream(getStorageDirectory() + "/books/" + fileName);

            byte[] arrayOfByte = new byte[1024];
            int offset;
            while ((offset = localInputStream.read(arrayOfByte)) > 0) {
                localFileOutputStream.write(arrayOfByte, 0, offset);
            }
            localFileOutputStream.close();
            localInputStream.close();
            Log.d(TAG, fileName + " copied to phone");
        } catch (IOException localIOException) {
            localIOException.printStackTrace();
            Log.d(TAG, "failed to copy");
        }
    }

    public boolean makeDirectory(String dirName) {
        boolean res;
        String filePath = getStorageDirectory() + "/" + dirName;
        File file = new File(filePath);
        if (!file.exists()) {
            res = file.mkdirs();
        } else {
            res = false;
        }
        return res;
    }

    public String getStorageDirectory() {
        return getFilesDir().getAbsolutePath() + "/" + getString(getApplicationInfo().labelRes);
    }


    public void onOpenBookPressed(View view) {
        Intent intent;
        intent = new Intent(this, BookActivity.class);
        intent.putExtra("BOOKCODE", 0);
        intent.putExtra("BOOKNAME", "Alice.epub");
        intent.putExtra("transitionType", 1);
        startActivity(intent);
    }
}