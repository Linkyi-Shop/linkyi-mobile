package com.example.linkyishop.ui.product

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.linkyishop.R
import com.example.linkyishop.data.ViewModelFactory
import com.example.linkyishop.data.preferences.UserPreference
import com.example.linkyishop.data.preferences.dataStore
import com.example.linkyishop.databinding.ActivityAddProductBinding
import com.example.linkyishop.ui.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import com.nex3z.flowlayout.FlowLayout
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class AddProductActivity : AppCompatActivity() {
    private val viewModel by viewModels<AddProductViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityAddProductBinding
    private lateinit var userPreference: UserPreference
    private var isActive: String = "0"

    private var currentImageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val linksEditText: EditText = findViewById(R.id.linksEditText)
        val scrollView: ScrollView = findViewById(R.id.mainAdd)

        linksEditText.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                scrollView.post {
                    scrollView.smoothScrollTo(0, linksEditText.bottom)
                }
            }
        }

        with(binding){
            topAppBar.setNavigationOnClickListener { finish() }
        }

        userPreference = UserPreference.getInstance(dataStore)

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.submitButton.setOnClickListener { submitProduct() }
        binding.featureSwitch.setOnCheckedChangeListener { _, isChecked ->
            isActive = if (isChecked) {
                "1"
            }else{
                "0"
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.mainAdd) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val flowLayout = binding.linksEditTextLayout
        val editTextTag = binding.linksEditText

        editTextTag.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val tagText = editTextTag.text.toString().trim()
                if (tagText.isNotEmpty()) {
                    addTagToFlowLayout(tagText, flowLayout, editTextTag)
                    editTextTag.text.clear()
                }
                true
            } else {
                false
            }
        }

        editTextTag.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.contains(",") == true) {
                    val tagText = s.toString().replace(",", "").trim()
                    if (tagText.isNotEmpty()) {
                        addTagToFlowLayout(tagText, flowLayout, editTextTag)
                        editTextTag.text.clear()
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun addTagToFlowLayout(tagText: String, flowLayout: FlowLayout, editText: EditText) {
        val tagView = LayoutInflater.from(this).inflate(R.layout.tag_item, flowLayout, false) as TextView
        tagView.text = tagText
        tagView.setOnClickListener {
            flowLayout.removeView(it)
        }
        flowLayout.addView(tagView, flowLayout.childCount - 1)
        editText.requestFocus()
    }

    private fun getLinksArray(): List<String> {
        val links = mutableListOf<String>()
        for (i in 0 until binding.linksEditTextLayout.childCount) {
            val view = binding.linksEditTextLayout.getChildAt(i)
            if (view is TextView && view != binding.linksEditText) {
                links.add(view.text.toString())
            }
        }
        return links
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
                setToolbarColor(ContextCompat.getColor(this@AddProductActivity, R.color.md_theme_primary))
                setToolbarWidgetColor(ContextCompat.getColor(this@AddProductActivity, R.color.md_theme_onTertiary))
                setActiveControlsWidgetColor(ContextCompat.getColor(this@AddProductActivity, R.color.md_theme_primary))
            }
            UCrop.of(uri, destinationUri)
                .withOptions(options)
                .start(this)
//            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            val resultUri = data?.let { UCrop.getOutput(it) }
            resultUri?.let {Uri ->
                val imageFile = uriToFile(Uri, this).reduceFileImage()
                val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
                val multipartBody = MultipartBody.Part.createFormData(
                    "file",
                    imageFile.name,
                    requestImageFile
                )
                lifecycleScope.launch {
                    viewModel.predictImage(multipartBody)
                    viewModel.predictionResult.observe(this@AddProductActivity){
                        if (it.decision == "reject"){
                            Snackbar.make(
                                binding.mainAdd, "Gambar mengandung elemen yang terlarang",
                                Snackbar.LENGTH_LONG).setAction("Action", null
                            ).show()
                        }else if (it.decision == "accept"){
                            currentImageUri = Uri
                            showImage()
                        }
                    }
                }
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(data!!)
            cropError?.let {
                Log.e("UCrop", "UCrop error: $cropError")
                Toast.makeText(this, "Crop error: $cropError", Toast.LENGTH_SHORT).show()
            }
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
        currentImageUri.let {

            // Mendapatkan nilai dari EditText
            val title = binding.nameEditText.text.toString()
            val price = binding.priceEditText.text.toString()
            val category = binding.categoryEditText.text.toString()
            val linksArray = getLinksArray()

            // Memastikan semua input terisi dan gambar sudah dipilih
            if (title.isEmpty() || price.isEmpty() || category.isEmpty() || it == null) {
                "Please fill all fields and select an image".showToast()
                return
            }

            // Mendapatkan file thumbnail dari URI
            val imageFile = uriToFile(it, this).reduceFileImage()
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "thumbnail",
                imageFile.name,
                requestImageFile
            )

            // Melakukan pengiriman data ke ViewModel untuk proses tambah produk
            lifecycleScope.launch {
                viewModel.addProduct(title, price, category, multipartBody, isActive, linksArray)
                viewModel.addProductResult.observe(this@AddProductActivity){
                    if (it.success == true){
                        it.message?.showToast()
                        finish()
                    }else{
                        it.message?.showToast()
                    }
                }
            }
        }
    }

    private fun String.showToast() {
        Toast.makeText(this@AddProductActivity, this, Toast.LENGTH_SHORT).show()
    }
}
