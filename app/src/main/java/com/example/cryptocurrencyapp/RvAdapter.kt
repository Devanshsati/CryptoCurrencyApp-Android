package com.example.cryptocurrencyapp

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptocurrencyapp.databinding.RvItemBinding

class RvAdapter(val context: Context, var data:ArrayList<Modal>): RecyclerView.Adapter<RvAdapter.ViewHolder>()  {

    @SuppressLint("NotifyDataSetChanged")
    fun changeData(filterData : ArrayList<Modal>){
        data = filterData
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: RvItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = RvItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        setAnimation(holder.itemView)
        val currentItem = data[position]
        holder.binding.name.text = currentItem.name
        holder.binding.symbol.text = currentItem.symbol
        holder.binding.price.text = currentItem.price
        Glide.with(context).load(currentItem.image).into(holder.binding.image)
        holder.binding.rank.text = currentItem.marketCapRank
        holder.binding.marketCap.text = currentItem.marketCap

    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setAnimation(view: View){
        val anim = AlphaAnimation(0.0f,10f)
        anim.duration = 1000
        view.startAnimation(anim)
    }
}