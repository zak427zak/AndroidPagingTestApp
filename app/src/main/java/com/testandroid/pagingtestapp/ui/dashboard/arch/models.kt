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

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is ContentItem) return false

            if (content != other.content) return false
            if (id != other.id) return false
            if (complexId != other.complexId) return false
            if (title != other.title) return false
            if (text != other.text) return false
            if (description != other.description) return false
            if (image != other.image) return false
            if (createdAt != other.createdAt) return false
            if (innerContentType != other.innerContentType) return false
            if (isViewed != other.isViewed) return false
            if (isReport != other.isReport) return false
            if (isCustomNotification != other.isCustomNotification) return false
            if (contentType != other.contentType) return false
            if (reportId != other.reportId) return false
            if (globalContentType != other.globalContentType) return false

            return true
        }

        override fun hashCode(): Int = id.toInt()

    }
}