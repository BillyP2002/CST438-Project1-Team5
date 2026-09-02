package com.example.cst438_project1_team5.ui.shop

import androidx.annotation.DrawableRes
import com.example.cst438_project1_team5.R

data class ShopItem(
    val id: Int,
    val title: String,
    val description: String,
    @param:DrawableRes val imageResId: Int
)

val placeholderShopItems: List<ShopItem> = List(8) { index ->
    val itemNumber = index + 1

    ShopItem(
        id = itemNumber,
        title = "Item $itemNumber",
        description = "Description for item $itemNumber",
        imageResId = R.drawable.shop_item_placeholder
    )
}
