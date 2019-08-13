package com.app.shop_firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val item = intent.getParcelableExtra<Item>("ITEM")
        Log.d("Detail","Oncreate  ${item.id}    ${item.title}")

        web.settings.javaScriptEnabled = true
        web.loadUrl("https://www.ptt.cc/bbs/studyabroad/index1493.html")
    }
}
