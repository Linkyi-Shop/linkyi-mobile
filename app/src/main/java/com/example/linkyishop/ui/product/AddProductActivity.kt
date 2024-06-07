package com.example.linkyishop.ui.product

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
import androidx.lifecycle.lifecycleScope
import com.example.linkyishop.R
import com.example.linkyishop.data.ViewModelFactory
import com.example.linkyishop.data.preferences.UserPreference
import com.example.linkyishop.data.preferences.dataStore
import com.example.linkyishop.databinding.ActivityAddProductBinding
import kotlinx.coroutines.launch
import java.io.File

class AddProductActivity : AppCompatActivity() {
    private val viewModel by viewModels<AddProductViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityAddProductBinding
    private lateinit var userPreference: UserPreference

    private var currentImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreference = UserPreference.getInstance(dataStore)

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.submitButton.setOnClickListener { submitProduct() }


        viewModel.addProductResult.observe(this, { result ->
            if (result.isSuccess) {
                showToast("Product added successfully")
                finish()
            } else {
                showToast("Failed to add product: ${result.exceptionOrNull()?.message}")
            }
        })

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

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.productImage.setImageURI(it)
        }
    }

    private fun submitProduct() {
        // Mendapatkan nilai dari EditText
        val title = binding.nameEditText.text.toString()
        val price = binding.priceEditText.text.toString()
        val category = binding.categoryEditText.text.toString()
        val isActiveText = binding.isActiveEditText.text.toString()
        val linksString = binding.linksEditText.text.toString()

        // Memastikan semua input terisi dan gambar sudah dipilih
        if (title.isEmpty() || price.isEmpty() || category.isEmpty() || isActiveText.isEmpty() || currentImageUri == null) {
            showToast("Please fill all fields and select an image")
            return
        }

        // Konversi isActiveText menjadi String
        val isActive = isActiveText

        // Mendapatkan file thumbnail dari URI
        val thumbnailFile = uriToFile(currentImageUri!!, this)

        // Konversi linksString menjadi List<String>
        val links = linksString.split("\n").map { it.trim() } // Membagi string berdasarkan baris dan menghapus whitespace di setiap string

        // Melakukan pengiriman data ke ViewModel untuk proses tambah produk
        lifecycleScope.launch {
            viewModel.addProduct(title, price, category, thumbnailFile, isActive, links)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}