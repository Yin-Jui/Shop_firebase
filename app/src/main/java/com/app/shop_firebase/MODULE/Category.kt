package com.app.shop_firebase.MODULE

data class Category(var id:String, var name:String){

    override fun toString(): String {
        return name
    }
}