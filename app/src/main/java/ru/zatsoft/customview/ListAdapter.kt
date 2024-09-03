package ru.zatsoft.customview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class ListAdapter(private val context: Context, private val dataList: MutableList<Product> )
    : ArrayAdapter<Product>(context, R.layout.list_item, dataList)
{

    override fun getCount(): Int {
      return dataList.size
    }

    override fun getItem(position: Int): Product{
        return dataList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View{
        var view = view ?: LayoutInflater.from(context).inflate(R.layout.list_item,parent,false)
        val data = getItem(position)
        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvAge = view.findViewById<TextView>(R.id.tvPrice)
        val ivImageView = view.findViewById<ImageView>(R.id.ivImage)
        tvName.text = data.name
        tvAge.text = data.price.toString()
        ivImageView.setImageBitmap(data.image)
// -------------------------  update -----------
        view.invalidate()
        this.notifyDataSetChanged()
// -----------------------------------
        return view
    }
}