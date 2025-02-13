package com.example.app

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app.entity.Phone
import com.example.app.repository.ProductRepository
import java.io.File
import java.io.FileOutputStream

class AddProductActivity  : AppCompatActivity() {
    private lateinit var brandEditText: EditText
    private lateinit var modelEditText: EditText
    private lateinit var osEditText: EditText
    private lateinit var storageEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var colorEditText: EditText
    private lateinit var releaseYearEditText: EditText
    private lateinit var  warrantyPeriodEditText: EditText
    private lateinit var addPhoneButton: Button
    private lateinit var backButton: Button
    private lateinit var imageView1: ImageView
    private lateinit var imageView2: ImageView
    private lateinit var imageView3: ImageView

    private val IMAGE_PICK_CODE1 = 1000
    private val IMAGE_PICK_CODE2 = 1001
    private val IMAGE_PICK_CODE3 = 1002

    private val phoneRepo = ProductRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        brandEditText = findViewById(R.id.brandEditText)
        modelEditText = findViewById(R.id.modelEditText)
        osEditText = findViewById(R.id.osEditText)
        storageEditText = findViewById(R.id.storageEditText)
        priceEditText = findViewById(R.id.priceEditText)
        colorEditText = findViewById(R.id.colorEditText)
        releaseYearEditText = findViewById(R.id.releaseYearEditText)
        warrantyPeriodEditText = findViewById(R.id.warrantyPeriodEditText)

        addPhoneButton = findViewById(R.id.addPhoneButton)
        backButton = findViewById(R.id.backButton)
        imageView1 = findViewById(R.id.imageView1)
        imageView2 = findViewById(R.id.imageView2)
        imageView3 = findViewById(R.id.imageView3)

        backButton.setOnClickListener{back()}

        val selectImageButton1: Button = findViewById(R.id.selectImageButton1)
        val selectImageButton2: Button = findViewById(R.id.selectImageButton2)
        val selectImageButton3: Button = findViewById(R.id.selectImageButton3)

        selectImageButton1.setOnClickListener {
            //pickImageFromGallery()
            pickImageFromDownloads1()
        }

        selectImageButton2.setOnClickListener {
            //pickImageFromGallery()
            pickImageFromDownloads2()
        }

        selectImageButton3.setOnClickListener {
            //pickImageFromGallery()
            pickImageFromDownloads3()
        }

        addPhoneButton.setOnClickListener {
            addPhone()
        }
    }

   /* private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    */
   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
       super.onActivityResult(requestCode, resultCode, data)
       if (requestCode == IMAGE_PICK_CODE1 && resultCode == Activity.RESULT_OK) {
           val imageUri: Uri? = data?.data
           imageView1.setImageURI(imageUri)
       }
       if (requestCode == IMAGE_PICK_CODE2 && resultCode == Activity.RESULT_OK) {
           val imageUri: Uri? = data?.data
           imageView2.setImageURI(imageUri)
       }
       if (requestCode == IMAGE_PICK_CODE3 && resultCode == Activity.RESULT_OK) {
           val imageUri: Uri? = data?.data
           imageView3.setImageURI(imageUri)
       }
   }

    private fun pickImageFromDownloads1() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
        }
        startActivityForResult(intent, IMAGE_PICK_CODE1)
    }

    private fun pickImageFromDownloads2() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
        }
        startActivityForResult(intent, IMAGE_PICK_CODE2)
    }

    private fun pickImageFromDownloads3() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
        }
        startActivityForResult(intent, IMAGE_PICK_CODE3)
    }


    private fun addPhone() {
        val brand = brandEditText.text.toString()
        val model = modelEditText.text.toString()
        val os = osEditText.text.toString()
        val storage = storageEditText.text.toString()
        val price = priceEditText.text.toString()
        val color = colorEditText.text.toString()
        val year = releaseYearEditText.text.toString()
        val warranty = warrantyPeriodEditText.text.toString()

        if (brand.isEmpty() || model.isEmpty() || os.isEmpty() || storage.isEmpty() || price.isEmpty() || color.isEmpty() || year.isEmpty() || warranty.isEmpty()) {
            Toast.makeText(this, "Пожалуйста, заполните все поля корректно", Toast.LENGTH_SHORT).show()
            return
        }

        val phone = Phone(brand, model, os, storage,price,color,year,warranty)

        saveImagesToStorage()

        phoneRepo.addPhone(phone,
            onSuccess = {
                Toast.makeText(this, "Телефон успешно добавлен", Toast.LENGTH_SHORT).show()
                clearFields()
                val intent = Intent(this, ProductActivity::class.java)
                startActivity(intent)
                finish()
            },
            onFailure = { exception ->
                Toast.makeText(this, "Ошибка при добавлении телефона: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        )

       // saveImageToStorage()
    }

    private fun clearFields() {
        brandEditText.text.clear()
        modelEditText.text.clear()
        osEditText.text.clear()
        storageEditText.text.clear()
    }

    private fun back() {
        val intent = Intent(this, ProductActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun saveImagesToStorage() {
        val brandName = brandEditText.text.toString()
        val model = modelEditText.text.toString()

        imageView1.isDrawingCacheEnabled = true
        imageView1.buildDrawingCache()
        val bitmap1 = imageView1.drawingCache

        imageView2.isDrawingCacheEnabled = true
        imageView2.buildDrawingCache()
        val bitmap2 = imageView2.drawingCache

        imageView3.isDrawingCacheEnabled = true
        imageView3.buildDrawingCache()
        val bitmap3 = imageView3.drawingCache

        val directory = File(filesDir, "images")
        if (!directory.exists()) {
            directory.mkdirs()
        }

        try {
            val file1 = File(directory, "1$model.png")
            val outputStream1 = FileOutputStream(file1)
            bitmap1.compress(Bitmap.CompressFormat.PNG, 100, outputStream1)
            outputStream1.flush()
            outputStream1.close()

            val file2 = File(directory, "2$model.png")
            val outputStream2= FileOutputStream(file2)
            bitmap2.compress(Bitmap.CompressFormat.PNG, 100, outputStream2)
            outputStream2.flush()
            outputStream2.close()

            val file3 = File(directory, "3$model.png")
            val outputStream3 = FileOutputStream(file3)
            bitmap3.compress(Bitmap.CompressFormat.PNG, 100, outputStream3)
            outputStream3.flush()
            outputStream3.close()

            imageView1.isDrawingCacheEnabled = false
            imageView2.isDrawingCacheEnabled = false
            imageView3.isDrawingCacheEnabled = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}