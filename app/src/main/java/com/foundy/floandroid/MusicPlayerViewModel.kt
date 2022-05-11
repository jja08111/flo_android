package com.foundy.floandroid

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foundy.floandroid.model.Song
import com.foundy.floandroid.repository.SongRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MusicPlayerViewModel : ViewModel() {
    private val repository = SongRepository()

    private val _song = MutableLiveData<Song>()
    val song: LiveData<Song>
        get() = _song

    private var mediaPlayer: MediaPlayer? = null

    private val _isPlaying = MutableLiveData(false)
    val isPlaying: LiveData<Boolean>
        get() = _isPlaying

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val song = repository.getSong() ?: return@withContext
                _song.postValue(song)
            }
        }
    }

    private val hasSong get() = _song.value != null

    fun play(context: Context) {
        if (!hasSong) return

        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, Uri.parse(song.value!!.file))
        }
        mediaPlayer!!.start()
        _isPlaying.value = true

    }

    fun pause() {
        if (!hasSong || mediaPlayer == null) return

        mediaPlayer!!.pause()
        _isPlaying.value = false
    }

    fun seekTo(milliseconds: Int) {
        if (!hasSong) return

        mediaPlayer?.seekTo(milliseconds)
    }

    fun getCurrentProgress(): Int? {
        return mediaPlayer?.currentPosition
    }

    fun getLyricsAt(milliseconds: Int): String? {
        if (!hasSong) return null

        return try {
            song.value!!.lyrics.timeLyricsPairs.last {
                milliseconds >= it.first
            }.second
        } catch (e: NoSuchElementException) {
            null
        }
    }

    fun getNextLyricsAt(milliseconds: Int): String? {
        if (!hasSong) return null

        return try {
            song.value!!.lyrics.timeLyricsPairs.first {
                milliseconds < it.first
            }.second
        } catch (e: NoSuchElementException) {
            null
        }
    }
}