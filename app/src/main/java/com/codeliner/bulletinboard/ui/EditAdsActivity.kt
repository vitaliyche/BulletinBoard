package com.codeliner.bulletinboard.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codeliner.bulletinboard.R
import com.codeliner.bulletinboard.databinding.ActivityEditAdsBinding

class EditAdsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditAdsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditAdsBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}