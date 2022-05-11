package com.foundy.floandroid

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.foundy.floandroid.databinding.FragmentLyricsBinding

class LyricsFragment : Fragment(R.layout.fragment_lyrics) {
    private lateinit var binding: FragmentLyricsBinding
    private val viewModel by activityViewModels<MusicPlayerViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding = FragmentLyricsBinding.bind(view)

        binding.lyricsRecyclerView.apply {
            this.adapter = LyricsAdapter(viewModel)
            layoutManager = LinearLayoutManager(view.context)
            setHasFixedSize(true)
        }
    }
}