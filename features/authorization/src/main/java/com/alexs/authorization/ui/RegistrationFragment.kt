package com.alexs.authorization.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.alexs.authorization.AuthenticationViewModel
import com.alexs.authorization.databinding.FragmentRegistrationBinding
import com.alexs.authorization.model.AuthenticationFormState
import com.alexs.authorization.model.RegistrationFormEvent
import com.alexs.common.launchWhenStarted
import com.alexs.common.updateText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private val viewModel: AuthenticationViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupListeners()
        setupObservers()
    }

    private fun setupListeners() = with(binding) {
        tvLogin.setOnClickListener { viewModel.onRegistrationFormEvent(RegistrationFormEvent.LoginSelected) }

        tieUserName.doAfterTextChanged {
            viewModel.onRegistrationFormEvent(RegistrationFormEvent.NameChanged(it.toString()))
        }

        tieUserEmail.doAfterTextChanged {
            viewModel.onRegistrationFormEvent(RegistrationFormEvent.EmailChanged(it.toString()))
        }

        tieUserPhone.doAfterTextChanged {
            viewModel.onRegistrationFormEvent(RegistrationFormEvent.PhoneChanged(it.toString()))
        }

        tieUserPassword.doAfterTextChanged {
            viewModel.onRegistrationFormEvent(RegistrationFormEvent.PasswordChanged(it.toString()))
        }

        tieRepeatedPassword.doAfterTextChanged {
            viewModel.onRegistrationFormEvent(RegistrationFormEvent.RepeatedPasswordChanged(it.toString()))
        }

        ternsCheckbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.onRegistrationFormEvent(RegistrationFormEvent.TernsAgreed(isChecked))
        }

        btnLogin.setOnClickListener { viewModel.onRegistrationFormEvent(RegistrationFormEvent.Submit) }
    }

    private fun setupObservers() {
        viewModel.registrationState.onEach { state ->
            updateRegistrationState(state)
        }.launchWhenStarted(lifecycleScope)
    }

    private fun updateRegistrationState(state: AuthenticationFormState) = with(binding) {
        tieUserName.updateText(state.fullName)
        tieUserEmail.updateText(state.email)
        tieUserPhone.updateText(state.phoneNumber)
        tieUserPassword.updateText(state.password)
        tieRepeatedPassword.updateText(state.repeatedPassword)

        btnLogin.isEnabled = state.ternsAgreed

        tilUseName.error = state.fullNameError
        tilUserEmail.error = state.emailError
        tilUserPhone.error = state.phoneNumberError
        tilUserPassword.error = state.passwordError
        tilRepeatedPassword.error = state.repeatedPasswordError
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}