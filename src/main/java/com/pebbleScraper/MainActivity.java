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
		
		DrawApp drawApp = new DrawApp();
		drawApp.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	
	private class DrawApp extends AsyncTask<void, Integer, void> {
	
		ArrayList<PebbleScraper> scrapers;
	
		@Override
        protected void doInBackground(void) {
            scrapers.add(new TeamTriviaAnswerScraper());
			scrapers.add(new HartmannGameStatusScraper());
			
			Button myButton = new Button(this);
			myButton.setText("Push Me");

			LinearLayout ll = (LinearLayout)findViewById(R.id.button_layout);
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			ll.addView(myButton, lp);
			
			SetSifter setSifter = new SetSifter(scrapers.get(0));
			setSifter.execute();
        }
	}

    private class SetSifter extends AsyncTask<PebbleSiteScraper, Integer, PebbleSiteScraper> {

        PebbleSiteScraper scraper;

        @Override
        protected PebbleSiteScraper doInBackground(PebbleSiteScraper... pebbleScraper) {
            scraper = pebbleScraper;
            return this.scraper;
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
