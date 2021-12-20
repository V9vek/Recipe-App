package com.vivek.recipeapp.di

import androidx.room.Room
import com.vivek.recipeapp.BaseApplication
import com.vivek.recipeapp.cache.RecipeDao
import com.vivek.recipeapp.cache.database.RecipeDatabase
import com.vivek.recipeapp.cache.database.RecipeDatabase.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

    @Singleton
    @Provides
    fun provideDb(app: BaseApplication): RecipeDatabase {
        return Room.databaseBuilder(app, RecipeDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideRecipeDao(db: RecipeDatabase): RecipeDao {
        return db.recipeDao()
    }
}











