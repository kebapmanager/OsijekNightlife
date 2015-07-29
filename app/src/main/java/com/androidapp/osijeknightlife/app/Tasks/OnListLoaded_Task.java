package com.androidapp.osijeknightlife.app.Tasks;

import android.os.AsyncTask;

/**
 * Created by Toni P on 7/29/2015.
 */
public class OnListLoaded_Task extends AsyncTask<String, Integer, String> {
    // Runs in UI before background thread is called
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        // Do something like display a progress bar
    }

    // This is run in a background thread
    @Override
    protected String doInBackground(String... params) {
        // get the string from params, which is an array
        String myString = params[0];

        // Do something that takes a long time, for example:
        for (int i = 0; i <= 100; i++) {

            // Do things

            // Call this to update your progress
            publishProgress(i);
        }

        return "this string is passed to onPostExecute";
    }

    // This is called from background thread but runs in UI
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);

        // Do things like update the progress bar
    }

    // This runs in UI when background thread finishes
    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        // Do things like hide the progress bar or change a TextView
    }
}
