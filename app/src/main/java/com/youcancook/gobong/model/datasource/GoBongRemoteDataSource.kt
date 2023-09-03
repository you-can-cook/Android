package com.youcancook.gobong.model.datasource

import android.util.Log
import com.youcancook.gobong.model.Card
import com.youcancook.gobong.model.Filter
import com.youcancook.gobong.model.RecipePost
import com.youcancook.gobong.model.UploadRecipe
import com.youcancook.gobong.model.User
import com.youcancook.gobong.model.network.GoBongService
import com.youcancook.gobong.model.network.ImageService
import com.youcancook.gobong.model.network.UserService
import com.youcancook.gobong.model.network.dto.FilterDTO
import com.youcancook.gobong.model.network.dto.ReviewDTO
import com.youcancook.gobong.model.network.dto.toCard
import com.youcancook.gobong.model.network.dto.toFilterDTO
import com.youcancook.gobong.model.network.dto.toRecipePost
import com.youcancook.gobong.model.network.dto.toUser
import com.youcancook.gobong.model.toRecipeStepAddedDTO
import com.youcancook.gobong.model.toUploadRecipeDTO
import com.youcancook.gobong.util.ACCESS_TOKEN
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody


class GoBongRemoteDataSource(
    private val goBongService: GoBongService,
    private val userService: UserService,
    private val imageService: ImageService,
) {
//    fun getUrl(res: Int): String {
//        return "android.resource://com.youcancook.gobong/$res"
//    }

    private fun getToken(): String {
        return "Bearer $ACCESS_TOKEN"
    }

    suspend fun getCurrentRecipe(recipePostId: Int): RecipePost {
        val response = goBongService.getCurrentRecipe(getToken(), recipePostId)
        Log.e(
            "GoBongBab",
            "getCurrentRecipe $response ${response.body()} ${
                response.errorBody()?.string()
            }"
        )
        if (response.isSuccessful) {
            return response.body()?.toRecipePost() ?: throw Exception("네트워크가 불안정합니다")
        } else {
            throw Exception("네트워크가 불안정합니다")
        }
    }

    suspend fun getFollowingRecipes(): List<Card> {
        val response = goBongService.getFollowingRecipes(getToken(), 10)
        Log.e(
            "GoBongBab",
            "getFollowingRecipes $response ${response.body()} ${
                response.errorBody()?.string()
            }"
        )
        if (response.isSuccessful) {
            return response.body()?.feeds?.map { it.toCard() } ?: throw Exception("네트워크가 불안정합니다")
        } else {
            throw Exception("네트워크가 불안정합니다")
        }
    }

    suspend fun getAllRecipes(): List<Card> {
        val response = goBongService.getAllRecipes(getToken(), 10)
        Log.e(
            "GoBongBab",
            "getAllRecipe ${response.body()} ${response.errorBody()?.string()}"
        )
        if (response.isSuccessful) {
            return response.body()?.feeds?.map { it.toCard() } ?: throw Exception("네트워크가 불안정합니다")
        } else {
            throw Exception("네트워크가 불안정합니다")
        }
    }

    suspend fun getFilteredRecipes(filter: Filter): List<Card> {

        val request = filter.toFilterDTO()
        println("request filter $request")

        val response = goBongService.getFilteredRecipes(getToken(), 0, 10, request)
        Log.e(
            "GoBongBab",
            "getFilteredRecipe ${response.body()} ${response.errorBody()?.string()}"
        )

        if (response.isSuccessful) {
            return response.body()?.feeds?.map { it.toCard() } ?: throw Exception("네트워크가 불안정합니다")
        } else {
            throw Exception("네트워크가 불안정합니다")
        }
    }

    suspend fun getBookmarkedRecipes(): List<Card> {
        val response = goBongService.getBookmarkedRecipes(getToken(), 10)
        Log.e(
            "GoBongBab",
            "getBookmarkedRecipes ${response.body()} ${response.errorBody()?.string()}"
        )
        if (response.isSuccessful) {
            return response.body()?.feeds?.map { it.toCard() } ?: throw Exception("네트워크가 불안정합니다")
        } else {
            throw Exception("네트워크가 불안정합니다")
        }
    }

    suspend fun bookmarkRecipe(marked: Boolean, recipeId: Int) {
        val response = if (marked) {
            goBongService.bookmarkRecipe(getToken(), recipeId)
        } else {
            goBongService.deleteBookmarkRecipe(getToken(), recipeId)
        }

        if (response.isSuccessful.not()) throw Exception("네트워크가 불안정합니다")
    }

    suspend fun deleteRecipe(recipeId: Int) {
        val response = goBongService.deleteRecipe(getToken(), recipeId)
        Log.e(
            "GoBongBab",
            "deleteRecipe ${response.body()} ${response.errorBody()?.string()}"
        )
        if (response.isSuccessful.not()) throw Exception("네트워크가 불안정합니다")
    }

    suspend fun reviewRecipe(recipeId: Int, star: Int) {
        val request = ReviewDTO(
            recipeId = recipeId,
            score = star
        )
        val response = goBongService.reviewRecipe(getToken(), request)
        Log.e(
            "GoBongBab",
            "reviewRecipe ${response.code()} ${response.errorBody()?.string()}"
        )
        if (response.isSuccessful.not()) {
            throw Exception("네트워크가 불안정합니다")
        }
    }

    suspend fun updateReviewRecipe(recipeId: Int, star: Int) {
        val request = ReviewDTO(
            recipeId = recipeId,
            score = star
        )
        val response = goBongService.updaterReviewRecipe(getToken(), request)
        Log.e(
            "GoBongBab",
            "updateReview ${response} ${response.errorBody()?.string()}"
        )
        if (response.isSuccessful.not()) {
            throw Exception("네트워크가 불안정합니다")
        }
    }

    suspend fun uploadRecipe(uploadRecipe: UploadRecipe): String {
        val thumbnailUrl = getImageUrlByByteArray(uploadRecipe.thumbnailByteArray)
        val request = uploadRecipe.toUploadRecipeDTO(thumbnailUrl, uploadRecipe.recipes.map {
            if (it.photoUrl.isNotEmpty()) {
                it.toRecipeStepAddedDTO(getImageUrlByByteArray(it.photoUrl))
            } else {
                it.toRecipeStepAddedDTO(null)
            }
        })

        Log.e("GoBongBab", "uploadRecipe request ${request}")
        val response = goBongService.uploadRecipe(getToken(), request)
        Log.e("GoBongBab", "uploadRecipe ${response} ${response.errorBody()?.string()}")
        if (response.isSuccessful) {
            return response.body()?.id.toString()
        } else {
            throw Exception("네트워크가 불안정합니다")
        }
    }

    suspend fun getMyRecipes(): List<Card> {
        val response = goBongService.getMyRecipes(getToken(), 10)
        if (response.isSuccessful) {
            return response.body()?.feeds?.map { it.toCard() } ?: throw Exception("네트워크가 불안정합니다")
        } else {
            throw Exception("네트워크가 불안정합니다")
        }

    }

    suspend fun getOthersRecipes(userId: Int): List<Card> {
        val response = goBongService.getOthersRecipes(getToken(), userId, 10)
        if (response.isSuccessful) {
            return response.body()?.feeds?.map { it.toCard() } ?: throw Exception("네트워크가 불안정합니다")
        } else {
            throw Exception("네트워크가 불안정합니다")
        }

    }

    suspend fun getMyInfo(): User {
        val response = userService.getMyInfo(getToken())
        if (response.isSuccessful) {
            return response.body()?.toUser() ?: throw Exception("네트워크가 불안정합니다")
        } else {
            throw Exception("네트워크가 불안정합니다")
        }
    }

    suspend fun getOthersInfo(userId: Int): User {
        val response = userService.getOthersInfo(getToken(), userId)
        if (response.isSuccessful) {
            return response.body()?.toUser() ?: throw Exception("네트워크가 불안정합니다")
        } else {
            throw Exception("네트워크가 불안정합니다")
        }
    }

    private suspend fun getImageUrlByByteArray(imageByte: ByteArray): String {
        val requestFile = RequestBody.create(MediaType.parse("image/*"), imageByte);
        val uploadFile = MultipartBody.Part.createFormData("image", "", requestFile);
        val response = imageService.postImage(uploadFile)
        if (response.isSuccessful) {
            return response.body()?.imageUrl ?: throw Exception("이미지 업로드에 실패했습니다")
        } else {
            throw Exception("이미지 업로드에 실패했습니다")
        }
    }
}

