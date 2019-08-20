package com.app.shop_firebase

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ItemViewModel : ViewModel() {
    private var items = MutableLiveData<List<Item>>()

    fun getItems(): MutableLiveData<List<Item>> {

        FirebaseFirestore.getInstance()
            .collection("items2")
            .orderBy("viewCount", Query.Direction.DESCENDING)
            .limit(10)
            .addSnapshotListener { querySnapshot, exception ->
                if (querySnapshot != null && !querySnapshot.isEmpty) {

                    items.value = querySnapshot.toObjects(Item::class.java)
                }
            }
        return items
    }
}
