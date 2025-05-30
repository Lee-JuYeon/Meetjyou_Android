package com.doggetdrunk.meetjyou_android.ui.screen.main.lounge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.doggetdrunk.meetjyou_android.databinding.FragmentHomeBinding
import com.doggetdrunk.meetjyou_android.databinding.FragmentLoungeBinding
import com.doggetdrunk.meetjyou_android.vm.PartyViewModel

class LoungeFragment : Fragment() {
    private lateinit var binding : FragmentLoungeBinding
    private val partyVM : PartyViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLoungeBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }
}

