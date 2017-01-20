package com.theselflessapp.utility;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.theselflessapp.R;
import com.theselflessapp.activity.HomeActivity;
import com.theselflessapp.fragment.HomeFragment;
import com.theselflessapp.interfaces.Constant;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.RequestBody;
import okio.Buffer;

/**
 * Created by Sony on 10/8/2016.
 */

public class Utilities implements Constant {


    // Set Font TypeFace
    public static void setTypeFace(Context ctx, TextView tv) {
        Typeface tf = Typeface.createFromAsset(ctx.getAssets(), fontPath);
        tv.setTypeface(tf);
    }

    public static void setTypeFace2(Context ctx, TextView tv) {
        Typeface tf = Typeface.createFromAsset(ctx.getAssets(), fontPath2);
        tv.setTypeface(tf);
    }

    public static void setTypeFace3(Context ctx, TextView tv) {
        Typeface tf = Typeface.createFromAsset(ctx.getAssets(), fontPath3);
        tv.setTypeface(tf);
    }

    public static void setTypeFace(Context ctx, Button btn) {
        Typeface tf = Typeface.createFromAsset(ctx.getAssets(), fontPath);
        btn.setTypeface(tf);
    }

    public static void setTypeFace2(Context ctx, Button btn) {
        Typeface tf = Typeface.createFromAsset(ctx.getAssets(), fontPath2);
        btn.setTypeface(tf);
    }

    public static void setTypeFace(Context ctx, EditText edt) {
        Typeface tf = Typeface.createFromAsset(ctx.getAssets(), fontPath);
        edt.setTypeface(tf);
    }

    public static void setTypeFace2(Context ctx, EditText edt) {
        Typeface tf = Typeface.createFromAsset(ctx.getAssets(), fontPath2);
        edt.setTypeface(tf);
    }

