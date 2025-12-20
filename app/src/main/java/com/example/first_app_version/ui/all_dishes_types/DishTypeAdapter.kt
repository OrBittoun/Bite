package com.example.first_app_version.ui.all_dishes_types

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.first_app_version.data.models.DishType
import com.example.first_app_version.databinding.DishTypeLayoutBinding

class DishTypeAdapter(
    private val items: List<DishType>,
    private val onClick: (DishType) -> Unit
) : RecyclerView.Adapter<DishTypeAdapter.DishTypeViewHolder>() {

    inner class DishTypeViewHolder(
        private val binding: DishTypeLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(item: DishType) {
            binding.dishTypeTitle.text = item.name
            // If you later add images, set binding.dishTypeImg.setImageResource(item.imageRes ?: ...)
        }

        override fun onClick(v: View?) {
            onClick(items[bindingAdapterPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishTypeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DishTypeLayoutBinding.inflate(inflater, parent, false)
        return DishTypeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DishTypeViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}