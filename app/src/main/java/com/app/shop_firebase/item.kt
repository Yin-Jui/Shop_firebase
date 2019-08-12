package com.app.shop_firebase

data class Item(val title: String, var price: Int) {
    constructor() : this("", 0)
}