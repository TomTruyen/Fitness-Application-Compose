package com.tomtruyen.fitoryx.providers

import com.tomtruyen.fitoryx.App
import com.tomtruyen.core.common.providers.KoinReloadProvider

class KoinReloadProviderImpl: KoinReloadProvider {
    override fun reload() {
        App.reloadKoinModules()
    }
}