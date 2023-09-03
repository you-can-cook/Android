package com.youcancook.gobong.model

data class Card(
    val id: Int,
    val user: UserProfile,
    val thumbnailUrl: String,
    val title: String,
    val bookmark: String,
    val bookmarked: Boolean = true,
    val time: String,
    val tools: List<String>,
    val level: String,
    val star: String,
    val description: String,
    val ingredients: List<String>,
) {
    companion object {
        fun createEmpty(): Card {
            return Card(
                id = 0,
                user = UserProfile(),
                thumbnailUrl = "",
                title = "내 레시피 대박임",
                bookmark = "0",
                time = "5분 0초",
                tools = listOf("전자레인지"),
                level = "쉬워요",
                star = "3.2공기",
                description = "",
                ingredients = listOf(
                    "자이언트 떡볶이",
                    "콕콕콕 스파게티",
                    "스트링 치즈",
                    "의성마늘 소시지 1인",
                    "모짜렐라(슈레드) 치즈"
                )
            )
        }
    }
}
