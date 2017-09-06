package com.naitiks.motionsensor;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.FloatMath;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{
    private SensorManager sensorManager;
    private Sensor motionSensor = null;
    private ShakeDetector mShakeDetector;
    private TextView shakeCount = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        shakeCount = (TextView) findViewById(R.id.txt_count);
        sensorManager = (SensorManager) MainActivity.this.getSystemService(SENSOR_SERVICE);
        motionSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {
            @Override
            public void onShake(int count) {
                handleShakeEvent(count);
            }
        });
    }

    private int oldCount = 0;
    private void handleShakeEvent(int count){
        oldCount++;
        shakeCount.animate()
                .alpha(0f)
                .setDuration(1)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        shakeCount.setText(String.valueOf(oldCount));
                        shakeCount.setAlpha(1);
                    }
                });

    }

    @Override
    protected void onPause() {
        if(sensorManager!=null)
            sensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(sensorManager != null && motionSensor != null)
            sensorManager.registerListener(mShakeDetector, motionSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
}
