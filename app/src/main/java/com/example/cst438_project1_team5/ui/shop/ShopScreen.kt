package com.example.cst438_project1_team5.ui.shop

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.TopAppBarDefaults
import com.example.cst438_project1_team5.R
import com.example.cst438_project1_team5.ui.theme.CST438Project1Team5Theme
import com.example.cst438_project1_team5.ui.components.ScreenBackground
import kotlinx.coroutines.launch

private const val STARTING_ANIME_COIN_BALANCE = 100

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopScreen(
    items: List<ShopItem> = placeholderShopItems,
    animeCoinBalance: Int? = null,
    ownedItemIds: Set<Int> = emptySet(),
    onPurchase: ((ShopItem) -> Boolean)? = null
) {
    //Column { onBackButton(onClick = onBackButton)
    var selectedItem by remember { mutableStateOf<ShopItem?>(null) }
    var showCartDialog by remember { mutableStateOf(false) }
    var localAnimeCoinBalance by remember { mutableIntStateOf(STARTING_ANIME_COIN_BALANCE) }
    val displayedCoinBalance = animeCoinBalance ?: localAnimeCoinBalance
    val availableItems = items.filterNot { it.id in ownedItemIds }
    val cartQuantities = remember { mutableStateMapOf<Int, Int>() }
    val cartItemCount = cartQuantities.values.sum()
    val cartTotal = cartQuantities.entries.sumOf { (itemId, quantity) ->
        val itemPrice = items.firstOrNull { item -> item.id == itemId }?.animeCoinPrice ?: 0
        itemPrice * quantity
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.shop_title), color = Color.White)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    Text(
                        text = stringResource(
                            R.string.anime_coin_balance,
                            displayedCoinBalance
                        ),
                        color = Color(0xFF7DD3FC)
                    )
                    TextButton(onClick = { showCartDialog = true }) {
                        Text(
                            text = stringResource(
                                R.string.cart_count,
                                cartItemCount
                            ),
                            color = Color(0xFF7DD3FC)
                        )
                    }
//                    TextButton(onClick = onBackButton) {
//                        Text(
//                            text = stringResource(
//                                R.string.back_button
//                            )
//                        )
//                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        ScreenBackground {
            LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                items = availableItems,
                key = { item -> item.id }
            ) { item ->
                ShopItemCard(
                    item = item,
                    onClick = {
                        selectedItem = item
                    }
                )
            }
        }
        }
    }

    selectedItem?.let { item ->
        val confirmationMessage = stringResource(
            R.string.added_to_cart,
            item.title
        )
        ShopItemDialog(
            item = item,
            onDismiss = {
                selectedItem = null
            },
            onAddToCart = {
                // Game-backed inventory contains one of each cosmetic item.
                cartQuantities[item.id] = if (onPurchase == null) {
                    (cartQuantities[item.id] ?: 0) + 1
                } else {
                    1
                }
                selectedItem = null

                coroutineScope.launch {
                    snackbarHostState.showSnackbar(confirmationMessage)
                }
            }
        )
    }

    if (showCartDialog) {
        val purchaseMessage = stringResource(
            R.string.purchase_complete,
            cartTotal
        )
        val insufficientFundsMessage = stringResource(
            R.string.not_enough_anime_coin
        )

        CartDialog(
            items = items,
            quantities = cartQuantities,
            total = cartTotal,
            onDismiss = { showCartDialog = false },
            onBuy = {
                val purchased = if (displayedCoinBalance >= cartTotal) {
                    if (onPurchase == null) {
                        localAnimeCoinBalance -= cartTotal
                        true
                    } else {
                        cartQuantities.all { (itemId, quantity) ->
                            val item = items.firstOrNull { it.id == itemId } ?: return@all false
                            (1..quantity).all { onPurchase(item) }
                        }
                    }
                } else {
                    false
                }
                val message = if (purchased) {
                    cartQuantities.clear()
                    purchaseMessage
                } else {
                    insufficientFundsMessage
                }

                showCartDialog = false

                coroutineScope.launch {
                    snackbarHostState.showSnackbar(message)
                }
            },
            onRemove = { itemId ->
                val newQuantity = (cartQuantities[itemId] ?: 0) - 1

                if (newQuantity > 0) {
                    cartQuantities[itemId] = newQuantity
                } else {
                    cartQuantities.remove(itemId)
                }
            }
        )
    }
}

@Composable
private fun CartDialog(
    items: List<ShopItem>,
    quantities: Map<Int, Int>,
    total: Int,
    onDismiss: () -> Unit,
    onBuy: () -> Unit,
    onRemove: (Int) -> Unit
) {
    val cartItems = items
        .distinctBy { item -> item.id }
        .mapNotNull { item ->
            quantities[item.id]
                ?.takeIf { quantity -> quantity > 0 }
                ?.let { quantity -> item to quantity }
        }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF111827).copy(alpha = 0.9f),
        titleContentColor = Color.White,
        textContentColor = Color(0xFFCBD5E1),
        title = {
            Text(text = stringResource(R.string.cart_title))
        },
        text = {
            if (cartItems.isEmpty()) {
                Text(text = stringResource(R.string.empty_cart))
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    cartItems.forEach { (item, quantity) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = item.title, color = Color.White)
                                Text(
                                    text = stringResource(
                                        R.string.cart_item_quantity,
                                        quantity
                                    ),
                                    color = Color(0xFFCBD5E1)
                                )
                            }
                            TextButton(onClick = { onRemove(item.id) }) {
                                Text(text = stringResource(R.string.remove), color = Color(0xFFFCA5A5))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(
                            R.string.cart_total,
                            total
                        ),
                        color = Color.White
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onBuy,
                enabled = cartItems.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C3AED))
            ) {
                Text(text = stringResource(R.string.buy), color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.close), color = Color(0xFF7DD3FC))
            }
        }
    )
}

@Composable
fun ShopItemCard(item: ShopItem, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val accessibilityDescription = stringResource(
        R.string.open_item_details,
        item.title
    )

    Card(
        onClick = onClick,
        modifier = modifier
            .aspectRatio(1f)
            .semantics {
                contentDescription = accessibilityDescription
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF111827).copy(alpha = 0.9f))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(item.imageResId),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .background(Color(0xCC111827))
                    .padding(10.dp)
            ) {
                Text(text = item.title, color = Color.White)
                Text(
                    text = "${item.animeCoinPrice} Anime Coins",
                    color = Color(0xFFFDE68A)
                )
            }
        }
    }
}

@Composable
fun ShopItemDialog(item: ShopItem, onDismiss: () -> Unit, onAddToCart: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF111827).copy(alpha = 0.9f),
        titleContentColor = Color.White,
        textContentColor = Color(0xFFCBD5E1),
        title = {
            Text(text = item.title)
        },
        text = {
            Column {
                Text(text = item.description)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(
                        R.string.anime_coin_price,
                        item.animeCoinPrice
                    ),
                    color = Color.White
                )
                Text(
                    text = "Effect: ${item.profileEffect.label}",
                    color = Color(0xFF7DD3FC)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onAddToCart,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C3AED))
            ) {
                Text(text = stringResource(R.string.add_to_cart), color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel), color = Color(0xFF7DD3FC))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun ShopScreenPreview() {
    CST438Project1Team5Theme(dynamicColor = false) {
        ShopScreen()
    }
}
