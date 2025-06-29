package com.doggetdrunk.meetjyou_android.ui.screen.notification

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.AppBarConfiguration
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.doggetdrunk.meetjyou_android.R
import com.doggetdrunk.meetjyou_android.databinding.ActivityNotificationBinding
import com.doggetdrunk.meetjyou_android.ui.custom.base.BaseFragmentAdapter
import com.doggetdrunk.meetjyou_android.ui.screen.notification.notify.NotificationNotifyFragment
import com.doggetdrunk.meetjyou_android.ui.screen.notification.apply.NotificationApplyStatusFragment
import com.doggetdrunk.meetjyou_android.ui.screen.notification.recruitment.NotificationRecruitmentStatusFragment
import com.doggetdrunk.meetjyou_android.vm.NotifyVM

class NotificationActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setVM()
        setTabLayoutWithViewPager2(binding.tabLayout, binding.viewPager)

    }

    private var notifyFragment : NotificationNotifyFragment? = null
    private var recruitmentStatusFragment : NotificationRecruitmentStatusFragment? = null
    private var applyStatusFragment : NotificationApplyStatusFragment? = null
    private fun setTabLayoutWithViewPager2(tabLayout: TabLayout, viewPager2: ViewPager2){
        try{
            notifyFragment = NotificationNotifyFragment()
            recruitmentStatusFragment = NotificationRecruitmentStatusFragment()
            applyStatusFragment = NotificationApplyStatusFragment()
            viewPager2.let {
                var viewpagerAdapter =  object : BaseFragmentAdapter.Adapter(this){
                    override fun setFragmentList(): List<Fragment> {
                        return listOf<Fragment>(
                            notifyFragment ?: NotificationNotifyFragment(),
                            recruitmentStatusFragment ?: NotificationRecruitmentStatusFragment(),
                            applyStatusFragment ?: NotificationApplyStatusFragment()
                        )
                    }
                }
                it.adapter = viewpagerAdapter
                it.isUserInputEnabled = false // 스크롤로 프래그먼트 이동 억제
            }

            tabLayout.let {
                it.tabMode = TabLayout.MODE_SCROLLABLE  // FIXED -> SCROLLABLE로 변경
                it.tabGravity = TabLayout.GRAVITY_START  // FILL -> START로 변경

                it.isTabIndicatorFullWidth = false  // 인디케이터를 텍스트 너비에 맞춤
            }
            TabLayoutMediator(binding.tabLayout, binding.viewPager){ tab, position ->
                val ACTIVITY = 0
                val RECRUITMENT_STATUS = 1
                val APPLY_STATUS = 2

                // 커스텀 뷰 설정
                val customView = layoutInflater.inflate(R.layout.holder_notification_tab, null)
                val tabText = customView.findViewById<TextView>(R.id.tab_text)
                val tabIndicator = customView.findViewById<ImageView>(R.id.tab_indicator)


                when(position){
                    ACTIVITY -> {
//                        tab.text = getString(R.string.tab_grouping)
                        tabText.text = "활동"
                    }
                    RECRUITMENT_STATUS -> {
//                        tab.text = getString(R.string.tab_donation)
                        tabText.text = "모집현황"
                    }
                    APPLY_STATUS -> {
//                        tab.text = getString(R.string.tab_donation)
                        tabText.text = "신청 현황"
                    }
                }

                tab.customView = customView
            }.attach()

            // 탭 선택 리스너 추가 (파란색 동그라미 표시/숨김)
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.customView?.let { customView ->
                        val tabText = customView.findViewById<TextView>(R.id.tab_text)
                        val tabIndicator = customView.findViewById<ImageView>(R.id.tab_indicator)

                        // 선택된 탭 스타일
                        tabText.setTextColor(getColor(R.color.black))
                        tabText.textSize = 16f
                        tabText.typeface = android.graphics.Typeface.DEFAULT_BOLD
                        tabIndicator.visibility = View.VISIBLE
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                    tab?.customView?.let { customView ->
                        val tabText = customView.findViewById<TextView>(R.id.tab_text)
                        val tabIndicator = customView.findViewById<ImageView>(R.id.tab_indicator)

                        // 선택되지 않은 탭 스타일
                        tabText.setTextColor(getColor(R.color.gray300))
                        tabText.textSize = 14f
                        tabText.typeface = android.graphics.Typeface.DEFAULT
                        tabIndicator.visibility = View.GONE
                    }
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                    // 재선택 시 특별한 동작 없음
                }
            })

            // 초기 선택 상태 설정 (첫 번째 탭)
            tabLayout.getTabAt(0)?.let { firstTab ->
                firstTab.customView?.let { customView ->
                    val tabText = customView.findViewById<TextView>(R.id.tab_text)
                    val tabIndicator = customView.findViewById<ImageView>(R.id.tab_indicator)

                    tabText.setTextColor(getColor(R.color.black))
                    tabText.textSize = 16f
                    tabText.typeface = android.graphics.Typeface.DEFAULT_BOLD
                    tabIndicator.visibility = View.VISIBLE
                }
            }
        }catch (e:Exception){
            Log.e("mException", "NotificationActivity, setTabLayoutWithViewPager2 // Exception : ${e.localizedMessage}")
        }
    }

    private lateinit var notifyVM : NotifyVM
    private fun setVM(){
        try{
            notifyVM = ViewModelProvider(this@NotificationActivity)[NotifyVM::class.java]
        }catch (e:Exception){
            Log.e("mException", "MainActivity, setVM // Exception : ${e.localizedMessage}")
        }
    }
}