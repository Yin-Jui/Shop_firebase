package com.app.shop_firebase.DATA

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.app.shop_firebase.MODULE.Item

@Database(entities = [Item::class], version = 1)  //標示類別使用了那些entity
abstract class ItemDatabase : RoomDatabase() {

    abstract fun getItemDao(): ItemDao

    companion object {
        //      var INSTANCE: ItemDatabase? = null
        private lateinit var context: Context
        private val database: ItemDatabase by lazy {  //有人要用database才會執行
            Room.databaseBuilder(context, ItemDatabase::class.java, "mydb")
                .allowMainThreadQueries()
                .build()
        }

        fun getDatabase(context: Context): ItemDatabase? {   //傳入呼叫的context
            Companion.context = context
            return database
        }
    }
}