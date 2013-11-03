package com.pebbleScraper.asyncTasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.pebblesifter.R;
import com.pebbleScraper.scrapers.HartmannGameStatusScraper;
import com.pebbleScraper.scrapers.PebbleSiteScraper;
import com.pebbleScraper.scrapers.TeamTriviaAnswerScraper;

import java.util.ArrayList;

public class DrawApp extends AsyncTask<Object, Integer, ArrayList<String>> {

    ArrayList<PebbleSiteScraper> scrapers = new ArrayList<PebbleSiteScraper>();
    Activity activity;

    public DrawApp(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected ArrayList<String> doInBackground(Object... objects) {
        // As new scrapers are implemented, add them here.
        scrapers.add(new TeamTriviaAnswerScraper());
        scrapers.add(new HartmannGameStatusScraper());

        ArrayList<String> scraperNames = new ArrayList<String>();

        for (PebbleSiteScraper scraper : scrapers) {
            scraperNames.add(scraper.getName());
        }

        return scraperNames;
    }

    @Override
    protected void onPostExecute(ArrayList<String> scraperNames) {
        ArrayList<Button> scraperButtons = new ArrayList<Button>();

        SetSifter setSifter = new SetSifter(activity, scrapers.get(0));
        setSifter.execute();

        for (String scraperName : scraperNames) {
            Button scraperButton = new Button(activity);
            scraperButton.setText(scraperName);
            scraperButtons.add(scraperButton);
        }

        LinearLayout linearLayout = (LinearLayout)activity.findViewById(R.id.button_layout);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);

        for (Button scraperButton : scraperButtons) {
            linearLayout.addView(scraperButton);
        }
    }
}
