package com.doggetdrunk.meetjyou_android.ui.screen.notification.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.doggetdrunk.meetjyou_android.databinding.FragmentNotificationApplyStatusBinding

class NotificationApplyStatusFragment : Fragment() {

    private var _binding: FragmentNotificationApplyStatusBinding? = null


    private val binding
        get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentNotificationApplyStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}