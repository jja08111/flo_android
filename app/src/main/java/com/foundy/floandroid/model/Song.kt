package com.foundy.floandroid.model

import android.graphics.Bitmap
import android.media.Image
import org.json.JSONObject
import java.io.File

data class Song(
    val singer: String,
    val album: String,
    val title: String,
    /**
     * 음악의 길이이며 단위는 초이다.
     */
    val duration: Int,
    val image: Bitmap,
    val file: String,
    val lyrics: Lyrics
) {
    constructor(json: JSONObject, image: Bitmap) : this(
        singer = json.getString("singer"),
        album = json.getString("album"),
        title = json.getString("title"),
        duration = json.getInt("duration"),
        image = image,
        file = json.getString("file"),
        lyrics = Lyrics(json.getString("lyrics"))
    )
}
