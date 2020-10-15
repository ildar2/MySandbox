package kz.ildar.sandbox.ui.main.sensor

import android.app.Activity
import android.app.Application
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import kz.ildar.sandbox.utils.EventWrapper
import kz.ildar.sandbox.utils.VoidEvent
import timber.log.Timber
import kotlin.math.absoluteValue

class SensorCallbacks(
    private val sensorManager: SensorManager?,
    private val sensor: Sensor? = sensorManager?.getDefaultSensor(Sensor.TYPE_PROXIMITY)
) : Application.ActivityLifecycleCallbacks {

    companion object {
        var enabled = true
        var mode = MutableLiveData(SensorMode.OPEN)

        fun toggle() {
            mode.value = if (mode.value == SensorMode.SECRET) SensorMode.OPEN
            else SensorMode.SECRET
        }
    }

    /**
     * Событие: юзер провёл пальцем около сенсора
     */
    val sensorEventLiveData = MutableLiveData<EventWrapper<VoidEvent>>()

    private var proximitySensorEventListener: SensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
            Timber.d("onAccuracyChanged: $sensor $accuracy")
        }

        override fun onSensorChanged(event: SensorEvent) {
            if (!enabled || event.sensor.type != Sensor.TYPE_PROXIMITY) return
            if (event.values[0].absoluteValue <= 4 && sensorEventLiveData.hasActiveObservers()) {
                sensorEventLiveData.value = VoidEvent.WRAPPED
            }
        }
    }

    override fun onActivityPaused(activity: Activity) {
        if (sensor != null) sensorManager?.unregisterListener(proximitySensorEventListener)
    }

    override fun onActivityResumed(activity: Activity) {
        if (sensor == null) return//"No Proximity Sensor!"
        else sensorManager?.registerListener(proximitySensorEventListener,
            sensor,
            SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onActivityStarted(activity: Activity) {
        //not needed
    }

    override fun onActivityDestroyed(activity: Activity) {
        //not needed
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        //not needed
    }

    override fun onActivityStopped(activity: Activity) {
        //not needed
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        //not needed
    }
}

enum class SensorMode {
    SECRET,
    OPEN
}