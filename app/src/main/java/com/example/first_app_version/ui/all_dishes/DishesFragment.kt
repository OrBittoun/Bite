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
import com.example.first_app_version.ui.all_kitchens.SelectionViewModel
import com.example.first_app_version.data.models.Dish

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

        // האזנה למצב: האם אנחנו במועדפים או במצב רגיל?
        selectionViewModel.isFavoritesMode.observe(viewLifecycleOwner) { isFavorites ->
            if (isFavorites) {
                // אפשרות 1: אנחנו במצב מועדפים - טוענים מה-Repository (דרך ה-ViewModel)
                loadFavoriteDishes()
            } else {
                // אפשרות 2: אנחנו במצב רגיל - טוענים לפי סוג מנה (הקוד המקורי שלך)
                loadDishesByType()
            }
        }
    }

    private fun loadFavoriteDishes() {
        // כאן אנחנו קוראים ל-ViewModel שיביא את המועדפים מה-Room
        dishesViewModel.getFavoriteDishes().observe(viewLifecycleOwner) { dishes ->
            updateUI(dishes)
        }
    }

    private fun loadDishesByType() {
        selectionViewModel.selectedDishType.observe(viewLifecycleOwner) { dishType ->
            if (dishType == null) return@observe

            dishesViewModel.getDishesForType(dishType.id).observe(viewLifecycleOwner) { dishes ->
                updateUI(dishes)
            }
        }
    }

    private fun updateUI(dishes: List<Dish>?) {
        if (dishes.isNullOrEmpty()) {
            Log.w("DishesFragment", "No dishes to display")
            binding.recyclerDishes.adapter = null // או להציג הודעת "אין מנות"
            return
        }

        val adapter = DishAdapter(dishes) { clickedDish ->
            selectionViewModel.setDishId(clickedDish.id)
            // שימי לב לשם ה-Action ב-NavGraph שלך
            findNavController().navigate(R.id.action_dishesFragment_to_dishDisplayPageFragment)
        }

        binding.recyclerDishes.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerDishes.adapter = adapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}