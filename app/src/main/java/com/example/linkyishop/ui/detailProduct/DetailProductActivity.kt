package com.example.linkyishop.ui.detailProduct

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.linkyishop.R
import com.example.linkyishop.data.ViewModelFactory
import com.example.linkyishop.data.retrofit.response.LinksItem
import com.example.linkyishop.databinding.ActivityDetailProductBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DetailProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProductBinding
    private val viewModel by viewModels<DetailProductViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val productImage = intent.getStringExtra("PRODUCT_IMAGE")
        val productName = intent.getStringExtra("PRODUCT_NAME")
        val productPrice = intent.getStringExtra("PRODUCT_PRICE")
        val productLinksJson = intent.getStringExtra("PRODUCT_LINKS")

        val productLinks: List<LinksItem>? = Gson().fromJson(productLinksJson, object : TypeToken<List<LinksItem>>() {}.type)

        Glide.with(this)
            .load(productImage)
            .into(binding.productImage)
        binding.productName.text = productName
        binding.productPrice.text = productPrice

        // Tambahkan TextView untuk setiap jenis tautan
        productLinks?.let {
            addLinkTextViews(it)
        }
    }

    private fun addLinkTextViews(links: List<LinksItem>) {
        val linksContainer = findViewById<LinearLayout>(R.id.links_container)
        links.forEach { link ->
            val textView = TextView(this).apply {
                text = link.type
                textSize = 16f
                setPadding(8, 8, 8, 8)
                setTextColor(ContextCompat.getColor(context, android.R.color.black))
                setBackgroundResource(R.drawable.textview_border)
            }
            linksContainer.addView(textView)
        }
    }
}
