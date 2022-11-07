package com.testandroid.pagingtestapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.testandroid.pagingtestapp.R
import com.testandroid.pagingtestapp.data.NewContent
import com.testandroid.pagingtestapp.databinding.RecyclerOffersContentCardBinding
import com.testandroid.pagingtestapp.tools.Converters


class OffersContentAdapter(
    private val context: Context?,
    private var items_type: String,
    private var items: ArrayList<NewContent>
) : RecyclerView.Adapter<OffersContentAdapter.ViewHolder>() {

    private var onClickListener: OnClickListener? = null
    var myTools: Converters? = Converters(context)

    class ViewHolder(binding: RecyclerOffersContentCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tvCategory = binding.tvContentCardCategory
        val tvCategoryNotifications = binding.tvContentCardCategoryNotifications
        val tvTitle = binding.textView2
        val tvImage = binding.imageView
        val imageViewNotifications = binding.imageViewNotifications
        val tvImageCard = binding.cardView
        val tvImageCardBlock = binding.cardViewBlock
        val tvImageCardNotifications = binding.cardViewNotifications
        val tvDate = binding.textView6
        val rlTextAndImage = binding.rlContentCard
        val rlMarker = binding.rlNotificationsReadedMarker
        val rlMarkerCustoms = binding.rlNotificationsReadedMarkerCustom
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RecyclerOffersContentCardBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val date = items[position].createdAt
        val toDate = myTools!!.convertTimestamp(date.toString(), "normal")
        holder.tvDate.text = toDate.toString()

        val params = LinearLayout.LayoutParams(
            MATCH_PARENT, WRAP_CONTENT
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

        holder.rlTextAndImage.layoutParams = params

        if (items_type != "notifications") {
            holder.tvCategory.text = items[position].text
            holder.tvCategory.visibility = View.VISIBLE
            holder.tvCategoryNotifications.visibility = View.GONE
            holder.tvTitle.text = items[position].title
            Glide.with(context).load(items[position].image).into(holder.tvImage)
            holder.tvImageCardBlock.visibility = View.VISIBLE
            holder.tvImageCardNotifications.visibility = View.GONE
            holder.rlMarker.visibility = View.GONE
            holder.rlMarkerCustoms.visibility = View.GONE
        } else {
            if (items[position].contentType == "custom_notification") {
                holder.tvCategory.visibility = View.VISIBLE
                holder.tvCategoryNotifications.visibility = View.GONE
                holder.tvTitle.text = items[position].title

                Glide.with(context).load(items[position].image).override(
                        context.resources.getDimension(com.intuit.sdp.R.dimen._108sdp).toInt(),
                        context.resources.getDimension(com.intuit.sdp.R.dimen._108sdp).toInt()
                    ).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transition(DrawableTransitionOptions.withCrossFade()).preload()

                Glide.with(context).load(items[position].image).override(
                        context.resources.getDimension(com.intuit.sdp.R.dimen._108sdp).toInt(),
                        context.resources.getDimension(com.intuit.sdp.R.dimen._108sdp).toInt()
                    ).centerCrop().diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transition(DrawableTransitionOptions.withCrossFade()).into(holder.tvImage)

                holder.tvImageCardBlock.visibility = View.VISIBLE
                holder.tvImageCardNotifications.visibility = View.GONE

                // Change size and corners
                holder.tvImageCard.radius = 100f // Change radius
                val a = holder.tvImageCard.resources.getDimension(com.intuit.sdp.R.dimen._43sdp)
                holder.tvImageCard.layoutParams.height = a.toInt() // Change size
                holder.tvImageCard.layoutParams.width = a.toInt()  // Change size

                holder.tvImage.layoutParams.height = MATCH_PARENT
                holder.tvImage.layoutParams.width = MATCH_PARENT
            } else {
                holder.tvCategoryNotifications.text = items[position].text
                holder.tvCategory.visibility = View.GONE
                holder.tvCategoryNotifications.visibility = View.VISIBLE
                val title = items[position].title
                holder.tvTitle.text = title
                holder.tvImageCardBlock.visibility = View.GONE
                holder.tvImageCardNotifications.visibility = View.VISIBLE
            }

            when (items[position].contentType) {
                "wishlist_changes" -> {
                    holder.imageViewNotifications.setImageResource(R.drawable.ic_content_notifications_wishlist_changes)
                }
                "chat" -> {
                    holder.imageViewNotifications.setImageResource(R.drawable.ic_content_notifications_chat)
                }
                "locked" -> {
                    holder.imageViewNotifications.setImageResource(R.drawable.ic_content_notifications_locked)
                }
                "price_changes" -> {
                    holder.imageViewNotifications.setImageResource(R.drawable.ic_content_notifications_price_changes)
                }
                "report" -> {
                    holder.imageViewNotifications.setImageResource(R.drawable.ic_content_notifications_report)
                }
            }
        }

        holder.rlTextAndImage.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, items[position])
            }
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }


    interface OnClickListener {
        fun onClick(position: Int, model: NewContent)
    }

}