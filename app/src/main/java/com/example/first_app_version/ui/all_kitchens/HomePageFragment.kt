package com.example.first_app_version.ui.all_kitchens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.first_app_version.R
import com.example.first_app_version.data.models.Kitchen
import com.example.first_app_version.databinding.HomePageLayoutBinding
import com.example.first_app_version.ui.KitchenViewModel

class HomePageFragment : Fragment() {

    private var _binding: HomePageLayoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: KitchenViewModel by activityViewModels()
    private var kitchensCache: List<Kitchen> = emptyList()

    private lateinit var adapter: HomeCategoriesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = HomePageLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = HomeCategoriesAdapter(
            categories = emptyList(),
            onCategoryClick = { category ->
                if (kitchensCache.isEmpty()) {
                    Toast.makeText(requireContext(), "הנתונים עדיין נטענים, נסי שוב בעוד רגע", Toast.LENGTH_SHORT).show()
                    return@HomeCategoriesAdapter
                }

                val selectedKitchen = kitchensCache.firstOrNull { it.name == category.title }
                if (selectedKitchen == null) {
                    Toast.makeText(requireContext(), "לא נמצא מטבח בשם ${category.title} בדאטהבייס", Toast.LENGTH_SHORT).show()
                    return@HomeCategoriesAdapter
                }

                viewModel.setKitchen(selectedKitchen)
                findNavController().navigate(R.id.action_homePageFragment_to_dishesTypesFragment)
            }
        )

        binding.homeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.homeRecyclerView.adapter = adapter

        viewModel.kitchens.observe(viewLifecycleOwner) { list ->
            kitchensCache = list ?: emptyList()
        }

        viewModel.categories.observe(viewLifecycleOwner) { cats ->
            adapter.submitList(cats ?: emptyList())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}