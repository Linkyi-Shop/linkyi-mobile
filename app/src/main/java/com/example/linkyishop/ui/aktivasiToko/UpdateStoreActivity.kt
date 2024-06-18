package com.example.linkyishop.ui.aktivasiToko

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
import com.example.linkyishop.databinding.ActivityLoginBinding
import com.example.linkyishop.databinding.ActivityUpdateStoreBinding
import com.example.linkyishop.ui.login.LoginViewModel
import com.example.linkyishop.ui.main.MainActivity
import com.example.linkyishop.ui.product.asRequestBody
import com.example.linkyishop.ui.product.reduceFileImage
import com.example.linkyishop.ui.product.toRequestBody
import com.example.linkyishop.ui.product.uriToFile
import com.example.linkyishop.ui.profile.ProfileFragment
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody

class UpdateStoreActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateStoreBinding
    private var currentImageUri: Uri? = null
    private var selectedLogo: MultipartBody.Part? = null

    private val viewModel by viewModels<UpdateStoreViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityUpdateStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.topAppBar.setOnClickListener {
            val intent = Intent(this@UpdateStoreActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnSelectLogo.setOnClickListener { startGallery() }
        binding.btnEditToko.setOnClickListener { updateStore() }

        observeViewModel()
    }

    private fun updateStore() {
        val name = binding.etName.text.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val descriptionText = binding.etDescription.text.toString()
        val description = if (descriptionText.isNotBlank()) {
            descriptionText.toRequestBody("text/plain".toMediaTypeOrNull())
        } else {
            null
        }

        viewModel.updateStore(name, description!!, selectedLogo!!)
    }

    private fun observeViewModel() {
        viewModel.updateStoreResult.observe(this) { result ->
            result.onSuccess { response ->
                Toast.makeText(this, "Toko berhasil diperbarui", Toast.LENGTH_SHORT).show()
                navigateToStoreProfile()
            }.onFailure { exception ->
                Toast.makeText(this, "Gagal memperbarui toko: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToStoreProfile() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
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
            selectedLogo = uriToMultipartBody(it)
        }
    }

    private fun uriToMultipartBody(uri: Uri): MultipartBody.Part? {
        val file = uriToFile(uri, this).reduceFileImage()
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("logo", file.name, requestFile)
    }
}