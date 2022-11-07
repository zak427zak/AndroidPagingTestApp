package com.testandroid.pagingtestapp.paging

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * ViewModel for the [SearchRepositoriesActivity] screen.
 * The ViewModel works with the [ContentRepository] to get the data.
 */
class SearchRepositoriesViewModel(
    private val repository: ContentRepository,
    private val savedStateHandle: SavedStateHandle,
    private val repositoryUrl: String
) : ViewModel() {

    /**
     * Stream of immutable states representative of the UI.
     */
    val state: LiveData<UiState>

    /**
     * Processor of side effects from the UI which in turn feedback into [state]
     */
    val accept: (UiAction) -> Unit

    init {
        val queryLiveData = MutableLiveData(savedStateHandle[LAST_SEARCH_QUERY] ?: DEFAULT_QUERY)

        state = queryLiveData.distinctUntilChanged().switchMap { queryString ->
                liveData {
                    val uiState = repository.getSearchResultStream(queryString, repositoryUrl).map {
                            UiState(
                                query = queryString, searchResult = it
                            )
                        }.asLiveData(Dispatchers.Main)
                    emitSource(uiState)
                }
            }

        accept = { action ->
            when (action) {
                is UiAction.Search -> queryLiveData.postValue(action.query)
                is UiAction.Scroll -> if (action.shouldFetchMore) {
                    val immutableQuery = queryLiveData.value
                    if (immutableQuery != null) {
                        viewModelScope.launch {
                            repository.requestMore(immutableQuery, repositoryUrl)
                        }
                    }
                }
            }
        }
    }

    override fun onCleared() {
        savedStateHandle[LAST_SEARCH_QUERY] = state.value?.query
        super.onCleared()
    }
}

private val UiAction.Scroll.shouldFetchMore
    get() = visibleItemCount + lastVisibleItemPosition + VISIBLE_THRESHOLD >= totalItemCount

sealed class UiAction {
    data class Search(val query: String) : UiAction()
    data class Scroll(
        val visibleItemCount: Int, val lastVisibleItemPosition: Int, val totalItemCount: Int
    ) : UiAction()
}

data class UiState(
    val query: String, val searchResult: RepoSearchResult
)

private const val VISIBLE_THRESHOLD = 5
private const val LAST_SEARCH_QUERY: String = "last_search_query"
var DEFAULT_QUERY = "user/content/news?"