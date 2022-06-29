package com.alexs.loan_details.ui

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.alexs.loan_details.databinding.BottomSheetSuccessBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SuccessFragment : BottomSheetDialogFragment() {

    companion object {
        fun newInstance() = SuccessFragment()
    }

    private var _binding: BottomSheetSuccessBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetSuccessBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initViews()
    }

    private fun initViews() {
        isCancelable = false

        with(binding) {
            successAnimation.addAnimatorListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) = Unit

                override fun onAnimationEnd(animation: Animator?) {
                    btnOpenHomePage.animate().alpha(1f)
                    btnOpenMap.animate().alpha(1f)

                    setupListeners()
                }

                override fun onAnimationCancel(animation: Animator?) = Unit

                override fun onAnimationRepeat(animation: Animator?) = Unit
            })
        }
    }

    private fun setupListeners() = with(binding) {
        btnOpenHomePage.setOnClickListener {
            findNavController().popBackStack()
        }

        btnOpenMap.setOnClickListener {
            findNavController().popBackStack()
            val intent = Intent(
                Intent.ACTION_VIEW,
                "https://www.google.com/maps/search/?api=1&query=sber".toUri()
            )
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}