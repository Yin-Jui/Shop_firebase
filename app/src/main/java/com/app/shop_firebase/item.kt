package com.app.shop_firebase

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Item(val title: String,
                var price: Int,
                var imageURL:String,
                var id:String,
                var content:String) : Parcelable {
    constructor() : this("", 0,"","","")

}