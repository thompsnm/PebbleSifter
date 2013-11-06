package com.pebbleSifter;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import com.example.pebblesifter.R;
import com.pebbleSifter.asyncTasks.DrawApp;

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
