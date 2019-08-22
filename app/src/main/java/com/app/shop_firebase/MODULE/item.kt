package com.app.shop_firebase.MODULE

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize


@Parcelize
@IgnoreExtraProperties
@Entity                       //要儲存到資料庫的類別要標示
data class Item(val title: String,
                var price: Int,
                var imageURL:String,
                @PrimaryKey                //標示哪一個是primary key
                @get:Exclude var id:String,    //不寫進firestore
                var content:String,
                var viewCount:Int,
                var category:String) : Parcelable {
    constructor() : this("", 0,"","","",0,"")

}