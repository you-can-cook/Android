package com.youcancook.gobong.model.network.dto

import com.google.gson.annotations.SerializedName
import com.youcancook.gobong.model.Card
import com.youcancook.gobong.model.User
import com.youcancook.gobong.model.UserProfile
import com.youcancook.gobong.model.UserToken
import com.youcancook.gobong.util.toKoreanTool
import com.youcancook.gobong.util.toStarRating
import com.youcancook.gobong.util.toTime

data class TemporaryTokenResponseDTO(
    @SerializedName("temporaryToken") val temporaryToken: String,
)

data class LoginResponseDTO(
    @SerializedName("grantType") val grantType: String,
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String,
)

data class ImageUrlResponseDTO(
    @SerializedName("imageUrl") val imageUrl: String,
)

data class AuthorDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("profileImageURL") val profileImageURL: String? = null,
    @SerializedName("myself") val myself: Boolean,
    @SerializedName("following") val following: Boolean,
)

data class FeedDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("thumbnailURL") val thumbnailURL: String? = null,
    @SerializedName("author") val author: AuthorDTO,
    @SerializedName("totalBookmarkCount") val totalBookmarkCount: Int,
    @SerializedName("totalCookTimeInSeconds") val totalCookTimeInSeconds: Int,
    @SerializedName("cookwares") val cookwares: List<String>,
    @SerializedName("difficulty") val difficulty: String,
    @SerializedName("averageRating") val averageRating: Double,
    @SerializedName("bookmarked") val bookmarked: Boolean,
)

data class RecipeFeedsResponseDTO(
    @SerializedName("feed") val feeds: List<FeedDTO>,
    @SerializedName("hasNext") val hasNext: Boolean,
)

data class UploadRecipeResponseDTO(
    @SerializedName("id") val id: Int,
)

fun LoginResponseDTO.toUserToken(): UserToken {
    return UserToken(
        accessToken,
        refreshToken
    )
}

data class FollowResponseDTO(
    @SerializedName("profileImageURL") val profileImageURL: String,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("userId") val userId: Int,
    @SerializedName("isFollowed") val isFollowed: Boolean?,
)

data class UserInfoResponseDTO(
    @SerializedName("id") val id: Int,
    @SerializedName("nickname") val nickname: String,
    @SerializedName("profileImageURL") val profileImageURL: String? = null,
    @SerializedName("oAuthProvider") val oAuthProvider: String,
    @SerializedName("followerNumber") val followerNumber: Int,
    @SerializedName("followingNumber") val followingNumber: Int,
    @SerializedName("recipeNumber") val recipeNumber: Int,
    @SerializedName("isFollowed") val isFollowed: Boolean? = null,
)

fun FeedDTO.toCard(): Card {
    return Card(
        id = id,
        user = UserProfile(
            profileUrl = author.profileImageURL ?: "",
            userId = author.id,
            notMine = author.myself.not(),
            followed = author.following,
            nickname = author.nickname
        ),
        thumbnailUrl = thumbnailURL ?: "",
        title = title,
        bookmark = totalBookmarkCount.toString(),
        bookmarked = bookmarked,
        time = totalCookTimeInSeconds.toTime(),
        tools = cookwares.map { it.toKoreanTool() },
        level = difficulty,
        star = averageRating.toStarRating(),
        description = "",
        ingredients = emptyList()
    )
}

fun FollowResponseDTO.toUserProfile(): UserProfile {
    return UserProfile(
        profileUrl = profileImageURL,
        nickname = nickname,
        userId = userId,
        followed = isFollowed ?: true
    )
}

fun UserInfoResponseDTO.toUser(): User {
    println("userInfoDTO $this")
    return User(
        profileUrl = profileImageURL ?: "",
        nickname,
        userId = id,
        recipe = recipeNumber.toString(),
        follower = followerNumber.toString(),
        following = followingNumber.toString(),
        followed = isFollowed ?: true
    )
}