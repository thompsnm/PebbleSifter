package com.pebbleSifter.asyncTasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.example.pebblesifter.R;
import com.pebbleSifter.sifters.PebbleSifter;

public class SetSifter extends AsyncTask<PebbleSifter, Integer, PebbleSifter> {

    Activity activity;
    PebbleSifter sifter;

    public SetSifter(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected PebbleSifter doInBackground(PebbleSifter... pebbleSifters) {
        this.sifter = pebbleSifters[0];
        return this.sifter;
    }

    @Override
    protected void onPostExecute(PebbleSifter pebbleSifter) {
        sifter = pebbleSifter;
        TextView name = (TextView) activity.findViewById(R.id.sifter_name);
        name.setText(sifter.getName() + ":");
        TextView siftedText = (TextView) activity.findViewById(R.id.sifted_text);

        // Prevent app from crashing if sifter.sift() throws an exception
        try {
            siftedText.setText(sifter.sift());
        } catch (Exception e) {
            siftedText.setText("ERROR: Exception occurred while sifting text.");
            Log.e("Sift", e.toString());
        }
    }
}
