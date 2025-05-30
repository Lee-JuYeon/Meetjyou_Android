package com.doggetdrunk.meetjyou_android.ui.screen.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.doggetdrunk.meetjyou_android.R


class OnBoardingAdapter(private val fragmentActivity : FragmentActivity) : FragmentStateAdapter(fragmentActivity){
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OnBoardingFragment.newInstance(
                title = fragmentActivity.getString(R.string.onboarding_first_title),
                description = fragmentActivity.getString(R.string.onboarding_first_description),
                imageRes = R.drawable.onboarding_1
            )
            1 -> OnBoardingFragment.newInstance(
                title = fragmentActivity.getString(R.string.onboarding_second_title),
                description = fragmentActivity.getString(R.string.onboarding_second_description),
                imageRes = R.drawable.onboarding_2
            )
            2 -> OnBoardingLoginFragment.newInstance(
                description = fragmentActivity.getString(R.string.onboarding_third_title),
                imageRes = R.drawable.onboarding_3
            )
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}