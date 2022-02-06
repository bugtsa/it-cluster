package com.itcluster.mobile.app.ext.shakedetector

import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager

/**
 * Базовый класс для детектирования тряски телефона
 *
 * @param sensorManager менеджер сенсоров
 */
open class ShakeDetectorBase(private val sensorManager: SensorManager?) {

    protected val alpha = ALPHA
    protected val accelDefaultValue = ACCELERATION_DEFAULT_VALUE
    protected val accelIndexOnXAxic = ACCELERATION_INDEX_ON_X_AXIS
    protected val accelIndexOnYAxic = ACCELERATION_INDEX_ON_Y_AXIS
    protected val accelIndexOnZAxic = ACCELERATION_INDEX_ON_Z_AXIS
    protected val shakeThresholdDefault = SHAKE_THRESHOLD_DEFAULT

    protected var accel = shakeThresholdDefault
    protected var accelCurrent = SensorManager.GRAVITY_EARTH
    protected var accelLast = SensorManager.GRAVITY_EARTH

    protected open lateinit var sensorEventListener: SensorEventListener

    /**
     * Зарегестрировать слушателя
     */
    fun registerListener() {
        sensorManager?.registerListener(
            sensorEventListener,
            sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    /**
     * Отменить регистрацию слушателя
     */
    fun unregisterListener() {
        sensorManager?.unregisterListener(sensorEventListener)
    }

    companion object {

        /**
         * Значения для фильтра низких частот
         */
        private const val ALPHA = 0.9f

        /**
         * Значение по умолчнию для учещения встряхивания
         */
        private const val SHAKE_THRESHOLD_DEFAULT = 10f

        /**
         * Значение по умолчнию для осей x, y, z
         */
        private const val ACCELERATION_DEFAULT_VALUE = 1F

        /**
         * Индекс в массиве данных с датчика по оси x
         */
        private const val ACCELERATION_INDEX_ON_X_AXIS = 0

        /**
         * Индекс в массиве данных с датчика по оси y
         */
        private const val ACCELERATION_INDEX_ON_Y_AXIS = 1

        /**
         * Индекс в массиве данных с датчика по оси z
         */
        private const val ACCELERATION_INDEX_ON_Z_AXIS = 2
    }
}