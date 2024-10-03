package ru.zatsoft.customview

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.net.toUri
import ru.zatsoft.customview.databinding.ActivityInfoBinding

class InfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoBinding
    private lateinit var toolBar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolBar = binding.toolbarMain
        setSupportActionBar(toolBar)
        title = " "
        val product = intent.getSerializableExtra ("product") as Product
        binding.tvName.text = product.name
        binding.tvPrice.text = product.price.toString()
        binding.ivImage.setImageURI(product.image.toUri())
        binding.tvDescription.text = product.description
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.exit)
           finishAffinity()
        return super.onOptionsItemSelected(item)
    }
}