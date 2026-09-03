package com.example.cst438_project1_team5.ui.shop

import androidx.compose.foundation.Image
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
import com.example.cst438_project1_team5.R
import com.example.cst438_project1_team5.ui.theme.CST438Project1Team5Theme
import kotlinx.coroutines.launch

private const val STARTING_ANIME_COIN_BALANCE = 100

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopScreen(
    items: List<ShopItem> = placeholderShopItems
) {
    var selectedItem by remember { mutableStateOf<ShopItem?>(null) }
    var showCartDialog by remember { mutableStateOf(false) }
    var animeCoinBalance by remember { mutableIntStateOf(STARTING_ANIME_COIN_BALANCE) }
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
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.shop_title))
                },
                actions = {
                    Text(
                        text = stringResource(
                            R.string.anime_coin_balance,
                            animeCoinBalance
                        )
                    )
                    TextButton(onClick = { showCartDialog = true }) {
                        Text(
                            text = stringResource(
                                R.string.cart_count,
                                cartItemCount
                            )
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
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
                items = items,
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
                cartQuantities[item.id] = (cartQuantities[item.id] ?: 0) + 1
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
                val message = if (animeCoinBalance >= cartTotal) {
                    animeCoinBalance -= cartTotal
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
                                Text(text = item.title)
                                Text(
                                    text = stringResource(
                                        R.string.cart_item_quantity,
                                        quantity
                                    )
                                )
                            }
                            TextButton(onClick = { onRemove(item.id) }) {
                                Text(text = stringResource(R.string.remove))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(
                            R.string.cart_total,
                            total
                        )
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onBuy,
                enabled = cartItems.isNotEmpty()
            ) {
                Text(text = stringResource(R.string.buy))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.close))
            }
        }
    )
}

@Composable
fun ShopItemCard(
    item: ShopItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
        )
    ) {
        Image(
            painter = painterResource(item.imageResId),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun ShopItemDialog(
    item: ShopItem,
    onDismiss: () -> Unit,
    onAddToCart: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
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
                    )
                )
            }
        },
        confirmButton = {
            Button(onClick = onAddToCart) {
                Text(text = stringResource(R.string.add_to_cart))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel))
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
