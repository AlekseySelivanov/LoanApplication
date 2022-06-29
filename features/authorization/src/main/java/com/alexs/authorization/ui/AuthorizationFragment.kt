package com.alexs.authorization.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.alexs.authorization.AuthenticationViewModel
import com.alexs.authorization.adapters.AuthenticationPagerAdapter
import com.alexs.authorization.databinding.FragmentAuthorizationBinding
import com.alexs.common.Routes
import com.alexs.common.launchWhenStarted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.onEach

@FlowPreview
@AndroidEntryPoint
class AuthorizationFragment : Fragment() {

    private val viewModel: AuthenticationViewModel by viewModels()

    private var _binding: FragmentAuthorizationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthorizationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initPager()
        setupListeners()
        setupObservers()
    }

    private fun initPager() {
        binding.authPager.adapter = AuthenticationPagerAdapter(
            fragmentManager = childFragmentManager,
            lifecycle = lifecycle
        )
        binding.authPager.isUserInputEnabled = false
    }

    private fun setupListeners() {
        viewModel.selectedPage.onEach { page ->
            binding.authPager.setCurrentItem(page, true)
        }.launchWhenStarted(lifecycleScope)
    }

    private fun setupObservers() {
        viewModel.authorizationEventChannel
            .onEach { event ->
                when (event) {
                    is AuthenticationViewModel.AuthorizationEvent.Success -> openHomeScreen()
                    is AuthenticationViewModel.AuthorizationEvent.Error -> showError(event.error)
                    AuthenticationViewModel.AuthorizationEvent.Idle -> showIdle()
                    AuthenticationViewModel.AuthorizationEvent.Loading -> showLoading()
                }
            }
            .launchWhenStarted(lifecycleScope)
    }

    private fun showLoading() {
        binding.authPager.isEnabled = false

        binding.progressIndicator.show()
    }

    private fun showIdle() {
        binding.authPager.isEnabled = true

        binding.progressIndicator.hide()
    }

    private fun showError(error: String) {
        showIdle()

        Toast.makeText(
            requireContext(),
            error,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun openHomeScreen() {
        NavDeepLinkRequest.Builder
            .fromUri(Routes.HOME.toUri())
            .build()
            .also { navRequest ->
                findNavController().navigate(navRequest)
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}