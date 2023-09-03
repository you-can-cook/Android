package com.youcancook.gobong.model

sealed interface Recipe {
    val id: Long
    fun isEmpty(): Boolean
}

data class RecipeStep(
    val time: String = "",
    val tools: List<String> = emptyList(),
    val photoUrl: String = "",
    val description: String = "",
    override val id: Long = System.currentTimeMillis(),
) : Recipe {
    companion object {
        fun createEmpty(): RecipeStep {
            return RecipeStep(
                "",
                emptyList(),
                "",
                "",
                System.currentTimeMillis()
            )
        }
    }

    override fun isEmpty(): Boolean {
        return this == createEmpty()
    }

}

data class RecipeStepAdded(
    val time: String = "",
    val tools: List<String> = emptyList(),
    val photoUrl: ByteArray,
    val description: String = "",
    override val id: Long = System.currentTimeMillis(),
) : Recipe {
    companion object {
        fun createEmpty(): RecipeStepAdded {
            return RecipeStepAdded(
                "",
                emptyList(),
                ByteArray(0),
                "",
                System.currentTimeMillis()
            )
        }
    }

    override fun isEmpty(): Boolean {
        return this == createEmpty()
    }
}

data class RecipeAdd(
    val user: User = User(),
    val thumbnailUrl: String = "",
    val title: String = "내 레시피 대박임",
    val bookmark: String = "0",
    val time: String = "5분",
    val tools: List<String> = listOf("전자레인지"),
    val level: String = "쉬워요",
    val star: String = "3.2공기",
    override val id: Long = System.currentTimeMillis(),
) : Recipe {
    companion object {
        fun createEmpty(): RecipeAdd {
            return RecipeAdd(
                User(),
                "",
                "",
                "0",
                "-",
                emptyList(),
                "",
                "-",
                System.currentTimeMillis()
            )
        }
    }

    override fun isEmpty(): Boolean {
        return this == createEmpty()
    }
}
