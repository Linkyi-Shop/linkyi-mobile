package com.example.linkyishop.ui.otp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.linkyishop.R
import com.example.linkyishop.data.ViewModelFactory
import com.example.linkyishop.databinding.ActivityOtpVerifBinding
import com.example.linkyishop.ui.main.MainActivity

class OtpVerifActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpVerifBinding

    private val viewModel by viewModels<OtpViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityOtpVerifBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Dapatkan email dari Intent
        val email = intent.getStringExtra(EXTRA_EMAIL)

        binding.btnVerify.setOnClickListener {
            val otpCode = getOtpCode()
            if (otpCode != null) {
                viewModel.verifyOtp(otpCode, email)
            } else {
                showError(getString(R.string.errorOTP))
            }
        }

        viewModel.otpResult.observe(this, { result ->
            result.onSuccess {
                // Navigasi ke halaman utama setelah berhasil verifikasi OTP
                navigateToMainScreen()
            }.onFailure {
                // Tampilkan pesan error
                showError(it.message)
            }
        })
    }

    private fun getOtpCode(): Int? {
        val otp1 = binding.etOtp1.text.toString()
        val otp2 = binding.etOtp2.text.toString()
        val otp3 = binding.etOtp3.text.toString()
        val otp4 = binding.etOtp4.text.toString()
        val otp5 = binding.etOtp5.text.toString()
        val otp6 = binding.etOtp6.text.toString()
        return if (otp1.isNotEmpty() && otp2.isNotEmpty() && otp3.isNotEmpty() && otp4.isNotEmpty() && otp5.isNotEmpty() && otp6.isNotEmpty()) {
            (otp1 + otp2 + otp3 + otp4 + otp5 + otp6).toIntOrNull()
        } else null
    }

    private fun navigateToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showError(message: String?) {
        // Implementasi menampilkan pesan error
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
//        fun start(context: Context) {
//            val intent = Intent(context, OtpVerifActivity::class.java)
//            context.startActivity(intent)
//        }

        const val EXTRA_EMAIL = "extra_email"
    }

}