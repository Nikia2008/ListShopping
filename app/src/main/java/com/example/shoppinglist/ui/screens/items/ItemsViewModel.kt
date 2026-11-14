package com.example.shoppinglist.ui.screens.items

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.data.local.entity.CategoryEntity
import com.example.shoppinglist.data.local.entity.ItemEntity
import com.example.shoppinglist.data.repository.ShoppingRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ItemsViewModel(
    private val repository: ShoppingRepository,
    private val listId: Long
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _categoryFilter = MutableStateFlow<Long?>(null)
    val categoryFilter: StateFlow<Long?> = _categoryFilter

    val items: StateFlow<List<ItemEntity>> =
        combine(_searchQuery, _categoryFilter) { q, c -> q to c }
            .flatMapLatest { (q, c) -> repository.getFilteredItems(listId, q, c) }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val categories: StateFlow<List<CategoryEntity>> = repository.getCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onSearchQueryChange(query: String) { _searchQuery.value = query }
    fun onCategoryFilterChange(categoryId: Long?) { _categoryFilter.value = categoryId }

    fun addItem(name: String, quantity: String, unit: String, categoryId: Long?) {
        if (name.isBlank()) return
        viewModelScope.launch {
            repository.addItem(
                ItemEntity(
                    listId = listId,
                    name = name.trim(),
                    quantity = quantity.trim(),
                    unit = unit.trim(),
                    categoryId = categoryId
                )
            )
        }
    }

    fun togglePurchased(item: ItemEntity) {
        viewModelScope.launch {
            val newState = !item.isPurchased
            repository.updateItem(
                item.copy(
                    isPurchased = newState,
                    purchasedAt = if (newState) System.currentTimeMillis() else null
                )
            )
        }
    }

    fun deleteItem(item: ItemEntity) {
        viewModelScope.launch { repository.deleteItem(item) }
    }

    fun clearAll() {
        viewModelScope.launch { repository.clearList(listId) }
    }

    fun addCategory(name: String) {
        if (name.isBlank()) return
        viewModelScope.launch { repository.addCategory(name.trim()) }
    }

    fun updateQuantity(item: ItemEntity, newQuantity: String) {
        viewModelScope.launch {
            repository.updateItem(item.copy(quantity = newQuantity))
        }
    }

    fun updateItem(
        item: ItemEntity,
        newName: String,
        newQuantity: String,
        newUnit: String,
        newCategoryId: Long?
    ) {
        if (newName.isBlank()) return
        viewModelScope.launch {
            repository.updateItem(
                item.copy(
                    name = newName.trim(),
                    quantity = newQuantity.trim(),
                    unit = newUnit.trim(),
                    categoryId = newCategoryId
                )
            )
        }
    }
}