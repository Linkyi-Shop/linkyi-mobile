package com.example.linkyishop.ui.aktivasiToko

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.linkyishop.data.repository.UserRepository
import com.example.linkyishop.data.retrofit.response.AktivasiTokoResponse
import com.example.linkyishop.data.retrofit.response.CekUsernameResponse
import com.example.linkyishop.data.retrofit.response.ProfileResponse
import com.example.linkyishop.ui.product.toRequestBody
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.io.IOException

class AktivasiTokoViewModel(private val repository: UserRepository) : ViewModel() {

    private val _cekUsernameResult = MutableLiveData<Pair<Boolean, CekUsernameResponse?>>()
    val cekUsernameResult: LiveData<Pair<Boolean, CekUsernameResponse?>> = _cekUsernameResult

    private val _activateStoreResult = MutableLiveData<Result<AktivasiTokoResponse>>()
    val activateStoreResult: LiveData<Result<AktivasiTokoResponse>> = _activateStoreResult

    private val _profileResult = MutableLiveData<ProfileResponse?>()
    val profileResult: LiveData<ProfileResponse?> = _profileResult

    fun checkUsername(username: String, isCheck: Boolean = true) {
        viewModelScope.launch {
            try {
                val response = repository.checkUsername(username)
                _cekUsernameResult.value = Pair(isCheck, response)
            } catch (e: IOException) {
                _cekUsernameResult.value = Pair(isCheck, null)
            } catch (e: HttpException) {
                _cekUsernameResult.value = Pair(isCheck, null)
            }
        }
    }

    fun activateStore(
        name: String,
        username: String,
        description: String,
        logo: MultipartBody.Part
    ) {
        viewModelScope.launch {
            val requestBodyName = name.toRequestBody("text/plain".toMediaTypeOrNull())
            val requestBodyUsername = username.toRequestBody("text/plain".toMediaTypeOrNull())
            val requestBodyDescription = description.toRequestBody("text/plain".toMediaTypeOrNull())

            try {
                val response = repository.activateStore(
                    requestBodyName,
                    requestBodyUsername,
                    requestBodyDescription,
                    logo
                )
                if (response.success == true) {
                    _activateStoreResult.value = Result.success(response)
                } else {
                    _activateStoreResult.value = Result.failure(Exception(response.message ?: "Gagal mengaktifkan toko"))
                }
            } catch (e: IOException) {
                _activateStoreResult.value = Result.failure(e)
            } catch (e: HttpException) {
                _activateStoreResult.value = Result.failure(Exception(e.message()))
            }
        }
    }

    fun getProfile() {
        viewModelScope.launch {
            try {
                val response = repository.getStoreProfile()
                _profileResult.value = response
            } catch (e: IOException) {
                _profileResult.value = null
            } catch (e: HttpException) {
                _profileResult.value = null
            }
        }
    }
}
