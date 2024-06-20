package com.example.linkyishop.ui.welcome

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.linkyishop.R
import com.example.linkyishop.data.ViewModelFactory
import com.example.linkyishop.data.preferences.UserPreference
import com.example.linkyishop.ui.login.LoginViewModel
import com.example.linkyishop.ui.main.MainActivity
import com.example.linkyishop.ui.otp.OtpVerifActivity
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var userPreference: UserPreference
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize UserPreference
        userPreference = UserPreference.getInstance(dataStore)

        // Get the user token
        getToken()
    }

    private fun getToken() {
        lifecycleScope.launch {
            try {
                val token = userPreference.getUserToken()
//                val status = userPreference.getUserStatus()
//                val email = userPreference.getUserEmail()

                if (token.isEmpty()){
                    startActivity(Intent(this@SplashActivity, WelcomeActivity::class.java))
//                    intent.putExtra(OtpVerifActivity.EXTRA_EMAIL, email)
                    finish()
                }else{
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }
                // Use the token as needed, e.g., to make API calls
            } catch (e: Exception) {
                Log.e("MyActivity", "Error getting token", e)
            }
        }
    }
}