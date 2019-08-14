package com.app.shop_firebase

import android.os.Parcelable
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@Parcelize
@IgnoreExtraProperties
data class Item(val title: String,
                var price: Int,
                var imageURL:String,
                @get:Exclude var id:String,    //不寫進firestore
                var content:String,
                var viewCount:Int) : Parcelable {
    constructor() : this("", 0,"","","",0)

}