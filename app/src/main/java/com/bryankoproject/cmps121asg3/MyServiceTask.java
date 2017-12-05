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
    public static boolean movedFlag = false;

    public MyServiceTask(Context _context) {
        context = _context;
    }

    @Override
    public void run() {
        final float[] xaccel = new float[1];
        final float[] yaccel = new float[1];
        final Calendar[] start = new Calendar[1];
        final long[] startAccel = new long[1];
        final long[] getStart = new long[1];
        Random rand = new Random();

        // Use phone sensor to detect change
        ((SensorManager) context.getSystemService(Context.SENSOR_SERVICE)).registerListener (new SensorEventListener() {
            public void onSensorChanged(SensorEvent sensorEvent) {

                // gets the x and y acceleration
                xaccel[0] = -sensorEvent.values[0];
                yaccel[0] = sensorEvent.values[1];
                // logs the acceleration
                Log.d("acceleration", "x:" + String.valueOf(xaccel[0]) + ", y:" + String.valueOf(yaccel[0]));

                start[0] = Calendar.getInstance();
                getStart[0] = start[0].getTimeInMillis();

                // determines if phone has moved and get the time it last moved
                if (getStart[0] - MainActivity.getStart > 30000) {
                    if (Math.abs(xaccel[0]) > 2 || Math.abs(yaccel[0]) > 2 && movedFlag == false) {
                        startAccel[0] = start[0].getTimeInMillis();
                        movedFlag = true;
                        Log.d("phoneMove","true");
                    }

                    // tells when to change the text
                    if (movedFlag == true && (start[0].getTimeInMillis() - startAccel[0]) > 30000){
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
