package com.latihan.android.githubuserapp3.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.latihan.android.githubuserapp3.room.User
import com.latihan.android.githubuserapp3.activities.DetailActivity
import com.latihan.android.githubuserapp3.databinding.ItemRowUserBinding
import com.latihan.android.githubuserapp3.helper.UserDiffCallback

class ListFavoriteAdapter: RecyclerView.Adapter<ListFavoriteAdapter.ListFavViewHolder>() {
    private val listUsers = ArrayList<User>()

    fun setListUsers(listUsers: List<User>) {
        val diffCallback = UserDiffCallback(this.listUsers, listUsers)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listUsers.clear()
        this.listUsers.addAll(listUsers)
        diffResult.dispatchUpdatesTo(this)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListFavViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListFavViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListFavViewHolder, position: Int) {
        holder.bind(listUsers[position])
    }

    override fun getItemCount(): Int {
        return listUsers.size
    }

    inner class ListFavViewHolder(private val binding: ItemRowUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding) {
                tvItemUsername.text = user.username
                tvItemId.text = "ID : ${user.id}"
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .into(binding.imgItemPhoto)
                cardView.setOnClickListener {
                    val intent = Intent(it.context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_DATA, user)
                    it.context.startActivity(intent)
                }
            }
        }
    }
}