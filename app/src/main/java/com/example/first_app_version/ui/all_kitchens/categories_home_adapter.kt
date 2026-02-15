package com.example.first_app_version.ui.all_kitchens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.first_app_version.R
import android.view.MotionEvent
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.widget.ImageView

class HomeCategoriesAdapter(
    var categories: List<HomeCategory>,
    private val previewProvider: (HomeCategory) -> List<DishPreview>,
    private val onDishClick: (Int) -> Unit,
    private val onCategoryClick: (HomeCategory) -> Unit
) : RecyclerView.Adapter<HomeCategoriesAdapter.CategoryViewHolder>() {

    // שלב 1: הגדרת מזהים יציבים למניעת בלבול בתמונות בזמן מיחזור השורות
    init {
        setHasStableIds(true)
    }

    override fun getItemCount(): Int = categories.size

    // החזרת ה-ID של המטבח כמזהה קבוע לשורה
    override fun getItemId(position: Int): Long {
        return categories[position].kitchenId.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_cards, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val categoryTitle: TextView =
            itemView.findViewById(R.id.categoryTitle)

        private val categoryRecyclerView: RecyclerView =
            itemView.findViewById(R.id.categoryRecyclerView)

        fun bind(category: HomeCategory) {
            categoryTitle.text = category.kitchenName
            categoryTitle.setOnClickListener { onCategoryClick(category) }

            val previewItems = previewProvider(category)

            val homeCategoryRowAdapter = HomeCategoryRowAdapter(
                dishPreviews = previewItems,
                onDishClick = onDishClick,
                onExploreClick = { onCategoryClick(category) },
                isExploreCategory = (category.kitchenId == 6)
            )

            categoryRecyclerView.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

            categoryRecyclerView.adapter = homeCategoryRowAdapter

            // טיפול בנקישה על השורה (מחוץ לתמונות)
            val gestureDetector = GestureDetector(itemView.context, object : SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean = true
            })

            categoryRecyclerView.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    if (!gestureDetector.onTouchEvent(e)) return false

                    val child = rv.findChildViewUnder(e.x, e.y)

                    if (child == null) {
                        onCategoryClick(category)
                        return true
                    }

                    val isImageItem = child.findViewById<ImageView>(R.id.previewImage) != null

                    return if (isImageItem) {
                        false
                    } else {
                        onCategoryClick(category)
                        true
                    }
                }

                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}
            })
        }
    }
}