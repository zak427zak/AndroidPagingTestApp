package com.testandroid.pagingtestapp.paging

import com.google.gson.annotations.SerializedName
import com.testandroid.pagingtestapp.data.NewContent
import com.testandroid.pagingtestapp.data.NewContentMeta

/**
 * Data class to hold repo responses from searchRepo API calls.
 */
data class RepoSearchResponse(
    @SerializedName("_meta") val _meta: NewContentMeta? = null,
    @SerializedName("items") val items: List<NewContent> = emptyList(),
    val nextPage: Int? = null
)

data class StatusUpdates(
    val contentUpdates :Updates,
    val howManyUnreaded :Int
)

data class Updates(
    val needUpdateNews :Boolean,
    val needUpdateNotifications :Boolean,
    val needUpdateOffers :Boolean
)
