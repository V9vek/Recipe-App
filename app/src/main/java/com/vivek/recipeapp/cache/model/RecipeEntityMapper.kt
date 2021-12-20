package com.vivek.recipeapp.cache.model

import com.vivek.recipeapp.domain.model.Recipe
import com.vivek.recipeapp.domain.util.DomainMapper
import com.vivek.recipeapp.utils.DateUtil
import javax.inject.Inject

class RecipeEntityMapper @Inject constructor() : DomainMapper<RecipeEntity, Recipe> {

    override fun mapToDomainModel(model: RecipeEntity): Recipe {
        return Recipe(
            id = model.id,
            title = model.title,
            publisher = model.publisher,
            featuredImage = model.featuredImage,
            rating = model.rating,
            sourceUrl = model.sourceUrl,
            ingredients = convertIngredientsToList(model.ingredients),
            dateAdded = DateUtil.longToDate(model.dateAdded),
            dateUpdated = DateUtil.longToDate(model.dateUpdated)
        )
    }

    override fun mapFromDomainModel(domainModel: Recipe): RecipeEntity {
        return RecipeEntity(
            id = domainModel.id,
            title = domainModel.title,
            publisher = domainModel.publisher,
            featuredImage = domainModel.featuredImage,
            rating = domainModel.rating,
            sourceUrl = domainModel.sourceUrl,
            ingredients = convertIngredientListToString(domainModel.ingredients),
            dateAdded = DateUtil.dateToLong(domainModel.dateAdded),
            dateUpdated = DateUtil.dateToLong(domainModel.dateUpdated),
            dateCached = DateUtil.dateToLong(DateUtil.createTimestamp())
        )
    }

    private fun convertIngredientListToString(ingredients: List<String>): String {
        val ingredientsString = StringBuilder()
        for (ingredient in ingredients) {
            ingredientsString.append("$ingredient,")
        }
        return ingredientsString.toString()
    }

    private fun convertIngredientsToList(ingredientsString: String?): List<String> {
        val list: ArrayList<String> = ArrayList()
        ingredientsString?.let {
            for (ingredient in it.split(",")) {
                list.add(ingredient)
            }
        }

        return list
    }
}



















