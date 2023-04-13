package com.udacity.shoestore.fragmentsUi

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.FragmentLoginBinding
import com.udacity.shoestore.main.MainViewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val shoeViewModel: MainViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_login, container, false)
        listenToClickEvents()
        return binding.root
    }

    private fun listenToClickEvents() {
        binding.loginBtn.setOnClickListener {
            if (formValidation().not()) {
                requireView().findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToWelcomeFragment())
            }
        }
        binding.signUpBtn.setOnClickListener {
            requireView().findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToWelcomeFragment())
        }
    }

    private fun formValidation() = when {
        binding.emailTiet.text.toString().isBlank() -> {
            binding.emailTil.error = getString(R.string.email_required)
            binding.emailTiet.requestFocus()
            true
        }
        Patterns.EMAIL_ADDRESS.matcher(binding.emailTiet.text.toString()).matches().not() -> {
            binding.emailTil.error = getString(R.string.email_invalid)
            binding.emailTiet.requestFocus()
            true
        }
        binding.passwordTiet.text.toString().isBlank() -> {
            binding.passwordTil.error = getString(R.string.password_required)
            binding.passwordTiet.requestFocus()
            true
        }
        binding.passwordTiet.text.toString().length < 5 -> {
            binding.passwordTil.error = getString(R.string.password_length_error)
            binding.passwordTiet.requestFocus()
            true
        }
        else -> false
    }
}
