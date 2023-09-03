package com.youcancook.gobong.ui.search.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.youcancook.gobong.R
import com.youcancook.gobong.databinding.BottomSheetFrameBinding

class FilterToolBottomSheet : BottomSheetDialogFragment() {
    private var _binding: BottomSheetFrameBinding? = null
    private val binding: BottomSheetFrameBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = BottomSheetFrameBinding.inflate(inflater, container, false)
        childFragmentManager.beginTransaction().replace(
            R.id.bottomSheetFragment,
            FilterToolBottomFragment()
        ).commit()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "FilterBottomSheet"
    }
}