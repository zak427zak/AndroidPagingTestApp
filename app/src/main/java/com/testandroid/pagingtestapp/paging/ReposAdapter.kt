package com.testandroid.pagingtestapp.paging

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.testandroid.pagingtestapp.data.NewContent
import com.testandroid.pagingtestapp.databinding.RecyclerOffersContentCardBinding

/**
 * Adapter for the list of repositories.
 */
class ReposAdapter(private val context: Context) :
    ListAdapter<NewContent, RepoViewHolder>(REPO_COMPARATOR) {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        return RepoViewHolder(
            RecyclerOffersContentCardBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), context
        )
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        val repoItem = getItem(position)
        if (repoItem != null) {
            holder.bind(repoItem)
        }
        holder.setOnClickListener(onClickListener!!)
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<NewContent>() {
            override fun areItemsTheSame(oldItem: NewContent, newItem: NewContent): Boolean =
                oldItem.text == newItem.text

            override fun areContentsTheSame(oldItem: NewContent, newItem: NewContent): Boolean =
                oldItem == newItem
        }
    }

    interface OnClickListener {
        fun onClick(position: Int, model: NewContent)
    }
}
