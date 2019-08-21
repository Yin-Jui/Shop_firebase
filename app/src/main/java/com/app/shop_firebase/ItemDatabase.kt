package com.app.shop_firebase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import java.security.AccessControlContext

@Database(entities = [Item::class], version = 1)  //標示類別使用了那些entity
abstract class ItemDatabase : RoomDatabase() {

    abstract fun getItemDao(): ItemDao

    companion object {
        var INSTANCE: ItemDatabase? = null
        fun getDatabase(context: Context): ItemDatabase? {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context, ItemDatabase::class.java, "mydb")
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE
        }

    }


}