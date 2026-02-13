package com.example.first_app_version.ui.all_dishes_types

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.first_app_version.R
import com.example.first_app_version.data.models.DishType
import com.example.first_app_version.databinding.DishTypeLayoutBinding

class DishTypeAdapter(
    private val onClick: (DishType) -> Unit
) : RecyclerView.Adapter<DishTypeAdapter.DishTypeViewHolder>() {

    private val dishTypes: MutableList<DishType> = mutableListOf()

    fun submitList(newDishTypes: List<DishType>) {
        dishTypes.clear()
        dishTypes.addAll(newDishTypes)
        notifyDataSetChanged()
    }

    inner class DishTypeViewHolder(
        private val binding: DishTypeLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(dishType: DishType) {
            binding.mealTitle.text = dishType.name
            Glide.with(binding.root.context)
                .load(dishType.imageRes ?: R.drawable.default_dish)
                .into(binding.mealImage)
            binding.root.setOnClickListener { onClick(dishType) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishTypeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DishTypeLayoutBinding.inflate(inflater, parent, false)
        return DishTypeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DishTypeViewHolder, position: Int) {
        holder.bind(dishTypes[position])
    }

    override fun getItemCount(): Int = dishTypes.size
}
