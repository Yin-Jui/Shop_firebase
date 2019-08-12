package com.app.shop_firebase

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_row.view.*

class itemHolder(view: View) : RecyclerView.ViewHolder(view) {


    var titleText = view.item_title
    var priceText = view.item_price

    fun bindTo(item: Item) {
        titleText.setText(item.title)
        priceText.setText(item.price.toString())

    }
}