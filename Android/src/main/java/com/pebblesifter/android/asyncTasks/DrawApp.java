package com.pebblesifter.android.asyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.pebblesifter.android.MainActivity;
import com.pebblesifter.android.R;
import com.pebblesifter.android.sifters.PebbleSifter;
import com.pebblesifter.android.sifters.exampleSifters.HartmannGameStatusSifter;
import com.pebblesifter.android.sifters.exampleSifters.TeamTriviaAnswerSifter;
import com.pebblesifter.android.sifters.personalSifters.*;

import java.util.ArrayList;

public class DrawApp extends AsyncTask<Object, Integer, ArrayList<String>> {

  private final int MAX_RETRIES = 3;

  Activity activity;
  boolean setSifters;
  private ProgressDialog dialog;

  public DrawApp(Activity activity, boolean setSifters) {
    this.activity = activity;
    this.setSifters = setSifters;
    dialog = new ProgressDialog(activity);
  }

  @Override
  protected void onPreExecute() {
    this.dialog.setMessage("Sifting data...");
    this.dialog.show();
  }

  @Override
  protected ArrayList<String> doInBackground(Object... objects) {
    if (setSifters) {
      ////////////////////////////////////////////////////
      // As new sifters are implemented, add them here: //
      ////////////////////////////////////////////////////
      MainActivity.sifters.add(new TeamTriviaAnswerSifter());
      MainActivity.sifters.add(new HartmannGameStatusSifter());

      ArrayList<String> sifterNames = new ArrayList<String>();

      for (PebbleSifter sifter : MainActivity.sifters) {
        sifterNames.add(sifter.getFullName());
      }

      return sifterNames;
    } else {
      return null;
    }
  }

  @Override
  protected void onPostExecute(ArrayList<String> sifterNames) {
    if (dialog.isShowing()) {
      dialog.dismiss();
    }

    SetSifter setSifter = new SetSifter(activity);
    setSifter.execute(MainActivity.sifters.get(0));

    if (setSifters) {
      for (int i = 0; i < sifterNames.size(); i++) {
        final PebbleSifter sifter = MainActivity.sifters.get(i);

        Button sifterButton = new Button(activity);
        sifterButton.setText(sifterNames.get(i));

        View.OnClickListener listener = new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            SetSifter setSifter = new SetSifter(activity);
            setSifter.execute(sifter);
          }
        };

        sifterButton.setOnClickListener(listener);
        MainActivity.sifterButtons.add(sifterButton);
      }
    }

    LinearLayout linearLayout = (LinearLayout)activity.findViewById(R.id.button_layout);

    for (Button sifterButton : MainActivity.sifterButtons) {
      linearLayout.addView(sifterButton);
    }

    SendSifters sendSifters = new SendSifters(activity, MainActivity.sifters);
    sendSifters.execute();
  }
}
