package com.vivek.recipeapp.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vivek.recipeapp.cache.RecipeDao
import com.vivek.recipeapp.cache.model.RecipeEntity

@Database(entities = [RecipeEntity::class], version = 1)
abstract class RecipeDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao

    companion object {
        val DATABASE_NAME = "recipe_db"
    }
}

























