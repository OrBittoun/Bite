package com.example.first_app_version.ui.all_kitchens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.first_app_version.R
import com.example.first_app_version.databinding.HomePageLayoutBinding
import com.example.first_app_version.ui.KitchenViewModel
import com.example.first_app_version.ui.HomeCategoriesAdapter
import com.example.first_app_version.ui.HomeCategory
import com.google.android.material.appbar.AppBarLayout

class HomePageFragment : Fragment() {

    private var _binding: HomePageLayoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: KitchenViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomePageLayoutBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val categories = listOf(
            HomeCategory(
                title = "Italian",
                previewImages = listOf(
                    R.drawable.pizza,
                    R.drawable.pasta,
                    R.drawable.lasagna
                )
            ),
            HomeCategory(
                title = "Asian",
                previewImages = listOf(
                    R.drawable.sushi,
                    R.drawable.noodles,
                    R.drawable.ramen
                )
            ),
            HomeCategory(
                title = "Vegan",
                previewImages = listOf(
                    R.drawable.salad,
                    R.drawable.vegan_burger,
                    R.drawable.smoothie
                )
            )
        )

        val adapter = HomeCategoriesAdapter(
            categories = categories,
            onCategoryClick = { category ->



                findNavController().navigate(
                    R.id.action_homePageFragment_to_dishesTypesFragment
                )
            }
        )

        binding.homeRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        binding.homeRecyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
