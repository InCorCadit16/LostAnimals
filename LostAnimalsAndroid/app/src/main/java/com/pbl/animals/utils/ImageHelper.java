package com.pbl.animals.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.TypedValue;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageHelper {
    public static File currentFile;

    public static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getFilesDir();
        currentFile = new File(storageDir, imageFileName + ".jpg");
        return currentFile;
    }

    public static Bitmap getScaledBitmap(float toHeight) {
        Bitmap source = BitmapFactory.decodeFile(currentFile.getAbsolutePath());
        float toWidth = source.getWidth() * toHeight / source.getHeight();
        return Bitmap.createScaledBitmap(source, (int) toWidth,(int) toHeight, false);
    }

    public static Bitmap getScaledBitmap(Bitmap source, float toHeight) {
        float toWidth = source.getWidth() * toHeight / source.getHeight();
        return Bitmap.createScaledBitmap(source, (int) toWidth,(int) toHeight, false);
    }

    public static byte[] imageToByteArray()  {
        try {
            return FileUtils.readFileToByteArray(currentFile);
        } catch (IOException ioe) {
            Log.e("IMG_HELPER", "failed to convert image file to byte array");
            return null;
        }
    }


    public static float DpToPx(float dp, Context context) {
        return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                context.getResources().getDisplayMetrics()
        );
    }

}
