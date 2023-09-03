package com.youcancook.gobong.ui.addRecipe.bottom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.chip.Chip
import com.youcancook.gobong.databinding.BottomSheetRecipeSearchBinding
import com.youcancook.gobong.ui.addRecipe.bottom.add.RecipeStepAddBottomViewModel

class RecipeStepSearchBottomFragment : Fragment() {

    private var _binding: BottomSheetRecipeSearchBinding? = null
    private val binding: BottomSheetRecipeSearchBinding get() = _binding!!
    private val recipeAddBottomViewModel: RecipeStepAddBottomViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = BottomSheetRecipeSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            vm = recipeAddBottomViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        binding.run {
            backArrowButton.setOnClickListener {
                parentFragmentManager.popBackStack()
            }

            searchView.setOnQueryTextListener(object : OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText ?: return true
                    recipeAddBottomViewModel.filterTools(newText)
                    return true
                }

            })

            allToolsGroup.setOnCheckedStateChangeListener { group, checkedIds ->
                val views = checkedIds.map {
                    group.findViewById<Chip>(it).text.toString()
                }
                recipeAddBottomViewModel.checkTools(views)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}