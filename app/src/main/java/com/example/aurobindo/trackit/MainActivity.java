package com.example.aurobindo.trackit;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationListener;
import android.media.browse.MediaBrowser;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private boolean toggle = false;
    private SensorManager sensormanager = null;
    private File externalStorageDirectory;
    private FileWriter filewriterAcc, filewriterGyro;
    private TextView dispAcc, dispGyro;
    private Button track, stop;

    SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensormanager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        externalStorageDirectory = Environment.getExternalStorageDirectory();

        dispAcc = (TextView) findViewById(R.id.tv_acc);
        dispGyro = (TextView) findViewById(R.id.tv_gyro);
        track = (Button) findViewById(R.id.button_Track);
        stop = (Button) findViewById(R.id.button_Stop);
        try {
            filewriterAcc = new FileWriter(new File(externalStorageDirectory, "accelerometer_"+dateFormat.format(new Date())+".tsv"), true);
            filewriterGyro = new FileWriter(new File(externalStorageDirectory, "gyroscope_"+dateFormat.format(new Date())+".tsv"), true);
        } catch (IOException e) {}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
    float x, y, z;
        /**************************** Accelerometer ***************************/
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
            if(toggle)  {
                dispAcc.setText("x=" + x + "\ty=" + y + "\tz=" + z);

                try {
                    filewriterAcc.write("\n" + dateFormat.format(new Date()) + "\t" + x + "\t" + y + "\t" + z);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        /***************************** Gyroscope ******************************/
       if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];
            if(toggle)  {
                dispAcc.setText("x=" + x + "\ty=" + y + "\tz=" + z);

                try {
                    filewriterGyro.write("\n" + dateFormat.format(new Date()) + "\t" + x + "\t" + y + "\t" + z);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void trackStart (View view) {
        toggle = true;
        // Sensor start to listen
        sensormanager.registerListener(this,
                sensormanager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);
        sensormanager.registerListener(this,
                sensormanager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void trackStop (View view) {
        toggle = false;
        sensormanager.unregisterListener(this);
        try {
            filewriterAcc.close();
            filewriterGyro.close();
        } catch (IOException e) {}
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
