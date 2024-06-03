package com.example.finanzapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _dataList = MutableLiveData<List<String>>()
    val dataList: LiveData<List<String>> get() = _dataList

    init {
        // Example data, could be fetched from a repository
        _dataList.value = listOf("Item 1", "Item 2", "Item 3", "Item 4")
    }
}