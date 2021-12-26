package com.vivek.recipeapp.cache

import com.vivek.recipeapp.cache.model.RecipeEntity

class RecipeDaoFake(
    private val appDatabaseFake: AppDatabaseFake
) : RecipeDao {
    override suspend fun insertRecipe(recipe: RecipeEntity): Long {
        appDatabaseFake.recipes.add(recipe)
        return 1        // return 1 for SUCCESS
    }

    override suspend fun insertRecipes(recipes: List<RecipeEntity>): LongArray {
        appDatabaseFake.recipes.addAll(recipes)
        return longArrayOf(1)
    }

    override suspend fun getRecipeById(id: Int): RecipeEntity? {
        return appDatabaseFake.recipes.find { it.id == id }
    }

    override suspend fun deleteRecipe(primaryKey: Int): Int {
        return 1
    }

    override suspend fun deleteRecipes(ids: List<Int>): Int {
        return 1
    }

    override suspend fun deleteAllRecipes() {}

    override suspend fun searchRecipes(
        query: String,
        page: Int,
        pageSize: Int
    ): List<RecipeEntity> {
        return appDatabaseFake.recipes
    }

    override suspend fun getAllRecipes(page: Int, pageSize: Int): List<RecipeEntity> {
        return appDatabaseFake.recipes
    }

    override suspend fun restoreRecipes(
        query: String,
        page: Int,
        pageSize: Int
    ): List<RecipeEntity> {
        return appDatabaseFake.recipes
    }

    override suspend fun restoreAllRecipes(page: Int, pageSize: Int): List<RecipeEntity> {
        return appDatabaseFake.recipes
    }
}