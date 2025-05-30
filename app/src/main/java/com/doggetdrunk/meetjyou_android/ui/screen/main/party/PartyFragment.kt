package com.doggetdrunk.meetjyou_android.ui.screen.main.party

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.doggetdrunk.meetjyou_android.databinding.FragmentMypageBinding
import com.doggetdrunk.meetjyou_android.databinding.FragmentPartyBinding
import com.doggetdrunk.meetjyou_android.vm.PartyViewModel

class PartyFragment : Fragment() {
    private lateinit var binding : FragmentPartyBinding
    private val partyVM : PartyViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPartyBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }
}
