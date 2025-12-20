package com.example.first_app_version.ui.all_dishes_types

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    companion object {
        private const val TAG = "DishesTypesFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DishesTypesLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerDishesTypes.layoutManager = LinearLayoutManager(requireContext())

        kitchenViewModel.chosenKitchen.observe(viewLifecycleOwner) { kitchen ->
            dishesTypesViewModel.getDishTypesForKitchen(kitchen.id)
                .observe(viewLifecycleOwner) { dishTypes ->
                    binding.recyclerDishesTypes.adapter =
                        DishTypeAdapter(dishTypes) { dishType ->
                            selectionViewModel.setDishType(dishType)
                            findNavController().navigate(
                                R.id.action_dishesTypesFragment_to_dishesFragment
                            )
                        }
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}