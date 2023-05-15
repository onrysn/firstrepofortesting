package com.google.zxing.client.android;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class YeniDenemezpl {

    public Bitmap SetBitmap(String filePath){
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        return bitmap;
    }
}
