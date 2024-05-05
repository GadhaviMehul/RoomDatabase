package com.example.roomdatabase42024

import android.app.DatePickerDialog
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.roomdatabase42024.databinding.FragmentRegistrationBinding
import com.google.android.material.snackbar.Snackbar
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.regex.Pattern

class RegistrationFragment : Fragment() {
    private lateinit var binding: FragmentRegistrationBinding
    private lateinit var repository: UserRepository
    private lateinit var viewModel: UserViewModel
    private var imagePath: String? = null
    private var gender: String = "Male"
    private val passwordPattern =
        Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")

    companion object {
        private const val DATE_FORMAT_PATTERN = "yyyy-MM-dd"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[UserViewModel::class.java]
        setUpListener()
        return binding.root
    }

    private fun setUpListener() {
        binding.image.setOnClickListener {
            pickImage.launch("image/*")
        }

        binding.birthDate.setOnClickListener {
            showDatePicker()
        }

        binding.radioGroup.setOnCheckedChangeListener { _, i ->
            gender = when (i) {
                R.id.male -> "Male"
                R.id.female -> "Female"

                else -> "Male"
            }
        }
        binding.registrationButton.setOnClickListener {
            addUserData()
        }
    }


    private fun getImageFilePath(imageUri: Uri): String? {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor =
            requireActivity().contentResolver.query(imageUri, filePathColumn, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
        val imagePath = cursor?.getString(columnIndex!!)
        cursor?.close()
        return imagePath
    }

    private fun loadImage(imagePath: String) {
        Glide.with(requireContext()).load(imagePath)
            .into(requireView().findViewById(R.id.image))
    }

    private val pickImage =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { selectedImageUri ->
                imagePath = getImageFilePath(selectedImageUri)
                if (imagePath != null) {
                    loadImage(imagePath!!)
                }
            }
        }


    private fun showDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(), { _, year, monthOfYear, dayOfMonth ->
                val formattedMonth = (monthOfYear + 1).toString().padStart(2, '0')
                val formattedDay = dayOfMonth.toString().padStart(2, '0')
                val date = "$year-$formattedMonth-$formattedDay"
                binding.birthDate.setText(date)
            }, year, month, day
        )
        datePickerDialog.show()
    }

    private fun checkAllFields(
        name: String,
        email: String,
        password: String,
        confirmPassword: String,
        birthDate: String,
        gender: String?,
        imagePath: String?
    ): Boolean {
        return when {
            name.isEmpty() -> {
                binding.name.error = "This field is required"
                false
            }
            email.isEmpty() -> {
                binding.email.error = "Email is required"
                false
            }

            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                binding.email.error = "Enter valid email"
                false
            }

            password.isEmpty() -> {
                binding.password.error = "Password is required"
                false
            }
            !passwordPattern.matcher(password).matches() -> {
                binding.password.error = "Enter Valid Password Like Password123#"
                false
            }
            confirmPassword.isEmpty() -> {
                binding.confrimPassword.error = "ConfirmPassword is required"
                false
            }
            confirmPassword != password -> {
                binding.confrimPassword.error = "ConfirmPassword and Password Don't Match"
                false
            }
            birthDate.isEmpty() -> {
                showSnackBar("Please select BirthDate")
                false

            }
            imagePath.isNullOrEmpty() -> {
                showSnackBar("Please select Image")
                false
            }

            gender.isNullOrEmpty() -> {
                showSnackBar("Please select Gender")
                false
            }
            else -> true
        }
    }

    private fun addUserData() {
        val name = binding.name.text.toString()
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        val confirmPassword = binding.confrimPassword.text.toString()
        val birthDate = binding.birthDate.text.toString()

        if (checkAllFields(name, email, password, confirmPassword, birthDate, gender, imagePath)) {
            val localDate = LocalDate.parse(
                birthDate, DateTimeFormatter.ofPattern(
                    DATE_FORMAT_PATTERN
                )
            )


            val userData =
                UserData(0, name, email, password, gender, localDate, imagePath.orEmpty())
            viewModel.addUserData(userData)
            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
            showSnackBar("Registration Successfully!")
        }


    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}