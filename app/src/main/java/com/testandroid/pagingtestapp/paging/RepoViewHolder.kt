package com.testandroid.pagingtestapp.paging

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.testandroid.pagingtestapp.R
import com.testandroid.pagingtestapp.data.NewContent
import com.testandroid.pagingtestapp.databinding.RecyclerOffersContentCardBinding
import com.testandroid.pagingtestapp.tools.Converters

/**
 * View Holder for a [Repo] RecyclerView list item.
 */
class RepoViewHolder(binding: RecyclerOffersContentCardBinding, private val context: Context?) :
    RecyclerView.ViewHolder(binding.root) {

    private var onClickListener: ReposAdapter.OnClickListener? = null

    private val tvCategory = binding.tvContentCardCategory
    private val tvCategoryNotifications = binding.tvContentCardCategoryNotifications
    private val tvTitle = binding.textView2
    private val tvImage = binding.imageView
    private val imageViewNotifications = binding.imageViewNotifications
    private val tvImageCard = binding.cardView
    private val tvImageCardBlock = binding.cardViewBlock
    private val tvImageCardNotifications = binding.cardViewNotifications
    private val tvDate = binding.textView6
    private val rlTextAndImage = binding.rlContentCard
    private val rlMarker = binding.rlNotificationsReadedMarker
    private val rlMarkerCustoms = binding.rlNotificationsReadedMarkerCustom

    private var repo: NewContent? = null
    var myTools: Converters? = Converters(context)


    fun setOnClickListener(onClickListener: ReposAdapter.OnClickListener) {
        this.onClickListener = onClickListener
    }

    fun bind(repo: NewContent) {
        if (repo == null) {
            val resources = itemView.resources
        } else {
            showRepoData(repo)
        }
    }

    private fun showRepoData(repo: NewContent) {
        this.repo = repo

        val date = repo.createdAt
        val toDate = myTools!!.convertTimestamp(date.toString(), "normal")
        tvDate.text = toDate.toString()

        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val topMargin: Int =
            "-${context!!.resources.getDimension(com.intuit.sdp.R.dimen._7sdp).toInt()}".toInt()
        val bottomMargin: Int =
            "-${context.resources.getDimension(com.intuit.sdp.R.dimen._2sdp).toInt()}".toInt()

        when (position) {
            0 -> params.setMargins(
                0,
                "-${context.resources.getDimension(com.intuit.sdp.R.dimen._4sdp).toInt()}".toInt(),
                0,
                0
            )
            else -> {
                params.setMargins(0, topMargin, 0, bottomMargin)
            }
        }

        rlTextAndImage.layoutParams = params

        if (repo.globalContentType != "notifications") {
            tvCategory.text = repo.text
            tvCategory.visibility = View.VISIBLE
            tvCategoryNotifications.visibility = View.GONE
            tvTitle.text = repo.title
            Glide.with(context).load(repo.image).into(tvImage)
            tvImageCardBlock.visibility = View.VISIBLE
            tvImageCardNotifications.visibility = View.GONE
            rlMarker.visibility = View.GONE
            rlMarkerCustoms.visibility = View.GONE
        } else {
            if (repo.contentType == "custom_notification") {
                tvCategory.visibility = View.VISIBLE
                tvCategoryNotifications.visibility = View.GONE
                tvTitle.text = repo.title

                Glide.with(context).load(repo.image).override(
                    context.resources.getDimension(com.intuit.sdp.R.dimen._108sdp).toInt(),
                    context.resources.getDimension(com.intuit.sdp.R.dimen._108sdp).toInt()
                ).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transition(DrawableTransitionOptions.withCrossFade()).preload()

                Glide.with(context).load(repo.image).override(
                    context.resources.getDimension(com.intuit.sdp.R.dimen._108sdp).toInt(),
                    context.resources.getDimension(com.intuit.sdp.R.dimen._108sdp).toInt()
                ).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transition(DrawableTransitionOptions.withCrossFade()).into(tvImage)

                tvImageCardBlock.visibility = View.VISIBLE
                tvImageCardNotifications.visibility = View.GONE

                // Change size and corners
                tvImageCard.radius = 100f // Change radius
                val a = tvImageCard.resources.getDimension(com.intuit.sdp.R.dimen._43sdp)
                tvImageCard.layoutParams.height = a.toInt() // Change size
                tvImageCard.layoutParams.width = a.toInt()  // Change size

                tvImage.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                tvImage.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
            } else {
                tvCategoryNotifications.text = repo.text
                tvCategory.visibility = View.GONE
                tvCategoryNotifications.visibility = View.VISIBLE
                val title = repo.title
                tvTitle.text = title
                tvImageCardBlock.visibility = View.GONE
                tvImageCardNotifications.visibility = View.VISIBLE
            }

            when (repo.contentType) {
                "wishlist_changes" -> {
                    imageViewNotifications.setImageResource(R.drawable.ic_content_notifications_wishlist_changes)
                }
                "chat" -> {
                    imageViewNotifications.setImageResource(R.drawable.ic_content_notifications_chat)
                }
                "locked" -> {
                    imageViewNotifications.setImageResource(R.drawable.ic_content_notifications_locked)
                }
                "price_changes" -> {
                    imageViewNotifications.setImageResource(R.drawable.ic_content_notifications_price_changes)
                }
                "report" -> {
                    imageViewNotifications.setImageResource(R.drawable.ic_content_notifications_report)
                }
            }
        }

        rlTextAndImage.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, repo)
            }
        }
    }

    interface OnClickListener {
        fun onClick(position: Int, model: NewContent)
    }

}
