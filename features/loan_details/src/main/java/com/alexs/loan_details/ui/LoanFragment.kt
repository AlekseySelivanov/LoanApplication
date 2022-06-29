package com.alexs.loan_details.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.alexs.common.launchWhenStarted
import com.alexs.common.updateText
import com.alexs.domain.model.items.LoanItem
import com.alexs.loan_details.LoanViewModel
import com.alexs.loan_details.R
import com.alexs.loan_details.databinding.FragmentLoanBinding
import com.alexs.loan_details.model.LoanFormEvent
import com.alexs.loan_details.model.LoanFormState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class LoanFragment : Fragment() {

    @Inject
    lateinit var loanViewModelFactory: LoanViewModel.LoanViewModelFactory.Factory

    private var _binding: FragmentLoanBinding? = null
    private val binding get() = _binding!!

    private val loanId by lazy { arguments?.getInt("loanId") ?: -1 }

    private val loanViewModel: LoanViewModel by viewModels {
        loanViewModelFactory.create(loanId = loanId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoanBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupListeners()
        setupObservers()
    }

    private fun setupListeners() = with(binding) {
        materialToolbar.setNavigationOnClickListener { findNavController().popBackStack() }

        tieFirstName.doAfterTextChanged {
            loanViewModel.onLoanFormEvent(LoanFormEvent.FirstNameChanged(it.toString()))
        }

        tieLastName.doAfterTextChanged {
            loanViewModel.onLoanFormEvent(LoanFormEvent.LastNameChanged(it.toString()))
        }

        tiePhoneNumber.doAfterTextChanged {
            loanViewModel.onLoanFormEvent(LoanFormEvent.PhoneChanged(it.toString()))
        }

        tieAmount.doAfterTextChanged {
            loanViewModel.onLoanFormEvent(LoanFormEvent.AmountChanged(it.toString()))
        }

        btnApply.setOnClickListener { loanViewModel.onLoanFormEvent(LoanFormEvent.Apply) }
    }

    private fun setupObservers() {
        loanViewModel.loanRequestEventChannel.onEach { event ->
            when (event) {
                LoanViewModel.LoanRequestEvent.Idle -> showIdle()
                LoanViewModel.LoanRequestEvent.Loading -> showLoading()
                LoanViewModel.LoanRequestEvent.Success -> showSuccess()
                is LoanViewModel.LoanRequestEvent.LoanLoaded -> showLoan(event.data)
                is LoanViewModel.LoanRequestEvent.Error -> showError(event.message)
            }
        }.launchWhenStarted(lifecycleScope)

        loanViewModel.loanConditions.onEach { conditions ->
            with(binding) {
                tvLoanPercent.text =
                    getString(R.string.percent_template, conditions.percent.toString())
                tilAmount.hint = conditions.maxAmount.toString()
                tieUntil.setText(conditions.endDate)
            }
        }.launchWhenStarted(lifecycleScope)

        loanViewModel.loanFormState.onEach { state ->
            updateFormState(state)
        }.launchWhenStarted(lifecycleScope)
    }

    private fun showLoan(data: LoanItem) = with(binding) {
        disableInput()

        tvLoanPercent.text = getString(R.string.percent_template, data.percent.toString())

        tieFirstName.setText(data.firstName)
        tieLastName.setText(data.lastName)
        tiePhoneNumber.setText(data.phoneNumber)
        tieAmount.setText(data.amount.toString())
        tieUntil.setText(data.until)

        showIdle()
    }

    private fun updateFormState(state: LoanFormState) = with(binding) {
        tieFirstName.updateText(state.firstName)
        tieLastName.updateText(state.lastName)
        tiePhoneNumber.updateText(state.phoneNumber)
        tieAmount.updateText(state.loanAmount)

        tilFirstName.error = state.firstNameError
        tilLastName.error = state.lastNameError
        tilPhoneNumber.error = state.phoneNumberError
        tilAmount.error = state.loanAmountError
    }

    private fun disableInput() = with(binding) {
        tieFirstName.isEnabled = false
        tieLastName.isEnabled = false
        tiePhoneNumber.isEnabled = false
        tieAmount.isEnabled = false
        tilAmount.isHintEnabled = false

        btnApply.isEnabled = false
    }

    private fun showError(errorMessage: String) = with(binding) {
        progressIndicator.hide()
        groupLoanForm.isVisible = false

        tvErrorMessage.isVisible = true
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading() = with(binding) {
        tvErrorMessage.isVisible = false
        groupLoanForm.isVisible = false

        progressIndicator.show()
    }

    private fun showIdle() = with(binding) {
        progressIndicator.hide()
        tvErrorMessage.isVisible = false

        groupLoanForm.isVisible = true
    }

    private fun showSuccess() {
        binding.groupLoanForm.isVisible = true
        binding.progressIndicator.hide()

        val successFragment = SuccessFragment.newInstance()
        successFragment.show(childFragmentManager, "success_fragment")
    }

}