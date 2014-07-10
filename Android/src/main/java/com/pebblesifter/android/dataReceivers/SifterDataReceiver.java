package com.pebblesifter.android.dataReceivers;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;

import com.pebblesifter.android.Constants;
import com.pebblesifter.android.MainActivity;
import com.pebblesifter.android.asyncTasks.DrawApp;
import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;
import com.pebblesifter.android.asyncTasks.SendSifters;
import com.pebblesifter.android.sifters.PebbleSifter;

import java.util.ArrayList;

import lombok.Setter;

public class SifterDataReceiver extends PebbleKit.PebbleDataReceiver {

  public SifterDataReceiver() {
    super(Constants.PEBBLE_SIFTER_UUID);
  }

  @Override
  public void receiveData(final Context context, final int transactionId, final PebbleDictionary data) {
    PebbleKit.sendAckToPebble(context, transactionId);

    // Start MainActivity
    Intent i = new Intent(context, MainActivity.class);
    i.addFlags(
        Intent.FLAG_ACTIVITY_NEW_TASK   // If set, this activity will become the start of a new task on this history stack
            | Intent.FLAG_ACTIVITY_SINGLE_TOP // If set, the activity will not be launched if it is already running at the top of the history stack
    );

    if (data.getInteger(Constants.HANDSHAKE_INIT) != null) {
      // Send sifter pebble names to Pebble app
      Log.i("SifterDataReceiver", "Adding extra: handshake");
      i.putExtra("handshake", true);
    } else {
      Log.i("SifterDataReceiver", "Sifter was selected");
      String newSifterFullName = data.getString(Constants.SIFTER_FULL_NAME);
      Log.i("SifterDataReceiver", "Sifter Full Name: " + newSifterFullName);

      for (Button sifterButton : MainActivity.sifterButtons) {
        if (sifterButton.getText().equals(newSifterFullName)) {
          sifterButton.performClick();
          break;
        }
      }

      Log.i("SifterDataReceiver", "Adding extra: newSifter");
      i.putExtra("newSifter", newSifterFullName);
    }

    context.startActivity(i);
  }

}
