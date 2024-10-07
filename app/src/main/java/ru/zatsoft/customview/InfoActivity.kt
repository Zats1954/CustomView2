package ru.zatsoft.customview

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import ru.zatsoft.customview.databinding.ActivityInfoBinding
import java.io.IOException

private const val REQUEST_GALLERY = 201

class InfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoBinding
    private lateinit var toolBar: Toolbar
    private lateinit var listProducts: MutableList<Product>
    private lateinit var product: Product
    private var position: Int = 0
    private var change = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolBar = binding.toolbarMain
        setSupportActionBar(toolBar)
        title = " "

        listProducts = intent.getSerializableExtra ("listProducts") as MutableList<Product>
        product = intent.getSerializableExtra ("product") as Product
        position = intent.getIntExtra("position", -1)

        binding.tvName.text = product.name
        binding.tvPrice.text = product.price.toString()
        binding.ivImage.setImageURI(product.image.toUri())
        binding.tvDescription.text = product.description
        binding.ivImage.setOnClickListener {
            val photoIntent = Intent(Intent.ACTION_PICK)
            photoIntent.type = "image/*"
            change = false
            startActivityForResult(photoIntent, REQUEST_GALLERY)
        }
        binding.btnSave.setOnClickListener {
            val intent = Intent(this, TradeActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.putExtra("change", change)
            intent.putExtra("newPoducts", listProducts as ArrayList<Product>)
            startActivity(intent)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.exit)
            finishAffinity()
        else if(item.itemId == R.id.back)
            finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
//       Загрузка нового изображения
            REQUEST_GALLERY -> if (resultCode === RESULT_OK && data != null && data.data != null) {
                val selectedPhoto: Uri? = data.data
                try {
                    product.image = selectedPhoto.toString()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                binding.ivImage.setImageURI(product.image.toUri())
                if(position > -1)
                {listProducts.add(position+1, product)
                    listProducts.removeAt(position)}
                change = true
            } else {
                Toast.makeText(
                    applicationContext,
                    "Ошибка загрузки изображения result.data",
                    Toast.LENGTH_LONG
                )
                    .show()
            }

            else -> Toast.makeText(
                applicationContext,
                "Ошибка загрузки изображени requestCode = $requestCode",
                Toast.LENGTH_LONG
            )
                .show()
        }


    }
}