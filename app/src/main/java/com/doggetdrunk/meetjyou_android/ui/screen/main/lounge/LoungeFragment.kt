package com.doggetdrunk.meetjyou_android.ui.screen.main.lounge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.doggetdrunk.meetjyou_android.databinding.FragmentLoungeBinding
import com.doggetdrunk.meetjyou_android.vm.PartyViewModel

class LoungeFragment : Fragment() {
    private var _binding: FragmentLoungeBinding? = null
    private val binding get() = _binding!!
    private val partyVM: PartyViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoungeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


