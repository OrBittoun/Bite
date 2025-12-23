
//עבד
//package com.example.first_app_version.ui.all_dishes_types
//
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.activityViewModels
//import androidx.fragment.app.viewModels
//import androidx.navigation.fragment.findNavController
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.first_app_version.R
//import com.example.first_app_version.databinding.DishesTypesLayoutBinding
//import com.example.first_app_version.ui.KitchenViewModel
//import com.example.first_app_version.ui.SelectionViewModel
//
//class DishesTypesFragment : Fragment() {
//
//    private var _binding: DishesTypesLayoutBinding? = null
//    private val binding get() = _binding!!
//
//    private val kitchenViewModel: KitchenViewModel by activityViewModels()
//    private val dishesTypesViewModel: DishesTypesViewModel by viewModels()
//    private val selectionViewModel: SelectionViewModel by activityViewModels()
//
//    private lateinit var adapter: DishTypeAdapter
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = DishesTypesLayoutBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        Log.d("NAV_CHECK", "DishesTypesFragment loaded")
//
//        adapter = DishTypeAdapter { dishType ->
//            selectionViewModel.setDishType(dishType)
//            findNavController().navigate(R.id.action_dishesTypesFragment_to_dishesFragment)
//        }
//
//        binding.recyclerDishesTypes.layoutManager = LinearLayoutManager(requireContext())
//        binding.recyclerDishesTypes.adapter = adapter
//
//        kitchenViewModel.chosenKitchen.observe(viewLifecycleOwner) { kitchen ->
//            if (kitchen == null) {
//                Log.d("NAV_CHECK", "chosenKitchen is NULL -> no data to show")
//                Toast.makeText(requireContext(), "לא נבחר מטבח", Toast.LENGTH_SHORT).show()
//                adapter.submitList(emptyList())
//                return@observe
//            }
//
//            Log.d("NAV_CHECK", "chosenKitchen id=${kitchen.id}")
//
//            dishesTypesViewModel.getDishTypesForKitchen(kitchen.id)
//                .observe(viewLifecycleOwner) { dishTypes ->
//                    Log.d("NAV_CHECK", "dishTypes size=${dishTypes.size}")
//                    if (dishTypes.isEmpty()) {
//                        Toast.makeText(requireContext(), "אין סוגי מנות למטבח הזה", Toast.LENGTH_SHORT).show()
//                    }
//                    adapter.submitList(dishTypes)
//                }
//        }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}


package com.example.first_app_version.ui.all_dishes_types

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.first_app_version.R
import com.example.first_app_version.databinding.DishesTypesLayoutBinding
import com.example.first_app_version.ui.KitchenViewModel
import com.example.first_app_version.ui.SelectionViewModel

class DishesTypesFragment : Fragment() {

    private var _binding: DishesTypesLayoutBinding? = null
    private val binding get() = _binding!!

    private val kitchenViewModel: KitchenViewModel by activityViewModels()
    private val dishesTypesViewModel: DishesTypesViewModel by viewModels()
    private val selectionViewModel: SelectionViewModel by activityViewModels()

    private lateinit var adapter: DishTypeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DishesTypesLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("NAV_CHECK", "DishesTypesFragment loaded")

        adapter = DishTypeAdapter { dishType ->
            selectionViewModel.setDishType(dishType)
            findNavController().navigate(R.id.action_dishesTypesFragment_to_dishesFragment)
        }

        binding.recyclerDishesTypes.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerDishesTypes.adapter = adapter

        kitchenViewModel.chosenKitchen.observe(viewLifecycleOwner) { kitchen ->
            if (kitchen == null) {
                Toast.makeText(requireContext(), "לא נבחר מטבח", Toast.LENGTH_SHORT).show()
                adapter.submitList(emptyList())
                return@observe
            }

            dishesTypesViewModel.getDishTypesForKitchen(kitchen.id)
                .observe(viewLifecycleOwner) { dishTypes ->
                    if (dishTypes.isEmpty()) {
                        Toast.makeText(requireContext(), "אין סוגי מנות למטבח הזה", Toast.LENGTH_SHORT).show()
                    }
                    adapter.submitList(dishTypes)
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
