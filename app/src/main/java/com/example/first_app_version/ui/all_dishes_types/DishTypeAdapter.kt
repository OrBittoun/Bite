package com.example.first_app_version.ui.all_dishes_types

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.first_app_version.R
import com.example.first_app_version.data.models.DishTypeForRetro
import com.example.first_app_version.databinding.DishTypeLayoutBinding

class DishTypeAdapter(
    private val onClick: (DishTypeForRetro) -> Unit
) : RecyclerView.Adapter<DishTypeAdapter.DishTypeViewHolder>() {

    private val dishTypes: MutableList<DishTypeForRetro> = mutableListOf()

    fun submitList(newDishTypes: List<DishTypeForRetro>) {
        dishTypes.clear()
        dishTypes.addAll(newDishTypes)
        notifyDataSetChanged()
    }

    inner class DishTypeViewHolder(
        private val binding: DishTypeLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(dishType: DishTypeForRetro) {
            binding.mealTitle.text = dishType.name

            // Glide מספיק חכם כדי לדעת אם imageRes הוא Int מקומי או String של URL מהאינטרנט!
            Glide.with(binding.root.context)
                .load(dishType.imageRes)
                .placeholder(R.drawable.default_dish)
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