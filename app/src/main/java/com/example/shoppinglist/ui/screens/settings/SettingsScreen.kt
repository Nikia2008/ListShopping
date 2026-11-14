package com.example.shoppinglist.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.shoppinglist.data.local.entity.CategoryEntity
import com.example.shoppinglist.data.local.entity.ShoppingListEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit
) {
    val lists by viewModel.lists.collectAsStateWithLifecycle()
    val categories by viewModel.categories.collectAsStateWithLifecycle()

    var showAddCategory by remember { mutableStateOf(false) }
    var categoryToDelete by remember { mutableStateOf<CategoryEntity?>(null) }
    var categoryToRename by remember { mutableStateOf<CategoryEntity?>(null) }
    var listToDelete by remember { mutableStateOf<ShoppingListEntity?>(null) }
    var listToRename by remember { mutableStateOf<ShoppingListEntity?>(null) }
    var showClearHistoryConfirm by remember { mutableStateOf(false) }
    var showResetAllConfirm by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Настройки", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item { SectionTitle("Категории") }
            item {
                SettingsCard {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Добавить новую", modifier = Modifier.weight(1f))
                        TextButton(onClick = { showAddCategory = true }) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Создать")
                        }
                    }
                }
            }
            if (categories.isEmpty()) {
                item { EmptyText("Нет категорий") }
            } else {
                items(categories) { cat ->
                    SettingsCard {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(cat.name, modifier = Modifier.weight(1f))
                            IconButton(onClick = { categoryToRename = cat }) {
                                Icon(Icons.Default.Edit, contentDescription = "Переименовать")
                            }
                            IconButton(onClick = { categoryToDelete = cat }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Удалить",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }

            item { SectionTitle("Списки покупок") }
            if (lists.isEmpty()) {
                item { EmptyText("Нет списков") }
            } else {
                items(lists) { list ->
                    SettingsCard {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(list.name, modifier = Modifier.weight(1f))
                            IconButton(onClick = { listToRename = list }) {
                                Icon(Icons.Default.Edit, contentDescription = "Переименовать")
                            }
                            IconButton(onClick = { listToDelete = list }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Удалить",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }

            item { SectionTitle("История") }
            item {
                SettingsCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Очистить историю покупок", modifier = Modifier.weight(1f))
                        TextButton(onClick = { showClearHistoryConfirm = true }) {
                            Text("Очистить", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }

            item { SectionTitle("Сброс") }
            item {
                SettingsCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Удалить все данные", modifier = Modifier.weight(1f))
                        TextButton(onClick = { showResetAllConfirm = true }) {
                            Text("Сбросить", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }

            item { Spacer(Modifier.height(32.dp)) }
        }
    }

    if (showAddCategory) {
        TextInputDialog(
            title = "Новая категория",
            initial = "",
            confirmText = "Создать",
            onConfirm = { viewModel.addCategory(it) },
            onDismiss = { showAddCategory = false }
        )
    }

    categoryToRename?.let { cat ->
        TextInputDialog(
            title = "Переименовать категорию",
            initial = cat.name,
            confirmText = "Сохранить",
            onConfirm = { viewModel.renameCategory(cat.id, it) },
            onDismiss = { categoryToRename = null }
        )
    }

    categoryToDelete?.let { cat ->
        ConfirmDialog(
            title = "Удалить категорию?",
            message = "Категория «${cat.name}» будет удалена. Товары останутся «Без категории».",
            confirmText = "Удалить",
            onConfirm = { viewModel.deleteCategory(cat) },
            onDismiss = { categoryToDelete = null }
        )
    }

    listToRename?.let { list ->
        TextInputDialog(
            title = "Переименовать список",
            initial = list.name,
            confirmText = "Сохранить",
            onConfirm = { viewModel.renameList(list.id, it) },
            onDismiss = { listToRename = null }
        )
    }

    listToDelete?.let { list ->
        ConfirmDialog(
            title = "Удалить список?",
            message = "Список «${list.name}» и все товары в нём будут удалены безвозвратно.",
            confirmText = "Удалить",
            onConfirm = { viewModel.deleteList(list) },
            onDismiss = { listToDelete = null }
        )
    }

    if (showClearHistoryConfirm) {
        ConfirmDialog(
            title = "Очистить историю?",
            message = "Все данные о купленных ранее товарах будут удалены.",
            confirmText = "Очистить",
            onConfirm = { viewModel.clearHistory() },
            onDismiss = { showClearHistoryConfirm = false }
        )
    }

    if (showResetAllConfirm) {
        ConfirmDialog(
            title = "Удалить ВСЕ данные?",
            message = "Будут удалены все списки, товары, категории и история. Действие необратимо.",
            confirmText = "Удалить всё",
            onConfirm = { viewModel.resetAll() },
            onDismiss = { showResetAllConfirm = false }
        )
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
    )
}

@Composable
private fun EmptyText(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
private fun SettingsCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) { content() }
    }
}

@Composable
private fun TextInputDialog(
    title: String,
    initial: String,
    confirmText: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var text by remember { mutableStateOf(initial) }
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        title = { Text(title, fontWeight = FontWeight.Bold) },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { if (it.length <= 60) text = it },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(text); onDismiss() },
                enabled = text.isNotBlank()
            ) { Text(confirmText) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )
}

@Composable
private fun ConfirmDialog(
    title: String,
    message: String,
    confirmText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        title = { Text(title, fontWeight = FontWeight.Bold) },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = { onConfirm(); onDismiss() }) {
                Text(confirmText, color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Отмена") }
        }
    )
}