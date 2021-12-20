package com.vivek.recipeapp.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Cache/Entity Model

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "title")
    var title: String,

    @ColumnInfo(name = "publisher")
    var publisher: String,

    @ColumnInfo(name = "featured_image")
    var featuredImage: String,

    @ColumnInfo(name = "rating")
    var rating: Int,

    @ColumnInfo(name = "source_url")
    var sourceUrl: String,

    /**
     * Comma separated list of ingredients
     * EX: "carrots, cabbage, chicken,"
     */
    @ColumnInfo(name = "ingredients")
    var ingredients: String,

    @ColumnInfo(name = "date_added")
    var dateAdded: Long,

    @ColumnInfo(name = "date_updated")
    var dateUpdated: Long,

    /**
     * The date this recipe was "added + refreshed" in the cache.
     */
    @ColumnInfo(name = "date_refreshed")
    var dateCached: Long
)






















