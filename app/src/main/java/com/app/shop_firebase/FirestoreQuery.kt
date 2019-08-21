package com.app.shop_firebase

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.*

class FirestoreQuery : LiveData<QuerySnapshot>(), EventListener<QuerySnapshot> {

    lateinit var registration: ListenerRegistration

    override fun onEvent(querySnapshot: QuerySnapshot?, exception: FirebaseFirestoreException?) {
        if (querySnapshot != null && !querySnapshot.isEmpty) {
            value = querySnapshot
        }
    }

    var query = FirebaseFirestore.getInstance()
        .collection("items2")
        .orderBy("viewCount", Query.Direction.DESCENDING)
        .limit(10)
    var isRegistrated = false

    override fun onInactive() {
        super.onInactive()
        if (isRegistrated) {
            registration.remove()
        }
    }

    override fun onActive() {
        registration = query.addSnapshotListener(this)
        isRegistrated = true
    }

    fun setCategory(categoryId:String){
        if(isRegistrated){
            registration.remove()
            isRegistrated = false
        }
        if(categoryId.length>0){
            query = FirebaseFirestore.getInstance()
                .collection("items2")
                .whereEqualTo("category",categoryId)
                .orderBy("viewCount", Query.Direction.DESCENDING)
                .limit(10)
        }else{
            query = FirebaseFirestore.getInstance()
                .collection("items2")
                .orderBy("viewCount", Query.Direction.DESCENDING)
                .limit(10)
        }
        registration = query.addSnapshotListener(this)
        isRegistrated = true
    }
}