package com.udacity.shoestore.fragmentsUi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.FragmentShoeDetailBinding
import com.udacity.shoestore.main.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ShoeDetailFragment : Fragment() {

    private lateinit var binding: FragmentShoeDetailBinding
    private val shoeViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shoe_detail, container, false)
        binding.shoeViewModel = shoeViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenToViewModelEvents()
        listenToClickEvents()
        shoeViewModel.resetIsNewShoeAddedState()
    }

    private fun listenToViewModelEvents() {
        shoeViewModel.isShoeAlreadyExist.observe(viewLifecycleOwner) { state ->
            if (state) {
                getSnackBar(getString(R.string.shoe_already_exist)).show()
                shoeViewModel.resetIsNewShoeAddedState()
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                shoeViewModel.isEmptyInputFieldFound.collectLatest { state ->
                    if (state) {
                        getSnackBar(getString(R.string.empty_form_validation_error)).show()
                        shoeViewModel.resetIsEmptyInputFieldFound()
                    }
                }
            }

            launch {
                shoeViewModel.isNewShoeAdded.collectLatest { state ->
                    if (state) {
                        binding.saveBtn.isEnabled = false
                        showSnackBar()
                    }
                }
            }
        }
    }

    private fun listenToClickEvents() {
        binding.cancelBtn.setOnClickListener {
            requireView().findNavController().navigate(ShoeDetailFragmentDirections.actionShoeDetailFragmentToShoeListFragment())
        }
    }

    private fun showSnackBar() {
        Snackbar.make(requireView(), getString(R.string.shoe_added_success_message), Snackbar.LENGTH_INDEFINITE).setAction(android.R.string.ok) {
            binding.saveBtn.isEnabled = true
            shoeViewModel.resetIsNewShoeAddedState()
            requireView().findNavController().navigate(ShoeDetailFragmentDirections.actionShoeDetailFragmentToShoeListFragment())
        }.show()
    }

    private fun getSnackBar(message: String) = Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
}
