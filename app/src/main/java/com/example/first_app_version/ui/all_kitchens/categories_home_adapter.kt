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
    private val onCategoryClick: (HomeCategory) -> Unit
) : RecyclerView.Adapter<HomeCategoriesAdapter.CategoryViewHolder>() {

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_cards, parent, false) //creating a card
        return CategoryViewHolder(view)
    }


    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position] //take the specific category
        holder.bind(category)
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryTitle: TextView = itemView.findViewById(R.id.categoryTitle) //name of the category
        private val categoryRecyclerView: RecyclerView = itemView.findViewById(R.id.categoryRecyclerView) //recycler of the category

        fun bind(category: HomeCategory) {

            categoryTitle.text = category.title

            val previewAdapter = PreviewAdapter( //creating preview_home_adapter
                images = category.previewImages,

                onImageClick = {
                    onCategoryClick(category)
                },
                onExploreClick = {
                    onCategoryClick(category)
                }
            )
            categoryRecyclerView.layoutManager =
                LinearLayoutManager(
                    itemView.context,
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            categoryRecyclerView.adapter = previewAdapter
        }
        }


    }




