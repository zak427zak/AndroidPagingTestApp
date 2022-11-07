package com.testandroid.pagingtestapp.paging

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner

/**
 * Factory for ViewModels
 */
class ViewModelFactory(
    owner: SavedStateRegistryOwner,
    private val repository: ContentRepository,
    private val repositoryUrl: String
) : AbstractSavedStateViewModelFactory(owner, null) {

    override fun <T : ViewModel?> create(
        key: String, modelClass: Class<T>, handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(SearchRepositoriesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST") return SearchRepositoriesViewModel(
                repository, handle, repositoryUrl
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
