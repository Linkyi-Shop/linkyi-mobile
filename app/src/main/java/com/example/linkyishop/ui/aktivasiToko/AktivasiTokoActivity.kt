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
import com.example.linkyishop.databinding.ActivityAktivasiTokoBinding
import com.example.linkyishop.ui.main.MainActivity
import com.example.linkyishop.ui.product.asRequestBody
import com.example.linkyishop.ui.product.reduceFileImage
import com.example.linkyishop.ui.product.uriToFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody

class AktivasiTokoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAktivasiTokoBinding
    private var currentImageUri: Uri? = null
    private val viewModel by viewModels<AktivasiTokoViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAktivasiTokoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.galleryButton.setOnClickListener { startGallery() }
        setupUI()
        observeViewModel()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupUI() {
        binding.checkUsernameButton.setOnClickListener {
            val username = binding.usernameInput.text.toString()
            if (username.isNotEmpty()) {
                viewModel.checkUsername(username)
            } else {
                Toast.makeText(this, "Harap masukkan username", Toast.LENGTH_SHORT).show()
            }
        }

        binding.activateButton.setOnClickListener {
            val name = binding.nameInput.text.toString()
            val username = binding.usernameInput.text.toString()
            val description = binding.descriptionInput.text.toString()
            val logoUri = binding.productLogo.tag as Uri?

            if (name.isNotEmpty() && username.isNotEmpty() && description.isNotEmpty() && logoUri != null) {
                val logoFile = uriToFile(logoUri, this).reduceFileImage()
                val requestFile = logoFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val logoPart = MultipartBody.Part.createFormData("logo", logoFile.name, requestFile)

                viewModel.activateStore(name, username, description, logoPart)
            } else {
                Toast.makeText(this, "Harap isi semua bidang", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.cekUsernameResult.observe(this) { (isCheck, response) ->
            response?.let {
                if (it.success == true && it.data?.availability == true) {
                    if (isCheck) {
                        Toast.makeText(this, "Username tersedia", Toast.LENGTH_SHORT).show()
                    } else {
                        val name = binding.nameInput.text.toString()
                        val username = it.data.username ?: binding.usernameInput.text.toString()
                        val description = binding.descriptionInput.text.toString()
                        val logoUri = binding.productLogo.tag as Uri
                        val logoFile = uriToFile(logoUri, this)
                        val requestFile = logoFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                        val logoPart = MultipartBody.Part.createFormData("logo", logoFile.name, requestFile)

                        viewModel.activateStore(name, username, description, logoPart)
                    }
                } else {
                    Toast.makeText(this, "Username tidak tersedia", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(this, "Terjadi kesalahan saat memeriksa username", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.activateStoreResult.observe(this) { response ->
            response?.let {
                if (it.isSuccess) {
                    Toast.makeText(this, "Aktivasi toko berhasil", Toast.LENGTH_SHORT).show()
                    navigateToMainScreen()
                } else {
                    Toast.makeText(this, "Aktivasi toko gagal", Toast.LENGTH_SHORT).show()
                }
            }
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

    private fun navigateToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.productLogo.setImageURI(it)
            binding.productLogo.tag = it
        }
    }
}


