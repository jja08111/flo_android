package com.foundy.floandroid.model

class Lyrics(raw: String) {

    /**
     * 밀리초와 가사가 페어로 있는 리스트이다.
     */
    val timeLyricsPairs: List<Pair<Int, String>> = raw.split("\n").map {
        val splitString = it.split("]")
        val time = splitString[0].removePrefix("[").toSecond()
        val lyrics = splitString[1]
        time to lyrics
    }.toList()

    /**
     * "분:초:밀리초" 로 구성된 문자열을 밀리초 단위로 변환한 것을 반환한다.
     */
    private fun String.toSecond(): Int {
        val splitString = this.split(":")
        val minutes = splitString[0].toInt()
        val seconds = splitString[1].toInt()
        val milliseconds = splitString[2].toInt()

        return minutes * 60 * 1000 + seconds * 1000 + milliseconds
    }
}
