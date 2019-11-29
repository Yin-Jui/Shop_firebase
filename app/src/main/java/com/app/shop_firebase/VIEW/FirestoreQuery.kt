package com.app.shop_firebase.VIEW

import androidx.lifecycle.LiveData
import com.app.shop_firebase.MODULE.Item
import com.google.firebase.firestore.*

class FirestoreQuery : LiveData<List<Item>>(), EventListener<QuerySnapshot> {

    lateinit var registration: ListenerRegistration

    override fun onEvent(querySnapshot: QuerySnapshot?, exception: FirebaseFirestoreException?) {
        if (querySnapshot != null && !querySnapshot.isEmpty) {
            val list = mutableListOf<Item>()
            for (doc in querySnapshot.documents) {
                val item = doc.toObject(Item::class.java) ?: Item()
                item.id = doc.id
                list.add(item)
            }
            value = list
        }
    }

    var query = FirebaseFirestore.getInstance()
        .collection("items")
        .orderBy("viewCount", Query.Direction.DESCENDING)
        .limit(10)
    var isRegistrated = false

    override fun onInactive() {
        super.onInactive()
        if (isRegistrated) {
            registration.remove()
        }
    }

    override fun onActive() {   //資料適合顯示的話
        registration = query.addSnapshotListener(this)
        isRegistrated = true
    }

    fun setCategory(categoryId: String) {
        if (isRegistrated) {
            registration.remove()
            isRegistrated = false
        }
        if (categoryId.length > 0) {
            query = FirebaseFirestore.getInstance()
                .collection("items")
                .whereEqualTo("category", categoryId)
                .orderBy("viewCount", Query.Direction.DESCENDING)
                .limit(10)
        } else {
            query = FirebaseFirestore.getInstance()
                .collection("items")
                .orderBy("viewCount", Query.Direction.DESCENDING)
                .limit(10)
        }
        registration = query.addSnapshotListener(this)
        isRegistrated = true
    }
}