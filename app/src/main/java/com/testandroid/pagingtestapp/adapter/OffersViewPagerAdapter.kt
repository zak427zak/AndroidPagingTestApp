package com.testandroid.pagingtestapp.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.testandroid.pagingtestapp.MainActivity
import com.testandroid.pagingtestapp.data.NewContent
import com.testandroid.pagingtestapp.databinding.OffersViewPagerInterfaceBinding
import com.testandroid.pagingtestapp.paging.*
import com.testandroid.pagingtestapp.ui.dashboard.DashboardFragment

class OffersViewPagerAdapter(
    val context: Context?,
    private val items: List<String>,
    private val currentFragment: DashboardFragment,
) : RecyclerView.Adapter<OffersViewPagerAdapter.ViewHolder>() {

    private var onClickListener: OffersContentAdapter.OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            OffersViewPagerInterfaceBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentModel = ViewModelProvider(
            currentFragment, Injection.provideViewModelFactory(
                owner = currentFragment, "user/test-content/${items[position]}?"
            )
        )[SearchRepositoriesViewModel::class.java]

        holder.bindState(
            uiState = currentModel.state, uiActions = currentModel.accept, holder
        )
    }

    override fun getItemCount(): Int = items.size

    class ViewHolder(binding: OffersViewPagerInterfaceBinding) :RecyclerView.ViewHolder(binding.root) {
        val mainRecycler = binding.list
        val mainRecyclerPlaceholder = binding.emptyList
    }

    fun setOnClickListener(onClickListener: OffersContentAdapter.OnClickListener) {
        this.onClickListener = onClickListener
    }


    /**
     * Binds the [UiState] provided  by the [SearchRepositoriesViewModel] to the UI,
     * and allows the UI to feed back user actions to it.
     */
    private fun OffersViewPagerAdapter.ViewHolder.bindState(
        uiState: LiveData<UiState>, uiActions: (UiAction) -> Unit, holder: ViewHolder
    ) {

        val repoAdapter = ReposAdapter(context!!)
        holder.mainRecycler.adapter = repoAdapter

        repoAdapter.setOnClickListener(object : ReposAdapter.OnClickListener {
            override fun onClick(position: Int, model: NewContent) {

                when (model.contentType) {
                    "report" -> {
                        // TODO: Open report activity
                    }
                    "chat" -> {
                        // TODO: Call chat
                    }
                    "locked", "wishlist_changes", "price_changes" -> {
                        val intent = Intent(context, MainActivity::class.java)
                        ContextCompat.startActivity(
                            context, intent, Bundle.EMPTY
                        )
                    }
                    "custom_notification" -> {
                        // TODO: Open custom event
                    }
                    else -> {
                        // TODO: Open content card
                    }
                }
            }
        })
        bindList(
            repoAdapter = repoAdapter, uiState = uiState, onScrollChanged = uiActions, holder
        )
    }

    private fun OffersViewPagerAdapter.ViewHolder.bindList(
        repoAdapter: ReposAdapter,
        uiState: LiveData<UiState>,
        onScrollChanged: (UiAction.Scroll) -> Unit,
        holder: ViewHolder
    ) {
        setupScrollListener(onScrollChanged, holder)

        uiState.map(UiState::searchResult).distinctUntilChanged()
            .observe(currentFragment) { result ->
                when (result) {
                    is RepoSearchResult.Success -> {
                        showEmptyList(result.data.isEmpty(), holder)
                        repoAdapter.submitList(result.data)
                    }
                    is RepoSearchResult.Error -> {
                        Toast.makeText(
                            context!!, "Wooops $result.message}", Toast.LENGTH_LONG
                        ).show()
                        Log.d("Wooops", "$result.message}")
                    }
                }
            }
    }

    private fun OffersViewPagerAdapter.ViewHolder.showEmptyList(show: Boolean, holder: ViewHolder) {
        holder.mainRecyclerPlaceholder.isVisible = show
        holder.mainRecycler.isVisible = !show
    }

    private fun OffersViewPagerAdapter.ViewHolder.setupScrollListener(
        onScrollChanged: (UiAction.Scroll) -> Unit, holder: ViewHolder
    ) {
        val layoutManager = holder.mainRecycler.layoutManager as LinearLayoutManager
        holder.mainRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = layoutManager.itemCount
                val visibleItemCount = layoutManager.childCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                onScrollChanged(
                    UiAction.Scroll(
                        visibleItemCount = visibleItemCount,
                        lastVisibleItemPosition = lastVisibleItem,
                        totalItemCount = totalItemCount
                    )
                )
            }
        })
    }
}

