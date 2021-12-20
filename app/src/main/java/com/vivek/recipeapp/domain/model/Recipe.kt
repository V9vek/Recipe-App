package com.vivek.recipeapp.domain.model

import java.util.*

// Domain Model

data class Recipe(
    val id: Int,
    val title: String,
    val publisher: String,
    val featuredImage: String,
    val rating: Int,
    val sourceUrl: String,
    val ingredients: List<String> = listOf(),
    val dateAdded: Date,
    val dateUpdated: Date,
)