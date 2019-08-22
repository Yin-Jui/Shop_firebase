package com.app.shop_firebase.DATA

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import com.app.shop_firebase.MODULE.Item
import com.app.shop_firebase.VIEW.FirestoreQuery

@Suppress("DEPRECATION")
class ItemRepository(application: Application) {
    private var itemDao: ItemDao
    private lateinit var items: LiveData<List<Item>>
    private var firestoreQuery = FirestoreQuery()
    public var network: Boolean

    init {
        itemDao = ItemDatabase.getDatabase(application)!!.getItemDao()
        items = itemDao.getItems()

        val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
       network = activeNetwork?.isConnectedOrConnecting == true

    }

    fun getAllItems(): LiveData<List<Item>> {
        if (network) {
            items = firestoreQuery
            Log.d("JIMMY","FIREEEE  $network")
        } else {
            items = itemDao.getItems()
            Log.d("JIMMY","DAOOOOO")
        }
        return items
    }

    fun setCategory(categoryId: String) {
        if (network) {
            firestoreQuery.setCategory(categoryId)
        } else {
            items = itemDao.getItemByCategory(categoryId)
        }
    }

}