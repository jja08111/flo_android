package com.foundy.floandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.foundy.floandroid.databinding.ActivityMusicPlayerBinding

class MusicPlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMusicPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}