package com.vivek.recipeapp.ui.screens.recipe

import androidx.lifecycle.ViewModel
import com.vivek.recipeapp.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val repository: RecipeRepository,
    private val token: String
) : ViewModel() {

}