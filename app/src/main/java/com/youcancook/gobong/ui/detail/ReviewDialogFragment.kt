package com.youcancook.gobong.ui.detail

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.youcancook.gobong.databinding.DialogReviewBinding

class ReviewDialogFragment(val onDismissListener: (Int) -> Unit) : DialogFragment() {
    private var _binding: DialogReviewBinding? = null
    private val binding get() = _binding!!
    private var starCount = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        createView()
        return MaterialAlertDialogBuilder(requireContext())
            .setView(binding.root)
            .create()
    }

    private fun createView() {
        _binding = DialogReviewBinding.inflate(requireActivity().layoutInflater)
            .apply {
                closeButton.setOnClickListener {
                    dismiss()
                }

                val stars = listOf(star1, star2, star3, star4, star5)

                for ((index, star) in stars.withIndex()) {
                    star.setOnClickListener {
                        setStar(index + 1)
                    }
                }

                reviewButton.setOnClickListener {
                    onDismissListener(starCount)
                    dismiss()
                }
            }
        showStar()

    }

    fun setOldStar(count: Int) {
        starCount = count
    }

    private fun setStar(count: Int) {
        starCount = count
        showStar()
    }

    private fun showStar() {
        binding.run {
            val stars = listOf(star1, star2, star3, star4, star5)
            stars.forEach { it2 -> it2.isSelected = false }

            for (i in 0 until starCount) {
                stars[i].isSelected = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}