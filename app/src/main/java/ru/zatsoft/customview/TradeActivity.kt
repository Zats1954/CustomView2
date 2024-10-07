package ru.zatsoft.customview

import android.content.Intent
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
    private val defaultImage = Uri.parse("android.resource://ru.zatsoft.customview/" + R.drawable.ic_unknown_foreground).toString()
    private lateinit var binding: ActivityTradeBinding
    private lateinit var toolBar: Toolbar
    private lateinit var listAdapter: ListAdapter
    private  lateinit var productModel: ProductViewModel

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
                        productIntent.putExtra("listProducts",
                            productModel.listProducts.value as ArrayList<Product>)
                        productIntent.putExtra( "product",product)
                        productIntent.putExtra( "position",position)
                        startActivity(productIntent)
        }

        binding.ivProductView.setOnClickListener {
            val photoIntent = Intent(Intent.ACTION_PICK)
            photoIntent.type = "image/*"
            startActivityForResult(photoIntent, REQUEST_GALLERY)
        }

        binding.save.setOnClickListener {
            try {
                productModel.add(Product(
                    binding.edName.text.toString(),
                    binding.edPrice.text.toString().toDouble(),
                    (productModel.getUri()?.toString() ?: defaultImage),
                    binding.edDescription?.text.toString()))

                listAdapter.notifyDataSetChanged()
                clearView()
                inputKeyboard.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Неправильный ввод", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val check = intent.extras?.getBoolean("change") ?: false
        if(check){
            productModel._listProducts.value =
                intent.getSerializableExtra("newPoducts")  as MutableList<Product>
            listAdapter = ListAdapter(this, productModel.listProducts.value!! )
            binding.listView.adapter = listAdapter
            listAdapter.notifyDataSetChanged()
            clearView()
        }
    }

    private fun clearView() {
        binding.edName.text.clear()
        binding.edPrice.text.clear()
        binding.edDescription?.text?.clear()
        binding.ivProductView.setImageResource(R.drawable.ic_unknown_foreground)
        productModel.setUri(null)
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