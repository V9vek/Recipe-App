package com.vivek.recipeapp.interactors.recipe

import com.vivek.recipeapp.cache.RecipeDao
import com.vivek.recipeapp.cache.model.RecipeEntityMapper
import com.vivek.recipeapp.domain.data.DataState
import com.vivek.recipeapp.domain.model.Recipe
import com.vivek.recipeapp.network.RecipeApiService
import com.vivek.recipeapp.network.model.RecipeDtoMapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetRecipe(
    private val recipeApiService: RecipeApiService,
    private val recipeDao: RecipeDao,
    private val recipeDtoMapper: RecipeDtoMapper,
    private val recipeEntityMapper: RecipeEntityMapper
) {
    fun execute(
        token: String,
        recipeId: Int,
        isNetworkAvailable: Boolean
    ): Flow<DataState<Recipe>> = flow {
        try {
            emit(DataState.loading())
            delay(1000)

            var recipe = getRecipeFromCache(recipeId = recipeId)

            if (recipe != null) {
                emit(DataState.success(data = recipe))
            } else {
                if (isNetworkAvailable) {
                    // if by chance recipe is not in Cache, get recipe from network and cache it
                    val networkRecipe = getRecipeFromNetwork(token = token, recipeId = recipeId)

                    recipeDao.insertRecipe(
                        recipe = recipeEntityMapper.mapFromDomainModel(networkRecipe)
                    )
                }

                recipe = getRecipeFromCache(recipeId = recipeId)

                if (recipe != null) {
                    emit(DataState.success(data = recipe))
                } else {
                    throw Exception("Unable to get the recipe from the cache!")
                }
            }

        } catch (e: Exception) {
            emit(DataState.error(message = e.message ?: "Unknown Error!"))
        }
    }

    private suspend fun getRecipeFromCache(recipeId: Int): Recipe? {
        return recipeDao.getRecipeById(id = recipeId)?.let { recipeEntity ->
            recipeEntityMapper.mapToDomainModel(recipeEntity)
        }
    }

    private suspend fun getRecipeFromNetwork(token: String, recipeId: Int): Recipe {
        return recipeDtoMapper.mapToDomainModel(
            recipeApiService.get(token = token, id = recipeId)
        )
    }
}

























