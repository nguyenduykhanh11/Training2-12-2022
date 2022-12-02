package com.example.pushnotificationfcm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pushnotificationfcm.databinding.ItemImageFirebaseBinding

class Adapter: RecyclerView.Adapter<Adapter.ViewHolder> (){
    private var list = listOf<String>()
    class ViewHolder(var binding: ItemImageFirebaseBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemImageFirebaseBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int = list.size
}