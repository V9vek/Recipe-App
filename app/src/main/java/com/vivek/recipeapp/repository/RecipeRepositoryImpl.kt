package com.vivek.recipeapp.repository

import com.vivek.recipeapp.domain.model.Recipe
import com.vivek.recipeapp.network.RecipeApiService
import com.vivek.recipeapp.network.model.RecipeDtoMapper

class RecipeRepositoryImpl(
    private val recipeApiService: RecipeApiService,
    private val mapper: RecipeDtoMapper
) : RecipeRepository {

    override suspend fun search(token: String, page: Int, query: String): List<Recipe> {
        val result = recipeApiService.search(token, page, query).recipes
        return mapper.toDomainModelList(result)
    }

    override suspend fun get(token: String, id: Int): Recipe {
        val result = recipeApiService.get(token, id)
        return mapper.mapToDomainModel(result)
    }
}