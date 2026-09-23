package com.example.cst438_project1_team5.ui.shop

import androidx.annotation.DrawableRes
import com.example.cst438_project1_team5.R

data class ShopItem(
    val id: Int,
    val title: String,
    val description: String,
    val animeCoinPrice: Int,
    val profileEffect: ProfileEffect,
    @param:DrawableRes val imageResId: Int
)

enum class ProfileEffect(val label: String) {
    FIRE("Fire profile theme"),
    SAKURA("Sakura profile theme"),
    OCEAN("Ocean profile theme"),
    GOLD("Gold profile frame")
}

/** Test cosmetics with bundled art that previews the profile color each item applies. */
val placeholderShopItems: List<ShopItem> = listOf(
    ShopItem(
        id = 1,
        title = "Fire Aura",
        description = "A warm ember palette for your profile card.",
        animeCoinPrice = 10,
        profileEffect = ProfileEffect.FIRE,
        imageResId = R.drawable.shop_fire_aura
    ),
    ShopItem(
        id = 2,
        title = "Sakura Bloom",
        description = "A soft pink sakura palette for your profile card.",
        animeCoinPrice = 20,
        profileEffect = ProfileEffect.SAKURA,
        imageResId = R.drawable.shop_sakura_bloom
    ),
    ShopItem(
        id = 3,
        title = "Ocean Wave",
        description = "A cool blue ocean palette for your profile card.",
        animeCoinPrice = 30,
        profileEffect = ProfileEffect.OCEAN,
        imageResId = R.drawable.shop_ocean_wave
    ),
    ShopItem(
        id = 4,
        title = "Golden Frame",
        description = "A gold frame around your profile avatar.",
        animeCoinPrice = 40,
        profileEffect = ProfileEffect.GOLD,
        imageResId = R.drawable.shop_golden_frame
    )
)
