package com.vivek.recipeapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DarkMode
import androidx.compose.material.icons.rounded.LightMode
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.vivek.recipeapp.ui.screens.recipe_list.FoodCategory
import com.vivek.recipeapp.ui.screens.recipe_list.getAllFoodCategories
import kotlinx.coroutines.launch

@Composable
fun SearchAppBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    onExecuteSearch: () -> Unit,
    categoryScrollPosition: Int,
    selectedCategory: FoodCategory?,
    isDarkTheme: Boolean,
    onSelectedCategoryChanged: (String) -> Unit,
    onChangeCategoryScrollPosition: (Int) -> Unit,
    onToggleTheme: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colors.surface,
        elevation = 8.dp
    ) {
        Column {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = query,
                    onValueChange = { onQueryChanged(it) },
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(20))
                        .background(color = MaterialTheme.colors.surface),
                    label = { Text(text = "Search") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = ""
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = { onExecuteSearch() }
                    ),
                    textStyle = TextStyle(color = MaterialTheme.colors.onSurface),
                )

                IconButton(onClick = { onToggleTheme() }) {
                    Icon(
                        imageVector = if (isDarkTheme) Icons.Rounded.LightMode else Icons.Rounded.DarkMode,
                        contentDescription = ""
                    )
                }
            }

            // Horizontal Scrollable Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState),
            ) {
                scope.launch {
                    scrollState.scrollTo(categoryScrollPosition)
                }

                for (category in getAllFoodCategories()) {
                    FoodCategoryChip(
                        category = category.value,
                        isSelected = selectedCategory == category,
                        onSelectedCategoryChanged = { selectedChip ->
                            onSelectedCategoryChanged(selectedChip)
                            onChangeCategoryScrollPosition(scrollState.value)
                        },
                        onExecuteSearch = { onExecuteSearch() }
                    )
                }
            }
        }
    }
}