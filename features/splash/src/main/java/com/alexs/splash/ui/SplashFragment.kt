package com.alexs.splash.ui

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.alexs.common.Routes
import com.alexs.common.launchWhenStarted
import com.alexs.domain.model.Resource
import com.alexs.splash.SplashViewModel
import com.alexs.splash.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private val viewModel: SplashViewModel by viewModels()

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.isAuthorized.onEach { resource ->
            when (resource) {
                is Resource.Success -> navigateToScreen(request = Routes.HOME)
                is Resource.Error -> navigateToScreen(request = Routes.AUTHORIZATION)
                else -> Unit
            }
        }.launchWhenStarted(lifecycleScope)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun navigateToScreen(request: String) {
        binding.animationView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) = Unit

            override fun onAnimationEnd(animation: Animator?) = Unit

            override fun onAnimationCancel(animation: Animator?) = Unit

            override fun onAnimationRepeat(animation: Animator?) {
                NavDeepLinkRequest.Builder
                    .fromUri(request.toUri())
                    .build()
                    .also { navRequest ->
                        findNavController().navigate(navRequest)
                    }
            }
        })
    }

}