//        return listOf(
//            Card(
//                "mark",
//                UserProfile(followed = true),
//                getUrl(R.drawable.markthumb),
//                "마크정식 100% 원조 레시피",
//                "100",
//                true,
//                "5분",
//                listOf("전자레인지"),
//                "쉬워요",
//                "4.5공기",
//                "편의점 꿀조합 레시피 best of best",
//                listOf(
//                    "자이언트떡볶이 1개",
//                    "의성마늘후랑크 소시지 1개",
//                    "콕콕콕 스파게티  1개",
//                    "모짜렐라 피자치즈 20g"
//                ),
//                listOf()
//            ),
//            Card(
//                "potato",
//                UserProfile(followed = true),
//                getUrl(R.drawable.potatothumb),
//                "초간단 고구마말랭이",
//                "125",
//                false,
//                "10분",
//                listOf("전자레인지"),
//                "쉬워요",
//                "4.2공기",
//                "전자레인지로 간단하게 만들 수 있는 고구마말랭이",
//                listOf(),
//                listOf()
//            ),
//            Card(
//                "noodle",
//                UserProfile(nickname = "국수 박사학위", followed = true),
//                getUrl(R.drawable.noodlethumb),
//                "냐혼자산다 화사의 간장국수 따라해보기",
//                "324",
//                false,
//                "7분 30초",
//                listOf("전자레인지"),
//                "보통이에요",
//                "4.4공기",
//                "만드는데 비용도 얼마 들지 않고 시간도 얼마 안걸리는 진짜 초간단 레시피",
//                listOf(),
//                listOf()
//            )
//        )


