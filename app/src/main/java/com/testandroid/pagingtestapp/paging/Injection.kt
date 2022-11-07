package com.testandroid.pagingtestapp.paging

import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner

/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injection {

    /**
     * Creates an instance of [ContentRepository] based on the [ContentService]
     */
    private fun provideGithubRepository(): ContentRepository {
        return ContentRepository(ContentService.create())
    }

    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    fun provideViewModelFactory(
        owner: SavedStateRegistryOwner,
        repositoryUrl: String
    ): ViewModelProvider.Factory {
        return ViewModelFactory(owner, provideGithubRepository(), repositoryUrl)
    }
}
