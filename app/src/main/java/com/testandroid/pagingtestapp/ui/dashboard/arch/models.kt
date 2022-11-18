package com.testandroid.pagingtestapp.ui.dashboard.arch

import com.testandroid.pagingtestapp.data.NewContent
import java.io.Serializable

sealed interface ViewEntities {
    object Loading :ViewEntities
    class ContentItem(val content :NewContent) : ViewEntities, Serializable {
        val id: String = content.id
        val complexId: Int = content.complexId
        val title: String = content.title
        val text: String = content.text
        val description: String = content.description
        val image: String = content.image
        val createdAt: Long = content.createdAt
        val innerContentType: String = content.innerContentType
        val isViewed: String = content.isViewed
        val isReport: String = content.isReport
        val isCustomNotification: String = content.isCustomNotification
        val contentType: String = content.contentType
        val reportId: String = content.reportId
        val globalContentType: String = content.globalContentType
    }
}