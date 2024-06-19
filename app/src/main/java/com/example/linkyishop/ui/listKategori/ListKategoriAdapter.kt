package com.example.linkyishop.ui.listKategori

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.linkyishop.data.retrofit.response.DataItemKategori
import com.example.linkyishop.databinding.CategoryCardBinding

class ListKategoriAdapter : ListAdapter<DataItemKategori, ListKategoriAdapter.ViewHolder>(DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CategoryCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder(private val binding: CategoryCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataItemKategori) {
            binding.categoryTextView.text = item.name
            binding.categorytotalTextView.text = item.totalProduct.toString()
        }
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<DataItemKategori>() {
            override fun areItemsTheSame(oldItem: DataItemKategori, newItem: DataItemKategori): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DataItemKategori, newItem: DataItemKategori): Boolean {
                return oldItem == newItem
            }
        }
    }
}




