package com.itcluster.mobile.app.ext.shakedetector

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlin.math.sqrt

/**
 * Класс для детектирования тряски телефона
 *
 * @param sensorManager менеджер сенсоров
 * @param onShake коллбэк, который будет вызван в момент тряски
 */
class ShakeDetector(
    private val sensorManager: SensorManager?,
    onShake: () -> Unit
) : ShakeDetectorBase(sensorManager) {

    override var sensorEventListener = object : SensorEventListener {

        override fun onSensorChanged(event: SensorEvent?) {
            val x: Float = event?.values?.get(accelIndexOnXAxic) ?: accelDefaultValue
            val y: Float = event?.values?.get(accelIndexOnYAxic) ?: accelDefaultValue
            val z: Float = event?.values?.get(accelIndexOnZAxic) ?: accelDefaultValue

            accelLast = accelCurrent
            accelCurrent = sqrt(x * x + y * y + z * z)

            val delta = accelCurrent - accelLast
            accel = accel * alpha + delta

            if (accel > SHAKE_THRESHOLD) {
                accel = shakeThresholdDefault
                onShake.invoke()
            }
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    companion object {
        /**
         * Порог ограничения для учещения встряхивания, чтобы установить значение по умолчанию
         */
        private const val SHAKE_THRESHOLD = 12
    }
}