package com.doggetdrunk.meetjyou_android.ui.screen.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.doggetdrunk.meetjyou_android.MainActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.doggetdrunk.meetjyou_android.databinding.ActivityOnboardingBinding

class OnBoardingActivity : AppCompatActivity() {

    private lateinit var _binding : ActivityOnboardingBinding
    private lateinit var onboardingAdapter : OnBoardingAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        setViewPager()
        setupPageIndicator()
    }

    private fun setViewPager(){
        onboardingAdapter = OnBoardingAdapter(this)
        _binding.viewPager.adapter = onboardingAdapter

        // 마지막 페이지에서 로그인 화면으로 이동하는 콜백 설정
        _binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // 마지막 페이지에서는 다음 버튼을 로그인 버튼으로 변경할 수 있음
            }
        })
    }

    private fun setupPageIndicator() {
        // TabLayout과 ViewPager2 연결
        TabLayoutMediator(_binding.pageIndicator, _binding.viewPager) { tab, position ->
            // 각 탭에 텍스트나 아이콘을 설정할 필요 없음 (점만 표시)
        }.attach()
    }

    fun navigateToLogin() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}

