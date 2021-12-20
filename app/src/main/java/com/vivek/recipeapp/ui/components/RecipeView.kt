package com.vivek.recipeapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.vivek.recipeapp.domain.model.Recipe
import com.vivek.recipeapp.utils.loadImage

const val IMAGE_HEIGHT = 260

@Composable
fun RecipeView(
    recipe: Recipe
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxWidth()
    ) {
        val image = loadImage(url = recipe.featuredImage, defaultImage = DEFAULT_RECIPE_IMAGE)
        image?.let { img ->
            Image(
                bitmap = img.asImageBitmap(),
                contentDescription = "",
                modifier = Modifier
                    .fillMaxWidth()
                    .requiredHeight(IMAGE_HEIGHT.dp),
                contentScale = ContentScale.Crop
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // Top Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = recipe.title,
                    modifier = Modifier.fillMaxWidth(0.85f),
                    style = MaterialTheme.typography.h6
                )

                val rating = recipe.rating.toString()
                Text(
                    text = rating,
                    style = MaterialTheme.typography.h6
                )
            }

            val updated = recipe.dateUpdated
            Text(
                text = "Updated $updated by ${recipe.publisher}",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                style = MaterialTheme.typography.subtitle1
            )

            for (ingredient in recipe.ingredients) {
                Text(
                    text = ingredient,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}



















