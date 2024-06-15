package com.example.linkyishop.ui.updatePassword

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.example.linkyishop.R
import com.example.linkyishop.data.ViewModelFactory
import com.example.linkyishop.databinding.ActivityUpdatePasswordBinding
import com.example.linkyishop.ui.main.MainActivity

class UpdatePasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdatePasswordBinding

    private val viewModel by viewModels<UpdatePasswordViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityUpdatePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        observeUpdatePassword()

        val backButton: ImageView = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            val intent = Intent(this@UpdatePasswordActivity, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
            finish() // Or use a navigation method to go back to the Profile fragment
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun observeUpdatePassword() {
        binding.btnUpdate.setOnClickListener {
            val password = binding.passwordUpEditText.text.toString().trim()
            val confirmPassword = binding.confirmPasswordUpEditText.text.toString().trim()
            val currentPassword = binding.passwordLamaEditText.text.toString().trim()

            Log.d("UpdatePassword", "Button clicked")

            if (isValidInput(password, confirmPassword, currentPassword)) {
                Log.d("UpdatePassword", "Valid input")
                viewModel.updatePassword(password, confirmPassword, currentPassword)
            } else {
                Log.d("UpdatePassword", "Invalid input")
                Toast.makeText(this, "Input tidak valid", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.updatePasswordResult.observe(this, Observer { response ->
            // Handle response, misalnya tampilkan toast atau navigasi ke halaman lain
            if (response.success == true) {
                Log.d("UpdatePassword", "Password updated successfully")
                val intent = Intent(this@UpdatePasswordActivity, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)
                finish()
            } else {
                // Tampilkan pesan kesalahan jika update password gagal
                Log.d("UpdatePassword", "Update password failed")
                Toast.makeText(this, "Update Password Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun isValidInput(password: String, confirmPassword: String, currentPassword: String): Boolean {
        return password.isNotEmpty() && confirmPassword.isNotEmpty() && currentPassword.isNotEmpty() && password == confirmPassword
    }
}