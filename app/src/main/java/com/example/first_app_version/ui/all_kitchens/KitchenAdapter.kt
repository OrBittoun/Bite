//package com.example.first_app_version.ui.all_kitchens
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.example.first_app_version.data.models.Kitchen
//import com.example.first_app_version.databinding.KitchenLayoutBinding
//
//class KitchenAdapter (val kitchens: List<Kitchen>, val callBack: KitchenListener)
//    : RecyclerView.Adapter<KitchenAdapter.KitchenViewHolder>() {
//
//    interface KitchenListener {
//        fun onKitchenClicked(index: Int)
//        fun onKitchenLongClicked(index: Int)
//    }
//
//    inner class KitchenViewHolder (private val binding: KitchenLayoutBinding)
//        : RecyclerView.ViewHolder(binding.root), View.OnClickListener, View.OnLongClickListener {
//
//        init {
//            binding.root.setOnClickListener ( this )
//            binding.root.setOnLongClickListener ( this )
//        }
//
//        override fun onClick(v: View?) {
//            callBack.onKitchenClicked(bindingAdapterPosition)
//        }
//
//        override fun onLongClick(v: View?): Boolean {
//            callBack.onKitchenLongClicked(bindingAdapterPosition)
//            return false
//        }
//
//        fun bind(kitchen: Kitchen) {
//            callBack
//            binding.kitchenTitle.text = kitchen.name
//        }
//    }
//
//    fun kitchenAt(position : Int) = kitchens[position]
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KitchenViewHolder =
//        KitchenViewHolder(KitchenLayoutBinding.inflate(LayoutInflater.from(parent.context), parent,false))
//
//    override fun onBindViewHolder(holder: KitchenViewHolder, position: Int) =
//        holder.bind(kitchens[position])
//
//    override fun getItemCount(): Int {
//        return kitchens.size
//    }
//}