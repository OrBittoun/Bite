package com.example.first_app_version.ui.all_dishes_types

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.first_app_version.R
import com.example.first_app_version.data.models.DishType
import com.example.first_app_version.data.models.DishTypeForRetro
import com.example.first_app_version.databinding.DishesTypesLayoutBinding
import com.example.first_app_version.ui.all_kitchens.KitchenViewModel
import com.example.first_app_version.ui.all_kitchens.SelectionViewModel
import com.example.first_app_version.ui.api_data.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DishesTypesFragment : Fragment() {

    private var _binding: DishesTypesLayoutBinding? = null
    private val binding get() = _binding!!

    private val kitchenViewModel: KitchenViewModel by activityViewModels()
    private val dishesTypesViewModel: DishesTypesViewModel by viewModels()
    private val selectionViewModel: SelectionViewModel by activityViewModels()

    // הוספת ה-ViewModel שאחראי על קריאות הרשת
    private val categoryViewModel: CategoryViewModel by activityViewModels()

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

        adapter = DishTypeAdapter { dishType ->
            // ניתוב חכם: אם זה מטבח API הולכים למסך API, אחרת למסך רגיל
            if (dishType.kitchenId == 6) {
                // התיקון לשגיאה הראשונה: שימוש בפונקציה האמיתית של ה-API
                categoryViewModel.fetchMealDetails(dishType.id.toString())
                findNavController().navigate(R.id.action_dishesTypesFragment_to_apiDishDetailsFragment)
            } else {
                // התיקון לשגיאות 2 ו-3: שימוש בשמות הפרמטרים כדי למנוע בלבול בסדר שלהם
                val localDishType = DishType(
                    id = dishType.id,
                    kitchenId = dishType.kitchenId,
                    name = dishType.name,
                    imageRes = dishType.imageRes as? Int
                )
                selectionViewModel.setDishType(localDishType)
                findNavController().navigate(R.id.action_dishesTypesFragment_to_dishesFragment)
            }
        }

        binding.recyclerDishesTypes.layoutManager = GridLayoutManager(requireContext(), 1)
        binding.recyclerDishesTypes.adapter = adapter

        kitchenViewModel.chosenKitchen.observe(viewLifecycleOwner) { kitchen ->
            if (kitchen == null) {
                Toast.makeText(requireContext(), R.string.no_kitchen_found, Toast.LENGTH_SHORT).show()
                adapter.submitList(emptyList())
                return@observe
            }

            // לוגיקת החלוקה: API לעומת Room
            if (kitchen.id == 6) {
                categoryViewModel.meals.observe(viewLifecycleOwner) { mealList ->
                    if (!mealList.isNullOrEmpty()) {
                        // המרת הנתונים מהאינטרנט למודל המשותף של המסך
                        val uiList = mealList.take(8).map { meal ->
                            DishTypeForRetro(
                                id = meal.idMeal.toInt(),
                                name = meal.strMeal,
                                kitchenId = 6,
                                imageRes = meal.strMealThumb
                            )
                        }
                        adapter.submitList(uiList)
                    } else {
                        Toast.makeText(requireContext(), "No categories found from API", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                dishesTypesViewModel.getDishTypesForKitchen(kitchen.id).observe(viewLifecycleOwner) { dishTypes ->
                    if (dishTypes.isEmpty()) {
                        Toast.makeText(requireContext(), R.string.no_dish_types_found, Toast.LENGTH_SHORT).show()
                    }
                    // המרת הנתונים מה-Room למודל המשותף של המסך
                    val uiList = dishTypes.map { dbType ->
                        DishTypeForRetro(
                            id = dbType.id,
                            name = dbType.name,
                            kitchenId = dbType.kitchenId,
                            imageRes = dbType.imageRes ?: R.drawable.default_dish
                        )
                    }
                    adapter.submitList(uiList)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}