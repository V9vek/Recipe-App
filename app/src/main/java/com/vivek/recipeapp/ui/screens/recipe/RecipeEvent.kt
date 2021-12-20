package com.vivek.recipeapp.ui.screens.recipe

sealed class RecipeEvent {

    data class GetRecipeEvent(val recipeId: Int) : RecipeEvent()
}