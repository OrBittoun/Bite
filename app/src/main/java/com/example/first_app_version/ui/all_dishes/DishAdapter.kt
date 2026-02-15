package com.example.first_app_version.ui.all_dishes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.first_app_version.R
import com.example.first_app_version.data.models.Dish
import com.example.first_app_version.databinding.DishLayoutBinding

class DishAdapter(
    private val dishes: List<Dish>,
    private val onClick: (Dish) -> Unit
) : RecyclerView.Adapter<DishAdapter.DishViewHolder>() {

    inner class DishViewHolder(
        private val binding: DishLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init { binding.root.setOnClickListener(this) }

        fun bind(dish: Dish) {
            val ctx = binding.root.context
            val count = dish.reviewsCount

            binding.dishDesc.text = dish.description.orEmpty()
            binding.dishTitle.text = dish.name
            binding.restaurantName.text = dish.restaurantName

            // טעינת התמונה
            if (!dish.imageUrl.isNullOrEmpty()) {
                Glide.with(ctx)
                    .load(dish.imageUrl)
                    .placeholder(R.drawable.default_dish)
                    .into(binding.dishImg)
            } else {
                val res = dish.imageRes ?: R.mipmap.pizza_foreground
                Glide.with(ctx)
                    .load(res)
                    .placeholder(R.drawable.default_dish)
                    .into(binding.dishImg)
            }

            // הלוגיקה החדשה: הסתרת מחיר וביקורות למנות API
            if (dish.dishTypeId == 0) {
                binding.dishPrice.visibility = View.GONE
                binding.reviewsCount.visibility = View.GONE
            } else {
                binding.dishPrice.visibility = View.VISIBLE
                binding.reviewsCount.visibility = View.VISIBLE
                binding.dishPrice.text = ctx.getString(R.string.dish_price_display, dish.price.toDouble())
                binding.reviewsCount.text = ctx.resources.getQuantityString(R.plurals.reviews_count, count, count)
                binding.reviewsCount.textDirection = View.TEXT_DIRECTION_LOCALE
            }
        }

        override fun onClick(v: View?) {
            onClick(dishes[bindingAdapterPosition])
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DishLayoutBinding.inflate(inflater, parent, false)
        return DishViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DishViewHolder, position: Int) =
        holder.bind(dishes[position])

    override fun getItemCount(): Int = dishes.size
}