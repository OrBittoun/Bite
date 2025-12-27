package com.example.first_app_version.ui.all_dishes

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
    ): View {
        _binding = DishesLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ⭐ מאזינים לסוג המנה שנבחר
        selectionViewModel.selectedDishType.observe(viewLifecycleOwner) { dishType ->

            if (dishType == null) {
                Log.e("DishesFragment", "DishType is null!")
                Toast.makeText(
                    requireContext(),
                    "לא נבחר סוג מנה",
                    Toast.LENGTH_SHORT
                ).show()
                return@observe
            }

            Log.d("DishesFragment", "Loading dishes for DishType: ${dishType.name} (ID: ${dishType.id})")

            // ⭐ טוענים מנות לפי ה-dishType
            dishesViewModel.getDishesForType(dishType.id)
                .observe(viewLifecycleOwner) { dishes ->

                    if (dishes.isNullOrEmpty()) {
                        Log.w("DishesFragment", "No dishes found for type: ${dishType.name}")
                        Toast.makeText(
                            requireContext(),
                            "אין מנות זמינות",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@observe
                    }

                    Log.d("DishesFragment", "Loaded ${dishes.size} dishes")

                    // ⭐ יוצרים adapter עם לחיצה על מנה
                    val adapter = DishAdapter(dishes) { clickedDish ->
                        try {
                            Log.d("DishesFragment", "Dish clicked: ${clickedDish.name} (ID: ${clickedDish.id})")

                            // שומרים את ה-ID של המנה
                            selectionViewModel.setDishId(clickedDish.id)

                            // עוברים למסך המנה
                            findNavController().navigate(
                                R.id.action_dishesFragment_to_dishDisplayPageFragment
                            )
                        } catch (e: Exception) {
                            Log.e("DishesFragment", "Navigation error: ${e.message}", e)
                            Toast.makeText(
                                requireContext(),
                                "שגיאה בניווט",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    binding.recyclerDishes.layoutManager = LinearLayoutManager(requireContext())
                    binding.recyclerDishes.adapter = adapter
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}