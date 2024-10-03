package ru.zatsoft.customview

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProductViewModel : ViewModel() {


    private var photoUri: Uri? = null

    fun setUri(imagePhoto: Uri?){
        this.photoUri = imagePhoto
    }

    fun getUri(): Uri?{
        return photoUri
    }


    private val _listProducts = MutableLiveData<MutableList<Product>>()
    val listProducts: MutableLiveData<MutableList<Product>>
        get() = _listProducts

    init {
        _listProducts.value = mutableListOf()
    }

    fun add(product: Product) {
        _listProducts.value?.add(product)
    }
}