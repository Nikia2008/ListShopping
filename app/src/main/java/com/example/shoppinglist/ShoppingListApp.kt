package com.example.shoppinglist

import android.app.Application
import com.example.shoppinglist.data.local.ShoppingListDatabase
import com.example.shoppinglist.data.repository.ShoppingRepository

class ShoppingListApp : Application() {
    val database by lazy { ShoppingListDatabase.getInstance(this) }
    val repository by lazy { ShoppingRepository(database.dao()) }
}