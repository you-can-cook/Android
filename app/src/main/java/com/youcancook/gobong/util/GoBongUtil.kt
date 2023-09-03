package com.youcancook.gobong.util

fun Int.toTime(): String {
    val min = this / 60
    val sec = this % 60

    return "${min}분 ${sec}초"
}

fun Double?.toStarRating(): String {
    return if (this == 0.0) {
        "-"
    } else {
        "${this}공기"
    }
}

fun String.toSeconds(): Int {
    val times = this.split(" ")
    val min = times[0].removeSuffix("분").toInt()
    val sec = times[1].removeSuffix("초").toInt()

    return min * 60 + sec
}

fun String.toEnglishTool(): String {
    return when (this) {
        "전자레인지" -> "MICROWAVE"
        "에어프라이어" -> "AIR_FRYER"
        "오븐" -> "OVEN"
        "가스레인지" -> "GAS_RANGE"
        "믹서" -> "MIXER"
        "커피포트" -> "ELECTRIC_KETTLE"
        "프라이팬" -> "PAN"
        else -> ""
    }
}

fun String.toKoreanTool(): String {
    return when (this) {
        "MICROWAVE" -> "전자레인지"
        "AIR_FRYER" -> "에어프라이어"
        "OVEN" -> "오븐"
        "GAS_RANGE" -> "가스레인지"
        "MIXER" -> "믹서"
        "ELECTRIC_KETTLE" -> "커피포트"
        "PAN" -> "프라이팬"
        else -> ""
    }
}

enum class TOOLS(toolName: String) {
    MICROWAVE("전자레인지"),
    AIR_FRYER("에어프라이어"),
    OVEN("오븐"),
    GAS_RANGE("가스레인지"),
    MIXER("믹서"),
    ELECTRIC_KETTLE("커피포트"),
    PAN("프라이팬")
}