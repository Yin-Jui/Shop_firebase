package com.app.shop_firebase.MODULE

import com.google.firebase.Timestamp

data class WatchItem(val id:String, val timestamp: com.google.firebase.Timestamp){

    constructor(id:String):this(id,Timestamp.now())
    constructor(): this("", Timestamp.now())

}