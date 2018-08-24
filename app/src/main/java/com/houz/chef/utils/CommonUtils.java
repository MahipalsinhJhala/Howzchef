package com.houz.chef.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Base64OutputStream;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.houz.chef.R;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CommonUtils {
    //public static final String URL_STORAGE_REFERENCE = "gs://gatecrasher-partner.appspot.com";
    //public static final String FOLDER_STORAGE_IMG = "images";

    public static String local(String latitudeFinal,String longitudeFinal){
        return "https://maps.googleapis.com/maps/api/staticmap?center="+latitudeFinal+","+longitudeFinal+"&zoom=18&size=280x280&markers=color:red|"+latitudeFinal+","+longitudeFinal;
    }

    public static String getBase64StringFromBitmap(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    // Converting File to Base64.encode String type using Method
    public static String getBase64StringFromFile(File f) {
        InputStream inputStream = null;
        String encodedFile= "", lastVal;
        try {
            inputStream = new FileInputStream(f.getAbsolutePath());

            byte[] buffer = new byte[10240];//specify the size to allow
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }
            output64.close();
            encodedFile =  output.toString();
        }
        catch (FileNotFoundException e1 ) {
            e1.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        lastVal = encodedFile;
        return lastVal;
    }

    public static Boolean checkEmail(String email) {
        Pattern p = Pattern.compile("^([a-zA-Z0-9_.-])+@([a-zA-Z0-9_])+(\\.([a-zA-Z]{2,4}+))?(\\.([a-zA-Z]{2,4}))$");
        Matcher m = p.matcher(email);
        return !m.matches();
    }

    public static boolean checkFloat(String input) {
        Pattern p = Pattern.compile(
                "[\\x00-\\x20]*[+-]?(NaN|Infinity|((((\\p{Digit}+)(\\.)?((\\p{Digit}+)?)" +
                        "([eE][+-]?(\\p{Digit}+))?)|(\\.((\\p{Digit}+))([eE][+-]?(\\p{Digit}+))?)|" +
                        "(((0[xX](\\p{XDigit}+)(\\.)?)|(0[xX](\\p{XDigit}+)?(\\.)(\\p{XDigit}+)))" +
                        "[pP][+-]?(\\p{Digit}+)))[fFdD]?))[\\x00-\\x20]*");
        Matcher m = p.matcher(input);
        return !m.matches();
    }

    public static boolean checkYoutubeUrl(String inputurl) {
        Pattern p = Pattern.compile("^(https?\\:\\/\\/)?(www\\.)?(youtube\\.com|youtu\\.?be)\\/.+$");
        Matcher m = p.matcher(inputurl);
        return m.matches();
    }

    public static void toast(Context context, String msg) {
        try {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void toast(Context context, int msg) {
        try {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isInternetOn(Context context) {
        final boolean[] haveConnectedWifi = {false};
        final boolean[] haveConnectedMobile = {false};

        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService
                    (Context.CONNECTIVITY_SERVICE);
            NetworkInfo[] netInfo = cm.getAllNetworkInfo();
            for (NetworkInfo ni : netInfo) {
                if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                    if (ni.isConnected())
                        haveConnectedWifi[0] = true;
                if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                    if (ni.isConnected())
                        haveConnectedMobile[0] = true;
            }
        } catch (Exception e) {
            haveConnectedWifi[0] = true;
            e.printStackTrace();
        }
        return haveConnectedWifi[0] || haveConnectedMobile[0];

    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static Map<String, RequestBody> converRequestBodyFromMap(Map<String, String> map) {
        Map<String, RequestBody> map1 = new HashMap<>();
        int size = map.size();
        for (int i = 0; i < size; i++) {
            try {
                String key = (String) map.keySet().toArray()[i];
                RequestBody value = RequestBody.create(MediaType.parse("text/plain"), map.get(key));
                map1.put(key, value);
            } catch (IndexOutOfBoundsException ignored) {
            }
        }
        return map1;
    }

    public static RequestBody convertRequestBodyFromJson(JSONObject jsonObject){
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), (jsonObject).toString());
    }

    public static ArrayList<MultipartBody.Part> converRequestBodyFromMapImage(Map<String, File> map, String[] type) {
        ArrayList<MultipartBody.Part> map1 = new ArrayList<>();
        int size = map.size();
        RequestBody requestFile = null;

        for (int i = 0; i < size; i++) {
            try {
                String key = (String) map.keySet().toArray()[i];
                switch (type[i]) {
                    case "1":
                        requestFile = RequestBody.create(MediaType.parse("video/mp4"), map.get(key));
                        break;

                    case "2":
                        requestFile = RequestBody.create(MediaType.parse("image/*"), map.get(key));
                        break;

                    case "3":
                        requestFile = RequestBody.create(MediaType.parse("audio/m4a"), map.get(key));
                        break;

                    case "4":
                        requestFile = RequestBody.create(MediaType.parse("audio/m4a"), map.get(key));
                        break;
                }
                MultipartBody.Part body = MultipartBody.Part.createFormData(key, map.get(key).getName(), requestFile);
                map1.add(body);
            } catch (IndexOutOfBoundsException ignored) {
            }
        }
        return map1;
    }

    public static void showSettingsAlert(final Activity activity) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);

        // Setting Dialog Title
        alertDialog.setTitle("GPS settings");

        // Setting Dialog Message
        alertDialog.setMessage(activity
                .getString(R.string.msg_gps_setting_prompt));

        // On Pressing Setting button
        alertDialog.setPositiveButton("Enable",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        activity.startActivityForResult(intent, Const.GPS_ENABLE);
                    }
                });

        // On pressing cancel button
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();
    }

    public static boolean canGetLocation(Context context) {
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);

        // getting GPS status
        boolean isGPSEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);


        return isGPSEnabled;
    }

