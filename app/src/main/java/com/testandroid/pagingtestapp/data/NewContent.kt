package com.testandroid.pagingtestapp.data

import java.io.Serializable

data class NewContent(
    val id: String,
    val complexId: Int,
    val title: String,
    val text: String,
    val description: String,
    val image: String,
    val createdAt: Long,
    val innerContentType: String,
    val isViewed: String,
    val isReport: String,
    val isCustomNotification: String,
    val contentType: String,
    val reportId: String,
    val globalContentType: String
) : Serializable