package com.pebblesifter.android.asyncTasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.pebblesifter.android.MainActivity;
import com.pebblesifter.android.R;
import com.pebblesifter.android.sifters.PebbleSifter;
import com.pebblesifter.android.sifters.exampleSifters.HartmannGameStatusSifter;
import com.pebblesifter.android.sifters.exampleSifters.TeamTriviaAnswerSifter;

import java.util.ArrayList;

public class DrawApp extends AsyncTask<Object, Integer, ArrayList<String>> {

    // TODO: Yeah, this is totally the wrong way to do things
    public static ArrayList<PebbleSifter> sifters = new ArrayList<PebbleSifter>();
    Activity activity;

    public DrawApp(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected ArrayList<String> doInBackground(Object... objects) {
        // As new sifters are implemented, add them here.
        sifters.add(new TeamTriviaAnswerSifter());
        sifters.add(new HartmannGameStatusSifter());

        ArrayList<String> sifterNames = new ArrayList<String>();

        for (PebbleSifter sifter : sifters) {
            sifterNames.add(sifter.getFullName());
        }

        return sifterNames;
    }

    @Override
    protected void onPostExecute(ArrayList<String> sifterNames) {
        SetSifter setSifter = new SetSifter(activity);
        setSifter.execute(sifters.get(0));

        for (int i = 0; i < sifterNames.size(); i++) {
            final PebbleSifter sifter = sifters.get(i);

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

        LinearLayout linearLayout = (LinearLayout)activity.findViewById(R.id.button_layout);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);

        for (Button sifterButton : MainActivity.sifterButtons) {
            linearLayout.addView(sifterButton);
        }
    }
}
