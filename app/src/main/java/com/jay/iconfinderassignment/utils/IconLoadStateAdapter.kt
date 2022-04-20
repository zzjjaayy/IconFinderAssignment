package com.jay.iconfinderassignment.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jay.iconfinderassignment.databinding.LoadStateFooterBinding

class IconLoadStateAdapter(
    private val retry : () -> Unit
) : LoadStateAdapter<IconLoadStateAdapter.LoadStateViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = LoadStateFooterBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return LoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class LoadStateViewHolder(private val binding: LoadStateFooterBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            binding.apply {
                loaderIcon.isVisible = loadState is LoadState.Loading
                loadErrorText.isVisible = loadState is LoadState.Error
                retryBtn.isVisible = loadState is LoadState.Error
                retryBtn.setOnClickListener { retry() }
            }
        }
    }
}