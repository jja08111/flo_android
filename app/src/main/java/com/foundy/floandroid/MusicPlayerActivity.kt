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
    private lateinit var binding: ActivityMusicPlayerBinding
    private val viewModel by viewModels<MusicPlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMusicPlayerBinding.inflate(layoutInflater)
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
    }

    private fun onChangedSong(song: Song) {
        binding.apply {
            albumCoverImage.setImageBitmap(song.image)
            musicTitleText.text = song.title
            musicSingerText.text = song.singer
            musicAlbumText.text = song.album
            musicSeekBar.apply {
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

                this@MusicPlayerActivity.runOnUiThread(SeekBarRunnable())
            }
        }
    }

    private fun onClickedPlay() {
        binding.playOrPauseButton.apply {
            setImageResource(R.drawable.ic_baseline_play_arrow_24)
            setOnClickListener { viewModel.play(context) }
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
            val currentProgress = viewModel.getCurrentProgress()
            if (currentProgress != null)
                binding.musicSeekBar.progress = currentProgress
            Handler(Looper.getMainLooper()).postDelayed(this@SeekBarRunnable, 500)
        }
    }
}