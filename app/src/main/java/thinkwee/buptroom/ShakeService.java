package thinkwee.buptroom;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by think on 20162016/10/11 001120:58
 * PACKAGE:thinkwee.buptroom
 * PROJECT:BuptRoom
 * TODO:摇一摇的Service，调用传感器
 */

public class ShakeService extends Service {
    public static final String TAG = "ShakeService";
    private SensorManager mSensorManager;
    public int flag=0;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Log.i(TAG,"Shake Service Create");
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mSensorManager.unregisterListener(mShakeListener);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        Log.i(TAG,"Shake Service Start");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        mSensorManager.registerListener(mShakeListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                //SensorManager.SENSOR_DELAY_GAME,
                50 * 1000); //batch every 50 milliseconds

        return super.onStartCommand(intent, flags, startId);
    }

    private final SensorEventListener mShakeListener = new SensorEventListener() {
        private static final float SENSITIVITY = 16;
        private static final int BUFFER = 5;
        private float[] gravity = new float[3];
        private float average = 0;
        private int fill = 0;

        @Override
        public void onAccuracyChanged(Sensor sensor, int acc) {
        }

        public void onSensorChanged(SensorEvent event) {
            final float alpha = 0.8F;

            for (int i = 0; i < 3; i++) {
                gravity[i] = alpha * gravity[i] + (1 - alpha) * event.values[i];
            }

            float x = event.values[0] - gravity[0];
            float y = event.values[1] - gravity[1];
            float z = event.values[2] - gravity[2];

            if (fill <= BUFFER) {
                average += Math.abs(x) + Math.abs(y) + Math.abs(z);
                fill++;
            } else {
                Log.i(TAG, "average:"+average);
                Log.i(TAG, "average / BUFFER:"+(average / BUFFER));
                if (average / BUFFER >= SENSITIVITY) {
                    handleShakeAction();
                }
                average = 0;
                fill = 0;
            }
        }
    };

    protected void handleShakeAction() {
        // TODO Auto-generated method stub
        Toast.makeText(getApplicationContext(), "摇个蛋蛋", Toast.LENGTH_SHORT).show();
        flag=1;
    }
    public  int getflag(){
        return flag;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }
}
