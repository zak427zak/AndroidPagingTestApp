package com.testandroid.pagingtestapp.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.testandroid.pagingtestapp.paging.ContentRepository.Companion.NETWORK_PAGE_SIZE
import com.testandroid.pagingtestapp.paging.ContentService
import com.testandroid.pagingtestapp.ui.dashboard.arch.ViewEntities
import kotlinx.coroutines.*
import java.util.*

class DashboardViewModel : ViewModel() {
    val contentService = ContentService.create()
    val timer = Timer()
    var updateJob :Job? = null
    var countOffers = 0
    var countNews = 0
    var countNotifications = 0
    val offers = MutableLiveData<LinkedHashSet<ViewEntities>>()
    val news = MutableLiveData<LinkedHashSet<ViewEntities>>()
    val notifications = MutableLiveData<LinkedHashSet<ViewEntities>>()

    init {

        viewModelScope.launch {
            val newsJob = async { loadFirstPage("news", news) }
            val offersJob = async { loadFirstPage("offers", offers) }
            val notifJob = async { loadFirstPage("notifications", notifications) }
            countNews = newsJob.await()
            countOffers = offersJob.await()
            countNotifications = notifJob.await()

            timer.schedule(object :TimerTask(){
                override fun run() {
                    if(updateJob != null) return
                    checkUpdates()
                }
            }, 1_000, 1_000)
        }

    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    private fun checkUpdates(){
        updateJob = viewModelScope.launch {
            val response = contentService.checkUpdates(countNotifications, countNews, countOffers)
            val jobsList = mutableListOf<Deferred<*>>()
            if(response.contentUpdates.needUpdateNews){
                jobsList.add(async { updateFirstPage("news", news) })
            }
            if(response.contentUpdates.needUpdateNotifications){
                jobsList.add(async { updateFirstPage("notifications", notifications) })
            }
            if(response.contentUpdates.needUpdateOffers) {
                jobsList.add(async { updateFirstPage("offers", offers) })
            }
            jobsList.forEach { it.await() }
            updateJob = null
        }
    }

    private suspend fun loadFirstPage(endpoint :String, receiver :MutableLiveData<LinkedHashSet<ViewEntities>>) :Int {
        val response = contentService.searchRepos("user/test-content/$endpoint", 1, NETWORK_PAGE_SIZE)
        receiver.postValue(LinkedHashSet(response.items.map{ ViewEntities.ContentItem(it) } + ViewEntities.Loading) )
        return response._meta!!.total_items
    }

    private suspend fun updateFirstPage(endpoint: String, receiver: MutableLiveData<LinkedHashSet<ViewEntities>>){
        val response = contentService.searchRepos("user/test-content/$endpoint", 1, NETWORK_PAGE_SIZE)
        receiver.postValue(addNewToHead(response.items.map{ ViewEntities.ContentItem(it) }, receiver.value!!.toList()))
        val newCount = response._meta!!.total_items
        when(endpoint){
            "offers" -> countOffers = newCount
            "notifications" -> countNotifications = newCount
            else /*news*/ -> countNews = newCount
        }
    }

    private fun addNewToHead(newItems :List<ViewEntities>, other :List<ViewEntities>) : LinkedHashSet<ViewEntities>{
        val difference :MutableSet<ViewEntities> = (newItems.toSet().minus(other.toSet())).toMutableSet()
        difference.addAll(other)
        return difference as LinkedHashSet<ViewEntities>
    }
}