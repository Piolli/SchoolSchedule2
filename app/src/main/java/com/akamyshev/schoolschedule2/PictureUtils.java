package com.akamyshev.schoolschedule2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Environment;
import android.support.v4.graphics.BitmapCompat;
import android.util.Log;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by Alexandr on 02.08.2016.
 */
public class PictureUtils {

    public static GradientDrawable createGradient(int[] colors, GradientDrawable.Orientation orientation, float cornerRadius) {
        GradientDrawable gradientDrawable = new GradientDrawable(orientation, colors);
        gradientDrawable.setCornerRadius(cornerRadius);
        gradientDrawable.setGradientCenter(0, 0);

        return gradientDrawable;
    }

    public static Bitmap scaleBitmapToSize(final Bitmap bitmap, final int imageSizePx) {
        if (bitmap == null) {
            return null;
        }

        boolean isHeightFirst = false;
        float maxSize, minSize;
        if (bitmap.getWidth() > bitmap.getHeight()) {
            maxSize = bitmap.getWidth();
            minSize = bitmap.getHeight();
        } else {
            maxSize = bitmap.getHeight();
            minSize = bitmap.getWidth();
            isHeightFirst = true;
        }

        float scaleFactor = maxSize / minSize;
        if (maxSize > imageSizePx) {
            maxSize -= (maxSize - imageSizePx);
            minSize = imageSizePx / scaleFactor;
        }

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, isHeightFirst ? (int) minSize : (int) maxSize,
                isHeightFirst ? (int) maxSize : (int) minSize, false);
        bitmap.recycle();
        if (scaledBitmap != null) {
            return scaledBitmap;
        }
        return null;
//        Observable<Bitmap> bitmapObservable = Observable.create(new Observable.OnSubscribe<Bitmap>() {
//            @Override
//            public void call(Subscriber<? super Bitmap> subscriber) {
//                if(bitmap == null) {
//                    subscriber.onNext(null);
//                    subscriber.onCompleted();
//                    return;
//                }
//
//                boolean isHeightFirst = false;
//                float maxSize, minSize;
//                if(bitmap.getWidth() > bitmap.getHeight()) {
//                    maxSize = bitmap.getWidth();
//                    minSize = bitmap.getHeight();
//                }
//                else {
//                    maxSize = bitmap.getHeight();
//                    minSize = bitmap.getWidth();
//                    isHeightFirst = true;
//                }
//
//                float scaleFactor = maxSize / minSize;
//                if(maxSize > imageSizePx) {
//                    maxSize -= (maxSize - imageSizePx);
//                    minSize = imageSizePx / scaleFactor;
//                }
//
//                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, isHeightFirst ? (int)minSize : (int)maxSize,
//                        isHeightFirst ? (int)maxSize : (int)minSize, false);
//                bitmap.recycle();
//                if(scaledBitmap != null) {
//                    subscriber.onNext(scaledBitmap);
//                    subscriber.onCompleted();
//                }
//            }
//        });
//
//        bitmapObservable.subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public synchronized static void asyncSaveBitmapToLocalStorage(final Bitmap bitmapS, final OnSavePictureCallback callback) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                File sdr = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/SchoolTimetable");
                Log.d("SaveBITMAP", sdr.getPath());
                if(!sdr.mkdir() && !sdr.exists()) {
                    callback.onSaveError("Невозможно создать папку");
                    return;
                }

                File file = new File(sdr, String.valueOf(bitmapS.hashCode()) + ".png");
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                    Log.d("SaveBITMAP", String.valueOf(BitmapCompat.getAllocationByteCount(bitmapS)));
                    if(bitmapS.compress(Bitmap.CompressFormat.PNG, 100, fos))
                        callback.onSaveCompleted("Pictures/SchoolTimetable/"+file.getName());

                } catch (IOException e) {
                    e.printStackTrace();
                    callback.onSaveError(e.getMessage());
                }
                finally {
                    if(fos != null)
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            callback.onSaveError(e.getMessage());
                        }
                }
            }
        });
        thread.start();
    }

    public enum SIZE_PX {
        NEWS, PHOTO_VIEW;

        int getSize() {
            if(name().equals("NEWS"))
                return 600;
            if(name().equals("PHOTO_VIEW"))
                return 1200;
            return 600;
        }
    }


    public static int getComplimentColor(int color) {
        // get existing colors
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int blue = Color.blue(color);
        int green = Color.green(color);

        // find compliments
        red = (~red) & 0xff;
        blue = (~blue) & 0xff;
        green = (~green) & 0xff;

        return Color.argb(alpha, red, green, blue);
    }

//    public static int getRGBColorofImage(Bitmap bitmap) {
//        if(bitmap == null)
//            return 0;
//        Palette.Swatch swatch = Palette.from(bitmap).generate().getDarkMutedSwatch();
//        if(swatch != null)
//            swatch.getRgb();
//        return 0;
//    }

    public static int setBrightnessColor(int color, float factor){
        float r = Color.red(color)*factor;
        float g = Color.green(color)*factor;
        float b = Color.blue(color)*factor;
        int ir = Math.min(255,(int)r);
        int ig = Math.min(255,(int)g);
        int ib = Math.min(255,(int)b);
        int ia = Color.alpha(color);
        return(Color.argb(ia, ir, ig, ib));
    }

    public interface OnSavePictureCallback {
        void onSaveCompleted(String pathFile);
        void onSaveError(String error);
    }
}
