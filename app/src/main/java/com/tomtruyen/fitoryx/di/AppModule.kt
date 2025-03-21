package com.tomtruyen.fitoryx.di

import android.content.Context
import com.tomtruyen.core.common.JsonInstance
import com.tomtruyen.core.common.ThemePreferences
import com.tomtruyen.core.common.providers.BuildConfigFieldProvider
import com.tomtruyen.core.common.providers.CredentialProvider
import com.tomtruyen.core.common.providers.KoinReloadProvider
import com.tomtruyen.core.common.utils.ImageLoader
import com.tomtruyen.fitoryx.BuildConfig
import com.tomtruyen.fitoryx.providers.BuildConfigFieldProviderImpl
import com.tomtruyen.fitoryx.providers.CredentialProviderImpl
import com.tomtruyen.fitoryx.providers.KoinReloadProviderImpl
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.PropertyConversionMethod
import io.github.jan.supabase.serializer.KotlinXSerializer
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import org.koin.dsl.module
import kotlin.time.Duration.Companion.seconds

val appModule = module {
    single<SupabaseClient> {
        createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_KEY
        ) {
            defaultSerializer = KotlinXSerializer(
                json = JsonInstance
            )

            install(Auth)
            install(Postgrest) {
                propertyConversionMethod = PropertyConversionMethod.SERIAL_NAME
            }
            install(Storage) {
                transferTimeout = 60.seconds
            }
        }
    }

    single<ImageLoader> {
        ImageLoader(
            storage = get<SupabaseClient>().storage
        )
    }

    single<CredentialProvider> {
        CredentialProviderImpl(
            googleServerClientId = BuildConfig.GOOGLE_SERVER_CLIENT_ID
        )
    }

    single<BuildConfigFieldProvider> {
        BuildConfigFieldProviderImpl(
            versionName = BuildConfig.VERSION_NAME,
            versionCode = BuildConfig.VERSION_CODE
        )
    }

    single<KoinReloadProvider> {
        KoinReloadProviderImpl()
    }

    single<ThemePreferences> {
        ThemePreferences(
            context = get<Context>()
        )
    }
}