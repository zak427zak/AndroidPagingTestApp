package com.testandroid.pagingtestapp.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.testandroid.pagingtestapp.R
import com.testandroid.pagingtestapp.adapter.OffersViewPagerAdapter
import com.testandroid.pagingtestapp.databinding.FragmentDashboardBinding
import com.testandroid.pagingtestapp.ui.dashboard.arch.PagerAdapter

class DashboardFragment : Fragment(), View.OnClickListener {
    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private var viewPager2: ViewPager2? = null

    private var newsFragment: ConstraintLayout? = null
    private var offersFragment: ConstraintLayout? = null
    private var notificationsFragment: ConstraintLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        dashboardViewModel = ViewModelProvider(this)[DashboardViewModel::class.java]

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Create variations carousel
        val itemsHeaders: List<String> = listOf("news", "offers", "notifications")
        //"user/test-content/${items[position]}?"
        //"news", "offers", "notifications"

        // Create viewer slider for content variations
        viewPager2 = binding.viewPager
//        val adapter = OffersViewPagerAdapter(context, itemsHeaders, this)
        val adapter = PagerAdapter(arrayOf("news", "offers", "notifications"), this)
        viewPager2!!.adapter = adapter

        viewPager2!!.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                dragChecker(viewPager2)
            }
        })

        // Linking with screens and tabs
        newsFragment = binding.includeTabs.clNewsBlock
        offersFragment = binding.includeTabs.clOffersBlock
        notificationsFragment = binding.includeTabs.clNotificationsBlock
        newsFragment!!.setOnClickListener(this)
        offersFragment!!.setOnClickListener(this)
        notificationsFragment!!.setOnClickListener(this)

        return root
    }

    private fun dragChecker(viewPager2: ViewPager2?) {
        when (viewPager2!!.currentItem) {
            0 -> {
                callFirstTabContent()
            }
            1 -> {
                callSecondTabContent()
            }
            2 -> {
                callThirdTabContent()
            }
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.clNewsBlock -> {
                callFirstTabContent()
                viewPager2!!.setCurrentItem(0, true)
            }
            R.id.clOffersBlock -> {
                callSecondTabContent()
                viewPager2!!.setCurrentItem(1, true)
            }
            R.id.clNotificationsBlock -> {
                callThirdTabContent()
                viewPager2!!.setCurrentItem(2, true)
            }
        }
    }

    private fun checkCurrentTab(checker: String) {
        when (checker) {
            "notifications" -> {
                callThirdTabContent()
                viewPager2!!.setCurrentItem(2, true)
            }
            "offers" -> {
                callSecondTabContent()
                viewPager2!!.setCurrentItem(1, true)
            }
            else -> {
                callFirstTabContent()
                viewPager2!!.setCurrentItem(0, true)
            }
        }

    }

    private fun callFirstTabContent() {
        binding.includeTabs.slNewsBlockSelected.visibility = View.VISIBLE
        binding.includeTabs.slNewsBlockUnselected.visibility = View.GONE
        binding.includeTabs.slOffersBlockSelected.visibility = View.GONE
        binding.includeTabs.slOffersBlockUnselected.visibility = View.VISIBLE
        binding.includeTabs.slNotificationsBlockSelected.visibility = View.GONE
        binding.includeTabs.slNotificationsBlockUnselected.visibility = View.VISIBLE
    }

    private fun callSecondTabContent() {
        binding.includeTabs.slNewsBlockSelected.visibility = View.GONE
        binding.includeTabs.slNewsBlockUnselected.visibility = View.VISIBLE
        binding.includeTabs.slOffersBlockSelected.visibility = View.VISIBLE
        binding.includeTabs.slOffersBlockUnselected.visibility = View.GONE
        binding.includeTabs.slNotificationsBlockSelected.visibility = View.GONE
        binding.includeTabs.slNotificationsBlockUnselected.visibility = View.VISIBLE
    }

    private fun callThirdTabContent() {
        binding.includeTabs.slNewsBlockSelected.visibility = View.GONE
        binding.includeTabs.slNewsBlockUnselected.visibility = View.VISIBLE
        binding.includeTabs.slOffersBlockSelected.visibility = View.GONE
        binding.includeTabs.slOffersBlockUnselected.visibility = View.VISIBLE
        binding.includeTabs.slNotificationsBlockSelected.visibility = View.VISIBLE
        binding.includeTabs.slNotificationsBlockUnselected.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}