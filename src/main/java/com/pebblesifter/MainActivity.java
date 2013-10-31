package com.pebblesifter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.TextView;

import com.example.pebblesifter.R;
import com.pebblesifter.sifters.PebbleSifter;
import com.pebblesifter.sifters.TeamTriviaAnswerSifter;

public class MainActivity extends Activity {

	PebbleSifter sifter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SetSifter setSifter = new SetSifter();
        setSifter.execute();
        TextView name = (TextView) findViewById(R.id.sifter_name);
        name.setText(sifter.getName());
        TextView siftedText = (TextView) findViewById(R.id.sifted_text);
        siftedText.setText("Text");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public class SetSifter extends AsyncTask {

        @Override
        protected PebbleSifter doInBackground(Object[] objects) {
            return new TeamTriviaAnswerSifter();
        }

        public void execute(PebbleSifter pebbleSifter) {
            sifter = pebbleSifter;
        }
    }
    
}
