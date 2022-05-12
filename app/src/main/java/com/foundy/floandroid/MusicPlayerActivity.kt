package com.foundy.floandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
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

        setupActionBar()
    }

    private fun setupActionBar() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        findViewById<Toolbar>(R.id.toolbar).setupWithNavController(
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