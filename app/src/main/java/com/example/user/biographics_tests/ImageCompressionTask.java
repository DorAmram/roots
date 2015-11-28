//package com.tasksforce.honeydo.utils;
package com.example.user.biographics_tests;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;

//package com.example.user.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageCompressionTask extends AsyncTask<String, Void, String> {

    private OnImageCompressionListener mListener;
    private ImageLoadingUtils mPhotoUtils;

    public ImageCompressionTask(OnImageCompressionListener listener,
                                Context context) {
        this.mListener = listener;
        mPhotoUtils = new ImageLoadingUtils(context);
    }

    private static String getRealPathFromURI(String contentURI, Context context) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = context.getContentResolver().query(contentUri, null,
                null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor
                    .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static String getKitKatImagePath(Uri originalUri, Context context) {

        String path = null;

        // get the id of the image selected by the user
        String wholeID = DocumentsContract.getDocumentId(originalUri);
        String id = wholeID.split(":")[1];

        String[] projection = {MediaStore.Images.Media.DATA};
        String whereClause = MediaStore.Images.Media._ID + "=?";
        Cursor cursor = context.getContentResolver().query(getUri(),
                projection, whereClause, new String[]{id}, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            if (cursor.moveToFirst()) {
                path = cursor.getString(column_index);
            }

            cursor.close();
        }

        return path;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getImagePath(Uri originalUri, Context context, String realPath) {
        String path;

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            path = getRealPathFromURI(realPath, context);
        } else {
            path = getKitKatImagePath(originalUri, context);
        }

        return path;
    }

    private static Uri getUri() {
        String state = Environment.getExternalStorageState();
        if (!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
            return MediaStore.Images.Media.INTERNAL_CONTENT_URI;

        return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    }

    @Override
    protected void onPreExecute() {
        if (mListener != null) {
            mListener.onImageCompressionStart();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String filePath = compressImage(params[0]);
        return filePath;
    }

    @Override
    protected void onPostExecute(String result) {
        mListener.onImageCompressionSuccess(result);
    }

    private String compressImage(String imageUri) {

        String filePath = imageUri;

        Bitmap scaledBitmap = null;
        Bitmap bmp = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float maxHeight = 816.0f;   // height
        float maxWidth = 612.0f;   // width
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

        options.inSampleSize = mPhotoUtils.calculateInSampleSize(options,
                actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight,
                    Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2,
                middleY - bmp.getHeight() / 2, new Paint(
                        Paint.FILTER_BITMAP_FLAG));

        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;
    }

    private String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory()
                .getPath(), "Roots/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/"
                + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    public interface OnImageCompressionListener {

        void onImageCompressionStart();

        void onImageCompressionSuccess(String result);

        void onImageCompressionFailed();

    }

    private class ImageLoadingUtils {

        public static final String EXTERNAL_STORAGE_DIR_IMAGES_RELATIVE_PATH = "Roots/Images";
        public Bitmap icon;
        private Context context;

        public ImageLoadingUtils(Context context) {
            this.context = context;
            icon = BitmapFactory.decodeResource(context.getResources(),
                    android.R.drawable.btn_star);
        }

        public int convertDipToPixels(float dips) {
            Resources r = context.getResources();
            return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    dips, r.getDisplayMetrics());
        }

        public int calculateInSampleSize(BitmapFactory.Options options,
                                         int reqWidth, int reqHeight) {
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {
                final int heightRatio = Math.round((float) height
                        / (float) reqHeight);
                final int widthRatio = Math.round((float) width / (float) reqWidth);
                inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            }
            final float totalPixels = width * height;
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }

            return inSampleSize;
        }

        public Bitmap decodeBitmapFromPath(String filePath) {
            Bitmap scaledBitmap = null;

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            options.inSampleSize = calculateInSampleSize(options,
                    convertDipToPixels(150), convertDipToPixels(200));
            options.inDither = false;
            options.inPurgeable = true;
            options.inInputShareable = true;
            options.inJustDecodeBounds = false;

            scaledBitmap = BitmapFactory.decodeFile(filePath, options);
            return scaledBitmap;
        }

        public File getExternalStorageDir() {
            File storageDir = new File(Environment.getExternalStorageDirectory()
                    .getPath(), EXTERNAL_STORAGE_DIR_IMAGES_RELATIVE_PATH);
            return storageDir;
        }


        public File searchFileByPath(String path) {
            File storageDir = new File(Environment.getExternalStorageDirectory()
                    .getPath(), "HoneyDo/Images");

            for (File f : storageDir.listFiles()) {
                if (f.getAbsolutePath().equals(path)) {
                    return f;
                }
            }

            return null;
        }
    }

}
