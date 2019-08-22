package com.app.shop_firebase.VIEW

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.app.shop_firebase.MODULE.Item
import com.app.shop_firebase.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_row.view.*

class itemHolder(view: View) : RecyclerView.ViewHolder(view) {


    var titleText = view.item_title
    var priceText = view.item_price
    var image = view.item_image
    var countText = view.itemCount

    fun bindTo(item: Item) {
        titleText.setText(item.title)
        priceText.setText(item.price.toString())
        Glide.with(itemView.context)
            .load(item.imageURL)
            .apply(RequestOptions().override(120))
            .into(image)
        countText.setText(item.viewCount.toString())
        countText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.eyee,0,0,0)
    }
}