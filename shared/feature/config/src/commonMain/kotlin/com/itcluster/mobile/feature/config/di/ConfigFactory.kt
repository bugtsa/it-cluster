package com.itcluster.mobile.feature.config.di

import dev.icerock.moko.mvvm.dispatcher.EventsDispatcher
import com.itcluster.mobile.feature.config.model.ConfigStore
import com.itcluster.mobile.feature.config.presentation.ConfigViewModel

class ConfigFactory(
    private val configStore: ConfigStore,
    private val validations: ConfigViewModel.Validations,
    private val defaultToken: String,
    private val defaultLanguage: String
) {
    fun createConfigViewModel(
        eventsDispatcher: EventsDispatcher<ConfigViewModel.EventsListener>
    ) = ConfigViewModel(
        eventsDispatcher = eventsDispatcher,
        configStore = configStore,
        validations = validations,
        defaultToken = defaultToken,
        defaultLanguage = defaultLanguage
    )
}
