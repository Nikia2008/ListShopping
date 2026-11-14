package com.example.shoppinglist.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.shoppinglist.data.local.entity.CategoryEntity
import com.example.shoppinglist.data.local.entity.ItemEntity
import com.example.shoppinglist.data.local.entity.ShoppingListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingListDao {

    @Query("SELECT * FROM shopping_lists ORDER BY createdAt DESC")
    fun getAllLists(): Flow<List<ShoppingListEntity>>

    @Insert
    suspend fun insertList(list: ShoppingListEntity): Long

    @Delete
    suspend fun deleteList(list: ShoppingListEntity)

    @Query("""
        SELECT * FROM items
        WHERE listId = :listId
          AND (:searchQuery = '' OR name LIKE '%' || :searchQuery || '%')
          AND (:categoryFilter IS NULL OR categoryId = :categoryFilter)
        ORDER BY isPurchased ASC, name ASC
    """)
    fun getFilteredItems(listId: Long, searchQuery: String, categoryFilter: Long?): Flow<List<ItemEntity>>


    @Insert
    suspend fun insertItem(item: ItemEntity): Long

    @Update
    suspend fun updateItem(item: ItemEntity)

    @Delete
    suspend fun deleteItem(item: ItemEntity)

    @Query("DELETE FROM items WHERE listId = :listId")
    suspend fun clearList(listId: Long)

    @Query("SELECT * FROM categories ORDER BY name")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Insert
    suspend fun insertCategory(category: CategoryEntity): Long

    @Query("SELECT * FROM items WHERE isPurchased = 1 AND purchasedAt IS NOT NULL ORDER BY purchasedAt DESC")
    fun getPurchaseHistory(): Flow<List<ItemEntity>>

    @Query("UPDATE shopping_lists SET name = :newName WHERE id = :id")
    suspend fun renameList(id: Long, newName: String)

    @Query("UPDATE categories SET name = :newName WHERE id = :id")
    suspend fun renameCategory(id: Long, newName: String)

    @Query("DELETE FROM items WHERE isPurchased = 1")
    suspend fun clearHistory()

    @Query("DELETE FROM items")
    suspend fun deleteAllItems()

    @Query("DELETE FROM categories")
    suspend fun deleteAllCategories()

    @Query("DELETE FROM shopping_lists")
    suspend fun deleteAllLists()

    @Delete
    suspend fun deleteCategory(category: CategoryEntity)

}