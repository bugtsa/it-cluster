package com.itcluster.mobile.app.view.base

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.itcluster.mobile.app.R

abstract class BaseActivity: AppCompatActivity() {

    lateinit var navController: NavController

    override fun setContentView(view: View?) {
        super.setContentView(view)

        navController = findNavController(R.id.root_nav_host)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    open fun subscribeOnNavigation(command: NavigationCommand) {
        when (command) {
            is NavigationCommand.Back -> navController.popBackStack()
            is NavigationCommand.To -> {
                navController.navigate(
                    command.destination,
                    command.args,
                    command.options,
                    command.extras
                )
            }
            is NavigationCommand.Dir -> {
                navController.navigate(
                    command.directions,
                    command.options
                )
            }
            is NavigationCommand.DirForward -> {
                navController.navigate(
                    command.directions
                )
                subscribeOnNavigation(
                    NavigationCommand.Dir(
                        command.forwardDirections,
                        command.options
                    )
                )
            }
//            is NavigationCommand.FinishLogin -> {
//                navController.graph.startDestination = R.id.nested_main
//                navController.navigate(R.id.action_global_to_main)
//            }
        }
    }

}
