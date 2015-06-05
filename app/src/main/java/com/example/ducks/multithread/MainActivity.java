package com.example.ducks.multithread;

import android.content.Context;

import android.graphics.Path;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    ProgressBar progress;
    ListView listView;// = (ListView) findViewById(R.id.listView);
    ArrayAdapter<String> stringAA;// = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
    //listView.setAdapter(stringAA);

    public void doLoad(View v) {
        //
        final Context context = this;
        listView = (ListView) findViewById(R.id.listView);

        FileInputStream fIn = null;
        try {
            fIn = openFileInput("numbers.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        final InputStreamReader isr = new InputStreamReader(fIn);

        /* Prepare a char-Array that will
         * hold the chars we read back in. */
        final char[] inputBuffer = new char[80];
        stringAA = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);
        listView.setAdapter(stringAA);


        for (int i = 0; i < 10; i++) {
            final int value = i * 10;
            try {
                isr.read(inputBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String readString = new String(inputBuffer);
            stringAA.add(readString);
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void doCreate(View v) {
        //
        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.post(new Runnable() {
            @Override
            public void run() {
                progress.setProgress(0);
            }
        });

        Runnable cRunnable = new Runnable() {
            @Override
            public void run() {
                try {

                    FileOutputStream fOut = openFileOutput("numbers.txt", MODE_WORLD_READABLE);
                    OutputStreamWriter osw = new OutputStreamWriter(fOut);

                    // Write the string to the file
                    for (int i = 1; i < 11; i++) {
                        final int value = i * 10;
                        String string = Integer.toString(i);
                        osw.write(string + "\n");
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        progress.post(new Runnable() {
                            @Override
                            public void run() {
                                progress.setProgress(value);
                            }
                        });
                    }

       /* ensure that everything is
        * really written out and close */
                    osw.flush();
                    osw.close();
                } catch (IOException ioe)
                {ioe.printStackTrace();}
            }
        };
        new Thread(cRunnable).start();
    }


    public void doClear(View v) {
        //
        stringAA.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
