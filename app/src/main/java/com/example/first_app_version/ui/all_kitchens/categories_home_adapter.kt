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
    private val categories: List<HomeCategory>,
    private val previewProvider: (HomeCategory) -> List<DishPreview>,
    private val onDishClick: (Int) -> Unit,
    private val onCategoryClick: (HomeCategory) -> Unit
) : RecyclerView.Adapter<HomeCategoriesAdapter.CategoryViewHolder>() {

    override fun getItemCount(): Int = categories.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_cards, parent, false) // Creating a card
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
            categoryTitle.setOnClickListener { onCategoryClick(category) } // Tap goes to kitchen
            val previewItems = previewProvider(category)

            val homeCategoryRowAdapter = HomeCategoryRowAdapter(
                dishPreviews = previewItems,
                onDishClick = onDishClick,
                onExploreClick = { onCategoryClick(category) }
            )

            categoryRecyclerView.layoutManager =
                LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)

            categoryRecyclerView.adapter = homeCategoryRowAdapter

            // A tap anywhere in the horizontal row that is NOT an image navigates to kitchen
            val gestureDetector = GestureDetector(itemView.context, object : SimpleOnGestureListener() {
                override fun onSingleTapUp(e: MotionEvent): Boolean = true
            })

            categoryRecyclerView.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
                override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                    // Only handle simple taps so that scrolling won't do anything
                    if (!gestureDetector.onTouchEvent(e)) return false

                    // Child under the tap (if any)
                    val child = rv.findChildViewUnder(e.x, e.y)

                    if (child == null) {
                        // Tap on blank/background area (no child view) to navigate to kitchen
                        onCategoryClick(category)
                        return true
                    }

                    // If tapping on a child, check if it's an image item
                    // We detect image items by presence of previewImage in the child
                    val isImageItem = child.findViewById<ImageView>(R.id.previewImage) != null

                    return if (isImageItem) {
                        // Let the image's own click listener handle dish navigation
                        false
                    } else {
                        // Tap on non-image child so navigate tp kitchen
                        onCategoryClick(category)
                        true
                    }
                }

                // Must have override functions
                override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
                    // No-op: handled in intercept
                }

                override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                    // No-op
                }
            })
        }
    }
}