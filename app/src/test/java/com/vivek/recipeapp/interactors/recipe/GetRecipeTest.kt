package com.vivek.recipeapp.interactors.recipe

import com.google.gson.GsonBuilder
import com.vivek.recipeapp.cache.AppDatabaseFake
import com.vivek.recipeapp.cache.RecipeDaoFake
import com.vivek.recipeapp.cache.model.RecipeEntityMapper
import com.vivek.recipeapp.domain.model.Recipe
import com.vivek.recipeapp.interactors.recipe_list.SearchRecipes
import com.vivek.recipeapp.network.RecipeApiService
import com.vivek.recipeapp.network.data.MockWebServerResponses
import com.vivek.recipeapp.network.model.RecipeDtoMapper
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

class GetRecipeTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var baseUrl: HttpUrl
    private val appDatabaseFake = AppDatabaseFake()
    private val DUMMY_TOKEN = "does not matter"     // can be anything
    private val DUMMY_QUERY = "does not matter"     // can be anything

    // system in test
    private lateinit var getRecipe: GetRecipe
    private val RECIPE_ID = 1551

    // Dependencies
    private lateinit var searchRecipes: SearchRecipes
    private lateinit var recipeApiService: RecipeApiService
    private lateinit var recipeDaoFake: RecipeDaoFake
    private val recipeDtoMapper = RecipeDtoMapper()
    private val recipeEntityMapper = RecipeEntityMapper()


    @BeforeEach
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        baseUrl = mockWebServer.url(path = "/api/recipe/")
        recipeApiService = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(RecipeApiService::class.java)

        recipeDaoFake = RecipeDaoFake(appDatabaseFake = appDatabaseFake)

        searchRecipes = SearchRecipes(
            recipeApiService = recipeApiService,
            recipeDao = recipeDaoFake,
            recipeDtoMapper = recipeDtoMapper,
            recipeEntityMapper = recipeEntityMapper
        )

        // instantiate the system in test
        getRecipe = GetRecipe(
            recipeApiService = recipeApiService,
            recipeDao = recipeDaoFake,
            recipeDtoMapper = recipeDtoMapper,
            recipeEntityMapper = recipeEntityMapper
        )
    }


    /**
     *  1. Get some recipes from network and insert them into cache
     *  2. Try to retrieve recipes by their unique recipe id
     */

    @Test
    fun getRecipesFromNetwork_getRecipeById() = runBlocking {
        // mockWebServer setup AND condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockWebServerResponses.recipeListResponse)
        )

        // confirm the cache is empty to start
        assert(recipeDaoFake.getAllRecipes(page = 1).isEmpty())

        // insert recipes into cache
        searchRecipes.execute(
            token = DUMMY_TOKEN, query = DUMMY_QUERY, page = 1, isNetworkAvailable = true
        ).toList()

        // confirm cache is NO longer EMPTY
        assert(recipeDaoFake.getAllRecipes(page = 1).isNotEmpty())

        // run our use case
        val flowItems = getRecipe.execute(
            token = DUMMY_TOKEN,
            recipeId = RECIPE_ID,
            isNetworkAvailable = true
        ).toList()

        // first emission should be LOADING
        assert(flowItems[0].loading)

        // second emission should be the RECIPE with RECIPE_ID
        val recipe = flowItems[1].data
        assert(recipe?.id == RECIPE_ID)

        // confirm it is actually RECIPE object
        assert(recipe is Recipe)

        // ensure LOADING is FALSE now
        assert(!flowItems[1].loading)
    }

    /**
     * 1. Try to get recipe that does not exist in the cache
     * Result should be:
     * 1. Recipe is retrieved from the network and inserted into cache
     * 2. Recipe is returned as flow from cache
     */

    @Test
    fun attemptGetNullRecipeFromCache_getRecipeById() = runBlocking {
        // mockWebServer setup AND condition the response
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(MockWebServerResponses.recipeWithId1551)
        )

        // confirm the cache is empty to start
        assert(recipeDaoFake.getAllRecipes(page = 1).isEmpty())

        // run our use case with empty cache, to trigger else statement in GetRecipe usecase
        val flowItems = getRecipe.execute(
            token = DUMMY_TOKEN,
            recipeId = RECIPE_ID,
            isNetworkAvailable = true
        ).toList()

        // first emission should be LOADING
        assert(flowItems[0].loading)

        // second emission should be the RECIPE with RECIPE_ID
        val recipe = flowItems[1].data
        assert(recipe?.id == RECIPE_ID)

        // confirm the recipe is in the cache now
        assert(recipeDaoFake.getRecipeById(id = RECIPE_ID)?.id == RECIPE_ID)

        // confirm it is actually RECIPE object
        assert(recipe is Recipe)

        // ensure LOADING is FALSE now
        assert(!flowItems[1].loading)
    }


    @AfterEach
    fun tearDown() {
        mockWebServer.shutdown()
    }
}























