package com.latihan.android.githubuserapp3.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.latihan.android.githubuserapp3.room.User
import com.latihan.android.githubuserapp3.databinding.ItemRowUserBinding

class ListUserAdapter(private val listUser: ArrayList<User>) :
    RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallBack: OnItemClickCallBack

    class ListViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root)

    interface OnItemClickCallBack {
        fun onItemClicked(data: User)
    }

    fun setOnItemClickCallBack(onItemClickCallback: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemRowUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (username, id, avatar) = listUser[position]

        holder.binding.apply {
            Glide.with(holder.itemView.context)
                .load(avatar)
                .into(holder.binding.imgItemPhoto)
            tvItemUsername.text = username
            tvItemId.text = "ID : $id"
        }

        holder.itemView.setOnClickListener { onItemClickCallBack.onItemClicked(listUser[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = listUser.size
}