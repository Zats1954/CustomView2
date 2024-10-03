package ru.zatsoft.customview

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import ru.zatsoft.customview.databinding.ActivityTradeBinding
import java.io.IOException

private const val REQUEST_GALLERY = 201

class TradeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTradeBinding
    private lateinit var toolBar: Toolbar
    private lateinit var listAdapter: ListAdapter
    private lateinit var productModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        productModel = ViewModelProvider(this)[ProductViewModel::class.java]
        binding = ActivityTradeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolBar = binding.toolbarMain
        setSupportActionBar(toolBar)
        title = " "

        listAdapter = ListAdapter(applicationContext, productModel.listProducts.value!!)
        val inputKeyboard = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        binding.listView.adapter = listAdapter
        binding.listView.setClickable(true)
        binding.listView.onItemClickListener = AdapterView.OnItemClickListener {
                parent, v, position, id ->
                        val product  = listAdapter.getItem(position)
                        val productIntent = Intent(this,InfoActivity::class.java)
                        productIntent.putExtra( "product",product)
                        startActivity(productIntent)
        }


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
                binding.edName.text.clear()
                binding.edPrice.text.clear()
                binding.ivProductView.setImageResource(R.drawable.ic_unknown_foreground)
                productModel.setUri(null)
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
            productModel.getUri().toString(),
            binding.edDescription?.text.toString()
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
//       Загрузка изображения
            REQUEST_GALLERY -> if (resultCode === RESULT_OK && data != null && data.data != null) {
                val selectedPhoto: Uri? = data.data
                try {
                    productModel.setUri(selectedPhoto)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                binding.ivProductView.setImageURI(productModel.getUri())
            } else {
                Toast.makeText(applicationContext, "Ошибка загрузки изображения result.data", Toast.LENGTH_LONG)
                    .show()
            }
            else -> Toast.makeText(applicationContext, "Ошибка загрузки изображени requestCode = $requestCode", Toast.LENGTH_LONG)
                .show()
        }


    }
}