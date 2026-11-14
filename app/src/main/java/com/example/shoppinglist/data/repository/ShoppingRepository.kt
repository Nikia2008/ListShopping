package com.example.shoppinglist.data.repository

import com.example.shoppinglist.data.local.ShoppingListDao
import com.example.shoppinglist.data.local.entity.CategoryEntity
import com.example.shoppinglist.data.local.entity.ItemEntity
import com.example.shoppinglist.data.local.entity.ShoppingListEntity
import kotlinx.coroutines.flow.Flow

class ShoppingRepository(private val dao: ShoppingListDao) {

    fun getAllLists(): Flow<List<ShoppingListEntity>> = dao.getAllLists()
    suspend fun createList(name: String) = dao.insertList(ShoppingListEntity(name = name))
    suspend fun deleteList(list: ShoppingListEntity) = dao.deleteList(list)

    fun getFilteredItems(listId: Long, query: String, categoryId: Long?): Flow<List<ItemEntity>> =
        dao.getFilteredItems(listId, query, categoryId)

    suspend fun addItem(item: ItemEntity) = dao.insertItem(item)
    suspend fun updateItem(item: ItemEntity) = dao.updateItem(item)
    suspend fun deleteItem(item: ItemEntity) = dao.deleteItem(item)
    suspend fun clearList(listId: Long) = dao.clearList(listId)

    fun getCategories(): Flow<List<CategoryEntity>> = dao.getAllCategories()
    suspend fun addCategory(name: String) = dao.insertCategory(CategoryEntity(name = name))

    suspend fun deleteCategory(category: CategoryEntity) = dao.deleteCategory(category)
    fun getHistory(): Flow<List<ItemEntity>> = dao.getPurchaseHistory()

    suspend fun renameList(id: Long, newName: String) = dao.renameList(id, newName)
    suspend fun renameCategory(id: Long, newName: String) = dao.renameCategory(id, newName)
    suspend fun clearHistory() = dao.clearHistory()

    suspend fun deleteAllData() {
        dao.deleteAllItems()
        dao.deleteAllCategories()
        dao.deleteAllLists()
    }
}