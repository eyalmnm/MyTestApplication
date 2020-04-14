package tests.em_projects.com.mytestapplication.utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

/**
 * Created by eyalmuchtar on 09/10/2017.
 */

public class FileUtils {
    private static final String TAG = "FileUtils";
    private static final String fSa = File.separator;
    private static String[] imageNameExtensions = {".png", ".jpg", ".gif", ".bmp"};

    public static ArrayList<String> getDirectories(String path) {
        File rootDir = new File(path);
        if (true == rootDir.exists() && rootDir.isDirectory()) {
            ArrayList<String> subDirs = new ArrayList<>();
            File[] subs = rootDir.listFiles();
            for (File sub : subs) {
                if (true == sub.isDirectory()) {
                    subDirs.add(sub.getName());
                }
            }
            return subDirs;
        } else {
            return null;
        }
    }

    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static File getFile(Context context, Uri uri) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "TEMP" + File.separator, "_" + uri.getLastPathSegment());
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        file.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file, false);
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        int total = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, len);
            total += len;
        }
        fileOutputStream.close();
        inputStream.close();
        return file;
    }

    public static Bitmap loadBitmap(String url) {
        if (true == StringUtils.isNullOrEmpty(url)) return null;
        Bitmap bm = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        try {
            URLConnection conn = new URL(url).openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is, 8192);
            bm = BitmapFactory.decodeStream(bis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != bis) {
                try {
                    bis.close();
                } catch (IOException e) {
                    Log.e(TAG, "loadBitmap", e);
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    Log.e(TAG, "loadBitmap", e);
                }
            }
        }
        return bm;
    }

    public static void findDirectories(File dir, String name, ArrayList<String> names) {
        if (true == dir.isDirectory()) {
            names.add(name);
            Log.d(TAG, "dir name: " + name);
            File[] files = dir.listFiles();
            if (null == files || 0 == files.length) return;
            for (File file : files) {
                findDirectories(file, name + fSa + file.getName(), names);
            }
        }
    }

    public static void writeBitmapToFile(String dir, String fileName, byte[] imageData) {
        File root = new File(dir);
        boolean resulst = false;
        if (!root.exists()) {
            resulst = root.mkdir();
        }
        Log.d(TAG, "resulst " + resulst);
        File bitmapFile = new File(root, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(bitmapFile);
            fos.write(imageData);
            fos.close();
        } catch (Exception e) {
            Log.e(TAG, "writeToLogFile", e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void writeBitmapToFile(String dir, String fileName, Bitmap bitmap) {
        File root = new File(dir);
        boolean resulst = false;
        if (!root.exists()) {
            resulst = root.mkdir();
        }
        Log.d(TAG, "resulst " + resulst);
        File bitmapFile = new File(root, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(bitmapFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            Log.e(TAG, "writeToLogFile", e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Bitmap readBitmapFromFile(String dir, String fileName) {
        Bitmap bitmap = null;
        File root = new File(dir);
        boolean resulst = false;
        if (!root.exists()) {
            return null;
        }
        Log.d(TAG, "resulst " + resulst);
        File bitmapFile = new File(root, fileName);
        if (!bitmapFile.exists()) {
            return null;
        }
        try {
            bitmap = BitmapFactory.decodeStream(new FileInputStream(bitmapFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    // Decodes image and scales it to reduce memory consumption
    public static Bitmap decodeFile(File f, int size) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // The new size we want to scale to
            final int REQUIRED_SIZE = size;

            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
            return bitmap;
        } catch (FileNotFoundException e) {
            FirebaseCrash.logcat(Log.ERROR, TAG, "decodeFile");
            FirebaseCrash.report(e);
        } catch (OutOfMemoryError e) {
            FirebaseCrash.logcat(Log.ERROR, TAG, "decodeFile");
            FirebaseCrash.report(e);
        } catch (Throwable e) {
            FirebaseCrash.logcat(Log.ERROR, TAG, "decodeFile");
            FirebaseCrash.report(e);
        }
        return null;
    }

    public static boolean containsImages(String dirName) throws Exception {
        File dirFile = new File(dirName);
        if (dirFile.exists()) {
            File[] files = dirFile.listFiles();
            for (File file : files) {
                if (true == isImageFile(file)) {
                    return true;
                }
            }
            return false;
        } else {
            throw new Exception("Unknown directory " + dirName);
        }
    }

    public static boolean isImageFile(File file) {
        String fileName = file.getName();
        for (String imgExt : imageNameExtensions) {
            if (true == StringUtils.containsIgnureCase(fileName, imgExt)) {
                return true;
            }
        }
        return false;
    }


    public static boolean removeFile(String fileName) {
        File file = new File(fileName);
        file.delete();
        return true;
    }
}
