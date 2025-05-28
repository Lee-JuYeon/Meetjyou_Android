package com.doggetdrunk.meetjyou_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.doggetdrunk.meetjyou_android.databinding.ActivitySplashBinding
import com.doggetdrunk.meetjyou_android.ui.screen.onboarding.OnBoardingActivity

class SplashActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        delay()
    }

    private fun delay(){
        // 1600ms 후 온보딩 화면으로 이동
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, OnBoardingActivity::class.java))
            finish()
        }, 1600)
    }
}