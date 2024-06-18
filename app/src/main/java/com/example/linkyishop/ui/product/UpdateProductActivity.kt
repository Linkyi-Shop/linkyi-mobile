package com.example.linkyishop.ui.product

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.linkyishop.R
import com.example.linkyishop.data.ViewModelFactory
import com.example.linkyishop.databinding.ActivityUpdateProductBinding
import com.example.linkyishop.ui.detailProduct.DetailProductActivity
import com.example.linkyishop.ui.main.MainActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody

class UpdateProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateProductBinding
    private val viewModel by viewModels<UpdateProductViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var productId: String
    private var currentImageUri: Uri? = null
    private var selectedThumbnail: MultipartBody.Part? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUpdateProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productId = intent.getStringExtra("PRODUCT_ID") ?: ""

        binding.btnSelectThumbnail.setOnClickListener { startGallery() }
        binding.btnEditProduk.setOnClickListener { updateProduct() }

        observeViewModel()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.editImage.setImageURI(it)
            selectedThumbnail = uriToMultipartBody(it)
        }
    }

    private fun updateProduct() {
        val title = binding.etTitle.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val price = binding.etPrice.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val category = binding.etCategory.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        viewModel.updateProduct(productId, title, price, category, selectedThumbnail)
    }

    private fun observeViewModel() {
        viewModel.updateProductResult.observe(this) { result ->
            result.onSuccess { response ->
                Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show()
                navigateToDetailProduct()
            }.onFailure { exception ->
                Toast.makeText(this, "Failed to update product: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToDetailProduct() {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("PRODUCT_ID", productId)
        startActivity(intent)
        finish()
    }

    private fun uriToMultipartBody(uri: Uri): MultipartBody.Part? {
        val file = uriToFile(uri, this).reduceFileImage()
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("thumbnail", file.name, requestFile)
    }
}