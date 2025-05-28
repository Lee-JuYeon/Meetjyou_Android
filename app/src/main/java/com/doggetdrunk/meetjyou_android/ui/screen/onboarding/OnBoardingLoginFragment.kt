package com.doggetdrunk.meetjyou_android.ui.screen.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.doggetdrunk.meetjyou_android.databinding.FragmentOnboardingLoginBinding
import com.doggetdrunk.meetjyou_android.ui.screen.home.HomeActivity

class OnBoardingLoginFragment : Fragment(){
    private var _binding: FragmentOnboardingLoginBinding? = null
    private val binding
        get() = _binding!!

    companion object {
        private const val ARG_DESCRIPTION = "description"
        private const val ARG_IMAGE_RES = "image_res"

        fun newInstance(description: String, imageRes: Int): OnBoardingLoginFragment {
            return OnBoardingLoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_DESCRIPTION, description)
                    putInt(ARG_IMAGE_RES, imageRes)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOnboardingLoginBinding.inflate(inflater, container, false)
        _binding?.run {
            lifecycleOwner = this@OnBoardingLoginFragment
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { args ->
            binding.description = args.getString(ARG_DESCRIPTION)
            binding.imageRes = args.getInt(ARG_IMAGE_RES)
        }

        binding.applelogin.setOnClickListener {
            // Fragment에서 Activity로 이동
            val intent = Intent(requireContext(), HomeActivity::class.java)
            startActivity(intent)

            // 현재 OnBoardingActivity 종료
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
