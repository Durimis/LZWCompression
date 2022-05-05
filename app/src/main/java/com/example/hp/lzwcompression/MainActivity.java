package com.example.hp.lzwcompression;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import org.apache.commons.io.FileUtils;

import AlgorithmClasses.Deflate;
import AlgorithmClasses.LZW;


public class MainActivity extends AppCompatActivity {
    public static   Integer  GALLERY_INTENT_CALLED = 1;
    public static   Integer  GALLERY_KITKAT_INTENT_CALLED = 2;
    LZW lzw;
    Deflate deflate;
    Button button_compress_lzw, button_compress_deflate, button_decompress_deflate, button_chose_file, button_compress_hauffman;
    File input_file, output_file;
    String input_file_path, input_file_name;
    TextView txtKompresuar, txtPaKomrpesuar, txtKohezgjatja;
    EditText editPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_compress_lzw = (Button)findViewById(R.id.button_compress_lzw);
        button_compress_deflate = (Button)findViewById(R.id.button_compress_deflate);
        button_decompress_deflate = (Button)findViewById(R.id.button_decompress_deflate);
        button_compress_hauffman = (Button)findViewById(R.id.button_compress_hauffman);
        button_chose_file  = (Button)findViewById(R.id.button_chose_file);
        editPath = (EditText)findViewById(R.id.editText_path);
        deflate = new Deflate();
        input_file = null;
        txtKompresuar = (TextView)findViewById(R.id.textView_kompresuar);
        txtPaKomrpesuar = (TextView)findViewById(R.id.textView_pakomresuar);
        txtKohezgjatja = (TextView) findViewById(R.id.textView_kohezgjatja);

        button_compress_lzw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    output_file = new File( "/mnt/sdcard",
                            input_file_name + ".lzw");
                    lzw = new LZW(new FileInputStream(input_file),new FileOutputStream(output_file));
                    lzw.compress();
                    txtPaKomrpesuar.setText(Objects.toString(input_file.length()));
                    txtKompresuar.setText(Objects.toString(output_file.length()));
                    txtKohezgjatja.setText(Objects.toString((lzw.endTime - lzw.startTime)/ (double) 1000) + "  Mikro sekonda");
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {

                }
            }
        });

        button_compress_deflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    output_file = new File( "/mnt/sdcard",
                            input_file_name +".deflate");


                   // deflate.compressData(bytes, new FileOutputStream(output_file));

                    deflate.compressFromFile(input_file, FileUtils.openOutputStream(output_file));
                    txtPaKomrpesuar.setText(Objects.toString(input_file.length()));
                    txtKompresuar.setText(Objects.toString(output_file.length()));
                    txtKohezgjatja.setText(Objects.toString((deflate.endTime - deflate.startTime)/ (double) 1000) + "  Mikro sekonda");

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {

                }
                }
        });

        button_compress_hauffman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    output_file = new File( "/mnt/sdcard",
                            input_file_name +".deflate");


                    // deflate.compressData(bytes, new FileOutputStream(output_file));

                    deflate.compressHauffmanFromFile(input_file, FileUtils.openOutputStream(output_file));
                    txtPaKomrpesuar.setText(Objects.toString(input_file.length()));
                    txtKompresuar.setText(Objects.toString(output_file.length()));
                    txtKohezgjatja.setText(Objects.toString((deflate.endTime - deflate.startTime)/ (double) 1000) + "  Mikro sekonda");

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {

                }
            }
        });

        button_decompress_deflate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{

                    input_file = new File( "/mnt/shared/s4",
                            "1.deflate");
                    output_file = new File( "/mnt/shared/s4",
                            "1_de.pdf");

                    InputStream is = deflate.getUncompressedDataInputStream(new FileInputStream( input_file));
                    FileUtils.copyInputStreamToFile(is, output_file);
                    txtPaKomrpesuar.setText(Objects.toString(input_file.length()));
                    txtKompresuar.setText(Objects.toString(output_file.length()));
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {

                }
            }
        });
        button_chose_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivityForResult(Intent.createChooser(new Intent(Intent.ACTION_GET_CONTENT).setType("*/*"), "Zgjidh  fajllin"), 1);
            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {


            input_file_path = getPath(getApplicationContext(),data.getData());
            input_file = new File(input_file_path);
            if(input_file_path != null) {
                input_file_name = input_file_path.substring(input_file_path.lastIndexOf("/") + 1);
            }

            editPath.setText(input_file_path);
           // Toast.makeText(getApplicationContext(), "Keni zgjedhu fajllin: " + input_file_path,Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Asnje fajll nuk është zgjedhur", Toast.LENGTH_LONG).show();
        }

    }




/*
    @SuppressLint("NewApi")
    private String getPath(Uri uri) {
        if( uri == null ) {
            return null;
        }

        String[] projection = { MediaStore.MediaColumns.DATA };
        String path = null;
        Cursor cursor;
        if(Build.VERSION.SDK_INT > 19)
        {
            // Will return "image:x*"
            String wholeID = DocumentsContract.getDocumentId(uri);
            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];
            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";

            cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection, sel, new String[]{ id }, null);
            try
            {
                int column_index = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();
                path = cursor.getString(column_index).toString();
                cursor.close();
            }
            catch(NullPointerException e) {

            }
        }
        else
        {// Get the cursor
            cursor = getContentResolver().query(uri,projection, null, null, null);
            // Move to first row
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(projection[0]);
            path = cursor.getString(columnIndex);
            cursor.close();

        }


        return path;
    }
*/
    public String getPathTest  (Uri uri){
        String[] projection = { MediaStore.Images.Media.DATA };
        String path = null;
        Cursor cursor;
        cursor = getContentResolver().query(uri,projection, null, null, null);
        // Move to first row
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(projection[0]);
        path = cursor.getString(columnIndex);
        cursor.close();
        return path;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static String getPath(final Context context, final Uri uri) {

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
                final String[] selectionArgs = new String[] {
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

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
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
}
