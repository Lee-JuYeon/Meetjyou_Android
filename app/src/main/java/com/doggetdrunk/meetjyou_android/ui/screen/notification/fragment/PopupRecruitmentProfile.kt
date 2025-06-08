package com.doggetdrunk.meetjyou_android.ui.screen.notification.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.doggetdrunk.meetjyou_android.R
import com.doggetdrunk.meetjyou_android.databinding.PopupRecruitmentProfileBinding
import com.doggetdrunk.meetjyou_android.ui.custom.chips.ChipItem
import com.doggetdrunk.meetjyou_android.ui.custom.chips.ChipsView
import com.doggetdrunk.meetjyou_android.ui.screen.notification.recruitment.recyclerview.RecruitmentModel

class PopupRecruitmentProfile : Fragment() {

    private var _binding : PopupRecruitmentProfileBinding? = null
    private val binding
        get() = _binding!!

    private var recruitmentModel : RecruitmentModel? = null
    fun setModel(model : RecruitmentModel){
        this.recruitmentModel = model
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = PopupRecruitmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
    }


    private fun setUI(){
        setTitleView(binding.title)
        setDescriptionView(binding.description)
    }


    private fun setTitleView(textView: TextView){
        try {
            textView.text = recruitmentModel?.userName
        }catch (e:Exception){
            Log.e("mException", "PopupRecruitmentProfile, setTitleView // Exception : ${e.localizedMessage}")
        }
    }

    private fun setDescriptionView(textView: TextView){
        try {
            textView.text = recruitmentModel?.userDescription
        }catch (e:Exception){
            Log.e("mException", "PopupRecruitmentProfile, setDescriptionView // Exception : ${e.localizedMessage}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}