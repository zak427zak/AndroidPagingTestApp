package com.testandroid.pagingtestapp.ui.dashboard.arch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.testandroid.pagingtestapp.databinding.ItemLoadingBinding
import com.testandroid.pagingtestapp.databinding.RecyclerOffersContentCardBinding
import com.testandroid.pagingtestapp.paging.RepoViewHolder

private val COMPARATOR = object : DiffUtil.ItemCallback<ViewEntities>() {
    override fun areItemsTheSame(old: ViewEntities, new: ViewEntities): Boolean =
        when{
            old is ViewEntities.ContentItem && new is ViewEntities.ContentItem ->
                new.id == old.id
            else -> false
        }

    override fun areContentsTheSame(old: ViewEntities, new: ViewEntities): Boolean =
        when{
            old is ViewEntities.ContentItem && new is ViewEntities.ContentItem ->
                new.text == old.text
            else -> false
        }
}

class PagingAdapter(
    val loadMore :()->Unit
) : ListAdapter<ViewEntities, RecyclerView.ViewHolder>(COMPARATOR) {
    companion object {
        private val LOADING = 0
        private val ITEM = 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        return if(viewType == LOADING){
            Loading(ItemLoadingBinding.inflate(inflater, parent, false))
        } else {
            RepoViewHolder(RecyclerOffersContentCardBinding.inflate(inflater, parent, false), context)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(val item = getItem(position)){
            is ViewEntities.Loading -> {
                if(holder.bindingAdapterPosition == 0) return
                loadMore()
            }
            is ViewEntities.ContentItem -> (holder as RepoViewHolder).bind(item.content)
        }
    }

    override fun getItemViewType(position: Int): Int =
        if(getItem(position) is ViewEntities.Loading) LOADING else ITEM

    class Loading(binding :ItemLoadingBinding) :RecyclerView.ViewHolder(binding.root)
}