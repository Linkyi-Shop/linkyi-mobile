package com.example.linkyishop.ui.updatePassword

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linkyishop.data.repository.UserRepository
import com.example.linkyishop.data.retrofit.response.UpdatePasswordResponse
import kotlinx.coroutines.launch

class UpdatePasswordViewModel(private val repository: UserRepository) : ViewModel() {
    private val _updatePasswordResult = MutableLiveData<UpdatePasswordResponse>()
    val updatePasswordResult: LiveData<UpdatePasswordResponse> = _updatePasswordResult

    fun updatePassword(password: String, confirmPassword: String, currentPassword: String) {
        viewModelScope.launch {
            try {
                Log.d("UpdatePasswordViewModel", "Calling repository to update password")
                val response = repository.updatePassword(password, confirmPassword, currentPassword)
                Log.d("UpdatePasswordViewModel", "Response received: $response")
                _updatePasswordResult.value = response
            } catch (e: Exception) {
                Log.e("UpdatePasswordViewModel", "Error updating password", e)
                // Handle error, e.g., show toast or log error
            }
        }
    }
}
