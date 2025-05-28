package com.doggetdrunk.meetjyou_android.ui.screen.notification

import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.doggetdrunk.meetjyou_android.R
import com.doggetdrunk.meetjyou_android.databinding.ActivityNotificationBinding
import com.doggetdrunk.meetjyou_android.ui.custom.base.BaseFragmentAdapter

class NotificationActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setTabLayoutWithViewPager2(binding.tabLayout, binding.viewPager)

    }

    private var activityFragment : NotificationActivityFragment? = null
    private var recruitmentStatusFragment : NotificationRecruitmentStatusFragment? = null
    private var applyStatusFragment : NotificationApplyStatusFragment? = null
    private fun setTabLayoutWithViewPager2(tabLayout: TabLayout, viewPager2: ViewPager2){
        try{
            activityFragment = NotificationActivityFragment()
            recruitmentStatusFragment = NotificationRecruitmentStatusFragment()
            applyStatusFragment = NotificationApplyStatusFragment()
            viewPager2.let {
                var viewpagerAdapter =  object : BaseFragmentAdapter.Adapter(this){
                    override fun setFragmentList(): List<Fragment> {
                        return listOf<Fragment>(
                            activityFragment ?: NotificationActivityFragment(),
                            recruitmentStatusFragment ?: NotificationRecruitmentStatusFragment(),
                            applyStatusFragment ?: NotificationApplyStatusFragment()
                        )
                    }
                }
                it.adapter = viewpagerAdapter
                it.isUserInputEnabled = false // 스크롤로 프래그먼트 이동 억제
            }

            tabLayout.let {
                it.tabMode = TabLayout.MODE_FIXED
                it.tabGravity = TabLayout.GRAVITY_FILL
            }
            TabLayoutMediator(binding.tabLayout, binding.viewPager){ tab, position ->
                val ACTIVITY = 0
                val RECRUITMENT_STATUS = 1
                val APPLY_STATUS = 2
                when(position){
                    ACTIVITY -> {
//                        tab.text = getString(R.string.tab_grouping)
                        tab.text = "활동"
                    }
                    RECRUITMENT_STATUS -> {
//                        tab.text = getString(R.string.tab_donation)
                        tab.text = "모집현황"
                    }
                    APPLY_STATUS -> {
//                        tab.text = getString(R.string.tab_donation)
                        tab.text = "신청 현황"
                    }
                }
            }.attach()
        }catch (e:Exception){
            Log.e("mException", "NotificationActivity, setTabLayoutWithViewPager2 // Exception : ${e.localizedMessage}")
        }
    }
}