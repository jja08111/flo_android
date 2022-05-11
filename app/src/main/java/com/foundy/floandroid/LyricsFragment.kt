package com.foundy.floandroid

import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.foundy.floandroid.databinding.FragmentLyricsBinding

class LyricsFragment: Fragment(R.layout.fragment_lyrics) {
    private lateinit var binding: FragmentLyricsBinding
    private val viewModel = viewModels<MusicPlayerViewModel>()

}