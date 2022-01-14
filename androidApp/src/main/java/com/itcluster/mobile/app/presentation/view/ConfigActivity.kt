package com.itcluster.mobile.app.presentation.view

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.itcluster.mobile.app.R
import com.itcluster.mobile.app.databinding.ActivityConfigBinding
import com.itcluster.mobile.feature.config.di.ConfigFactory
import com.itcluster.mobile.feature.config.presentation.ConfigViewModel
import dagger.hilt.android.AndroidEntryPoint
import dev.icerock.moko.mvvm.MvvmEventsActivity
import dev.icerock.moko.mvvm.createViewModelFactory
import dev.icerock.moko.mvvm.dispatcher.eventsDispatcherOnMain
import javax.inject.Inject
import com.itcluster.mobile.app.BR

// MvvmEventsActivity for simplify creation of MVVM screen with https://github.com/icerockdev/moko-mvvm
@AndroidEntryPoint
class ConfigActivity :
    MvvmEventsActivity<ActivityConfigBinding, ConfigViewModel, ConfigViewModel.EventsListener>(),
    ConfigViewModel.EventsListener {

    override val layoutId: Int = R.layout.activity_config
    override val viewModelClass: Class<ConfigViewModel> = ConfigViewModel::class.java
    override val viewModelVariableId: Int = BR.viewModel

    @Inject
    lateinit var factory: ConfigFactory

    // createViewModelFactory is extension from https://github.com/icerockdev/moko-mvvm
    // ViewModel not recreating at configuration changes
    override fun viewModelFactory(): ViewModelProvider.Factory = createViewModelFactory {
        factory.createConfigViewModel(
            eventsDispatcher = eventsDispatcherOnMain()
        )
    }

    // route called by EventsDispatcher from ViewModel (https://github.com/icerockdev/moko-mvvm)
    override fun routeToNews() {
        Intent(this, NewsActivity::class.java).also { startActivity(it) }
    }
}
