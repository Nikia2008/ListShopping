package com.example.shoppinglist.ui.screens.lists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.data.local.entity.ShoppingListEntity
import com.example.shoppinglist.data.repository.ShoppingRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ListsViewModel(private val repository: ShoppingRepository) : ViewModel() {
    val lists = repository.getAllLists()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun createList(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch { repository.createList(name.trim()) }
    }

    fun deleteList(list: ShoppingListEntity) {
        viewModelScope.launch { repository.deleteList(list) }
    }

    fun renameList(id: Long, newName: String) {
        if (newName.isBlank()) return
        viewModelScope.launch { repository.renameList(id, newName.trim()) }
    }
}