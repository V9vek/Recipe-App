package com.vivek.recipeapp.ui.screens.recipeList

sealed class RecipeListEvent {

    object NewSearchEvent : RecipeListEvent()

    object NextPageSearchEvent : RecipeListEvent()

    // restore after process death
    object RestoreStateEvent : RecipeListEvent()
}