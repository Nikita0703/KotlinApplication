package com.example.app
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
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

    private lateinit var searchEditText: EditText
    private lateinit var buttonSearch: Button
    private lateinit var buttonReturn:Button

    private val namesList = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>

    private lateinit var imageView: ImageView

    private val phoneRepo = ProductRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        database = FirebaseDatabase.getInstance().getReference("names")
        buttonAdd = findViewById(R.id.buttonAdd)
        listViewNames = findViewById(R.id.listViewNames)
        buttonProfile = findViewById(R.id.buttonProfile)

        searchEditText = findViewById(R.id.searchEditText)
        buttonSearch = findViewById(R.id.buttonSearch)
        buttonReturn = findViewById(R.id.buttonReturn)

        readNames()

        buttonAdd.setOnClickListener { addName() }
        buttonProfile.setOnClickListener { profile()}
        buttonSearch.setOnClickListener{ readNamesSearch()}
        buttonReturn.setOnClickListener{ returnRes()}
    }
    private fun addName() {
        val intent = Intent(this, AddProductActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun readNames() {
        phoneRepo.readAll(
            onSuccess = { phones ->
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

    private fun readNamesSearch() {
        val searchQuery = findViewById<EditText>(R.id.searchEditText).text.toString()

        val listView = findViewById<ListView>(R.id.listViewNames)
        listView.adapter = null

        phoneRepo.readAll(
            onSuccess = { phones ->
                val filteredPhones = phones.filter { phone ->
                    phone.brand.contains(searchQuery, ignoreCase = true)
                }

                val adapter = PhoneAdapter(this, filteredPhones)
                listView.adapter = adapter
            },
            onFailure = { exception ->
                Toast.makeText(this, "Ошибка при добавлении телефона: ", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun returnRes() {
        val listView = findViewById<ListView>(R.id.listViewNames)
        listView.adapter = null

        phoneRepo.readAll(
            onSuccess = { phones ->
                val adapter = PhoneAdapter(this, phones)
                val listView = findViewById<ListView>(R.id.listViewNames)
                listView.adapter = adapter
            },
            onFailure = {
                    exception ->
                Toast.makeText(this, "Ошибка при добавлении телефона:", Toast.LENGTH_SHORT).show()
            }
        )
        searchEditText = findViewById(R.id.searchEditText)
        searchEditText.text = Editable.Factory.getInstance().newEditable("")
    }


}