package com.youcancook.gobong.model

import java.io.Serializable

data class Filter(
    val searchWord: String,
    val sortType: String,
    val level: String,
    val time: String,
    val star: String,
    val tools: List<String>,
) : Serializable {
    companion object {
        fun createEmpty(): Filter {
            return Filter(
                "",
                "",
                "",
                "30",
                "",
                emptyList()
            )
        }
    }

    fun isEmpty(): Boolean {
        return this == createEmpty()
    }
}