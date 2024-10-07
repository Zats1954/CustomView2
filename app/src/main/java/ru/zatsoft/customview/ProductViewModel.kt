package ru.zatsoft.customview

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProductViewModel : ViewModel() {

    private var photoUri: Uri? = null

     val _listProducts = MutableLiveData<MutableList<Product>>()
    val listProducts: LiveData<MutableList<Product>> = _listProducts

//    fun getListProduct():List{}
    init {
        _listProducts.value = mutableListOf()
    }

    fun setUri(imagePhoto: Uri?){
        this.photoUri = imagePhoto
    }

    fun getUri(): Uri?{
        return photoUri
    }

    fun add(product: Product) {
        _listProducts.value?.add(product)
    }
}