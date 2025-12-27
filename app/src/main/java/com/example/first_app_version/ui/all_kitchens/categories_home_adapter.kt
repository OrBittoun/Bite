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
    private var categories: List<HomeCategory>,            // make mutable
    private val onCategoryClick: (HomeCategory) -> Unit
) : RecyclerView.Adapter<HomeCategoriesAdapter.CategoryViewHolder>() {

    fun submitList(newCategories: List<HomeCategory>) {
        categories = newCategories
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = categories.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_cards, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.bind(category)
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryTitle: TextView = itemView.findViewById(R.id.categoryTitle)
        private val categoryRecyclerView: RecyclerView = itemView.findViewById(R.id.categoryRecyclerView)

        fun bind(category: HomeCategory) {
            categoryTitle.text = category.title

            val previewAdapter = PreviewAdapter(
                images = category.previewImages,
                onImageClick = { onCategoryClick(category) },
                onExploreClick = { onCategoryClick(category) }
            )
            categoryRecyclerView.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            categoryRecyclerView.adapter = previewAdapter
        }
    }
}