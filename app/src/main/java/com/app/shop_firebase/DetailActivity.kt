package com.app.shop_firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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
    }

    override fun onStart() {
        super.onStart()
        item.viewCount++
        item.id?.let {
            FirebaseFirestore.getInstance().collection("items2")
                .document(it).update("viewCount", item.viewCount)
                //document(it).set(item)的話可以重新設定整個物件
        }

    }
}
