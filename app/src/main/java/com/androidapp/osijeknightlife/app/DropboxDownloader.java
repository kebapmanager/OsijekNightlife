package com.androidapp.osijeknightlife.app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import retrofit.ResponseCallback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Toni P on 5/30/2015.
 */
public class DropboxDownloader extends ActionBarActivity {

    public Bitmap bitmap;
    public File dir;
    String ret;

    public String DBCall()
    {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://api-content.dropbox.com")
                .build();
        DropBox DB = restAdapter.create(DropBox.class);

        DB.Download("/cold_mclelun.jpg", new ResponseCallback() {
            @Override
            public void success(Response response) {

                System.out.print(response.getReason());
                ret = response.getReason();
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inMutable = true;
                try {
                    bitmap = BitmapFactory.decodeStream(response.getBody().in());
                } catch (Exception e) {
                }
                SaveBitmap();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                System.out.print(retrofitError);
                ret = retrofitError.toString();
            }
        });

        return ret;
    }
    public String SaveBitmap()
    {
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath();
        try {
            dir = new File(file_path);
            if (!dir.exists())
                dir.mkdirs();
            File file = new File(dir, "sketchpad.png");
            FileOutputStream fOut = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        }catch (Exception e){}
        finally {
            return file_path;
        }
    }
}
