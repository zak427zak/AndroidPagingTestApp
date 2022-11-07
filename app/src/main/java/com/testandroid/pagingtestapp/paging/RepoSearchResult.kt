package com.testandroid.pagingtestapp.paging

import com.testandroid.pagingtestapp.data.NewContent
import java.lang.Exception

/**
 * RepoSearchResult from a search, which contains List<Repo> holding query data,
 * and a String of network error state.
 */
sealed class RepoSearchResult {
    data class Success(val data: List<NewContent>) : RepoSearchResult()
    data class Error(val error: Exception) : RepoSearchResult()
}