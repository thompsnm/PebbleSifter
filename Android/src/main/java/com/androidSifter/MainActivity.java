package com.androidSifter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.Button;

import com.androidSifter.asyncTasks.SetSifter;
import com.example.pebblesifter.R;
import com.androidSifter.asyncTasks.DrawApp;
import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import java.util.ArrayList;

public class MainActivity extends Activity {

    // TODO: This seems like the wrong way to do things
    public static ArrayList<Button> sifterButtons = new ArrayList<Button>();

    private PebbleKit.PebbleDataReceiver sifterDataReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		DrawApp drawApp = new DrawApp(this);
		drawApp.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
