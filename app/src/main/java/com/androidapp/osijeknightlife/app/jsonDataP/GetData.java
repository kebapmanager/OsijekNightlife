package com.androidapp.osijeknightlife.app.jsonDataP;

import android.app.Activity;
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
        public void errorReport(RetrofitError.Kind kind);
    }
    private Listener mListener = null;
    public void registerListener (Listener listener) {
        mListener = listener;
    }

    /////////////////////Class
    public boolean info = false;
    public boolean done = false;
    int CheckDate;
    public String Status;
    public List<Bitmap> pictures = new ArrayList<Bitmap>();

    public Bitmap bitmap,mutable;
    public DataLoader data =  new DataLoader();
    public Gson gson =  new Gson();
    private RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint("https://api-content.dropbox.com")
            .build();
    String path,extrPath;
    int Si;
    public Bitmap[][] Slike = new Bitmap[20][5];

    Activity mainActivity;
    public GetData(Activity mainActivity){this.mainActivity = mainActivity;}


    public void Start(String Path,final int dan)
    {
        CheckDate = dan;
        data = new DataLoader();
        this.path = Path;
        DropBox DB = restAdapter.create(DropBox.class);
        pictures.add(getImageByName("bastion.png",mainActivity));
        DB.Download(path+"/Event.json", new ResponseCallback() {
            @Override
            public void success(Response response)  {
            if(CheckDate == dan) {
                String json = new String();
                try {
                    InputStream in = response.getBody().in();
                    BufferedReader r = new BufferedReader(new InputStreamReader(in));
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
            }
            }

            @Override
            public void failure(RetrofitError retrofitError)
            {
                Status = new String();
                System.out.println("Get data failed" + retrofitError.toString());
                Status = "Failed to Recieve Data\n"+retrofitError.toString();
                data = null;
                mListener.errorReport(retrofitError.getKind());

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
                    if (Si < data.getEvents().get(event).getPics().size())
                        moarImg(event);
                    else if (mListener != null && Si == data.getEvents().get(event).getPics().size()) {
                        mListener.dataRecieved(true);
                        done = true;
                    }
                }
            }
            @Override
            public void failure(RetrofitError error)
            {
                Status += "\nFailed to recieve Pic : " + data.getEvents().get(event).getPics().get(Si)+".jpg" + "\n";
            }
        });
    }
    private void moarImg(final int event)
    {
        DropBox DB = restAdapter.create(DropBox.class);

        DB.Download(path +"/"+data.getEvents().get(event).getPics().get(Si), new ResponseCallback()
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
                if (Si < data.getEvents().get(event).getPics().size())
                    moarImg(event);
                else if (mListener != null && Si == data.getEvents().get(event).getPics().size()){
                     mListener.dataRecieved(true);
                }
            }
            @Override
            public void failure(RetrofitError error)
            {
                Status += "\nFailed to recieve Pic : " + data.getEvents().get(event).getPics().get(Si)+".jpg" + "\n";
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
    public Bitmap getImageByName(String nameOfTheDrawable, Activity a){
        int path = a.getResources().getIdentifier(nameOfTheDrawable,
                "drawable", "com.androidapp.osijeknightlife.app");

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap source = BitmapFactory.decodeResource(a.getResources(), path, options);

        return source;
    }

}