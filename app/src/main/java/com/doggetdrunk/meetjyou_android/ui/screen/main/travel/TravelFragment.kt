package com.doggetdrunk.meetjyou_android.ui.screen.main.travel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.doggetdrunk.meetjyou_android.databinding.FragmentPartyBinding
import com.doggetdrunk.meetjyou_android.databinding.FragmentTravelBinding
import com.doggetdrunk.meetjyou_android.vm.PartyViewModel

class TravelFragment: Fragment() {
    private lateinit var binding : FragmentTravelBinding
    private val partyVM : PartyViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTravelBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }
}
