package com.testandroid.pagingtestapp.data

import java.io.Serializable

data class NewContentMeta(
    val page: Int,
    val per_page: Int,
    val total_items: Int,
    val total_pages: Int,
    val current_page: Int
) : Serializable