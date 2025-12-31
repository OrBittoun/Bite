package com.example.first_app_version.ui.all_kitchens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.first_app_version.R
import com.example.first_app_version.data.models.HomeCategory

class HomeCategoriesAdapter(
    private val categories: List<HomeCategory>,
    private val previewProvider: (HomeCategory) -> List<DishPreview>,
    private val onDishClick: (Int) -> Unit,
    private val onCategoryClick: (HomeCategory) -> Unit
) : RecyclerView.Adapter<HomeCategoriesAdapter.CategoryViewHolder>() {

    override fun getItemCount(): Int = categories.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_cards, parent, false) // creating a card
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val categoryTitle: TextView =
            itemView.findViewById(R.id.categoryTitle) // name of the category

        private val categoryRecyclerView: RecyclerView =
            itemView.findViewById(R.id.categoryRecyclerView) // recycler of the category

        fun bind(category: HomeCategory) {

            categoryTitle.text = category.kitchenName

            val previewItems = previewProvider(category)

            val homeCategoryRowAdapter = HomeCategoryRowAdapter(
                items = previewItems,
                onDishClick = onDishClick,
                onExploreClick = {
                    onCategoryClick(category)
                }
            )

            categoryRecyclerView.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

            categoryRecyclerView.adapter = homeCategoryRowAdapter
        }
    }
}
