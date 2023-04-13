package com.udacity.shoestore.fragmentsUi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.FragmentInstructionBinding

class InstructionFragment : Fragment() {

    private lateinit var binding: FragmentInstructionBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_instruction, container, false)
        listenToClickEvents()
        return binding.root
    }

    private fun listenToClickEvents() {
        binding.nextPageBtn.setOnClickListener {
            requireView().findNavController().navigate(InstructionFragmentDirections.actionInstructionFragmentToShoeListFragment())
        }
    }
}
