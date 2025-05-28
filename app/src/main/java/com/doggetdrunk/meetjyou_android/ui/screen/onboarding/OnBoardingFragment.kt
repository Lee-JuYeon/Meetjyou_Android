package com.doggetdrunk.meetjyou_android.ui.screen.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.doggetdrunk.meetjyou_android.databinding.FragmentOnboardingBinding

class OnBoardingFragment : Fragment(){
    private var _binding: FragmentOnboardingBinding? = null
    private val binding
        get() = _binding!!

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_DESCRIPTION = "description"
        private const val ARG_IMAGE_RES = "image_res"

        fun newInstance(title: String, description: String, imageRes: Int): OnBoardingFragment {
            return OnBoardingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putString(ARG_DESCRIPTION, description)
                    putInt(ARG_IMAGE_RES, imageRes)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        _binding?.run {
            lifecycleOwner = this@OnBoardingFragment
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { args ->

            binding.title = args.getString(ARG_TITLE)
            binding.description = args.getString(ARG_DESCRIPTION)
            binding.imageRes = args.getInt(ARG_IMAGE_RES)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

