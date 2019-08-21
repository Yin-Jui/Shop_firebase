package com.app.shop_firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ItemViewModel : ViewModel() {   //將get資料移到viewmodel
    private var items = MutableLiveData<List<Item>>()
    private var firestoreQuery = com.app.shop_firebase.FirestoreQuery()

    fun getItems(): FirestoreQuery {

        return firestoreQuery
    }

    fun setCategory(categoryId: String) {
        firestoreQuery.setCategory(categoryId)

    }
}
