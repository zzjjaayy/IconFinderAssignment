package com.jay.iconfinderassignment.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jay.iconfinderassignment.R
import com.jay.iconfinderassignment.databinding.ItemIconBinding
import com.jay.iconfinderassignment.data.model.Icon
import com.jay.iconfinderassignment.utils.TAG

class IconsAdapter(private val downloadCallback : (Icon?) -> Unit) :
    PagingDataAdapter<Icon, IconsAdapter.IconViewHolder>(ICON_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {
        val binding =
            ItemIconBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return IconViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
        holder.itemView.findViewById<ImageView>(R.id.download_icon).setOnClickListener {
            Log.d(TAG, "onBindViewHolder: download button clicked")
            downloadCallback(getItem(holder.absoluteAdapterPosition))
        }
    }

    class IconViewHolder(private val binding: ItemIconBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(icon: Icon) {
            binding.apply {
                iconTitle.text = icon.icon_id.toString() // The API doesn't provide names for icons
                iconPrice.isVisible = icon.is_premium
                icon?.prices?.let {
                    if(it.isNotEmpty()) {
                        if(icon.prices[0].currency == "USD") {
                            iconPrice.text = "$"+icon.prices[0].price
                        } else iconPrice.text = icon.prices[0].currency + " " +icon.prices[0].price
                    }
                }
                downloadIcon.isVisible = !icon.is_premium
                paidTag.isVisible = icon.is_premium

                val rasterSizes = icon.raster_sizes
                if(rasterSizes.isNotEmpty()) {
                    val suitableRaster = rasterSizes.find { it.size >= 128 } ?: rasterSizes[rasterSizes.size - 1]
                    if(suitableRaster.formats.isNotEmpty()) {
                        val url = suitableRaster.formats[0].preview_url
                        Glide.with(binding.iconImage).load(url).error(R.drawable.ic_error).into(binding.iconImage)
                    }
                }
            }
        }
    }

    companion object {
        private val ICON_COMPARATOR = object : DiffUtil.ItemCallback<Icon>() {
            override fun areItemsTheSame(oldItem: Icon, newItem: Icon) =
                oldItem.icon_id == newItem.icon_id
            override fun areContentsTheSame(oldItem: Icon, newItem: Icon) =
                oldItem == newItem
        }
    }
}