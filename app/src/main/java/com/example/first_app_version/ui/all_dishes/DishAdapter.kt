package com.example.first_app_version.ui.all_dishes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.first_app_version.R
import com.example.first_app_version.data.models.Dish
import com.example.first_app_version.databinding.DishLayoutBinding

class DishAdapter(
    private val items: List<Dish>,
    private val onClick: (Dish) -> Unit
) : RecyclerView.Adapter<DishAdapter.DishViewHolder>() {

    inner class DishViewHolder(
        private val binding: DishLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init { binding.root.setOnClickListener(this) }

        fun bind(item: Dish) {
            binding.dishTitle.text = item.name
            val res = item.imageRes ?: R.mipmap.pizza_foreground
            binding.dishImg.setImageResource(res)
            binding.restaurantName.text = item.restaurantName

            //Add dish price to binding function
            binding.dishPrice.text = "Price: ${item.price}₪"

            binding.dishDesc.text = item.description ?: ""
            binding.reviewsCount.text = "${item.reviewsCount} reviews"

        }

        override fun onClick(v: View?) {
            onClick(items[bindingAdapterPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DishLayoutBinding.inflate(inflater, parent, false)
        return DishViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) =
        holder.bind(items[position])

    override fun getItemCount(): Int = items.size
}
