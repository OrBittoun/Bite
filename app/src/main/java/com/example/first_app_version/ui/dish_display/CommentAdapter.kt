package com.example.first_app_version.ui.dish_display

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.first_app_version.data.models.Comment
import com.example.first_app_version.databinding.CommentDisplayBinding

class CommentAdapter : RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    private val items = mutableListOf<Comment>()

    fun submitList(newItems: List<Comment>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class CommentViewHolder(
        private val binding: CommentDisplayBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Comment) {
            binding.commentAuthor.text = item.authorName
            binding.commentDate.text = item.createdAt
            binding.commentRating.rating = item.rating.toFloat()
            binding.commentText.text = item.text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CommentDisplayBinding.inflate(inflater, parent, false)
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
