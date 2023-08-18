package com.tomtruyen.fitnessapplication.helpers

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.tomtruyen.fitnessapplication.model.FetchedData

class GlobalProvider(
    val context: Context,
    val isBottomBarVisible: MutableState<Boolean> = mutableStateOf(false),
    val fetchedData: FetchedData = FetchedData()
)
