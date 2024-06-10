package com.example.linkyishop.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.linkyishop.R
import com.example.linkyishop.data.ViewModelFactory
import com.example.linkyishop.databinding.ActivityRegisterBinding
import com.example.linkyishop.ui.login.LoginActivity
import com.example.linkyishop.ui.otp.OtpVerifActivity

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var email: String

    private val viewModel by viewModels<RegisterViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRegister()

        binding.signUpTextView.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun setupRegister() {
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            email = binding.emaileditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (validateInput(name, email, password)) {
                viewModel.register(name, email, password)
                true.showLoading()
            }
        }

        viewModel.registerResult.observe(this) { result ->
            result.onSuccess { response ->
                if (response.success == true) {
                    // Tidak perlu memeriksa registerResult.email karena data adalah array kosong
                    navigateToOtpScreen(email)  // Ubah navigasi ke OTP tanpa parameter email
                } else {
                    // Registrasi gagal
                    showError(response.message ?: "Registration failed. Please try again.")
                }
            }.onFailure {
                // Tampilkan pesan error
                showError(it.message)
            }
        }
    }
    private fun navigateToOtpScreen(email: String) {
        val intent = Intent(this@RegisterActivity, OtpVerifActivity::class.java)
        intent.putExtra(OtpVerifActivity.EXTRA_EMAIL, email)
        startActivity(intent)
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun validateInput(name: String, email: String, password: String): Boolean {
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showError(getString(R.string.validasiRegister))
            return false
        }
        return true
    }

    private fun showError(message: String?) {
        // Implementasi menampilkan pesan error
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun Boolean.showLoading() {
        binding.progressBar.visibility = if (this) View.VISIBLE else View.GONE
    }
}