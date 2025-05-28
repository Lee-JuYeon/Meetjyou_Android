package com.doggetdrunk.meetjyou_android.ui.screen.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.doggetdrunk.meetjyou_android.R


class OnBoardingAdapter(fragmentActivity : FragmentActivity) : FragmentStateAdapter(fragmentActivity){
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OnBoardingFragment.newInstance(
                title = "안녕하세요!",
                description = "저는 만쥬에요 만나서반가워요 :)\n함께 할 멋진 여행을 준비했어요",
                imageRes = R.drawable.onboarding_1
            )
            1 -> OnBoardingFragment.newInstance(
                title = "함께 떠나는 여행, 즐거움이 두 배!",
                description = "다양한 사람들과 여행을 계획하고\n특별한 추억을 만들어 보세요",
                imageRes = R.drawable.onboarding_2
            )
            2 -> OnBoardingLoginFragment.newInstance(
                description = "저와 함께 특별한 동행을 시작해볼까요?",
                imageRes = R.drawable.onboarding_3
            )
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}