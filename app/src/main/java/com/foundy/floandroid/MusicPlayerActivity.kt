package com.foundy.floandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.SeekBar
import androidx.activity.viewModels
import com.foundy.floandroid.databinding.ActivityMusicPlayerBinding
import com.foundy.floandroid.model.Song

class MusicPlayerActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMusicPlayerBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<MusicPlayerViewModel>()
    private var runnable: SeekBarRunnable? = null
    private var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel.song.observe(this) {
            onChangedSong(it)
        }
        viewModel.isPlaying.observe(this) { isPlaying ->
            binding.apply {
                if (isPlaying) {
                    onClickedPause()
                } else {
                    onClickedPlay()
                }
            }
        }

        runnable = SeekBarRunnable()
        this.runOnUiThread(runnable)
    }

    private fun onChangedSong(song: Song) {
        binding.musicSeekBar.apply {
            max = song.duration * 1000
            setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) viewModel.seekTo(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }

    private fun onClickedPlay() {
        binding.playOrPauseButton.apply {
            setImageResource(R.drawable.ic_baseline_play_arrow_24)
            setOnClickListener { viewModel.play() }
        }
    }

    private fun onClickedPause() {
        binding.playOrPauseButton.apply {
            setImageResource(R.drawable.ic_baseline_pause_24)
            setOnClickListener { viewModel.pause() }
        }
    }

    inner class SeekBarRunnable : Runnable {
        override fun run() {
            val currentProgressMilli = viewModel.getCurrentProgress()
            binding.musicSeekBar.progress = currentProgressMilli

            handler = Handler(Looper.getMainLooper())
            handler!!.postDelayed(this@SeekBarRunnable, 200)
        }
    }

    override fun onStop() {
        super.onStop()
        if (handler != null && runnable != null) {
            handler!!.removeCallbacks(runnable!!)
        }
    }
}