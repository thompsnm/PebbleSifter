package com.androidSifter;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

/**
 * Created by Tom on 11/28/13.
 */
public class SifterDataReceiver extends PebbleKit.PebbleDataReceiver {

    public SifterDataReceiver() {
        super(Constants.PEBBLE_SIFTER_UUID);
    }

    @Override
    public void receiveData(final Context context, final int transactionId, final PebbleDictionary data) {
            String newSifterFullName = data.getString(Constants.SIFTER_FULL_NAME);

            PebbleKit.sendAckToPebble(context, transactionId);

            for (Button sifterButton : MainActivity.sifterButtons) {
                if (sifterButton.getText().equals(newSifterFullName)) {
                    sifterButton.performClick();
                    break;
                }
            }

            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK   // If set, this activity will become the start of a new task on this history stack
                            | Intent.FLAG_ACTIVITY_CLEAR_TOP  // If set, and the activity being launched is already running in the current task, then instead of launching a new instance of that activity, all of the other activities on top of it will be closed and this Intent will be delivered to the (now on top) old activity as a new Intent
                            | Intent.FLAG_ACTIVITY_SINGLE_TOP // If set, the activity will not be launched if it is already running at the top of the history stack
            );

            context.startActivity(i);
        }
}
