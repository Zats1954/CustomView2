package ru.zatsoft.customview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProductViewModel : ViewModel() {

    private val _listUsers = MutableLiveData<MutableList<Product>>()
    val listUsers: MutableLiveData<MutableList<Product>>
        get() = _listUsers

    init {
        _listUsers.value = mutableListOf()
    }

    fun add(user: Product) {
        _listUsers.value?.add(user)
    }
}