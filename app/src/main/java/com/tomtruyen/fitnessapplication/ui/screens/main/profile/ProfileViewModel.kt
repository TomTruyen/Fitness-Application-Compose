package com.tomtruyen.fitnessapplication.ui.screens.main.profile

import com.tomtruyen.fitnessapplication.base.BaseViewModel
import com.tomtruyen.fitnessapplication.repositories.interfaces.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow

class ProfileViewModel(
    private val userRepository: UserRepository
): BaseViewModel<ProfileNavigationType>() {
    val state = MutableStateFlow(ProfileUiState())

    private fun logout() {
        userRepository.logout()
        navigate(ProfileNavigationType.Logout)
    }

    fun onEvent(event: ProfileUiEvent) {
        when(event) {
            is ProfileUiEvent.Logout -> logout()
        }
    }
}
