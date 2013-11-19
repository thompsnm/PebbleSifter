package com.androidSifter.asyncTasks;

import java.util.UUID;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import com.example.pebblesifter.R;
import com.androidSifter.sifters.PebbleSifter;

public class SetSifter extends AsyncTask<PebbleSifter, Integer, PebbleSifter> {

    private static final UUID PEBBLE_SIFTER_UUID = UUID.fromString("ACA3B3D0-BF4A-4777-9238-FF95F07AA221");
    private static final int SIFTER_NAME = 0;
    private static final int SIFTER_TEXT = 1;

    Activity activity;
    PebbleSifter sifter;

    public SetSifter(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected PebbleSifter doInBackground(PebbleSifter... pebbleSifters) {
        this.sifter = pebbleSifters[0];
        this.sifter.connect();
        return this.sifter;
    }

    @Override
    protected void onPostExecute(PebbleSifter pebbleSifter) {
        // Set sifter and sift text
        sifter = pebbleSifter;
        String siftedTextString;

        // Prevent app from crashing if sifter.sift() throws an exception
        try {
            siftedTextString = sifter.sift();
        } catch (Exception e) {
            siftedTextString = "ERROR: Exception occurred while sifting text.";
            Log.e("Sift", e.toString());
        }

        // Update Android views
        TextView name = (TextView) activity.findViewById(R.id.sifter_name);
        name.setText(sifter.getFullName() + ":");
        TextView siftedText = (TextView) activity.findViewById(R.id.sifted_text);
        siftedText.setText(siftedTextString);

        // Send text to Pebble app
        PebbleDictionary dictionary = new PebbleDictionary();
        dictionary.addString(SIFTER_NAME, sifter.getPebbleName());
        dictionary.addString(SIFTER_TEXT, siftedTextString);
        PebbleKit.sendDataToPebble(activity, PEBBLE_SIFTER_UUID, dictionary);
    }
}
