package com.tomtruyen.fitoryx

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tomtruyen.data.repositories.interfaces.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean

class MainViewModel(
    private val userRepository: UserRepository,
): ViewModel() {
    var hasCheckedLoggedIn: AtomicBoolean = AtomicBoolean(false)
        private set

    suspend fun isLoggedIn(): Boolean {
        return userRepository.isLoggedIn().also {
            updateHasCheckedLoggedIn()
        }
    }

    fun updateHasCheckedLoggedIn() = viewModelScope.launch {
        // Simulate a little delay to allow for navigation to happen
        delay(50L)
        hasCheckedLoggedIn.set(true)
    }
}