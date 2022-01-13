package com.itcluster.mobile.app.view.base

import android.os.Bundle
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

sealed class NavigationCommand {

    data class To(
        val destination: Int,
        val args: Bundle? = null,
        val options: NavOptions? = null,
        val extras: Navigator.Extras? = null
    ) : NavigationCommand()

    data class Dir(
        val directions: NavDirections,
        val options: NavOptions? = null
    ) : NavigationCommand()

    data class DirForward(
        val directions: NavDirections,
        val forwardDirections: NavDirections,
        val options: NavOptions? = null
    ) : NavigationCommand()

    object StartLogin : NavigationCommand()

    object FinishLogin : NavigationCommand()

    object Yacht : NavigationCommand()

    object Logout : NavigationCommand()

    object Back : NavigationCommand()
}