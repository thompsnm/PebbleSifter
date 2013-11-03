package com.pebbleScraper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.*;
import android.widget.TextView;
import android.content.Context;

import com.example.pebblesifter.R;
import com.pebbleScraper.scrapers.HartmannGameStatusScraper;
import com.pebbleScraper.scrapers.PebbleSiteScraper;
import com.pebbleScraper.scrapers.TeamTriviaAnswerScraper;

import java.util.ArrayList;

public class MainActivity extends Activity {

	PebbleSiteScraper scraper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		DrawApp drawApp = new DrawApp(this);
		drawApp.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	
	private class DrawApp extends AsyncTask<Object, Integer, ArrayList<String>> {

		ArrayList<PebbleSiteScraper> scrapers = new ArrayList<PebbleSiteScraper>();
        ArrayList<String> scraperNames = new ArrayList<String>();
        Context context;

        public DrawApp(Context context) {
            this.context = context;
        }

        @Override
        protected ArrayList<String> doInBackground(Object... objects) {
            // As new scrapers are implemented, add them here.
            scrapers.add(new TeamTriviaAnswerScraper());
			scrapers.add(new HartmannGameStatusScraper());

            for (PebbleSiteScraper scraper : scrapers) {
                scraperNames.add(scraper.getName());
            }

            return scraperNames;
        }

        @Override
        protected void onPostExecute(ArrayList<String> scraperNames) {
            ArrayList<Button> scraperButtons = new ArrayList<Button>();

			SetSifter setSifter = new SetSifter(scrapers.get(0));
			setSifter.execute();

            for (String scraperName : scraperNames) {
                Button scraperButton = new Button(context);
                scraperButton.setText(scraperName);
                scraperButtons.add(scraperButton);
            }

            LinearLayout linearLayout = (LinearLayout)findViewById(R.id.button_layout);
			LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);

            for (Button scraperButton : scraperButtons) {
                linearLayout.addView(scraperButton);
            }
        }
	}

    private class SetSifter extends AsyncTask<PebbleSiteScraper, Integer, PebbleSiteScraper> {

        PebbleSiteScraper scraper;

        public SetSifter(PebbleSiteScraper pebbleSiteScraper) {
            scraper = pebbleSiteScraper;
        }

        @Override
        protected PebbleSiteScraper doInBackground(PebbleSiteScraper... pebbleScrapers) {
            this.scraper = new TeamTriviaAnswerScraper();
            return this.scraper;
        }

        @Override
        protected void onPostExecute(PebbleSiteScraper pebbleScraper) {
            scraper = pebbleScraper;
            TextView name = (TextView) findViewById(R.id.sifter_name);
            name.setText(scraper.getName() + ":");
            TextView siftedText = (TextView) findViewById(R.id.sifted_text);
            siftedText.setText(scraper.scrape());
        }
    }

}
