package ru.zatsoft.customview

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.AbsListView
import android.widget.AbsListView.OnScrollListener.SCROLL_STATE_IDLE
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.graphics.drawable.toBitmap
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userModel = ViewModelProvider(this)[UserViewModel::class.java]
        binding = ActivityTradeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        toolBar = binding.toolbarMain
        setSupportActionBar(toolBar)
        title = " "

        emptyBitmap = getDrawable(R.drawable.ic_unknown_foreground)!!.toBitmap()
        listAdapter = ListAdapter(this, userModel.listUsers.value!!)

        userModel.listUsers.observe(this) {
            listAdapter = ListAdapter(this, it)
// -------------------------  update -----------
            listAdapter.notifyDataSetChanged()
            binding.root.invalidate()
// -----------------------------------------
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
            try{
            val user = Product(binding.edName.text.toString(),
                binding.edPrice.text.toString().toDouble(),
                bitmap?: emptyBitmap )
            userModel.add(user)
            binding.edName.text.clear()
            binding.edPrice.text.clear()
            binding.ivProductView.setImageResource(R.drawable.ic_unknown_foreground)
// -------------------------  update -----------
            listAdapter.notifyDataSetChanged()
            binding.root.invalidate()
// --------------------------------------------------------
            inputKeyboard.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
        }catch(e:NumberFormatException){
            Toast.makeText(this, "Неправильный ввод", Toast.LENGTH_LONG).show()
        }
        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            REQUEST_GALLERY -> if(resultCode === RESULT_OK){
                val selectedPhoto : Uri? = data?.data
                try{
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhoto )
                } catch(e:IOException){
                    e.printStackTrace()
                }
                binding.ivProductView.setImageBitmap(bitmap)
            }            }
    }
}