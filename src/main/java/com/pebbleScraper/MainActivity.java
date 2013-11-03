package com.pebbleScraper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;
import android.content.Context;

import com.example.pebblesifter.R;
import com.pebbleScraper.scrapers.PebbleSiteScraper;
import com.pebbleScraper.scrapers.TeamTriviaAnswerScraper;

public class MainActivity extends Activity {

	PebbleSiteScraper scraper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SetSifter setSifter = new SetSifter();
        setSifter.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private class SetSifter extends AsyncTask<Object[], Integer, PebbleSiteScraper> {

        PebbleSiteScraper scraper;

        @Override
        protected PebbleSiteScraper doInBackground(Object[]... objects) {
            scraper = new TeamTriviaAnswerScraper();
            return scraper;
        }

        @Override
        protected void onPostExecute(PebbleSiteScraper pebbleScraper) {
            scraper = pebbleScraper;
            TextView name = (TextView) findViewById(R.id.sifter_name);
            name.setText(scraper.getName());
            TextView siftedText = (TextView) findViewById(R.id.sifted_text);
            siftedText.setText(scraper.scrape());
        }
    }
    
}
