package com.testandroid.pagingtestapp.ui.dashboard.arch.news

import android.util.Log
import androidx.lifecycle.*
import com.testandroid.pagingtestapp.paging.ContentRepository.Companion.NETWORK_PAGE_SIZE
import com.testandroid.pagingtestapp.paging.ContentService
import com.testandroid.pagingtestapp.ui.dashboard.arch.ViewEntities
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewsViewModel(
    private val service :ContentService,
    private val endpoint :String
) :ViewModel() {
    class Factory(
        private val service :ContentService,
        private val endpoint :String
    ) :ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            NewsViewModel(service, endpoint) as T
    }

    private val newsData = MutableLiveData<List<ViewEntities>>(listOf(ViewEntities.Loading))
    val news :LiveData<List<ViewEntities>> get() = newsData
    private var lastPage = 1

    init {
        viewModelScope.launch {
            loadPage(lastPage)
        }
    }

    fun loadNewPage(){
        if(lastPage == 0) return
        viewModelScope.launch {
            loadPage(lastPage)
        }
    }

    private suspend fun loadPage(numberPage :Int) = withContext(Default) {
        val response = service.searchRepos("user/test-content/$endpoint", numberPage, NETWORK_PAGE_SIZE)
        if(response._meta?.page == response._meta?.total_pages) lastPage = 0
        else lastPage++
        Log.i("Jorik", "last page of $endpoint = $lastPage")
        val news = response.items
        val newItems :List<ViewEntities> = newsData.value!!
            .filterNot { it is ViewEntities.Loading }
            .plus(news.map { ViewEntities.ContentItem(it) })
            .let { if(lastPage > 0) it.plus(ViewEntities.Loading)
                else it
            }
        newsData.postValue(newItems)
    }
}