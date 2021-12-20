package com.vivek.recipeapp.network.model

import com.vivek.recipeapp.domain.model.Recipe
import com.vivek.recipeapp.domain.util.DomainMapper
import com.vivek.recipeapp.utils.DateUtil
import javax.inject.Inject

class RecipeDtoMapper @Inject constructor() : DomainMapper<RecipeDto, Recipe> {
    override fun mapToDomainModel(model: RecipeDto): Recipe {
        return Recipe(
            id = model.pk,
            title = model.title,
            publisher = model.publisher,
            featuredImage = model.featuredImage,
            rating = model.rating,
            sourceUrl = model.sourceUrl,
            ingredients = model.ingredients ?: listOf(),
            dateAdded = DateUtil.longToDate(model.longDateAdded),
            dateUpdated = DateUtil.longToDate(model.longDateUpdated)
        )
    }

    override fun mapFromDomainModel(domainModel: Recipe): RecipeDto {
        return RecipeDto(
            pk = domainModel.id,
            title = domainModel.title,
            publisher = domainModel.publisher,
            featuredImage = domainModel.featuredImage,
            rating = domainModel.rating,
            sourceUrl = domainModel.sourceUrl,
            ingredients = domainModel.ingredients,
            longDateAdded = DateUtil.dateToLong(domainModel.dateAdded),
            longDateUpdated = DateUtil.dateToLong(domainModel.dateUpdated)
        )
    }

    fun toDomainModelList(initial: List<RecipeDto>): List<Recipe> {
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainModelList(initial: List<Recipe>): List<RecipeDto> {
        return initial.map { mapFromDomainModel(it) }
    }
}













