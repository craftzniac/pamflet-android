package com.pamflet.shared.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class SharedUiEventViewModel : ViewModel() {
    var snackBarMessage by mutableStateOf("")
        private set

    fun clearSnackBarMessage() {
        snackBarMessage = ""
    }

    fun emitSnackBarMessage(msg: String) {
        snackBarMessage = msg
    }
}