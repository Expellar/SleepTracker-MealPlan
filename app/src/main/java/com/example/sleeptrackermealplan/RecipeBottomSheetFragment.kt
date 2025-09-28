package com.example.sleeptrackermealplan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sleeptrackermealplan.databinding.BottomSheetRecipeBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RecipeBottomSheetFragment : BottomSheetDialogFragment() {

    private var _binding: BottomSheetRecipeBinding? = null
    private val binding get() = _binding!!

    // Companion object to create a new instance of the fragment with arguments
    companion object {
        const val TAG = "RecipeBottomSheet"
        private const val ARG_RECIPE_NAME = "recipe_name"
        private const val ARG_RECIPE_DETAILS = "recipe_details"

        fun newInstance(recipeName: String, recipeDetails: String): RecipeBottomSheetFragment {
            val args = Bundle()
            args.putString(ARG_RECIPE_NAME, recipeName)
            args.putString(ARG_RECIPE_DETAILS, recipeDetails)
            val fragment = RecipeBottomSheetFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the arguments and set the text
        arguments?.let {
            binding.textRecipeBottomSheetTitle.text = it.getString(ARG_RECIPE_NAME)
            binding.textRecipeBottomSheetDetails.text = it.getString(ARG_RECIPE_DETAILS)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
