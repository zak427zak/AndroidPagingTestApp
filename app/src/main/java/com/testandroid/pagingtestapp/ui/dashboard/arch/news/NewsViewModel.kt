package com.testandroid.pagingtestapp.ui.dashboard.arch.news

import androidx.lifecycle.*
import com.testandroid.pagingtestapp.paging.ContentRepository.Companion.NETWORK_PAGE_SIZE
import com.testandroid.pagingtestapp.paging.ContentService
import com.testandroid.pagingtestapp.ui.dashboard.arch.ViewEntities
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewsViewModel(
    private val data :MutableLiveData<LinkedHashSet<ViewEntities>>,
    private val service :ContentService,
    private val endpoint :String
) :ViewModel() {
    class Factory(
        private val service :ContentService,
        private val data :MutableLiveData<LinkedHashSet<ViewEntities>>,
        private val endpoint :String
    ) :ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            NewsViewModel(data, service, endpoint) as T
    }

    val news :LiveData<LinkedHashSet<ViewEntities>> get() = data
    private var lastPage = 2

    fun loadNewPage(){
        if(lastPage == 0 || (news.value != null && news.value!!.size in 1 until NETWORK_PAGE_SIZE)){
            data.postValue(LinkedHashSet(data.value!!.filterIsInstance(ViewEntities.ContentItem::class.java)))
            return
        }
        viewModelScope.launch { loadPage(lastPage) }
    }

    private suspend fun loadPage(numberPage :Int) = withContext(Default) {
        val response = service.searchRepos("user/test-content/$endpoint", numberPage, NETWORK_PAGE_SIZE)
        if(response._meta?.page == response._meta?.total_pages) lastPage = 0
        else lastPage++
        val news = response.items
        val newItems :List<ViewEntities> = data.value!!
            .filterNot { it is ViewEntities.Loading }
            .plus(news.map { ViewEntities.ContentItem(it) })
            .let { if(lastPage > 0) it.plus(ViewEntities.Loading) else it }
        data.postValue(LinkedHashSet(newItems))
    }
}