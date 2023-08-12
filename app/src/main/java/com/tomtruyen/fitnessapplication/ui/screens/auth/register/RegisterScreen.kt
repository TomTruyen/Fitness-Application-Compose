package com.tomtruyen.fitnessapplication.ui.screens.auth.register

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph

@RootNavGraph
@Destination
@Composable
fun RegisterScreen() {
    RegisterScreenLayout()
}

@Composable
fun RegisterScreenLayout() {
    Text(text = "RegisterScreen")
}
