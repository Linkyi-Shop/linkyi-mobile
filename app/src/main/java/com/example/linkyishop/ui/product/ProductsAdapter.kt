package com.example.linkyishop.ui.product

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.linkyishop.R
import com.example.linkyishop.data.retrofit.response.DataItem
import com.example.linkyishop.databinding.ProductsCardBinding
import com.example.linkyishop.ui.detailProduct.DetailProductActivity

class ProductsAdapter(private val context: Context) : ListAdapter<DataItem, ProductsAdapter.MyViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ProductsCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    inner class MyViewHolder(private val binding: ProductsCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(products: DataItem) {
            binding.nameTextView.text = products.title
            binding.descTextView.text = products.price.toString()
            Glide.with(context)
                .load(products.thumbnail)
                .placeholder(R.drawable.linkyi_logo)
                .into(binding.profileImageView)

            binding.root.setOnClickListener {
                val intent = Intent(context, DetailProductActivity::class.java).apply {
                    putExtra("PRODUCT_IMAGE", products.thumbnail)
                    putExtra("PRODUCT_NAME", products.title)
                    putExtra("PRODUCT_PRICE", products.price)
                }
                context.startActivity(intent)
            }

//            binding.cardView.setOnClickListener{
//                val intent = Intent(context, UserDetailActivity::class.java)
//                intent.putExtra(UserDetailActivity.EXTRA_USER, user)
//                context.startActivity(intent)
//            }
        }
    }
}