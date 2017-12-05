package com.bryankoproject.cmps121asg3;

/**
 * Created by Bryan on 12/4/2017.
 */

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import java.util.Calendar;
import java.util.Random;
import de.greenrobot.event.EventBus;

/**
 * Created by luca on 7/5/2015.
 */
public class MyServiceTask implements Runnable {

    public static final String LOG_TAG = "MyService";
    private Context context;

    float xaccel,yaccel;
    Calendar start;
    long startAccel;
    long getStart;
    public static boolean movedFlag = false;

    public MyServiceTask(Context _context) {
        context = _context;
    }

    @Override
    public void run() {
        Random rand = new Random();

        // Use phone sensor to detect change
        ((SensorManager) context.getSystemService(Context.SENSOR_SERVICE)).registerListener (new SensorEventListener() {
            public void onSensorChanged(SensorEvent event) {
                // gets the x and y acceleration
                xaccel = -event.values[0];
                yaccel = event.values[1];
                // logs the acceleration
                Log.d("accel", "x:" + String.valueOf(xaccel) + "y:" + String.valueOf(yaccel));

                start = Calendar.getInstance();
                getStart = start.getTimeInMillis();

                // determines if phone has moved and get the time it last moved
                if (getStart - MainActivity.getStart > 30000) {
                    if (Math.abs(xaccel) > 2 || Math.abs(yaccel) > 2 && movedFlag == false) {
                        startAccel = start.getTimeInMillis();
                        movedFlag = true;
                        Log.d("phoneMove","true");
                    }

                    // tells when to change the text
                    if (movedFlag == true && (start.getTimeInMillis() - startAccel) > 30000){
                        EventBus.getDefault().post("The phone moved!");
                    }
                }
            }
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        }, ((SensorManager) context.getSystemService(Context.SENSOR_SERVICE)).getSensorList(Sensor.TYPE_ACCELEROMETER).get(0), SensorManager.SENSOR_DELAY_GAME);
    }

    public void stopProcessing() {
    }

    public void setTaskState(boolean b) {
    }
}
