package com.pebbleScraper.asyncTasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;

import com.example.pebblesifter.R;
import com.pebbleScraper.scrapers.PebbleSiteScraper;
import com.pebbleScraper.scrapers.TeamTriviaAnswerScraper;

public class SetSifter extends AsyncTask<PebbleSiteScraper, Integer, PebbleSiteScraper> {

    Activity activity;
    PebbleSiteScraper scraper;

    public SetSifter(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected PebbleSiteScraper doInBackground(PebbleSiteScraper... pebbleScrapers) {
        this.scraper = pebbleScrapers[0];
        return this.scraper;
    }

    @Override
    protected void onPostExecute(PebbleSiteScraper pebbleScraper) {
        scraper = pebbleScraper;
        TextView name = (TextView) activity.findViewById(R.id.sifter_name);
        name.setText(scraper.getName() + ":");
        TextView siftedText = (TextView) activity.findViewById(R.id.sifted_text);
        siftedText.setText(scraper.scrape());
    }
}
