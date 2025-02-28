package com.tomtruyen.fitoryx.providers

import com.tomtruyen.core.common.providers.KoinReloadProvider
import com.tomtruyen.fitoryx.App

class KoinReloadProviderImpl : KoinReloadProvider {
    override fun reload() {
        App.reloadKoinModules()
    }
}