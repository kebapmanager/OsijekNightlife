package com.androidapp.osijeknightlife.app.jsonDataP;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import com.androidapp.osijeknightlife.app.DropBox;
import com.google.gson.Gson;
import retrofit.ResponseCallback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.io.*;
import java.util.*;

/**
 * Created by Toni P on 6/15/2015.
 */
public class GetData {

    public interface Listener{
        public void dataRecieved(boolean state);
    }

    private Listener mListener = null;
    public void registerListener (Listener listener) {
        mListener = listener;
    }

    /////////////////////Class
    public boolean info = false;
    public boolean done = false;
    public String Status;
    public List<Bitmap> Slike = new ArrayList<Bitmap>();

    public Bitmap bitmap,mutable;
    public DataLoader data =  new DataLoader();
    public Gson gson =  new Gson();
    private RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint("https://api-content.dropbox.com")
            .build();
    String path,extrPath;
    int Si;

    public void Start(String path,final String dan)
    {
        this.path = path;
        DropBox DB = restAdapter.create(DropBox.class);
        DB.Download(path + dan + ".json", new ResponseCallback() {
            @Override
            public void success(Response response)  {

                String json = new String();
                try
                {
                    InputStream in = response.getBody().in();
                    BufferedReader r = new BufferedReader(new InputStreamReader(in));
                    StringBuilder total = new StringBuilder();

                    while ((json = r.readLine()) != null) {
                        total.append(json);
                    }
                    json = total.toString();
                }catch(Exception e){}

                data = gson.fromJson(json, DataLoader.class);
                Status = "Data Recieved\n";
                Status = "DONE";
                info = true;
                getImg(dan);
            }

            @Override
            public void failure(RetrofitError retrofitError)
            {
                System.out.println("Get data failed" + retrofitError.toString());
                Status = "Failed to Recieve Data\n";
            }
        });
    }
    public void getImg(final String dan)
    {
        DropBox DB = restAdapter.create(DropBox.class);
        Si = 0;
        DB.Download(path +dan+"_"+ (Si+1)+".jpg", new ResponseCallback()
        {
            @Override
            public void success(Response response)
            {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inMutable = false;
                try
                {
                    bitmap = BitmapFactory.decodeStream(response.getBody().in());
                }
                catch (Exception e) {}

                mutable = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                saveImg(mutable);
                Slike.add(Si,mutable);
                Si++;
                if (Si < data.getEvents().get(0).getPics().size())
                    moarImg(dan);
                else if (mListener != null && Si == data.getEvents().get(0).getPics().size()) {
                    mListener.dataRecieved(true);
                    done = true;
                }
            }
            @Override
            public void failure(RetrofitError error)
            {
                Status += "\nFailed to recieve Pic : " + dan + (Si + 1) + ".jpg" + "\n";
            }
        });
    }
    private void moarImg(final String dan)
    {
        DropBox DB = restAdapter.create(DropBox.class);

        DB.Download(path +dan+"_"+ (Si+1)+".jpg", new ResponseCallback()
        {
            @Override
            public void success(Response response)
            {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inMutable = false;
                try
                {
                    bitmap = BitmapFactory.decodeStream(response.getBody().in());
                }
                catch (Exception e) {}

                mutable = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                saveImg(mutable);

                Slike.add(Si,mutable);
                Si++;
                if (Si < data.getEvents().get(0).getPics().size())
                    moarImg(dan);
                else if (mListener != null && Si == data.getEvents().get(0).getPics().size()){
                     mListener.dataRecieved(true);
                }
            }
            @Override
            public void failure(RetrofitError error)
            {
                Status += "\nFailed to recieve Pic : " + dan + (Si + 1) + ".jpg" + "\n";
            }
        });
    }
    private void saveImg(Bitmap bmp)
    {
        try {
            String path = Environment.getExternalStorageDirectory().toString();
            extrPath = path;
            OutputStream fOut = null;
            File file = new File(path, "Img_" + Si + ".jpg"); // the File to save to
            fOut = new FileOutputStream(file);


            bmp.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
            fOut.flush();
            fOut.close();
        }catch(Exception e){}
    }
}