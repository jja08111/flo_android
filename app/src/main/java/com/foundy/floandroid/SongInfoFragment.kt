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

        activity?.runOnUiThread(LyricsRunnable())
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
            val currentProgressMilli = viewModel.getCurrentProgress()
            if (currentProgressMilli != null && viewModel.isPlaying.value == true) {
                binding.apply {
                    hightlightLyricsText.text = viewModel.getLyricsAt(currentProgressMilli)
                    nextLyricsText.text = viewModel.getNextLyricsAt(currentProgressMilli)
                }
            }

            Handler(Looper.getMainLooper()).postDelayed(this@LyricsRunnable, 200)
        }
    }

    private fun navigateToLyrics() {
        findNavController().navigate(R.id.action_songInfoFragment_to_lyricsFragment)
    }
}