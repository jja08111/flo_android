package com.foundy.floandroid

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.foundy.floandroid.databinding.ActivityMusicPlayerBinding
import com.foundy.floandroid.model.Song

class MusicPlayerActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMusicPlayerBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<MusicPlayerViewModel>()

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
        viewModel.musicProgressMilli.observe(this) { progress ->
            binding.musicSeekBar.progress = progress
        }
        viewModel.visibleLyricsSeekingButton.observe(this) { isVisible ->
            binding.lyricsSeekButton.isVisible = isVisible
        }

        setupActionBar()
        binding.lyricsSeekButton.setOnClickListener {
            viewModel.activeLyricsSeeking = !viewModel.activeLyricsSeeking
            (it as ImageButton).imageTintList =
                ColorStateList.valueOf(
                    ContextCompat.getColor(
                        this,
                        if (viewModel.activeLyricsSeeking) R.color.purple_500 else R.color.gray
                    )
                )
        }
    }

    private fun setupActionBar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(
            navController,
            appBarConfiguration
        )
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
}