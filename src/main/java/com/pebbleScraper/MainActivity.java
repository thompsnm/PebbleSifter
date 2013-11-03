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
import com.pebbleScraper.asyncTasks.DrawApp;
import com.pebbleScraper.scrapers.HartmannGameStatusScraper;
import com.pebbleScraper.scrapers.PebbleSiteScraper;
import com.pebbleScraper.scrapers.TeamTriviaAnswerScraper;

import java.util.ArrayList;

public class MainActivity extends Activity {

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

}
