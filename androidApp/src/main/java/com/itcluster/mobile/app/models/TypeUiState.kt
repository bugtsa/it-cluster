package com.itcluster.mobile.app.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


sealed class TypeUiState(val humanValue: String): Parcelable {

    @Parcelize
    object Addition: TypeUiState(ADDITION_STRING_TYPE)

    @Parcelize
    object Emission: TypeUiState(EMISSION_STRING_TYPE)

    @Parcelize
    object Decrease: TypeUiState(DECREASE_STRING_TYPE)

    @Parcelize
    object Burning: TypeUiState(BURNING_STRING_TYPE)

    @Parcelize
    object Unknown: TypeUiState("Unknown")

    companion object {

        fun Int.toTypeUiStase(): TypeUiState =
            when(this) {
                ADDITION_VALUE -> Addition
                EMISSION_VALUE -> Emission
                DECREASE_VALUE -> Decrease
                BURNING_VALUE -> Burning
                else -> Unknown
            }

        private const val ADDITION_STRING_TYPE = "прибавление"
        private const val EMISSION_STRING_TYPE = "эмиссия"
        private const val DECREASE_STRING_TYPE = "убавление"
        private const val BURNING_STRING_TYPE = "сжигание"

        private const val ADDITION_VALUE = 1
        private const val EMISSION_VALUE = 2
        private const val DECREASE_VALUE = -1
        private const val BURNING_VALUE = -2
    }
}