package com.pebbleScraper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Button;
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

        SetSifter setSifter = new SetSifter();
        setSifter.execute();
		
		DrawApp drawApp = new DrawApp(this);
		drawApp.execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	
	private class DrawApp extends AsyncTask<Object, Integer, Object> {
	
		ArrayList<PebbleSiteScraper> scrapers = new ArrayList<PebbleSiteScraper>();
        Context context;

        public DrawApp(Context context) {
            this.context = context;
        }

        @Override
        protected Object doInBackground(Object... objects) {
            scrapers.add(new TeamTriviaAnswerScraper());
			scrapers.add(new HartmannGameStatusScraper());

			Button myButton = new Button(context);
			myButton.setText("Push Me");

//            RelativeLayout rl = (RelativeLayout)findViewById(R.id.button_layout);
//			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//			rl.addView(myButton, lp);
//
//			SetSifter setSifter = new SetSifter(scrapers.get(0));
//			setSifter.execute();

            return null;
        }
	}

    private class SetSifter extends AsyncTask<PebbleSiteScraper, Integer, PebbleSiteScraper> {

        PebbleSiteScraper scraper;

//        public SetSifter(PebbleSiteScraper pebbleSiteScraper) {
//            scraper = pebbleSiteScraper;
//        }

        @Override
        protected PebbleSiteScraper doInBackground(PebbleSiteScraper... pebbleScrapers) {
            this.scraper = new TeamTriviaAnswerScraper();
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
