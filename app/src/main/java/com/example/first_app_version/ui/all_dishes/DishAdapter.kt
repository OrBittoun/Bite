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

        fun bind(dish: Dish) {
            val ctx = binding.root.context
            val res = dish.imageRes ?: R.mipmap.pizza_foreground
            val count = dish.reviewsCount

            binding.dishDesc.text = dish.description.orEmpty()
            binding.dishTitle.text = dish.name
            binding.dishImg.setImageResource(res)
            binding.restaurantName.text = dish.restaurantName
            binding.dishPrice.text = ctx.getString(R.string.dish_price_display, dish.price.toDouble())
            binding.dishDesc.text = dish.description ?: ""
            binding.reviewsCount.text = ctx.resources.getQuantityString(R.plurals.reviews_count, count, count)
            binding.reviewsCount.textDirection = View.TEXT_DIRECTION_LOCALE
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
