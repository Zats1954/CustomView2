package ru.zatsoft.customview

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProductViewModel : ViewModel() {

    private var bitmap: Bitmap? = null

    fun setBitmap(imageMap: Bitmap?){
        this.bitmap = imageMap
    }

    fun getBitmap(): Bitmap?{
        return bitmap
    }


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