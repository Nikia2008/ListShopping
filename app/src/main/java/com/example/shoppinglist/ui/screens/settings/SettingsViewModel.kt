package com.example.shoppinglist.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.data.local.entity.CategoryEntity
import com.example.shoppinglist.data.local.entity.ShoppingListEntity
import com.example.shoppinglist.data.repository.ShoppingRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: ShoppingRepository) : ViewModel() {
    val lists = repository.getAllLists()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categories = repository.getCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addCategory(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch { repository.addCategory(name.trim()) }
    }

    fun deleteCategory(category: CategoryEntity) {
        viewModelScope.launch { repository.deleteCategory(category) }
    }

    fun renameCategory(id: Long, newName: String) {
        if (newName.isBlank()) return
        viewModelScope.launch { repository.renameCategory(id, newName.trim()) }
    }

    fun deleteList(list: ShoppingListEntity) {
        viewModelScope.launch { repository.deleteList(list) }
    }

    fun renameList(id: Long, newName: String) {
        if (newName.isBlank()) return
        viewModelScope.launch { repository.renameList(id, newName.trim()) }
    }

    fun clearHistory() {
        viewModelScope.launch { repository.clearHistory() }
    }

    fun resetAll() {
        viewModelScope.launch { repository.deleteAllData() }
    }
}