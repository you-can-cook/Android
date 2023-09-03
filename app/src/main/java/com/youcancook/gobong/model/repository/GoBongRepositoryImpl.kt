package com.youcancook.gobong.model.repository

import com.youcancook.gobong.model.Card
import com.youcancook.gobong.model.Filter
import com.youcancook.gobong.model.RecipePost
import com.youcancook.gobong.model.UploadRecipe
import com.youcancook.gobong.model.User
import com.youcancook.gobong.model.datasource.GoBongRemoteDataSource

class GoBongRepositoryImpl(
    private val goBongDataSource: GoBongRemoteDataSource,
) : GoBongRepository {
    override suspend fun getCurrentRecipe(recipePostId: Int): RecipePost {
        return goBongDataSource.getCurrentRecipe(recipePostId)
    }

    override suspend fun getFollowingRecipes(): List<Card> {
        return goBongDataSource.getFollowingRecipes()
    }

    override suspend fun getAllRecipes(): List<Card> {
        return goBongDataSource.getAllRecipes()
    }

    override suspend fun getFilteredRecipes(filter: Filter): List<Card> {
        return goBongDataSource.getFilteredRecipes(filter)
    }

    override suspend fun getBookmarkedRecipes(): List<Card> {
        return goBongDataSource.getBookmarkedRecipes()
    }

    override suspend fun bookmarkRecipe(marked: Boolean, recipeId: Int) {
        goBongDataSource.bookmarkRecipe(marked, recipeId)
    }

    override suspend fun deleteRecipe(recipeId: Int) {
        goBongDataSource.deleteRecipe(recipeId)
    }

    override suspend fun reviewRecipe(recipeId: Int, star: Int) {
        goBongDataSource.reviewRecipe(recipeId, star)
    }

    override suspend fun updateReviewRecipe(recipeId: Int, star: Int) {
        goBongDataSource.updateReviewRecipe(recipeId, star)
    }

    override suspend fun uploadRecipe(uploadRecipe: UploadRecipe) {
        goBongDataSource.uploadRecipe(uploadRecipe)
    }

    override suspend fun getMyRecipes(): List<Card> {
        return goBongDataSource.getMyRecipes()
    }

    override suspend fun getOthersRecipes(userId: Int): List<Card> {
        return goBongDataSource.getOthersRecipes(userId)
    }

    override suspend fun getMyInfo(): User {
        return goBongDataSource.getMyInfo()
    }

    override suspend fun getOthersInfo(userId: Int): User {
        return goBongDataSource.getOthersInfo(userId)
    }
}