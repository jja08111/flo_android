package com.foundy.floandroid.model

class Lyrics(raw: String) {
    private val list: List<Pair<String, String>> = raw.split("\n").map {
        val splitString = it.split("]")
        // TODO: 시간을 더 명확히 표현할 수 있도록 클래스 만들어 이용하기
        val time = splitString[0].removePrefix("[")
        val lyrics = splitString[1]
        time to lyrics
    }.toList()
}
