package com.doggetdrunk.meetjyou_android.ui.screen.main.travel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.doggetdrunk.meetjyou_android.databinding.FragmentTravelBinding
import com.doggetdrunk.meetjyou_android.vm.PartyViewModel

class TravelFragment : Fragment() {
    private var _binding: FragmentTravelBinding? = null
    private val binding get() = _binding!!
    private val partyVM: PartyViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTravelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}