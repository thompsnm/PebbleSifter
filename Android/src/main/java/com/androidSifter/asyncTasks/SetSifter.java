package com.androidSifter.asyncTasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import com.example.pebblesifter.R;
import com.androidSifter.sifters.PebbleSifter;

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
        siftedText.setText(sifter.sift());
    }
}
