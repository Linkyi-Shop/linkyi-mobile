package com.example.linkyishop.ui.linkyi

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LinkyiViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is linkyi Fragment"
    }
    val text: LiveData<String> = _text
}