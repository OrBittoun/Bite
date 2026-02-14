package com.example.first_app_version.ui.all_kitchens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.first_app_version.R

class HomeCategoryRowAdapter(
    private val dishPreviews: List<DishPreview>,
    private val onDishClick: (Int) -> Unit,
    private val onExploreClick: () -> Unit,
    private val isExploreCategory: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_IMAGE = 0
        private const val TYPE_BUTTON = 1
    }

    override fun getItemCount(): Int {
        return if (isExploreCategory) dishPreviews.size else dishPreviews.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < dishPreviews.size) TYPE_IMAGE else TYPE_BUTTON
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return if (viewType == TYPE_IMAGE) {
            val view = inflater.inflate(R.layout.preview_image, parent, false)
            ImageViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.preview_button, parent, false)
            ButtonViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ImageViewHolder -> holder.bind(dishPreviews[position])
            is ButtonViewHolder -> holder.bind()
        }
    }

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val imageView: ImageView =
            itemView.findViewById(R.id.previewImage)

        fun bind(item: DishPreview) {

            Glide.with(itemView.context)
                .load(item.imageRes)
                .into(imageView)

            imageView.setOnClickListener {
                onDishClick(item.dishId)
            }
        }
    }

    inner class ButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val button: Button =
            itemView.findViewById(R.id.btnExplore)

        fun bind() {
            button.setOnClickListener {
                onExploreClick()
            }
        }
    }
}