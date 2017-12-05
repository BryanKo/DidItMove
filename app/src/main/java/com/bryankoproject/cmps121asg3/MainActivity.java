package com.bryankoproject.cmps121asg3;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.bryankoproject.cmps121asg3.MyService.MyBinder;

import java.util.Calendar;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {
    Calendar start = Calendar.getInstance();;
    public static long getStart;
    private static final String LOG_TAG = "MainActivity";

    //HERE
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //start the service
        Intent intent = new Intent(this, MyService.class);
        startService(intent);
        bindMyService();

        // Prevents display from sleeping while on app
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Starts the service, so that the service will only stop when explicitly stopped.
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void bindMyService() {
        // We are ready to show images, and we should start getting the bitmaps
        // from the motion detection service.
        // Binds to the service.
        Log.i(LOG_TAG, "Starting the service");
        Intent intent = new Intent(this, MyService.class);
        Log.i("LOG_TAG", "Trying to bind");
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }


    // Service connection code.
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder serviceBinder) {
            // We have bound to the service.
            MyBinder binder = (MyBinder) serviceBinder;
            MyService myService = binder.getService();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
    }

    //clear button
    public void clear (View view){
        TextView tvCheck = (TextView) findViewById(R.id.tvCheck);
        tvCheck.setText("Everything was quiet.");

        //update current time
        getStart = start.getTimeInMillis();

        MyServiceTask.movedFlag=false;
        Log.i("currTime", String.valueOf(getStart));
    }

    // Exits the app and stops service
    public void exit (View view){
        unbindService(serviceConnection);
        Log.i(LOG_TAG, "Stopping.");
        Intent intent = new Intent(this, MyService.class);
        stopService(intent);
        Log.i(LOG_TAG, "Stopped.");
        finish();
        System.exit(0);
    }

    //event bus for receiving message
    public void onEventMainThread(String result) {
        //Log.i(LOG_TAG, "Displaying: " + result);
        TextView tvCheck = (TextView) findViewById(R.id.tvCheck);
        tvCheck.setText(result);
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
