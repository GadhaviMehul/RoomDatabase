package com.example.roomdatabase42024

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.roomdatabase42024.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import java.util.Objects


class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: UserViewModel
    private val text = "Don't have an Account? Sign up"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        setUpListener()
        return binding.root
    }

    private fun setUpListener() {
        val spannableString = SpannableString(text)
        val clickableSpan: ClickableSpan = object : ClickableSpan() {
            override fun onClick(p0: View) {
                findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
            }
        }

        spannableString.setSpan(clickableSpan, 23, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.spannable.text = spannableString
        binding.spannable.movementMethod = LinkMovementMethod.getInstance()

        binding.loginButton.setOnClickListener {
            userLogin()
        }
    }

    private fun userLogin() {
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        viewModel.userLogin(email, password).observe(viewLifecycleOwner) { UserData ->

            if (UserData != null) {
                val action = LoginFragmentDirections.actionLoginFragmentToProfileFragment(UserData)
                findNavController().navigate(action)

                showSnackBar("Login Successfully")
            } else {
                showSnackBar("Login Failed")
            }
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}