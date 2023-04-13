package com.udacity.shoestore.fragmentsUi

import android.os.Bundle
import android.view.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.FragmentShoeListBinding
import com.udacity.shoestore.main.MainViewModel
import com.udacity.shoestore.models.Shoe
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ShoeListFragment : Fragment() {

    private lateinit var binding: FragmentShoeListBinding
    private val shoeViewModel: MainViewModel by activityViewModels()
    private val nameParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
        setMargins(8, 8, 8, 8)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shoe_list, container, false)
        setUpToolbarMenu()
        listenToClickEvents()
        listenToViewModelEvents()
        return binding.root
    }

    private fun listenToClickEvents() {
        binding.addNewShoeFab.setOnClickListener {
            requireView().findNavController().navigate(ShoeListFragmentDirections.actionShoeListFragmentToShoeDetailFragment())
        }
    }

    private fun listenToViewModelEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            shoeViewModel.shoeList.collectLatest { items ->
                if (items.isEmpty()) {
                    binding.emptyStoreTv.isVisible = true
                    binding.shoeItemsSv.isVisible = false
                } else {
                    binding.emptyStoreTv.isVisible = false
                    binding.shoeItemsSv.isVisible = true

                    items.forEach { shoe -> addNewItemToStore(shoe) }
                }
            }
        }
    }

    private fun setUpToolbarMenu() {
        requireActivity().addMenuProvider(
            object : MenuProvider {
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.shoe_list_menu, menu)
                }

                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.actionLogout -> {
                            requireView().findNavController().navigate(ShoeListFragmentDirections.actionShoeListFragmentToLoginFragment())
                            true
                        }
                        else -> false
                    }
                }
            },
            viewLifecycleOwner,
            Lifecycle.State.RESUMED,
        )
    }

    private fun addNewItemToStore(shoe: Shoe) {
        // name
        val nameView = TextView(requireContext()).apply {
            layoutParams = nameParams
            text = getString(R.string.name_label, shoe.name)
            textSize = 25f
            setOnClickListener { showDialog(shoe.name, shoe.description) }
        }

        // company
        val companyView = TextView(requireContext()).apply {
            layoutParams = nameParams
            text = getString(R.string.company_label, shoe.company)
            textSize = 18f
        }

        // size
        val sizeView = TextView(requireContext()).apply {
            layoutParams = nameParams
            text = getString(R.string.size_label, shoe.size.toString())
            textSize = 18f
        }

        // delimiter
        val delimiter = View(requireContext())
        delimiter.layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, 1)
        delimiter.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))

        binding.shoeItemsLl.apply {
            addView(nameView)
            addView(companyView)
            addView(sizeView)
            addView(delimiter)
        }
    }

    private fun showDialog(title: String, message: String) {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton(android.R.string.ok) { d, _ ->
                d.dismiss()
            }
        }.show()
    }
}
