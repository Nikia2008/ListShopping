package com.example.shoppinglist.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.shoppinglist.data.repository.ShoppingRepository
import com.example.shoppinglist.ui.ItemsViewModelFactory
import com.example.shoppinglist.ui.ListsHistoryViewModelFactory
import com.example.shoppinglist.ui.screens.history.HistoryScreen
import com.example.shoppinglist.ui.screens.history.HistoryViewModel
import com.example.shoppinglist.ui.screens.items.ItemsScreen
import com.example.shoppinglist.ui.screens.items.ItemsViewModel
import com.example.shoppinglist.ui.screens.lists.ListsScreen
import com.example.shoppinglist.ui.screens.lists.ListsViewModel
import com.example.shoppinglist.ui.screens.settings.SettingsScreen
import com.example.shoppinglist.ui.screens.settings.SettingsViewModel

@Composable
fun AppNavigation(repository: ShoppingRepository) {
    val navController = rememberNavController()
    val baseFactory = ListsHistoryViewModelFactory(repository)

    NavHost(navController = navController, startDestination = "lists") {
        composable("lists") {
            val vm: ListsViewModel = viewModel(factory = baseFactory)
            ListsScreen(
                viewModel = vm,
                onListClick = { listId -> navController.navigate("items/$listId") },
                onHistoryClick = { navController.navigate("history") },
                onSettingsClick = {navController.navigate("settings")}
            )
        }

        composable(
            route = "items/{listId}",
            arguments = listOf(navArgument("listId") { type = NavType.LongType })
        ) { entry ->
            val listId = entry.arguments?.getLong("listId") ?: 0L
            val vm: ItemsViewModel = viewModel(
                key = "items_$listId",
                factory = ItemsViewModelFactory(repository, listId)
            )
            ItemsScreen(viewModel = vm, onBack = { navController.popBackStack() })
        }

        composable("history") {
            val vm: HistoryViewModel = viewModel(factory = baseFactory)
            HistoryScreen(viewModel = vm, onBack = { navController.popBackStack() })
        }

        composable("settings") {
            val vm: SettingsViewModel = viewModel(factory = baseFactory)
            SettingsScreen(
                viewModel = vm,
                onBack = { navController.popBackStack() }
            )
        }
        
    }
}