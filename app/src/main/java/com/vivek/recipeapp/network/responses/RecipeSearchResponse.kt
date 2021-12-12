package com.vivek.recipeapp.network.responses

import com.google.gson.annotations.SerializedName
import com.vivek.recipeapp.network.model.RecipeDto

// Network Response

data class RecipeSearchResponse(

    @SerializedName("count")
    val count: Int,

    @SerializedName("results")
    val recipes: List<RecipeDto>
)