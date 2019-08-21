package com.app.shop_firebase

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ItemViewModel : ViewModel() {   //將get資料移到viewmodel
    private var items = MutableLiveData<List<Item>>()

    fun getItems(): MutableLiveData<List<Item>> {

        FirebaseFirestore.getInstance()
            .collection("items2")
            .orderBy("viewCount", Query.Direction.DESCENDING)
            .limit(10)
            .addSnapshotListener { querySnapshot, exception ->
                if (querySnapshot != null && !querySnapshot.isEmpty) {
                    val list = mutableListOf<Item>()
                    for (doc in querySnapshot.documents) {    //一個一個得到id值
                        val item = doc.toObject(Item::class.java)?:Item()  //如果左邊的value值為空 就建一個空的item
                        item.id = doc.id
                        list.add(item)
                    }
                    items.value = list
                }
                //                 items.value = querySnapshot.toObjects(Item::class.java)   //每個livedata裡都會有一個value值,為存放資料的形式
            }

        return items
    }
}
