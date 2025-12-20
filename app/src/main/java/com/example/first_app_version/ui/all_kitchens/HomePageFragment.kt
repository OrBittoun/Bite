package com.example.first_app_version.ui.all_kitchens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.first_app_version.R
import com.example.first_app_version.databinding.HomePageLayoutBinding
import com.example.first_app_version.ui.KitchenViewModel
import kotlin.getValue

class HomePageFragment : Fragment() {
    private var _binding : HomePageLayoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel : KitchenViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = HomePageLayoutBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.kitchens?.observe(viewLifecycleOwner) {

            binding.recyclerKitchens.adapter = KitchenAdapter(it, object : KitchenAdapter.KitchenListener {

                override fun onKitchenClicked(index: Int) {
                    val item = (binding.recyclerKitchens.adapter as KitchenAdapter).kitchenAt(index)

                    viewModel.setKitchen(item)
                    findNavController().navigate(
                        R.id.action_homePageFragment_to_dishesTypesFragment,
                    )
                }

                override fun onKitchenLongClicked(index: Int) {
                    val item = (binding.recyclerKitchens.adapter as KitchenAdapter).kitchenAt(index)

                    viewModel.setKitchen(item)
                    findNavController().navigate(
                        R.id.action_homePageFragment_to_dishesTypesFragment,
                    )
                }

            })
            binding.recyclerKitchens.layoutManager = LinearLayoutManager(requireContext())

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}