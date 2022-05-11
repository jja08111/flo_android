package com.foundy.floandroid.repository

import android.graphics.BitmapFactory
import com.foundy.floandroid.model.Song
import org.json.JSONObject
import java.lang.Exception
import java.net.URL

class SongRepository {
    companion object {
        const val link =
            "https://grepp-programmers-challenges.s3.ap-northeast-2.amazonaws.com/2020-flo/song.json"
    }

    fun getSong(): Song? {
        return try {
            val text = URL(link).readText()
            val json = JSONObject(text)
            val imageUrl = URL(json.getString("image"))
            val bmp = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream())
            Song(json, bmp)
        } catch (e: Exception) {
            null
        }
    }
}