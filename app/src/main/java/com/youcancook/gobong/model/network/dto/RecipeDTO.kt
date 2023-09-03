package com.youcancook.gobong.model.network.dto

import com.google.gson.annotations.SerializedName
import com.youcancook.gobong.model.Filter
import com.youcancook.gobong.model.RecipePost
import com.youcancook.gobong.model.RecipeStep
import com.youcancook.gobong.util.toEnglishTool
import com.youcancook.gobong.util.toKoreanTool
import com.youcancook.gobong.util.toTime

data class UploadRecipeDTO(
    @SerializedName("title") val title: String,
    @SerializedName("introduction") val introduction: String,
    @SerializedName("ingredients") val ingredients: List<String>,
    @SerializedName("difficulty") val difficulty: String,
    @SerializedName("thumbnailURL") val thumbnailURL: String,
    @SerializedName("recipeDetails") val recipeDetails: List<RecipeStepAddedDTO>,
)

data class RecipeStepAddedDTO(
    @SerializedName("content") val content: String,
    @SerializedName("imageURL") val imageURL: String? = null,
    @SerializedName("cookTimeInSeconds") val cookTimeInSeconds: Int,
    @SerializedName("cookwares") val cookwares: List<String>,
)

data class RecipeStepDTO(
    @SerializedName("id") val id: String,
    @SerializedName("content") val content: String,
    @SerializedName("imageURL") val imageURL: String? = null,
    @SerializedName("cookTimeInSeconds") val cookTimeInSeconds: Int,
    @SerializedName("cookwares") val cookwares: List<String>,
    @SerializedName("step") val step: Int,
)

data class CurrentRecipeDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("summary") val summary: FeedDTO,
    @SerializedName("introduction") val introduction: String,
    @SerializedName("ingredients") val ingredients: List<String>,
    @SerializedName("recipeDetails") val recipeDetails: List<RecipeStepDTO>,
    @SerializedName("myRate") val myRate: Int,
)

data class ReviewDTO(
    @SerializedName("recipeId") val recipeId: Int,
    @SerializedName("score") val score: Int,
)

data class FilterDTO(
    @SerializedName("query") val query: String?,
    @SerializedName("filterType") val filterType: String?,
    @SerializedName("difficulty") val difficulty: String?,
    @SerializedName("maxTotalCookTime") val maxTotalCookTime: Int?,
    @SerializedName("minRating") val minRating: Int?,
    @SerializedName("cookwares") val cookwares: List<String>?,
)

fun RecipeStepDTO.toRecipeStep(): RecipeStep {
    return RecipeStep(
        time = cookTimeInSeconds.toTime(),
        tools = cookwares.map { it.toKoreanTool() },
        photoUrl = imageURL ?: "",
        description = content,
        id = id.toLong()
    )
}

fun CurrentRecipeDTO.toRecipePost(): RecipePost {
    return RecipePost(
        id = id,
        cardInfo = summary.toCard(),
        description = introduction,
        ingredients = ingredients,
        recipes = recipeDetails.map { it.toRecipeStep() },
        myRate = myRate
    )
}

fun Filter.toFilterDTO(): FilterDTO {
    return FilterDTO(
        searchWord,
        if (sortType == "" || sortType == "최신순") "recent" else "popular",
        if (level == "") null else level,
        time.toInt() * 60,
        if (star == "") null else star.toInt(),
        cookwares = tools.map { it.toEnglishTool() }
    )
}
