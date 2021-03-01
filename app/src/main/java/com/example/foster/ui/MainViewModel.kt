package com.example.foster.ui

import android.os.Bundle
import androidx.annotation.MainThread
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.core.os.bundleOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.foster.ui.ScreenName.HOME
import com.example.foster.ui.ScreenName.valueOf
import com.example.foster.utils.getMutableStateOf
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

enum class ScreenName { HOME, }

sealed class Screen(val id: ScreenName) {
    object Home : Screen(HOME)
}

private fun Screen.toBundle(): Bundle {
    return bundleOf(SIS_NAME to id.name).also {
        //
    }
}

private fun Bundle.toScreen(): Screen {
    val screenName = valueOf(getStringOrThrow(SIS_NAME))
    return when (screenName) {
        HOME -> Screen.Home
    }
}

private fun Bundle.getStringOrThrow(key: String) =
    requireNotNull(getString(key)) { "Missing key '$key' in $this ." }

private const val SIS_SCREEN = "sis_screen"
private const val SIS_NAME = "screen_name"
private const val SIS_POST = "post"

@HiltViewModel
class MainViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var currentScreen: Screen by savedStateHandle.getMutableStateOf<Screen>(
        key = SIS_SCREEN,
        default = Screen.Home,
        save = { it.toBundle() },
        restore = { it.toScreen() }
    )

    @MainThread
    fun onBack() : Boolean {
        val wasHandle = currentScreen != Screen.Home
        currentScreen = Screen.Home
        return wasHandle
    }

    @MainThread
    fun navigateTo(screen: Screen) {
        currentScreen = screen
    }
}