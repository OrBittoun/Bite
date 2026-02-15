package com.example.first_app_version.ui.all_kitchens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.first_app_version.R
import com.example.first_app_version.data.models.DishPreview

class HomeCategoryRowAdapter(
    private val dishPreviews: List<DishPreview>,
    private val onDishClick: (Int) -> Unit,
    private val onExploreClick: () -> Unit,
    private val isExploreCategory: Boolean,
    private val isFavoritesCategory: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_IMAGE = 0
        private const val TYPE_BUTTON = 1
        private const val TYPE_EMPTY_TEXT = 2
    }

    override fun getItemCount(): Int {
        if (isFavoritesCategory && dishPreviews.isEmpty()) return 1
        return if (isExploreCategory) dishPreviews.size else dishPreviews.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        if (isFavoritesCategory && dishPreviews.isEmpty()) return TYPE_EMPTY_TEXT
        return if (position < dishPreviews.size) TYPE_IMAGE else TYPE_BUTTON
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            TYPE_IMAGE -> ImageViewHolder(inflater.inflate(R.layout.preview_image, parent, false))
            TYPE_BUTTON -> ButtonViewHolder(inflater.inflate(R.layout.preview_button, parent, false))
            else ->  {
                val view = inflater.inflate(R.layout.item_empty_favorites, parent, false)
                EmptyTextViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ImageViewHolder -> holder.bind(dishPreviews[position])
            is ButtonViewHolder -> holder.bind()
            is EmptyTextViewHolder -> holder.bind()
        }
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.previewImage)

        fun bind(item: DishPreview) {
            // טיפול חכם של השותף: בדיקה אם זו תמונה מהרשת או מקומית
            if (!item.imageUrl.isNullOrEmpty()) {
                Glide.with(itemView.context)
                    .load(item.imageUrl)
                    .placeholder(R.drawable.baseline_fastfood_24)
                    .error(R.drawable.baseline_fastfood_24)
                    .into(imageView)
            } else if (item.imageRes != null && item.imageRes != 0) {
                Glide.with(itemView.context)
                    .load(item.imageRes)
                    .placeholder(R.drawable.baseline_fastfood_24)
                    .into(imageView)
            } else {
                imageView.setImageResource(R.drawable.baseline_fastfood_24)
            }

            imageView.setOnClickListener {
                onDishClick(item.dishId)
            }
        }
    }

    inner class ButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val button: Button = itemView.findViewById(R.id.btnExplore)

        fun bind() {
            button.setOnClickListener {
                onExploreClick()
            }
        }
    }

    inner class EmptyTextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.emptyText)

        fun bind() {
            textView.text = itemView.context.getString(R.string.empty_favorites)
        }
    }
}