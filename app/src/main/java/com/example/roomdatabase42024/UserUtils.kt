package com.example.roomdatabase42024

import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import java.util.*
import java.util.regex.Pattern

object UserUtils {

    private val passwordPattern =
        Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$")

    private val pickImage =
        ActivityResultContracts.GetContent()

    fun getImageFilePath(imageUri: Uri, context: Context): String? {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(imageUri, filePathColumn, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
        val imagePath = cursor?.getString(columnIndex!!)
        cursor?.close()
        return imagePath
    }

    fun loadImage(imagePath: String, context: Context, imageView: ImageView) {
        Glide.with(context).load(imagePath).into(imageView)
    }

    fun pickImage(fragment: Fragment, onImagePicked: (Uri?) -> Unit) {
        val pickImage = fragment.registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            onImagePicked(uri)
        }
        pickImage.launch("image/*")
    }

    fun showDatePicker(context: Context, onDateSelected: (String) -> Unit) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            context, { _, year, monthOfYear, dayOfMonth ->
                val formattedMonth = (monthOfYear + 1).toString().padStart(2, '0')
                val formattedDay = dayOfMonth.toString().padStart(2, '0')
                val date = "$year-$formattedMonth-$formattedDay"
                onDateSelected.invoke(date)
            }, year, month, day
        )
        datePickerDialog.show()
    }
}
