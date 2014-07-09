package com.pebblesifter.android.asyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;
import com.pebblesifter.android.Constants;
import com.pebblesifter.android.MainActivity;
import com.pebblesifter.android.dataReceivers.DefaultAckReceiver;
import com.pebblesifter.android.dataReceivers.DefaultNackReceiver;
import com.pebblesifter.android.sifters.PebbleSifter;

import java.util.ArrayList;

public class SendSifters extends AsyncTask<Object, Integer, Void> {

  private final int MAX_RETRIES = 3;

  private ArrayList<PebbleSifter> sifters = new ArrayList<PebbleSifter>();
  Activity activity;
  private ProgressDialog dialog;
  private PebbleSifter sifter;
  private int sifterIndex;
  private int retries = MAX_RETRIES;

  public SendSifters(Activity activity, ArrayList<PebbleSifter> sifters) {
    this.activity = activity;
    this.sifters = sifters;
  }

  @Override
  protected Void doInBackground(Object... params) {

    Log.i("MainActivity", "Checking for extras");
    Bundle extras = activity.getIntent().getExtras();
    if (extras != null) {
      Log.i("MainActivity", "Extras found");
      if (extras.getBoolean("handshake", false)) {
        Log.i("MainActivity", "handshake found");
        sendSifters();
      } else if (extras.getString("newSifter") != null) {
        Log.i("MainActivity", "newSifter found with value " + extras.getString("newSifter"));
        String newSifterFullName = extras.getString("newSifter");
        for (Button sifterButton : MainActivity.sifterButtons) {
          if (sifterButton.getText().equals(newSifterFullName)) {
            sifterButton.performClick();
            break;
          }
        }
      }
    }

    return null;
  }

  private void sendSifters() {
    Log.i("MainActivity", "Starting sendSifters(). sifterIndex is " + sifterIndex);
    if (sifterIndex == sifters.size()) {
      Log.i("MainActivity", "Sending HANDSHAKE_SUCCESS to watch");
      PebbleKit.registerReceivedAckHandler(activity.getApplicationContext(), new DefaultAckReceiver(Constants.PEBBLE_SIFTER_UUID));
      PebbleKit.registerReceivedNackHandler(activity.getApplicationContext(), new DefaultNackReceiver(Constants.PEBBLE_SIFTER_UUID));

      PebbleDictionary handshakeComplete = new PebbleDictionary();
      handshakeComplete.addInt32(Constants.HANDSHAKE_SUCCESS, 1);
      PebbleKit.sendDataToPebbleWithTransactionId(activity, Constants.PEBBLE_SIFTER_UUID, handshakeComplete, 100);
      return;
    } else if (retries == 0) {
      Log.w("MainActivity", "Sending HANDSHAKE_FAIL to watch");
      PebbleKit.registerReceivedAckHandler(activity.getApplicationContext(), new DefaultAckReceiver(Constants.PEBBLE_SIFTER_UUID));
      PebbleKit.registerReceivedNackHandler(activity.getApplicationContext(), new DefaultNackReceiver(Constants.PEBBLE_SIFTER_UUID));

      PebbleDictionary handshakeComplete = new PebbleDictionary();
      handshakeComplete.addInt32(Constants.HANDSHAKE_FAIL, 1);
      PebbleKit.sendDataToPebbleWithTransactionId(activity, Constants.PEBBLE_SIFTER_UUID, handshakeComplete, 100);
      return;
    }

    sifter = sifters.get(sifterIndex);

    sendSifter(activity, sifter, sifterIndex);

    //Register recursive Ack and Nack receivers
    PebbleKit.registerReceivedAckHandler(activity.getApplicationContext(), new PebbleKit.PebbleAckReceiver(Constants.PEBBLE_SIFTER_UUID) {
      @Override
      public void receiveAck(Context context, int transactionId) {
        Log.i("MainActivity", "Received ack for transaction " + transactionId);
        if (transactionId == sifterIndex) {
          retries = MAX_RETRIES;
          sifterIndex++;
          sendSifters();
        }
      }
    });

    PebbleKit.registerReceivedNackHandler(activity.getApplicationContext(), new PebbleKit.PebbleNackReceiver(Constants.PEBBLE_SIFTER_UUID) {
      @Override
      public void receiveNack(Context context, int transactionId) {
        Log.w("MainActivity", "Received nack for transaction " + transactionId);
        if (transactionId == sifterIndex) {
          retries--;
          sendSifter(context, sifter, sifterIndex);
        }
      }
    });
  }

  private void sendSifter(Context context, PebbleSifter sifter, int transactionId) {
    Log.i("MainActivity", "Sending sifter '" + sifter.getPebbleName() + "' to watch");
    PebbleDictionary dictionary = new PebbleDictionary();
    dictionary.addString(Constants.SIFTER_FULL_NAME, sifter.getFullName());
    dictionary.addString(Constants.SIFTER_PEBBLE_MENU_NAME, sifter.getPebbleName());
    PebbleKit.sendDataToPebbleWithTransactionId(context, Constants.PEBBLE_SIFTER_UUID, dictionary, transactionId);
  }
}
