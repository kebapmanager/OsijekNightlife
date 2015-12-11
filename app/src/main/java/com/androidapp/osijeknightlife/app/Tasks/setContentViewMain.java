package com.androidapp.osijeknightlife.app.Tasks;

import android.content.Context;
import android.os.AsyncTask;
import com.androidapp.osijeknightlife.app.MainActivity;

/**
 * Created by Ema on 11.12.2015..
 */
public class setContentViewMain extends AsyncTask<Void, Void, String> {

    Context context;
    public setContentViewMain(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(Void... params) {

        return null;
    }

    @Override
    protected void onPostExecute(String result) {

    }
}
