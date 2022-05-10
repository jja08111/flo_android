package com.foundy.floandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.foundy.floandroid.databinding.ActivityMusicPlayerBinding

class MusicPlayerActivity : AppCompatActivity() {
    private val viewModel by viewModels<MusicPlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMusicPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.song.observe(this) {
            binding.apply {
                albumCoverImage.setImageBitmap(it.image)
                musicTitleText.text = it.title
                musicSingerText.text = it.singer
                musicAlbumText.text = it.album
            }
        }
        viewModel.isPlaying.observe(this) { isPlaying ->
            binding.playStopButton.apply {
                if (isPlaying) {
                    setImageResource(R.drawable.ic_baseline_pause_24)
                    setOnClickListener { viewModel.stop() }
                } else {
                    setImageResource(R.drawable.ic_baseline_play_arrow_24)
                    setOnClickListener { viewModel.play(context) }
                }
            }
        }
    }
}