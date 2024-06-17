package com.example.linkyishop.ui.detailProduct

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginBottom
import com.bumptech.glide.Glide
import com.example.linkyishop.R
import com.example.linkyishop.data.ViewModelFactory
import com.example.linkyishop.data.retrofit.response.LinksItemDetail
import com.example.linkyishop.databinding.ActivityDetailProductBinding
import com.example.linkyishop.ui.main.MainActivity
import com.example.linkyishop.ui.product.UpdateProductActivity
import com.example.linkyishop.ui.product.UpdateProductViewModel
import com.google.android.material.button.MaterialButton

class DetailProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailProductBinding
    private val viewModel by viewModels<DetailProductViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val updateViewModel by viewModels<UpdateProductViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var productId: String? = null

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

        productId = intent.getStringExtra("PRODUCT_ID")

        binding.btnEditProduk.setOnClickListener {
            navigateToUpdateProduct(productId.toString())
        }

        viewModel.isLoading.observe(this) {
            if (it){
                binding.progressBar.visibility = View.VISIBLE
                binding.btnEditProduk.visibility = View.GONE
                binding.deleteProductButton.visibility = View.GONE
                binding.btnAddLink.visibility = View.GONE
                binding.featureSwitch.visibility = View.GONE
                binding.edAddLink.visibility = View.GONE
                binding.addLink.visibility = View.GONE
            }else{
                binding.progressBar.visibility = View.GONE
                binding.btnEditProduk.visibility = View.VISIBLE
                binding.deleteProductButton.visibility = View.VISIBLE
                binding.btnAddLink.visibility = View.VISIBLE
                binding.featureSwitch.visibility = View.VISIBLE
                binding.edAddLink.visibility = View.VISIBLE
                binding.addLink.visibility = View.VISIBLE
            }
        }
        setProduct()

        with(binding){
            topAppBar.setNavigationOnClickListener { finish() }

            deleteProductButton.setOnClickListener {
                viewModel.deleteProduct(productId!!)
                viewModel.deleteResponse.observe(this@DetailProductActivity) {
                    if (it.success == true) {
                        val intent = Intent(this@DetailProductActivity, MainActivity::class.java)
                        Toast.makeText(this@DetailProductActivity, it.message, Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@DetailProductActivity, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

            btnAddLink.setOnClickListener{
                viewModel.addLink(productId!!, edAddLink.text.toString())
                viewModel.deleteResponse.observe(this@DetailProductActivity) {
                    if (it.success == true) {
                        Toast.makeText(this@DetailProductActivity, it.message, Toast.LENGTH_SHORT).show()
                        setProduct()
                    } else {
                        Toast.makeText(this@DetailProductActivity, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            featureSwitch.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    // Aktivasi produk
                    updateViewModel.productStatus(productId!!, "1")
                    Toast.makeText(this@DetailProductActivity, "Produk Aktif", Toast.LENGTH_SHORT).show()
                }else{
                    // Non-aktif produk
                    updateViewModel.productStatus(productId!!, "0")
                    Toast.makeText(this@DetailProductActivity, "Produk non-Aktif", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setProduct() {
        viewModel.fetchProductDetail(productId!!)
        viewModel.productDetail.observe(this) {
            Glide.with(this)
                .load(it.data?.thumbnail)
                .into(binding.productImage)
            binding.productName.text = it.data?.title
            binding.productPrice.text = "Rp. ${it.data?.price}"
            it.data?.links.let {
                clearLinksContainer()
                addLinkTextViews(it)
            }

            if (it.data?.isActive == false) {
                binding.featureSwitch.isChecked = false
            }
        }
    }

    private fun navigateToUpdateProduct(productId: String) {
        val intent = Intent(this, UpdateProductActivity::class.java)
        intent.putExtra("PRODUCT_ID", productId)
        startActivity(intent)
    }

    private fun clearLinksContainer() {
        val linksContainer = findViewById<LinearLayout>(R.id.links_container)
        linksContainer.removeAllViews()
    }
    private fun addLinkTextViews(links: List<LinksItemDetail?>?) {
        val linksContainer = findViewById<LinearLayout>(R.id.links_container)
        if (links != null) {
            links.forEach { link ->
                val horizontalLayout = LinearLayout(this).apply {
                    orientation = LinearLayout.HORIZONTAL
                    layoutParams = ViewGroup.MarginLayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(0, 0, 0, 16)
                    }
                }

                val textView = TextView(this).apply {
                    text = link?.type
                    textSize = 16f
                    setPadding(8, 8, 8, 8)
                    setBackgroundResource(R.drawable.textview_border)
                    layoutParams = LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1f
                    )
                    setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link?.link))
                        startActivity(intent)
                    }
                }

                val button = MaterialButton(this, null, R.attr.myAttr).apply {
                    text = "Hapus"
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                    setTextColor(getColor(R.color.md_theme_error))
                    setOnClickListener {
                        viewModel.deleteLink(productId!!, link?.id!!)
                        viewModel.deleteResponse.observe(this@DetailProductActivity) {
                            if (it.success == true) {
                                Toast.makeText(this@DetailProductActivity, it.message, Toast.LENGTH_SHORT).show()
                                linksContainer.removeView(horizontalLayout)
                            } else {
                                Toast.makeText(this@DetailProductActivity, it.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                // Add TextView and Button to the horizontal LinearLayout
                horizontalLayout.addView(textView)
                horizontalLayout.addView(button)

                // Add the horizontal LinearLayout to the linksContainer
                linksContainer.addView(horizontalLayout)
            }
        }
    }
}
