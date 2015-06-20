package com.androidapp.osijeknightlife.app.jsonDataP;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import com.androidapp.osijeknightlife.app.DropBox;
import com.google.gson.Gson;
import retrofit.ResponseCallback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    public boolean done = false;
    public String Status;
    public List<List<Bitmap>> Slike = new ArrayList<List<Bitmap>>();

    public Bitmap bitmap,mutable;
    public DataLoader data =  new DataLoader();
    public Gson gson =  new Gson();
    private RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint("https://api-content.dropbox.com")
            .build();
    String path;
    int Si = -1;

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
                done =  true;

                Slike.add(0, new ArrayList<Bitmap>());
                getImg(0, dan);
            }

            @Override
            public void failure(RetrofitError retrofitError)
            {
                System.out.println("Get data failed");
                Status = "Failed to Recieve Data\n";
            }
        });
    }
    public void getImg(final int i,final String dan)
    {
        DropBox DB = restAdapter.create(DropBox.class);
        Si++;
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

                System.out.println("Dan: "+dan+"_"+ (Si+1));
                Slike.get(0).add(Si,mutable);

                if (Si < data.getEvents().get(i).getPics().size())
                    getImg(i, dan);

                if (mListener != null)
                    mListener.dataRecieved(true);
            }
            @Override
            public void failure(RetrofitError error)
            {
                Status += "\nFailed to recieve Pic : " + dan + (Si + 1) + ".jpg" + "\n";
            }
        });
    }
}