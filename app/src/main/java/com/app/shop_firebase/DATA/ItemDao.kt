package com.app.shop_firebase.DATA

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.shop_firebase.MODULE.Item

@Dao
interface ItemDao{

    @Query("select * from Item order by viewCount")
    fun getItems(): LiveData<List<Item>>

    @Query("select * from Item where category== :categoryId order by viewCount")
    fun getItemByCategory(categoryId: String):LiveData<List<Item>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addItem(item: Item)


}