//    public static String GetTimeZoneValue(List<CoutryCodeBean> loginBean){
//        String time = "";
//        if(loginBean != null && loginBean.size() > 0 &&
//                loginBean.get(0).getTimezones() != null && loginBean.get(0).getTimezones().size() > 0
//                && loginBean.get(0).getTimezones().get(0)!=null)
//            time = loginBean.get(0).getTimezones().get(0).replaceAll("[^\\d:+]", "");
//        else
//            time = StaticField.time;
//        return time;
//    }
//
//    public static String getCurrentDate() {
//        Calendar calendar = Calendar.getInstance();
//        SimpleDateFormat mdFormat = new SimpleDateFormat("EEEE dd MMM yyyy", Locale.getDefault());
//        return mdFormat.format(calendar.getTime());
//    }


    /* Get uri related content real local file path. */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getUriRealPath(Context ctx, Uri uri)
    {
        String ret = "";

        if( isAboveKitKat() )
        {
            // Android OS above sdk version 19.
            ret = getUriRealPathAboveKitkat(ctx, uri);
        }else
        {
            // Android OS below sdk version 19
            ret = getImageRealPath(ctx.getContentResolver(), uri, null);
        }

        return ret;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static String getUriRealPathAboveKitkat(Context ctx, Uri uri)
    {
        String ret = "";

        if(ctx != null && uri != null) {

            if(isContentUri(uri))
            {
                if(isGooglePhotoDoc(uri.getAuthority()))
                {
                    ret = uri.getLastPathSegment();
                }else {
                    ret = getImageRealPath(ctx.getContentResolver(), uri, null);
                }
            }else if(isFileUri(uri)) {
                ret = uri.getPath();
            }else if(isDocumentUri(ctx, uri)){

                // Get uri related document id.
                String documentId = DocumentsContract.getDocumentId(uri);

                // Get uri authority.
                String uriAuthority = uri.getAuthority();

                if(isMediaDoc(uriAuthority))
                {
                    String idArr[] = documentId.split(":");
                    if(idArr.length == 2)
                    {
                        // First item is document type.
                        String docType = idArr[0];

                        // Second item is document real id.
                        String realDocId = idArr[1];

                        // Get content uri by document type.
                        Uri mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        if("image".equals(docType))
                        {
                            mediaContentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        }else if("video".equals(docType))
                        {
                            mediaContentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        }else if("audio".equals(docType))
                        {
                            mediaContentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        }

                        // Get where clause with real document id.
                        String whereClause = MediaStore.Images.Media._ID + " = " + realDocId;

                        ret = getImageRealPath(ctx.getContentResolver(), mediaContentUri, whereClause);
                    }

                }else if(isDownloadDoc(uriAuthority))
                {
                    // Build download uri.
                    Uri downloadUri = Uri.parse("content://downloads/public_downloads");

                    // Append download document id at uri end.
                    Uri downloadUriAppendId = ContentUris.withAppendedId(downloadUri, Long.valueOf(documentId));

                    ret = getImageRealPath(ctx.getContentResolver(), downloadUriAppendId, null);

                }else if(isExternalStoreDoc(uriAuthority))
                {
                    String idArr[] = documentId.split(":");
                    if(idArr.length == 2)
                    {
                        String type = idArr[0];
                        String realDocId = idArr[1];

                        if("primary".equalsIgnoreCase(type))
                        {
                            ret = Environment.getExternalStorageDirectory() + "/" + realDocId;
                        }
                    }
                }
            }
        }

        return ret;
    }

    /* Check whether current android os version is bigger than kitkat or not. */
    private static boolean isAboveKitKat()
    {
        boolean ret = false;
        ret = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        return ret;
    }

    /* Check whether this uri represent a document or not. */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static boolean isDocumentUri(Context ctx, Uri uri)
    {
        boolean ret = false;
        if(ctx != null && uri != null) {
            ret = DocumentsContract.isDocumentUri(ctx, uri);
        }
        return ret;
    }

    /* Check whether this uri is a content uri or not.
    *  content uri like content://media/external/images/media/1302716
    *  */
    private static boolean isContentUri(Uri uri)
    {
        boolean ret = false;
        if(uri != null) {
            String uriSchema = uri.getScheme();
            if("content".equalsIgnoreCase(uriSchema))
            {
                ret = true;
            }
        }
        return ret;
    }

    /* Check whether this uri is a file uri or not.
    *  file uri like file:///storage/41B7-12F1/DCIM/Camera/IMG_20180211_095139.jpg
    * */
    private static boolean isFileUri(Uri uri)
    {
        boolean ret = false;
        if(uri != null) {
            String uriSchema = uri.getScheme();
            if("file".equalsIgnoreCase(uriSchema))
            {
                ret = true;
            }
        }
        return ret;
    }


    /* Check whether this document is provided by ExternalStorageProvider. */
    private static boolean isExternalStoreDoc(String uriAuthority)
    {
        boolean ret = false;

        if("com.android.externalstorage.documents".equals(uriAuthority))
        {
            ret = true;
        }

        return ret;
    }

    /* Check whether this document is provided by DownloadsProvider. */
    private static boolean isDownloadDoc(String uriAuthority)
    {
        boolean ret = false;

        if("com.android.providers.downloads.documents".equals(uriAuthority))
        {
            ret = true;
        }

        return ret;
    }

    /* Check whether this document is provided by MediaProvider. */
    private static boolean isMediaDoc(String uriAuthority)
    {
        boolean ret = false;

        if("com.android.providers.media.documents".equals(uriAuthority))
        {
            ret = true;
        }

        return ret;
    }

    /* Check whether this document is provided by google photos. */
    private static boolean isGooglePhotoDoc(String uriAuthority)
    {
        boolean ret = false;

        if("com.google.android.apps.photos.content".equals(uriAuthority))
        {
            ret = true;
        }

        return ret;
    }

    /* Return uri represented document file real local path.*/
    private static String getImageRealPath(ContentResolver contentResolver, Uri uri, String whereClause)
    {
        String ret = "";

        // Query the uri with condition.
        Cursor cursor = contentResolver.query(uri, null, whereClause, null, null);

        if(cursor!=null)
        {
            boolean moveToFirst = cursor.moveToFirst();
            if(moveToFirst)
            {

                // Get columns name by uri type.
                String columnName = MediaStore.Images.Media.DATA;

                if( uri== MediaStore.Images.Media.EXTERNAL_CONTENT_URI )
                {
                    columnName = MediaStore.Images.Media.DATA;
                }else if( uri==MediaStore.Audio.Media.EXTERNAL_CONTENT_URI )
                {
                    columnName = MediaStore.Audio.Media.DATA;
                }else if( uri==MediaStore.Video.Media.EXTERNAL_CONTENT_URI )
                {
                    columnName = MediaStore.Video.Media.DATA;
                }

                // Get column index.
                int imageColumnIndex = cursor.getColumnIndex(columnName);

                // Get column value which is the uri related file local path.
                ret = cursor.getString(imageColumnIndex);
            }
        }

        return ret;
    }
}