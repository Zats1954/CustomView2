package ru.zatsoft.customview

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.size
import androidx.lifecycle.ViewModelProvider
import ru.zatsoft.customview.databinding.ActivityTradeBinding
import java.io.IOException

private const val REQUEST_GALLERY = 201

class TradeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTradeBinding
    private lateinit var toolBar: Toolbar
    private lateinit var listAdapter: ListAdapter
    private var bitmap: Bitmap? = null
    private lateinit var emptyBitmap: Bitmap
    private lateinit var productModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productModel = ViewModelProvider(this)[ProductViewModel::class.java]
        binding = ActivityTradeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolBar = binding.toolbarMain
        setSupportActionBar(toolBar)
        title = " "

        emptyBitmap = getDrawable(R.drawable.ic_unknown_foreground)!!.toBitmap()
        listAdapter = ListAdapter(applicationContext, productModel.listUsers.value!!)

        productModel.listUsers.observe(this) {
            listAdapter = ListAdapter(this, it)
        }
        val inputKeyboard = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        binding.listView.adapter = listAdapter
        binding.listView.setClickable(true)

        binding.ivProductView.setOnClickListener {
            val photoIntent = Intent(Intent.ACTION_PICK)
            photoIntent.type = "image/*"
            startActivityForResult(photoIntent, REQUEST_GALLERY)
        }

        binding.save.setOnClickListener {
            try {
                val product = product()
                productModel.add(product)
                listAdapter.notifyDataSetChanged()
                println("////////-----------listAdapter.count ${listAdapter.count}")
                println("--------------------binding.listView.size ${binding.listView.size}")
                binding.edName.text.clear()
                binding.edPrice.text.clear()
                binding.ivProductView.setImageResource(R.drawable.ic_unknown_foreground)
                inputKeyboard.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Неправильный ввод", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun product(): Product {
        val product = Product(
            binding.edName.text.toString(),
            binding.edPrice.text.toString().toDouble(),
            bitmap ?: emptyBitmap
        )
        return product
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.exit)
            finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_GALLERY -> if (resultCode === RESULT_OK && data != null && data.data != null) {
                val selectedPhoto: Uri? = data.data
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhoto)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                binding.ivProductView.setImageBitmap(bitmap)
            } else {
                Toast.makeText(applicationContext, "Ошибка загрузки изображения result.data", Toast.LENGTH_LONG)
                    .show()
            }
            else -> Toast.makeText(applicationContext, "Ошибка загрузки изображени requestCode = $requestCode", Toast.LENGTH_LONG)
                .show()
        }
    }
}