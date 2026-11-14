package com.example.shoppinglist.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shoppinglist.data.repository.ShoppingRepository
import com.example.shoppinglist.ui.screens.history.HistoryViewModel
import com.example.shoppinglist.ui.screens.items.ItemsViewModel
import com.example.shoppinglist.ui.screens.lists.ListsViewModel
import com.example.shoppinglist.ui.screens.settings.SettingsViewModel

class ListsHistoryViewModelFactory(private val repository: ShoppingRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ListsViewModel::class.java) ->
                ListsViewModel(repository) as T
            modelClass.isAssignableFrom(HistoryViewModel::class.java) ->
                HistoryViewModel(repository) as T
            modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
                SettingsViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
        }

    }
}

class ItemsViewModelFactory(
    private val repository: ShoppingRepository,
    private val listId: Long
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItemsViewModel::class.java)) {
            return ItemsViewModel(repository, listId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}

