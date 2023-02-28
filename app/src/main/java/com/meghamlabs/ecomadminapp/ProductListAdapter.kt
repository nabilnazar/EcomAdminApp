package com.meghamlabs.ecomadminapp


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.core.Context
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.InputStream

class ProductListAdapter(private val productList: List<Product>) :
    RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Add any UI elements from the list item layout that you want to access in the adapter
        val nameTextView: TextView = itemView.findViewById(R.id.nameTv)
        val priceTextView: TextView = itemView.findViewById(R.id.priceTv)
        val imageView: ImageView = itemView.findViewById(R.id.imageV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the list item layout and create a new ViewHolder
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Bind the product data to the UI elements in the list item layout
        val product = productList[position]
        holder.nameTextView.text = product.name
        holder.priceTextView.text = product.price
        product.image?.let { Log.d("ImageURL", it) } //
        Glide.with(holder.itemView.context).load(product.image).into(holder.imageView)


    }

    override fun getItemCount(): Int {
        return productList.size
    }


}