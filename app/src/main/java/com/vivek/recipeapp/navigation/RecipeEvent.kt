package com.vivek.recipeapp.navigation

sealed class RecipeEvent {

    data class GetRecipeEvent(val recipeId: Int) : RecipeEvent()
}