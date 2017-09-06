package com.naitiks.motionsensor.Kotlin

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

import com.naitiks.motionsensor.R

class MainActivity : AppCompatActivity() {
    private var sensorManager: SensorManager? = null
    private var motionSensor: Sensor? = null
    private var mShakeDetector: ShakeDetector? = null
    private var shakeCount: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        shakeCount = findViewById(R.id.txt_count) as TextView
        sensorManager = this@MainActivity.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        motionSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        mShakeDetector = ShakeDetector()
        mShakeDetector!!.setOnShakeListener(object : ShakeDetector.OnShakeListener {
            override fun onShake(count: Int) {
                handleShakeEvent(count)
            }
        })
    }

    private var oldCount = 0
    private fun handleShakeEvent(count: Int) {
        oldCount++
        shakeCount!!.animate()
                .alpha(0f)
                .setDuration(1)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        shakeCount!!.text = oldCount.toString()
                        shakeCount!!.alpha = 1f
                    }
                })

    }

    override fun onPause() {
        if (sensorManager != null)
            sensorManager!!.unregisterListener(mShakeDetector)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (sensorManager != null && motionSensor != null)
            sensorManager!!.registerListener(mShakeDetector, motionSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }
}
