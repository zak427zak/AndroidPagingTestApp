package com.testandroid.pagingtestapp.ui.dashboard.arch

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.testandroid.pagingtestapp.ui.dashboard.arch.news.NewsFragment

class PagerAdapter(
    private val links :Array<String>,
    container :Fragment
) :FragmentStateAdapter(container){
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return NewsFragment(links[position])
    }
}