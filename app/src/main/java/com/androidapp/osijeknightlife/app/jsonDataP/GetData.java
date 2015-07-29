package com.androidapp.osijeknightlife.app.jsonDataP;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.androidapp.osijeknightlife.app.DropBox;
import com.google.gson.Gson;
import retrofit.ResponseCallback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Created by Toni P on 6/15/2015.
 */
public class GetData {

    public interface Listener{
        public void dataRecieved(boolean state);
        public void errorReport(RetrofitError error);
    }
    private Listener mListener = null;
    public void registerListener (Listener listener) {
        mListener = listener;
    }

    /////////////////////Class
    public boolean info = false;
    public boolean done = false;
    public int CheckDate;
    public String Status;
    public List<Bitmap> pictures = new ArrayList<Bitmap>();

    public Bitmap bitmap,mutable;
    public DataLoader data =  new DataLoader();
    public Gson gson =  new Gson();
    private RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint("https://api-content.dropbox.com")
            .build();
    private RestAdapter metarestAdapter = new RestAdapter.Builder()
            .setEndpoint("https://api.dropbox.com")
            .build();
    String path,extrPath;
    int Si;
    public Bitmap[][] Slike = new Bitmap[20][5];
    public String MetaData;
    Activity mainActivity;
    public GetData(Activity mainActivity){this.mainActivity = mainActivity;}

    public void Start(String Path,final int dan)
    {

        this.path = "1/files/auto/"+Path;
        CheckDate = dan;
        data = new DataLoader();

        final DropBox DB = restAdapter.create(DropBox.class);
        pictures.add(getImageByName("bastion.png",mainActivity));
        DB.Download(path+"/Event.json", new ResponseCallback() {
            @Override
            public void success(Response response)  {
            if(CheckDate == dan) {
                String json = new String();
                try {
                    InputStream in = response.getBody().in();
                    BufferedReader r = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                    StringBuilder total = new StringBuilder();

                    while ((json = r.readLine()) != null) {
                        total.append(json);
                    }
                    json = total.toString();
                } catch (Exception e) {
                }

                data = gson.fromJson(json, DataLoader.class);
                Status = "Data Recieved\n";
                Status = "DONE";
                info = true;
                //mListener.dataRecieved(true);
                System.out.println("retrofit Sucess");
                for (int i = 0; i < data.getEvents().size(); i++)
                    if (data.getEvents().get(i).getPics().size() != 0) {
                        getImg(i, true);
                        break;
                    }
                //else if(data.getEvents().get(i).getPics().size() == 0)mListener.dataRecieved(true);

                //Finish request


                }
            }

            @Override
            public void failure(RetrofitError retrofitError)
            {
                Status = new String();
                System.out.println("Get data failed" + retrofitError.toString());
                Status = "Failed to Recieve Data\n"+retrofitError.toString();
                data = null;
                mListener.errorReport(retrofitError);

            }
        });
    }
    public void getImg(final int event, final boolean Flag)
    {
        DropBox DB = restAdapter.create(DropBox.class);
        Si = 0;
        DB.Download(path +"/"+data.getEvents().get(event).getPics().get(Si), new ResponseCallback()         //////////////////DODATI
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
                //saveImg(mutable);
                Slike[event][Si] = mutable;
                Si++;

                if(Flag)
                {
                    if(event+1 == data.getEvents().size())
                    {
                        mListener.dataRecieved(true);
                    }
                    else if(event+1 < data.getEvents().size())
                        getImg(event+1,true);
                }
                else {
                    if (mListener != null && Si == data.getEvents().get(event).getPics().size()) {
                        mListener.dataRecieved(true);
                        done = true;
                    }
                }
            }
            @Override
            public void failure(RetrofitError error)
            {
                Status += "\nFailed to recieve Pic : " + data.getEvents().get(event).getPics().get(Si) + "\n";
            }
        });
    }
    public Bitmap getImageByName(String nameOfTheDrawable, Activity a){
        int path = a.getResources().getIdentifier(nameOfTheDrawable,
                "drawable", "com.androidapp.osijeknightlife.app");

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap source = BitmapFactory.decodeResource(a.getResources(), path, options);

        return source;
    }

}