package com.javimay.uala.ui.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.javimay.uala.ui.theme.Gold
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hintText: String = "Search...",
    onUpdateText: (String) -> Unit,
    textSearch: String,
    onSearch: (String, Boolean) -> Unit,
    onUpdateFavorite: (Boolean) -> Unit = {},
    isFavorite: Boolean = false
) {
    var text by remember { mutableStateOf("") }
    var favorite by remember { mutableStateOf(isFavorite) }
    var isHintDisplayed by remember {
        mutableStateOf(hintText.isNotEmpty() && text.isEmpty())
    }
    val focusManager = LocalFocusManager.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(2.dp, shape = CircleShape)
            .background(Color.White, shape = CircleShape)
    ) {
        text = textSearch
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Black
            )
            BasicTextField(
                value = text,
                onValueChange = {
                    text = it
                    onUpdateText.invoke(it)
                    onSearch(it, isFavorite)
                },
                maxLines = 1,
                singleLine = true,
                textStyle = TextStyle(color = Color.Black),
                modifier = Modifier
                    .padding(start = 20.dp)
                    .onFocusChanged { isHintDisplayed = it.isFocused != true && text.isEmpty() }
                    .weight(1f)
                    .semantics { contentDescription = "Search text field" }
            )

        }
        if (isHintDisplayed) {
            Text(
                text = hintText,
                color = Color.LightGray,
                modifier = Modifier
                    .padding(start = 50.dp)
                    .align(Alignment.CenterStart)
            )
        } else {
            IconButton(
                modifier = Modifier
                    .padding(end = 30.dp)
                    .align(Alignment.CenterEnd),
                onClick = {
                    onUpdateFavorite.invoke(!isFavorite)
                    favorite = !isFavorite
                    onSearch.invoke(text, favorite)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Search",
                    tint = if(isFavorite) Gold else Color.DarkGray
                )
            }
            IconButton(
                onClick = {
                    text = ""
                    onUpdateText.invoke("")
                    isHintDisplayed = true
                    focusManager.clearFocus()
                    onSearch(text, isFavorite)
                },
                content = {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear Search",
                        tint = Color.Black
                    )
                },
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}

@Composable
@Preview(uiMode = UI_MODE_NIGHT_YES, name = "Dark mode", showBackground = true)
private fun SearchBarPreview() {
    SearchBar(
        onUpdateText = {},
        textSearch = "",
        onSearch = { _, _ -> }
    )
}