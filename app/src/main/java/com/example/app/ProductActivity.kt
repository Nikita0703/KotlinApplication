package com.example.app
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app.repository.ProductRepository
import com.example.app.service.PhoneAdapter
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseReference
class ProductActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference
    private lateinit var buttonAdd: Button
    private lateinit var buttonRead: Button
    private lateinit var buttonProfile: Button
    private lateinit var listViewNames: ListView

    private val namesList = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>

    private val phoneRepo = ProductRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        database = FirebaseDatabase.getInstance().getReference("names")
        buttonAdd = findViewById(R.id.buttonAdd)
        listViewNames = findViewById(R.id.listViewNames)
        buttonProfile = findViewById(R.id.buttonProfile)

        readNames()

        buttonAdd.setOnClickListener { addName() }
        buttonProfile.setOnClickListener { profile()}
    }
    private fun addName() {
        val intent = Intent(this, AddProductActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun readNames() {
        phoneRepo.readAll(
            onSuccess = { phones ->
                // Создаем адаптер и устанавливаем его для ListView
                val adapter = PhoneAdapter(this, phones)
                val listView = findViewById<ListView>(R.id.listViewNames)
                listView.adapter = adapter
            },
            onFailure = {
                    exception ->
                Toast.makeText(this, "Ошибка при добавлении телефона:", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun profile(){
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
        finish()
    }
}