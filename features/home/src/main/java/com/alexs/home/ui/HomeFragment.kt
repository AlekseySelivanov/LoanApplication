package com.alexs.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.alexs.common.Routes
import com.alexs.common.launchWhenStarted
import com.alexs.domain.model.Resource
import com.alexs.domain.model.items.LoanItem
import com.alexs.home.HomeViewModel
import com.alexs.home.adapters.LoansListAdapter
import com.alexs.home.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by activityViewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val loansListAdapter by lazy(LazyThreadSafetyMode.NONE) {
        LoansListAdapter(
            onLoanClicked = { navigateToLoanScreen(loanId = it) }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupListeners()
        initViews()
        setupObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initViews() {
        binding.rvLoanList.adapter = loansListAdapter
    }

    private fun setupListeners() = with(binding) {
        fabApplyLoan.setOnClickListener { navigateToLoanScreen(loanId = -1) }
        swipeRefresh.setOnRefreshListener { viewModel.refreshLoansList() }
    }

    private fun setupObservers() {
        viewModel.loansList.onEach { resource ->
            when (resource) {
                is Resource.Error -> showError(resource.error)
                Resource.Loading -> showLoading()
                is Resource.Success -> showSuccess(resource.data)
                Resource.Idle -> showIdle()
            }
        }.launchWhenStarted(lifecycleScope)
    }

    private fun showSuccess(data: List<LoanItem>) = with(binding) {
        swipeRefresh.isRefreshing = false
        hintGroup.isVisible = data.isEmpty()

        loansListAdapter.submitList(data)
    }

    private fun showIdle() = with(binding) {
        swipeRefresh.isRefreshing = false
        hintGroup.isVisible = false
    }

    private fun showError(error: String) = with(binding) {
        swipeRefresh.isRefreshing = false
        if (loansListAdapter.itemCount == 0) hintGroup.isVisible = true
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading() {
        binding.swipeRefresh.isRefreshing = true
    }

    private fun navigateToLoanScreen(loanId: Int) {
        findNavController().navigate(Routes.loanRoute(loanId).toUri())
    }

}