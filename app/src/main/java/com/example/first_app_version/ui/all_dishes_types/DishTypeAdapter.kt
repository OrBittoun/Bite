package com.example.first_app_version.ui.all_dishes_types

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.first_app_version.R
import com.example.first_app_version.data.models.DishType
import com.example.first_app_version.databinding.DishTypeLayoutBinding

class DishTypeAdapter(
    private val onClick: (DishType) -> Unit
) : RecyclerView.Adapter<DishTypeAdapter.DishTypeViewHolder>() {

    private val items: MutableList<DishType> = mutableListOf()

    fun submitList(newItems: List<DishType>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class DishTypeViewHolder(
        private val binding: DishTypeLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DishType) {
            binding.mealTitle.text = item.name
            binding.mealImage.setImageResource(item.imageRes ?: R.drawable.default_dish)
            binding.root.setOnClickListener { onClick(item) }
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
