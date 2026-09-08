package com.example.cst438_project1_team5.ui.soundTest

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.cst438_project1_team5.R

@Composable
fun SoundTestEntry(modifier: Modifier = Modifier)
{
    var soundTest by rememberSaveable { mutableStateOf("") }

    Spacer(modifier = Modifier.height(24.dp))

    var searchSong = ""
    OutlinedTextField(
        value = searchSong,
        onValueChange = { searchSong = "" },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.searchSong)) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Color(0xFF7DD3FC)
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        colors = androidx.compose.material3.TextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color(0xFF7DD3FC),
            unfocusedIndicatorColor = Color(0xFF475569),
            focusedLabelColor = Color(0xFF7DD3FC),
            unfocusedLabelColor = Color(0xFFCBD5E1)
        )
    )

}

