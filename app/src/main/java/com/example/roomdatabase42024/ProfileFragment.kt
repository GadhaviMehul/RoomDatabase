package com.example.roomdatabase42024

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.roomdatabase42024.databinding.FragmentProfileBinding
import com.google.android.material.snackbar.Snackbar


class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: UserViewModel
    private val args by navArgs<ProfileFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        setUpListener()
        return binding.root
    }

    private fun setUpListener() {
        showUserData()
        binding.updateButton.setOnClickListener {
            updateUserData()
        }
        binding.deleteButton.setOnClickListener {
            deleteUserData(args.profile)
        }

    }

    private fun showUserData() {
        with(binding) {
            val profile = args.profile

            name.text = profile.name
            email.text = profile.email
            Glide.with(image.context).load(profile.image).into(image)
        }
    }

    private fun updateUserData() {
        val action = ProfileFragmentDirections.actionProfileFragmentToUpdateFragment(args.profile)
        findNavController().navigate(action)
    }

    private fun deleteUserData(userData: UserData) {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("Delete User Data")
            setMessage(("Are you sure you want to delete this user's data?"))
            setPositiveButton(android.R.string.ok) { _, _ ->
                viewModel.deleteUserData(userData)
                showSnackBar("Data deleted Successfully")
                findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
            }

            setNegativeButton(android.R.string.cancel) { _, _ ->
                showSnackBar("Deletion operation canceled")
            }

        }.create().show()
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}