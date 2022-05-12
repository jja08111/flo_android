package com.foundy.floandroid

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.foundy.floandroid.model.Song
import com.foundy.floandroid.repository.SongRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.NoSuchElementException

class MusicPlayerViewModel : ViewModel() {
    private val repository = SongRepository()

    private val _song = MutableLiveData<Song>()
    val song: LiveData<Song>
        get() = _song

    private var mediaPlayer: MediaPlayer? = null

    private val _isPlaying = MutableLiveData(false)
    val isPlaying: LiveData<Boolean>
        get() = _isPlaying

    private val _musicProgressMilli = MutableLiveData(0)
    val musicProgressMilli: LiveData<Int>
        get() = _musicProgressMilli

    private var runnable: ProgressRunnable? = null
    private var handler: Handler? = null

    var activeLyricsSeeking = false
    val visibleLyricsSeekingButton = MutableLiveData(false)

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val song = repository.getSong() ?: return@withContext
                _song.postValue(song)
                mediaPlayer = MediaPlayer().apply {
                    setDataSource(song.file)
                    prepare()
                }
                startHandlerLoop()
            }
        }
    }

    private val hasSong get() = _song.value != null

    private fun startHandlerLoop() {
        runnable = ProgressRunnable()
        handler = Handler(Looper.getMainLooper())
        handler!!.post(runnable!!)
    }

    private fun stopHandlerLoop() {
        if (runnable != null)
            handler?.removeCallbacks(runnable!!)
    }

    private inner class ProgressRunnable : Runnable {
        override fun run() {
            if (isPlaying.value == true) {
                _musicProgressMilli.value = mediaPlayer?.currentPosition ?: 0
            }
            handler?.postDelayed(this, 500)
        }
    }

    fun play() {
        if (!hasSong) return

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

        _musicProgressMilli.value = milliseconds
        mediaPlayer?.seekTo(milliseconds)
    }

    fun getCurrentLyricsIndexAt(milliseconds: Int): Int {
        if (!hasSong) return -1

        return song.value!!.lyrics.timeLyricsPairs.indexOfLast {
            milliseconds >= it.first
        }
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

    override fun onCleared() {
        stopHandlerLoop()
    }
}