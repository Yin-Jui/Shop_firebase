package com.app.shop_firebase.VIEW

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.shop_firebase.DATA.ItemRepository
import com.app.shop_firebase.MODULE.Item

class ItemViewModel(application: Application) : AndroidViewModel(application) {   //將get資料移到viewmodel
   // private var items = MutableLiveData<List<Item>>()
   // private var firestoreQuery = FirestoreQuery()
private lateinit var itemRepository: ItemRepository
    init {
        itemRepository = ItemRepository(application)
    }

    fun getItems(): LiveData<List<Item>> {

        return itemRepository.getAllItems()
    }

    fun setCategory(categoryId: String) {
        itemRepository.setCategory(categoryId)

    }
}
