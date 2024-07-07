package com.tomtruyen.fitnessapplication.providers

import com.tomtruyen.fitnessapplication.App
import com.tomtruyen.models.providers.KoinReloadProvider

class KoinReloadProviderImpl: KoinReloadProvider {
    override fun reload() {
        App.reloadKoinModules()
    }
}