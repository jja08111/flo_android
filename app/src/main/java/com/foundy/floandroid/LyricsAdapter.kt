package com.foundy.floandroid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.foundy.floandroid.databinding.LyricsItemLayoutBinding

class LyricsAdapter(private val viewModel: MusicPlayerViewModel) :
    RecyclerView.Adapter<LyricsAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: LyricsItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setContents(position: Int) {
            binding.lyricsItem.text = viewModel.song.value!!.lyrics.timeLyricsPairs[position].second
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LyricsItemLayoutBinding.inflate(layoutInflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setContents(position)
    }

    override fun getItemCount(): Int = viewModel.song.value!!.lyrics.timeLyricsPairs.size
}