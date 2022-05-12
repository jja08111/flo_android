package com.foundy.floandroid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.foundy.floandroid.databinding.LyricsItemLayoutBinding

class LyricsAdapter(
    private val viewModel: MusicPlayerViewModel,
    private val navController: NavController
) :
    RecyclerView.Adapter<LyricsAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: LyricsItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setContents(position: Int) {
            binding.lyricsItem.apply {
                val timeLyricsPair = viewModel.song.value!!.lyrics.timeLyricsPairs[position]

                text = timeLyricsPair.second
                setOnClickListener {
                    if (viewModel.activeLyricsSeeking) {
                        viewModel.seekTo(timeLyricsPair.first)
                    } else {
                        navController.navigateUp()
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LyricsItemLayoutBinding.inflate(layoutInflater)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setContents(position)

        viewModel.apply {
            musicProgressMilli.observe(holder.itemView.context as LifecycleOwner) { milli ->
                val index = getCurrentLyricsIndexAt(milli)
                holder.binding.lyricsItem.textSize = if (index == position) 20F else 16F
            }
        }
    }

    override fun getItemCount(): Int = viewModel.song.value!!.lyrics.timeLyricsPairs.size
}