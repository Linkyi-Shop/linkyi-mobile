package com.example.linkyishop.data

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.linkyishop.data.repository.UserRepository
import com.example.linkyishop.di.Injection
import com.example.linkyishop.ui.login.LoginViewModel
import com.example.linkyishop.ui.lupaPassword.LupaPasswordViewModel
import com.example.linkyishop.ui.main.MainViewModel
import com.example.linkyishop.ui.newPassword.NewPasswordViewModel
import com.example.linkyishop.ui.otp.OtpViewModel
import com.example.linkyishop.ui.register.RegisterViewModel

class ViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(OtpViewModel::class.java) -> {
                OtpViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LupaPasswordViewModel::class.java) -> {
                LupaPasswordViewModel(repository) as T
            }
            modelClass.isAssignableFrom(NewPasswordViewModel::class.java) -> {
                NewPasswordViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}