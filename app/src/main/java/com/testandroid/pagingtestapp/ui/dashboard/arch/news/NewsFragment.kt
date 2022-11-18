package com.testandroid.pagingtestapp.ui.dashboard.arch.news

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.testandroid.pagingtestapp.databinding.OffersViewPagerInterfaceBinding
import com.testandroid.pagingtestapp.paging.ReposAdapter
import com.testandroid.pagingtestapp.ui.dashboard.DashboardViewModel
import com.testandroid.pagingtestapp.ui.dashboard.arch.PagingAdapter

class NewsFragment() : Fragment() {
    constructor(link :String) :this(){
        arguments = bundleOf("link" to link)
    }

    private val containerViewmodel :DashboardViewModel by viewModels(ownerProducer = { requireParentFragment() })
    private val viewmodel :NewsViewModel by viewModels {
        NewsViewModel.Factory(containerViewmodel.contentService, requireArguments().getString("link", ""))
    }
    private val views by lazy { OffersViewPagerInterfaceBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = views.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = PagingAdapter{ viewmodel.loadNewPage() }
        views.list.adapter = adapter
        viewmodel.news.observe(viewLifecycleOwner){
            Log.i("jorik", it.joinToString("\n"))
            adapter.submitList(it)
        }
    }
}