//return Card(
//            "mark",
//            UserProfile(followed = true),
//            getUrl(R.drawable.markthumb),
//            "마크정식 100% 원조 레시피",
//            "100",
//            true,
//            "5분",
//            listOf("전자레인지"),
//            "쉬워요",
//            "4.5공기",
//            "편의점 꿀조합 레시피 best of best",
//            listOf(
//                "자이언트떡볶이 1개",
//                "의성마늘후랑크 소시지 1개",
//                "콕콕콕 스파게티  1개",
//                "모짜렐라 피자치즈 20g"
//            ),
//            listOf(
//                RecipeStep(
//                    "30초",
//                    listOf("전자레인지"),
//                    getUrl(R.drawable.mark1),
//                    "준비한 소시지를 전자렌지에 약 30초간 데워주세요",
//                ),
//                RecipeStep(
//                    "20초",
//                    listOf(),
//                    getUrl(R.drawable.mark2),
//                    "자이언트떡볶이와 콕콕콕 스파게티에 물을 넣어주세요",
//                ),
//                RecipeStep(
//                    "3분",
//                    listOf("전자레인지"),
//                    getUrl(R.drawable.mark3),
//                    "물을 넣은 자이언트 떡볶이를 전자렌지에 3분 돌려주세요",
//                ),
//                RecipeStep(
//                    "50초",
//                    listOf(),
//                    "",
//                    "스파게티 물을 버리고 전자렌지에 돌린 떡볶이에 라면과 스프를 넣고 잘 섞어주세요. 의성마늘후랑크소시지를 먹기 좋은 크기로 잘라 넣어주세요",
//                ),
//                RecipeStep(
//                    "30초",
//                    listOf("전자레인지"),
//                    getUrl(R.drawable.mark4),
//                    "모짜렐라 피자치즈를 위에 얹어주고, 치즈가 녹을 정도로 전자렌지에 약 30초간 데워주세요",
//                )
//            )
//        )