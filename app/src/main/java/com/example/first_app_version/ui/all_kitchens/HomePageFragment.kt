//package com.example.first_app_version.ui.all_kitchens
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.activityViewModels
//import androidx.navigation.fragment.findNavController
//import androidx.recyclerview.widget.LinearLayoutManager
//import com.example.first_app_version.R
//import com.example.first_app_version.databinding.HomePageLayoutBinding
//import com.example.first_app_version.ui.KitchenViewModel
//import com.example.first_app_version.data.models.HomeCategory
//import com.example.first_app_version.data.models.Kitchen
//
//class HomePageFragment : Fragment() {
//
//    private var _binding: HomePageLayoutBinding? = null
//    private val binding get() = _binding!!
//
//    private val viewModel: KitchenViewModel by activityViewModels()
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = HomePageLayoutBinding.inflate(inflater, container, false)
//
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//
//        val categories = listOf(
//            HomeCategory(
//                title = "Italian",
//                previewImages = listOf(
//                    R.drawable.pizza,
//                    R.drawable.pasta,
//                    R.drawable.lasagna
//                )
//            ),
//            HomeCategory(
//                title = "Asian",
//                previewImages = listOf(
//                    R.drawable.sushi,
//                    R.drawable.noodles,
//                    R.drawable.ramen
//                )
//            ),
//            HomeCategory(
//                title = "Vegan",
//                previewImages = listOf(
//                    R.drawable.salad,
//                    R.drawable.vegan_burger,
//                    R.drawable.smoothie
//                )
//            )
//        )
//
//        val adapter = HomeCategoriesAdapter(
//            categories = categories,
//            onCategoryClick = { category ->
//
//                // 🔴 זה היה חסר – בחירת מטבח
//                viewModel.setKitchen(
//                    Kitchen(
//                        id = category.title.hashCode(), // זמני, רק כדי שיהיה id
//                        name = category.title
//                    )
//                )
//
//                // ניווט למסך הבא
//                findNavController().navigate(
//                    R.id.action_homePageFragment_to_dishesTypesFragment
//                )
//            }
//        )
//
////        val adapter = HomeCategoriesAdapter(
////            categories = categories,
////            onCategoryClick = { category ->
////                viewModel.selectKitchenByTitle(category.title) // או שם דומה אצלך
////                findNavController().navigate(R.id.action_homePageFragment_to_dishesTypesFragment)
////            }
////
//////            onCategoryClick = { category ->
//////
//////                findNavController().navigate(
//////                    R.id.action_homePageFragment_to_dishesTypesFragment
//////                )
//////            }
////        )
//
//        binding.homeRecyclerView.layoutManager =
//            LinearLayoutManager(requireContext())
//
//        binding.homeRecyclerView.adapter = adapter
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}

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
import com.example.first_app_version.data.models.HomeCategory
import com.example.first_app_version.data.models.Kitchen
import com.example.first_app_version.databinding.HomePageLayoutBinding
import com.example.first_app_version.ui.KitchenViewModel

class HomePageFragment : Fragment() {

    private var _binding: HomePageLayoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: KitchenViewModel by activityViewModels()

    // נשמור כאן את רשימת המטבחים מה-DB כדי שלא נתקל ב-null בזמן לחיצה
    private var kitchensCache: List<Kitchen> = emptyList()

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
                    R.drawable.napoli_pizza,
                    R.drawable.pasta_alfredo,
                    R.drawable.lasagna_eggplant_parmesan
                )
            ),
            HomeCategory(
                title = "Asian",
                previewImages = listOf(
                    R.drawable.sushi_california_roll,
                    R.drawable.ramen_tonkotsu,
                    R.drawable.dim_sum_beef_dumplings
                )
            ),

            HomeCategory(
                title = "Meat & Fish",
                previewImages = listOf(
                    R.drawable.meat_goulash,
                    R.drawable.meat_classic_burger,
                    R.drawable.meat_entrecote,
                    R.drawable.fish_teriyaki
                )
            ),

            HomeCategory(
                title = "Vegan",
                previewImages = listOf(
                    R.drawable.vegan_chickpea_salad,
                    R.drawable.vegan_stuffed_zucchini,
                    R.drawable.vegan_buddha_bowl
                )
            ),

            HomeCategory(
                title = "Desserts",
                previewImages = listOf(
                    R.drawable.cake,
                    R.drawable.cookies,
                    R.drawable.smoothie
                )
            )


        )

        val adapter = HomeCategoriesAdapter(
            categories = categories,
            onCategoryClick = { category ->

                // ודאי שהמידע מה-DB כבר נטען
                if (kitchensCache.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "הנתונים עדיין נטענים, נסי שוב בעוד רגע",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@HomeCategoriesAdapter
                }

                // למצוא Kitchen אמיתי לפי השם (כדי לקבל id אמיתי מה-DB)
                val selectedKitchen = kitchensCache.firstOrNull { it.name == category.title }

                if (selectedKitchen == null) {
                    Toast.makeText(
                        requireContext(),
                        "לא נמצא מטבח בשם ${category.title} בדאטהבייס",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@HomeCategoriesAdapter
                }

                // ⭐ זה החלק שהיה חסר אצלך
                viewModel.setKitchen(selectedKitchen)

                // ניווט למסך סוגי המנות
                findNavController().navigate(
                    R.id.action_homePageFragment_to_dishesTypesFragment
                )
            }
        )

        binding.homeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.homeRecyclerView.adapter = adapter

        // טעינת המטבחים מה-DB לתוך cache
        viewModel.kitchens?.observe(viewLifecycleOwner) { list ->
            kitchensCache = list ?: emptyList()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
