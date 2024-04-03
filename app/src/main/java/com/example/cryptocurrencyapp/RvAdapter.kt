package com.example.cryptocurrencyapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptocurrencyapp.databinding.RvItemBinding

class RvAdapter(val context: Context, var data:ArrayList<Modal>): RecyclerView.Adapter<RvAdapter.ViewHolder>()  {

    inner class ViewHolder(val binding: RvItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvAdapter.ViewHolder {
        val view = RvItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RvAdapter.ViewHolder, position: Int) {
        holder.binding.name.text = data[position].name
        holder.binding.symbol.text = data[position].symbol
        holder.binding.price.text = data[position].price
        Glide.with(context).load(data[position].image).into(holder.binding.image)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}