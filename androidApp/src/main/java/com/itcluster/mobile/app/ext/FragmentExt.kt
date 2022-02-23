package com.itcluster.mobile.app.ext

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Fragment.hideKeyboard() {
    view?.hideKeyboard()
}

fun Activity.hideSoftKeyboard() {
    val view: View = currentFocus ?: return
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
    imm?.hideSoftInputFromWindow(view.windowToken, 0)
}

/**
 * Устанавливает переданный бандл в аргументы фрагмента и возвращает вызываемый фрагмент
 * @param bundle - бандл с параметрами
 */
fun <T : Fragment> T.applyArguments(bundle: Bundle) = apply { arguments = bundle }