    // Pass Next Activity Intent
    public static void passActivityIntent(Context ctx, Class nextActivity) {
        Intent intent = new Intent(ctx, nextActivity);

        if (nextActivity.getName().toString().equals(activityName2)) {
            intent.putExtra(SNAP_PIC, HomeFragment.getInstance().getImage());
            intent.putExtra(KEY_USER_ID, HomeFragment.getInstance().getUserId());
            intent.putExtra(KEY_IMAGE_TIME, HomeFragment.getInstance().getImageTime());
        }

        if (nextActivity.getName().toString().equals(activityName)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
        ctx.startActivity(intent);
    }

    /* Check Internet Available Or Not */
    public static boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    /* Show SnackBar */
    public static void showSnackBar(Context ctx, View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        View views = snackbar.getView();
        views.setBackgroundColor(ContextCompat.getColor(ctx, R.color.color_button));
        TextView tv = (TextView) views.findViewById(android.support.design.R.id.snackbar_text);
        setTypeFace(ctx, tv);
        tv.setTextColor(ContextCompat.getColor(ctx, R.color.md_white_1000));
        snackbar.show();
    }

    /* Show SnackBar With Call Back*/
    public static void showSnackBarWithCallBack(final Context ctx, View view, String message,
                                                final Class nextActivity) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        View views = snackbar.getView();
        views.setBackgroundColor(ContextCompat.getColor(ctx, R.color.color_button));
        TextView tv = (TextView) views.findViewById(android.support.design.R.id.snackbar_text);
        setTypeFace(ctx, tv);
        tv.setTextColor(ContextCompat.getColor(ctx, R.color.md_white_1000));
        snackbar.setCallback(new Snackbar.Callback() {

            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                Utilities.passActivityIntent(ctx, nextActivity);
                ((Activity) ctx).finish();
            }

            @Override
            public void onShown(Snackbar snackbar) {

            }
        });
        snackbar.show();
    }

    /* Show SnackBar With Call Back*/
    public static void showSnackBarWithCallBackLoadFragment(final Context ctx, View view, String message,
                                                            final int position) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        View views = snackbar.getView();
        views.setBackgroundColor(ContextCompat.getColor(ctx, R.color.color_button));
        TextView tv = (TextView) views.findViewById(android.support.design.R.id.snackbar_text);
        setTypeFace(ctx, tv);
        tv.setTextColor(ContextCompat.getColor(ctx, R.color.md_white_1000));
        snackbar.setCallback(new Snackbar.Callback() {

            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                HomeActivity.getInstance().loadFragment(position);
            }

            @Override
            public void onShown(Snackbar snackbar) {

            }
        });
        snackbar.show();
    }

    /* Show SnackBar For Internet */
    public static void showSnackBarInternet(Context ctx, View view, String message) {
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_SHORT)
                .setAction(ctx.getString(R.string.retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.e("Retry", "Retry");
                    }
                });

        // Changing message text color
        snackbar.setActionTextColor(Color.RED);

        // Changing action button text color
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(ctx, R.color.color_button));

        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        setTypeFace(ctx, textView);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    /* Get Path */
    public static String getPath(Context ctx, Uri uri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = ctx.getContentResolver().query(uri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            ;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;

    }

    /* Check Edit text is empty or not */
    public static boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    /* Check image path */
    public static boolean isImagePath(String path) {
        return path.trim().length() == 0;
    }

    /* Check Spinner is empty or not */
    public static boolean isSpinnerItemValid(Context ctx, Spinner spnr) {
        boolean isValid = false;
        if (spnr.getSelectedItem().toString().equals(ctx.getString(R.string.country_prompt))) {
            isValid = true;

        } else if (spnr.getSelectedItem().toString().equals(ctx.getString(R.string.select_gender))) {
            isValid = true;
        }
        return isValid;
    }

    /* Check Email is Valid or not */
    public final static boolean isValidEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /* Show Progress Dialog */
    public static void showProgressDialog(Context ctx, ProgressBar progressBar) {
        progressBar.setVisibility(View.VISIBLE);
        ((Activity) ctx).getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    /* Dismiss Progress Dialog */
    public static void dismissProgressDialog(Context ctx, ProgressBar progressBar) {
        progressBar.setVisibility(View.GONE);
        ((Activity) ctx).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

    }

    /* Get Image Bitmap */
    public static Bitmap getImageBitmap(String url) {
        Bitmap bitmap = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(getImageOrientation(url));
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
        return rotatedBitmap;
    }

    /* Get Image Rotation */
    public static int getImageOrientation(String imagePath) {
        int rotate = 0;
        try {

            File imageFile = new File(imagePath);
            ExifInterface exif = new ExifInterface(
                    imageFile.getAbsolutePath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rotate;
    }


    public static String getResponse(RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    /* Format Date From String */
    public static String formatDateFromString(String inputDate) {

        Date parsed = null;
        String outputDate = "";
        String inputFormat = "yyyy-MM-dd hh:mm:ss";
        String outputFormat = "MMMM yyyy";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        df_input.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());
        df_output.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            Log.e("ParseException", "ParseException??" + e.getMessage());
        }

        return outputDate;

    }

    /* Format Date From String */
    public static String formatDateFromString2(String inputDate) {

        Date parsed = null;
        String outputDate = "";


        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = dateFormat.parse(inputDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar currentCalDate = Calendar.getInstance();
        currentCalDate.setTime(date);

        String dayNumberSuffix = getDayNumberSuffix(currentCalDate.get(Calendar.DAY_OF_MONTH));

        String inputFormat = "yyyy-MM-dd hh:mm:ss";
        String outputFormat = "MMMM dd, yyyy";
        String outputFormat2 = "MMMM dd'" + dayNumberSuffix + "," + "' yyyy";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        df_input.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat2);
        df_output.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            Log.e("ParseException", "ParseException??" + e.getMessage());
        }

        return outputDate;
    }

    /* Format Date From String */
    public static String formatDateFromString3(String inputDate) {

        Date parsed = null;
        String outputDate = "";
        String inputFormat = "yyyy-MM-dd hh:mm:ss";
        String outputFormat = "hh:mmaa";

        SimpleDateFormat df_input = new SimpleDateFormat(inputFormat, java.util.Locale.getDefault());
        df_input.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat df_output = new SimpleDateFormat(outputFormat, java.util.Locale.getDefault());
        df_output.setTimeZone(TimeZone.getTimeZone("UTC"));

        try {
            parsed = df_input.parse(inputDate);
            outputDate = df_output.format(parsed);

        } catch (ParseException e) {
            Log.e("ParseException", "ParseException??" + e.getMessage());
        }

        return outputDate;

    }


    /* Format Date From String */
    public static String formatDateFromString4(String inputDate) {

        String outputDate = "";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = dateFormat.parse(inputDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        int x = 48;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.add(Calendar.HOUR, x);
        outputDate = formatDateFromString2(dateFormat.format(calendar.getTime()));

        return outputDate;

    }

    public static String getDayNumberSuffix(int day) {
        if (day >= 11 && day <= 13) {
            return "th";
        }
        switch (day % 10) {
            case 1:
                return "st";
            case 2:
                return "nd";
            case 3:
                return "rd";
            default:
                return "th";
        }
    }

    /* Show Setting Alert */
    public static void showSettingsAlert(final Context ctx) {
        Typeface tf = Typeface.createFromAsset(ctx.getAssets(), fontPath);
        Typeface tf2 = Typeface.createFromAsset(ctx.getAssets(), fontPath2);
        MaterialDialog.Builder builder = new MaterialDialog.Builder(ctx)
                .title(ctx.getString(R.string.settings))
                .content(ctx.getString(R.string.message_gps))
                .typeface(tf2, tf)
                .positiveText(ctx.getString(R.string.settingss))
                .negativeText(ctx.getString(R.string.cancel));

        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                Intent intent = new Intent(
                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                ctx.startActivity(intent);
                dialog.dismiss();

            }
        })

                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                });

        MaterialDialog dialog = builder.build();
        dialog.show();
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(),
                inImage, "Title", null);
        return Uri.parse(path);
    }

    public static String getRealPathFromURI(Context inContext, Uri uri) {
        Cursor cursor = inContext.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    /* Download File From URL */
    public static Bitmap downloadFile(Context ctx, String fileUrl) {
        URL myFileUrl = null;
        Bitmap bmImg = null;
        try {
            myFileUrl = new URL(fileUrl);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) myFileUrl
                    .openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            Log.i("im connected", "Download");
            bmImg = BitmapFactory.decodeStream(is);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        File filename;
        try {
            String path = Environment.getExternalStorageDirectory().toString();
            new File(path + "/mvc/mvc").mkdirs();
            filename = new File(path + "/mvc/mvc/var3.jpg");
            FileOutputStream out = new FileOutputStream(filename);
            bmImg.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            MediaStore.Images.Media.insertImage(ctx.getContentResolver(),
                    filename.getAbsolutePath(), filename.getName(),
                    filename.getName());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bmImg;
    }


    public static void downloadFile2(Context ctx, String uRl) {
        File direct = new File(Environment.getExternalStorageDirectory()
                + "/MemoriesGiven");

        if (!direct.exists()) {
            direct.mkdirs();
        }

        DownloadManager mgr = (DownloadManager) ctx.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(uRl);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);

        request.setAllowedNetworkTypes(
                DownloadManager.Request.NETWORK_WIFI
                        | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false).setTitle("Demo")
                .setDescription("Something useful. No, really.")
                .setDestinationInExternalPublicDir("/MemoriesGiven", System.currentTimeMillis() + ".jpg");

        mgr.enqueue(request);

    }
}
