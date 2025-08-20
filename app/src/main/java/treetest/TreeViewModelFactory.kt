package com.example.treetest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TreeViewModelFactory(private val treeStorage: TreeStorage) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(TreeViewModel::class.java) -> {
                TreeViewModel(treeStorage) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}