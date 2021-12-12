package com.vivek.recipeapp.ui.screens.recipe

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun RecipeScreen(
    viewModel: RecipeViewModel = hiltViewModel()
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Recipe Screen")
    }
}