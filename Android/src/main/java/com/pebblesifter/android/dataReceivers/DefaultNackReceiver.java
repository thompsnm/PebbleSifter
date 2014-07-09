package com.pebblesifter.android.dataReceivers;

import android.content.Context;
import android.util.Log;

import com.getpebble.android.kit.PebbleKit;

import java.util.UUID;

/**
 * Created by nathan.thompson on 6/5/14.
 */
public class DefaultNackReceiver extends PebbleKit.PebbleNackReceiver {
  public DefaultNackReceiver(UUID subscribedUuid) {
    super(subscribedUuid);
  }

  @Override
  public void receiveNack(Context context, int transactionId) {
    Log.i("SifterDataReceiver", "Received nack for transaction " + transactionId);
  }
}
