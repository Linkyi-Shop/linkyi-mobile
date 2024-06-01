package com.example.linkyishop.ui.newPassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linkyishop.data.repository.UserRepository
import kotlinx.coroutines.launch

class NewPasswordViewModel(private val repository: UserRepository): ViewModel() {
    private val _passwordChanged = MutableLiveData<Boolean>()
    val passwordChanged: LiveData<Boolean> get() = _passwordChanged

    fun changePassword(password: String, confirmPassword: String, otp: Int) {
        viewModelScope.launch {
            try {
                val response = repository.newPassword(password, confirmPassword, otp)
                if (response.success == true) {
                    _passwordChanged.value = true
                } else {
                    _passwordChanged.value = false
                }
            } catch (e: Exception) {
                _passwordChanged.value = false
            }
        }
    }
}