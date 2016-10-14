package thinkwee.buptroom;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by think on 20162016/10/11 001120:58
 * PACKAGE:thinkwee.buptroom
 * PROJECT:BuptRoom
 * TODO:摇一摇的Service，调用传感器
 */

public class ShakeService extends Service {
    public static final String TAG = "ShakeService";
    private SensorManager mSensorManager;
    public boolean flag=false;
    private ShakeBinder shakebinder= new ShakeBinder();

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
        flag=false;
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
        private static final float SENSITIVITY = 10;
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
                    handleShakeAction();//如果达到阈值则处理摇一摇响应
                }
                average = 0;
                fill = 0;
            }
        }
    };

    protected void handleShakeAction() {
        // TODO Auto-generated method stub
        flag=true;
        Toast.makeText(getApplicationContext(), "摇一摇成功", Toast.LENGTH_SHORT).show();
        Intent intent= new Intent();
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName(this,"thinkwee.buptroom.ShakeTestActivity");
        startActivity(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return shakebinder;
    }
    class ShakeBinder extends Binder{

    }
}
