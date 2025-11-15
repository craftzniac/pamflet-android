package com.pamflet.features.settings.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.pamflet.core.domain.DeleteAllDecksUseCase
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pamflet.core.domain.DeleteAllDecksResult
import com.pamflet.shared.viewmodel.SharedUiEventViewModel
import kotlinx.coroutines.launch

class SettingsViewModelFactory(
    private val deleteAllDecksUseCase: DeleteAllDecksUseCase,
    private val sharedUiEventViewModel: SharedUiEventViewModel,
    private val refetchDecks: () -> Unit
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return SettingsViewModel(deleteAllDecksUseCase, sharedUiEventViewModel, refetchDecks) as T
    }
}

sealed class DeleteAllDecksActionStatus {
    data object Submitting : DeleteAllDecksActionStatus()
    data class Error(val message: String) : DeleteAllDecksActionStatus()
    data class Success(val message: String) : DeleteAllDecksActionStatus()
    data object NotStarted : DeleteAllDecksActionStatus()
}

class SettingsViewModel(
    private val deleteAllDecksUseCase: DeleteAllDecksUseCase,
    private val sharedUiEventViewModel: SharedUiEventViewModel,
    private val refetchDecks: () -> Unit
) : ViewModel() {
    private val _deleteAllDecksActionStatus = mutableStateOf<DeleteAllDecksActionStatus>(
        DeleteAllDecksActionStatus.NotStarted
    )
    val deleteAllDecksActionStatus: State<DeleteAllDecksActionStatus> = _deleteAllDecksActionStatus

    var isDeleteAllDeckDialogOpen by mutableStateOf(false)
        private set

    fun openDeleteDialog() {
        isDeleteAllDeckDialogOpen = true
    }

    fun closeDeleteDialog() {
        isDeleteAllDeckDialogOpen = false
    }

    fun deleteAll() {
        viewModelScope.launch {
            _deleteAllDecksActionStatus.value = DeleteAllDecksActionStatus.Submitting
            val res = deleteAllDecksUseCase()
            _deleteAllDecksActionStatus.value = when (res) {
                is DeleteAllDecksResult.Error -> {
                    sharedUiEventViewModel.emitSnackBarMessage(res.message)
                    DeleteAllDecksActionStatus.Error(message = res.message)
                }

                is DeleteAllDecksResult.Success -> {
                    sharedUiEventViewModel.emitSnackBarMessage(res.message)
                    DeleteAllDecksActionStatus.Success(message = res.message)
                }
            }
            closeDeleteDialog()
            refetchDecks()
        }
    }

    fun resetDeleteAllActionStatus() {
        _deleteAllDecksActionStatus.value = DeleteAllDecksActionStatus.NotStarted
    }
}