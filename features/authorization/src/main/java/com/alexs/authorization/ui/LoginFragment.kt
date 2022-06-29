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
import com.alexs.authorization.databinding.FragmentLoginBinding
import com.alexs.authorization.model.AuthenticationFormState
import com.alexs.authorization.model.LoginFormEvent
import com.alexs.common.launchWhenStarted
import com.alexs.common.updateText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private val viewModel: AuthenticationViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupListeners()
        setupObservers()
    }

    private fun setupListeners() = with(binding) {
        tieUserName.doAfterTextChanged {
            viewModel.onLoginFormEvent(LoginFormEvent.NameChanged(it.toString()))
        }

        tieUserPassword.doAfterTextChanged {
            viewModel.onLoginFormEvent(LoginFormEvent.PasswordChanged(it.toString()))
        }

        btnLogin.setOnClickListener { viewModel.onLoginFormEvent(LoginFormEvent.Submit) }

        tvSignUp.setOnClickListener {
            viewModel.onLoginFormEvent(LoginFormEvent.RegistrationSelected)
        }
    }

    private fun setupObservers() {
        viewModel.loginState.onEach { state ->
            updateFormState(state)
        }.launchWhenStarted(lifecycleScope)
    }

    private fun updateFormState(state: AuthenticationFormState) = with(binding) {
        tieUserName.updateText(state.fullName)
        tieUserPassword.updateText(state.password)

        tilUserName.error = state.fullNameError
        tilUserPassword.error = state.passwordError
    }

}