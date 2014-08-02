package com.pebblesifter.android.asyncTasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.pebblesifter.android.Constants;
import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import com.pebblesifter.android.R;
import com.pebblesifter.android.sifters.PebbleSifter;

public class SetSifter extends AsyncTask<PebbleSifter, Integer, PebbleSifter> {

  // Sifted text is limited to 93 characters + "..."
  final int MAX_TEXT_LENGTH = 93;

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
    // Set sifter and sift text
    sifter = pebbleSifter;
    String siftedTextString;

    // Prevent app from crashing if sifter.sift() throws an exception
    try {
      siftedTextString = sifter.sift();
    } catch (Exception e) {
      siftedTextString = "ERROR: Exception occurred while sifting text.";
      Log.e("Sift Exception: ", e.toString());
    }

    // Update Android views
    TextView name = (TextView) activity.findViewById(R.id.sifter_name);
    name.setText(sifter.getFullName() + ":");
    TextView siftedText = (TextView) activity.findViewById(R.id.sifted_text);
    siftedText.setText(siftedTextString);

    // Truncate sifted text to MAX_TEXT_LENGTH to prevent APP_MSG_BUFFER_OVERFLOW error
    if (siftedTextString.length() > MAX_TEXT_LENGTH) {
      siftedTextString = siftedTextString.substring(0, MAX_TEXT_LENGTH) + "...";
    }

    // Send text to Pebble app
    PebbleDictionary dictionary = new PebbleDictionary();
    dictionary.addString(Constants.SIFTER_PEBBLE_NAME, sifter.getPebbleName());
    dictionary.addString(Constants.SIFTER_TEXT, siftedTextString);
    PebbleKit.sendDataToPebble(activity, Constants.PEBBLE_SIFTER_UUID, dictionary);
  }
}
