package com.example.linkyishop.ui.lupaPassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linkyishop.data.repository.UserRepository
import kotlinx.coroutines.launch


class LupaPasswordViewModel(private val repository: UserRepository) : ViewModel() {
    private val _lupaPasswordResult = MutableLiveData<LupaPasswordResult>()
    val lupaPasswordResult: LiveData<LupaPasswordResult> get() = _lupaPasswordResult

    fun lupaPassword(email: String) {
        viewModelScope.launch {
            try {
                val response = repository.lupaPassword(email)
                if (response.success == true) {
                    _lupaPasswordResult.value = LupaPasswordResult.Success
                } else {
                    _lupaPasswordResult.value = LupaPasswordResult.Error("Gagal melakukan reset password")
                }
            } catch (e: Exception) {
                _lupaPasswordResult.value = LupaPasswordResult.Error("Terjadi kesalahan saat menghubungi server")
            }
        }
    }

    sealed class LupaPasswordResult {
        object Success : LupaPasswordResult()
        data class Error(val message: String) : LupaPasswordResult()
    }
}