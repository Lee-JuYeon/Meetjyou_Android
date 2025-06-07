package com.doggetdrunk.meetjyou_android.ui.screen.notification.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.doggetdrunk.meetjyou_android.R
import com.doggetdrunk.meetjyou_android.binding.loadSelfie
import com.doggetdrunk.meetjyou_android.databinding.FragmentNotificationApplyStatusBinding
import com.doggetdrunk.meetjyou_android.databinding.FragmentNotifyProfileBinding
import com.doggetdrunk.meetjyou_android.ui.custom.chips.ChipItem
import com.doggetdrunk.meetjyou_android.ui.custom.chips.ChipsView
import com.doggetdrunk.meetjyou_android.ui.screen.notification.recruitment.recyclerview.RecruitmentModel

class NotifyProfileFragment : Fragment() {

    private var _binding : FragmentNotifyProfileBinding? = null
    private val binding
        get() = _binding!!

    private var recruitmentModel : RecruitmentModel? = null
    fun setModel(model : RecruitmentModel){
        this.recruitmentModel = model
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentNotifyProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
    }


    private fun setUI(){
        setUserSelfie(binding.selfie)
        setUserName(binding.name)
        setUserDescription(binding.description)
        setUserInfoChips(binding.userinfoChips)
        setUserLifestyleChips(binding.lifestyleChips)
    }

    private fun setUserSelfie(imageView: ImageView){
        try {
            if (recruitmentModel?.userSelfieURL != null){
                Glide.with(requireContext())
                    .load(recruitmentModel?.userSelfieURL)
                    .placeholder(R.drawable.icon_placeholder_selfie)
                    .error(R.drawable.icon_placeholder_selfie)
                    .centerCrop()
                    .transform(CircleCrop()) // 원형으로 자르기
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView)
            }else{imageView
                imageView.setImageResource(R.drawable.icon_placeholder_selfie)
            }
        }catch (e:Exception){
            Log.e("mException", "NotifyProfileFragment, setUserSelfie // Exception : ${e.localizedMessage}")
        }
    }

    private fun setUserName(textView: TextView){
        try {
            textView.text = recruitmentModel?.userName
        }catch (e:Exception){
            Log.e("mException", "NotifyProfileFragment, setUserName // Exception : ${e.localizedMessage}")
        }
    }

    private fun setUserDescription(textView: TextView){
        try {
            textView.text = recruitmentModel?.userDescription
        }catch (e:Exception){
            Log.e("mException", "NotifyProfileFragment, setUserDescription // Exception : ${e.localizedMessage}")
        }
    }

    private fun setUserInfoChips(chipsView: ChipsView){
        try {
            val partyTags = listOf(
                ChipItem("tag1", "20대"),
                ChipItem("tag2", "남성")
            )

            chipsView.apply {
                setupNonSelectableChips(partyTags)
            }
        }catch (e:Exception){
            Log.e("mException", "NotifyProfileFragment, setUserInfoChips // Exception : ${e.localizedMessage}")
        }
    }
    private fun setUserLifestyleChips(chipsView: ChipsView){
        try {
            val partyTags = listOf(
                ChipItem("tag1", "외향적인"),
                ChipItem("tag2", "자유로운"),
                ChipItem("tag3", "낙천적인"),
                ChipItem("tag4", "스포츠"),
                ChipItem("tag5", "예술"),
                ChipItem("tag6", "자연"),
                ChipItem("tag7", "흡연자"),
                ChipItem("tag8", "음주를 해요"),

                )

            chipsView.apply {
                setupNonSelectableChips(partyTags)
            }
        }catch (e:Exception){
            Log.e("mException", "NotifyProfileFragment, setUserLifestyleChips // Exception : ${e.localizedMessage}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}