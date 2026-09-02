package com.example.cst438_project1_team5.ui.shop

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShopScreen(
    items: List<ShopItem> = placeholderShopItems
) {
    var selectedItem by remember { mutableStateOf<ShopItem?>(null) }
    var cartItemCount by rememberSaveable { mutableIntStateOf(0) }

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
                            R.string.cart_count,
                            cartItemCount
                        ),
                        modifier = Modifier.padding(end = 16.dp)
                    )
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
            onBuy = {
                cartItemCount++
                selectedItem = null

                coroutineScope.launch {
                    snackbarHostState.showSnackbar(confirmationMessage)
                }
            }
        )
    }
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
    onBuy: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = item.title)
        },
        text = {
            Text(text = item.description)
        },
        confirmButton = {
            Button(onClick = onBuy) {
                Text(text = stringResource(R.string.buy))
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
