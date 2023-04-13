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
        binding.lifecycleOwner = this
        listenToViewModelEvents()
        listenToClickEvents()
        shoeViewModel.resetIsNewShoeAddedState()
        return binding.root
    }

    private fun listenToViewModelEvents() {
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
                    if (state) showSnackBar()
                }
            }

            launch {
                shoeViewModel.isShoeAlreadyExist.collectLatest { state ->
                    if (state) getSnackBar(getString(R.string.shoe_already_exist))
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
        getSnackBar(getString(R.string.shoe_added_success_message)).setAction(android.R.string.ok) {
            shoeViewModel.resetIsNewShoeAddedState()
            requireView().findNavController().navigate(ShoeDetailFragmentDirections.actionShoeDetailFragmentToShoeListFragment())
        }.show()
    }

    private fun getSnackBar(message: String) = Snackbar.make(binding.containerCl, message, Snackbar.LENGTH_LONG)
}
