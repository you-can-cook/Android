package com.youcancook.gobong.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.youcancook.gobong.ui.GoBongApplication

abstract class GoBongFragment<
        T : ViewDataBinding,
        >(@LayoutRes private val layoutRes: Int) :
    Fragment() {

    private var _binding: T? = null
    val binding get() = _binding!!
    val  appContainer by lazy{
        (requireActivity().application as GoBongApplication).container
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}