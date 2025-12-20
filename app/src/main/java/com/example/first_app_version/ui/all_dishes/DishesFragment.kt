package com.example.first_app_version.ui.all_dishes

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
import com.example.first_app_version.databinding.DishesLayoutBinding
import com.example.first_app_version.ui.SelectionViewModel

class DishesFragment : Fragment() {

    private var _binding: DishesLayoutBinding? = null
    private val binding get() = _binding!!

    private val selectionViewModel: SelectionViewModel by activityViewModels()
    private val dishesViewModel: DishesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DishesLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerDishes.layoutManager = LinearLayoutManager(requireContext())

        selectionViewModel.selectedDishType.observe(viewLifecycleOwner) { type ->
            dishesViewModel.getDishesForType(type.id).observe(viewLifecycleOwner) { dishes ->
                binding.recyclerDishes.adapter = DishAdapter(dishes) { dish ->
                    selectionViewModel.setDish(dish)
                    findNavController().navigate(
                        R.id.action_dishesFragment_to_dishDisplayPageFragment
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