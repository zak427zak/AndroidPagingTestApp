package com.testandroid.pagingtestapp.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.testandroid.pagingtestapp.paging.ContentService

class DashboardViewModel : ViewModel() {
    val contentService = ContentService.create()

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
}