package com.example.linkyishop.ui.product

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.linkyishop.R
import com.example.linkyishop.data.ViewModelFactory
import com.example.linkyishop.databinding.ActivityUpdateProductBinding
import com.example.linkyishop.ui.detailProduct.DetailProductActivity
import com.example.linkyishop.ui.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import com.yalantis.ucrop.UCrop
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class UpdateProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateProductBinding
    private val viewModel by viewModels<UpdateProductViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val addViewModel by viewModels<AddProductViewModel> {
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

        with(binding){
            topAppBar.setNavigationOnClickListener { finish() }
        }

        observeViewModel()
        addViewModel.predictionResult.observe(this@UpdateProductActivity){
            if (it.decision == "accept"){
                showImage()
            }else{
                binding.editImage.setImageDrawable(ContextCompat.getDrawable(this@UpdateProductActivity, R.drawable.baseline_preview_image_24))
                currentImageUri = null
                Snackbar.make(
                    binding.main, "Gambar mengandung elemen yang terlarang",
                    Snackbar.LENGTH_LONG).setAction("Action", null
                ).show()
            }
        }

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
            val timeStamp = System.currentTimeMillis()
            val destinationFileName = "cropped_image_$timeStamp.jpg"
            val destinationUri = Uri.fromFile(File(cacheDir, destinationFileName))
            val options = UCrop.Options().apply {
                setCompressionFormat(Bitmap.CompressFormat.JPEG)
                setCompressionQuality(80)
                setToolbarColor(ContextCompat.getColor(this@UpdateProductActivity, R.color.md_theme_primary))
                setToolbarWidgetColor(ContextCompat.getColor(this@UpdateProductActivity, R.color.md_theme_onTertiary))
                setActiveControlsWidgetColor(ContextCompat.getColor(this@UpdateProductActivity, R.color.md_theme_primary))
            }
            UCrop.of(uri, destinationUri)
                .withOptions(options)
                .start(this)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            val resultUri = data?.let { UCrop.getOutput(it) }
            if (resultUri != null){
                val imageFile = uriToFile(resultUri, this).reduceFileImage()
                val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
                val multipartBody = MultipartBody.Part.createFormData(
                    "file",
                    imageFile.name,
                    requestImageFile
                )
                addViewModel.predictImage(multipartBody)
                currentImageUri = resultUri
            }else{
                binding.editImage.setImageDrawable(ContextCompat.getDrawable(this@UpdateProductActivity, R.drawable.baseline_preview_image_24))
                currentImageUri = null
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            cropError?.let {
                Log.e("UCrop", "UCrop error: $cropError")
                Toast.makeText(this, "Crop error: $cropError", Toast.LENGTH_SHORT).show()
            }
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