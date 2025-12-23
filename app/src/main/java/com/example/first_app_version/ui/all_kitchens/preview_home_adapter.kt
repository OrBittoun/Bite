package com.example.first_app_version.ui.all_kitchens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.first_app_version.R

class PreviewAdapter(
    private val images: List<Int>,
    private val onImageClick: () -> Unit,
    private val onExploreClick: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_IMAGE = 0
        private const val TYPE_BUTTON = 1
    }

    override fun getItemCount(): Int {
        return images.size + 1 //images + 1 button each card
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < images.size) TYPE_IMAGE else TYPE_BUTTON
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        return if (viewType == TYPE_IMAGE) {
            val view = inflater.inflate(R.layout.preview_image, parent, false) //creating image view
            ImageViewHolder(view)

        } else {
            val view = inflater.inflate(R.layout.preview_button, parent, false) //creating a button
            ButtonViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) { //when we want to fill the view
        when (holder) {

            is ImageViewHolder -> {
                holder.bind(images[position]) //if it's an image- take it from the specific position
            }
            is ButtonViewHolder -> { //take the button
                holder.bind()
            }
        }
    }


    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.previewImage) //take the image from preview_image


        fun bind(imageResId: Int) {
            imageView.setImageResource(imageResId) //the specific image
            imageView.setOnClickListener {
                onImageClick()
            }
        }
    }

    inner class ButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val button: Button =  itemView.findViewById(R.id.btnExplore) //take the button from preview_button

        fun bind() {
            button.setOnClickListener {
                onExploreClick()
            }
        }
    }
}


