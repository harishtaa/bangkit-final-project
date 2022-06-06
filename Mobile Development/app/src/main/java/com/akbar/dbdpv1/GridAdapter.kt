package com.akbar.dbdpv1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GridAdapter(private val listDog: ArrayList<DogBreed>): RecyclerView.Adapter<GridAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: DogBreed)
    }

    fun setOnItemClickCallback(onItemClickCallback: Any) {
        this.onItemClickCallback = onItemClickCallback as OnItemClickCallback
    }

    class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var tvNama: TextView = itemView.findViewById(R.id.tv_nama_breed)
        var imgDog: ImageView = itemView.findViewById(R.id.iv_foto_breed)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_grid, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (nama, foto) = listDog[position]

        holder.tvNama.text = nama
        if (foto != null){
            holder.imgDog.setImageResource(foto)
        }
        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(listDog[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = listDog.size
}