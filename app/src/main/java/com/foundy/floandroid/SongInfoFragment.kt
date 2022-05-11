package com.foundy.floandroid

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.foundy.floandroid.databinding.FragmentSongInfoBinding
import com.foundy.floandroid.model.Song

class SongInfoFragment : Fragment(R.layout.fragment_song_info) {
    private lateinit var binding: FragmentSongInfoBinding
    private val viewModel by activityViewModels<MusicPlayerViewModel>()
    private var runnable : LyricsRunnable? = null
    private var handler : Handler? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSongInfoBinding.bind(view)

        binding.apply {
            hightlightLyricsText.setOnClickListener { navigateToLyrics() }
            nextLyricsText.setOnClickListener { navigateToLyrics() }
        }

        viewModel.song.observe(viewLifecycleOwner) {
            onChangedSong(it)
        }

        runnable = LyricsRunnable()
        activity?.runOnUiThread(runnable)
    }

    override fun onResume() {
        super.onResume()
        setLyricsTexts()
    }

    private fun onChangedSong(song: Song) {
        binding.apply {
            albumCoverImage.setImageBitmap(song.image)
            musicTitleText.text = song.title
            musicSingerText.text = song.singer
            musicAlbumText.text = song.album
            nextLyricsText.text = viewModel.getNextLyricsAt(0)
        }
    }

    inner class LyricsRunnable : Runnable {
        override fun run() {
            setLyricsTexts()
            handler = Handler(Looper.getMainLooper())
            handler!!.postDelayed(this@LyricsRunnable, 200)
        }
    }

    private fun setLyricsTexts() {
        binding.apply {
            val currentProgressMilli = viewModel.getCurrentProgress()

            hightlightLyricsText.text = viewModel.getLyricsAt(currentProgressMilli)
            nextLyricsText.text = viewModel.getNextLyricsAt(currentProgressMilli)
        }
    }

    private fun navigateToLyrics() {
        findNavController().navigate(R.id.action_songInfoFragment_to_lyricsFragment)
    }

    override fun onStop() {
        super.onStop()
        if (handler != null && runnable != null) {
            handler!!.removeCallbacks(runnable!!)
        }
    }
}