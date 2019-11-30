package com.app.shop_firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.app.shop_firebase.MODULE.Item
import com.app.shop_firebase.MODULE.WatchItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    lateinit var item: Item

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        item = intent.getParcelableExtra<Item>("ITEM")
        Log.d("Detail", "Oncreate  ${item.id}    ${item.title}")

        web.settings.javaScriptEnabled = true
        web.loadUrl(item.content)
        //read watch item

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            FirebaseFirestore.getInstance().collection("users")
                .document(uid!!)
                .collection("watchItems")
                .document(item.id).get()         //看現在點進去的商品 有沒有在收藏清單內
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val watchItem = task.result?.toObject(WatchItem::class.java)
                        if (watchItem != null) {
                            watch.isChecked = true
                        }
                    }
                }
        }

        //watches
        watch.setOnCheckedChangeListener { button, checked ->

            if (checked) {
                FirebaseFirestore.getInstance().collection("users")
                    .document(uid!!)
                    .collection("watchItems")
                    .document(item.id)
                    .set(WatchItem(item.id))
                item.id?.let {
                    FirebaseFirestore.getInstance().collection("items")
                        .document(it).update("favorite", 1)
                }

            } else {
                FirebaseFirestore.getInstance().collection("users")
                    .document(uid!!)
                    .collection("watchItems")
                    .document(item.id)
                    .delete()
                item.id?.let {
                    FirebaseFirestore.getInstance().collection("items")
                        .document(it).update("favorite", 0)
                }

            }

        }
    }

    override fun onStart() {
        super.onStart()
        item.viewCount++
        item.id?.let {
            FirebaseFirestore.getInstance().collection("items")
                .document(it).update("viewCount", item.viewCount)
            //.document(it).set(item)//的話可以重新設定整個物件根據我們定義的Item
        }
    }
}
