package com.singularityindonesia.android.mviliteralstatestateautomation.screens.search1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.singularityindonesia.android.mviliteralstatestateautomation.databinding.Screen1Binding
import com.singularityindonesia.android.mviliteralstatestateautomation.util.collect
import com.singularityindonesia.android.mviliteralstatestateautomation.util.viewBinding

/**
 * Created by : Stefanus Ayudha Junior
 * Mailto : stefanus.ayudha@gmail.com
 * Created at : Friday 1/26/24 : 8:57 AM
 **/
class Screen1 : Fragment() {

    private val binding: Screen1Binding by viewBinding()
    private val vm: Screen1ViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupInputEmail()
        setupInputPassword()
        setupSubmitButton()
        setupErrorToast()
    }


    private fun setupInputEmail() {
        // set default state
        // ... no default state

        // Set state listener
        // The case for EditText, we cannot handle its text state due to EditText is a stateful widget.
        // All we can do is only listen to its text change event, and save that state to the viewmodel.
        binding.form.edEmail.doOnTextChanged { text, _, _, _ ->
            vm.SendIntent(
                Screen1ViewModel.Intent.UpdateEmailState(
                    email = text.toString()
                )
            )
        }

        // State Observer
        // Normally we will submit the viewModel's state to the edit text, so the source of truth is still the viewmodel.
        // But for the case of EditText, since it is a StateFull widget - doing that will cause overlapping state for the displaying text.
        // Also, resetting the text on the EditText will re emit onTextChanged event, which will cause infinite loop.
        // ex:
        // collect(
        //     state = vm.Email,
        //     collector = { email ->
        //         binding.form.edEmail.setText(email)
        //     }
        // )

        // Email Validation
        // With Literal State, we don't need to process it, just display it right away.
        collect(
            state = vm.EmailError,
            collector = { emailValidation ->
                binding.form.tlEmail.error = emailValidation
            }
        )
    }

    private fun setupInputPassword() {
        // set default state
        // ... no default state

        // Set state listener
        // The case for EditText, we cannot handle its text state due to EditText is a stateful widget.
        // All we can do is only listen to its text change event, and save that state to the viewmodel.
        binding.form.edPassword.doOnTextChanged { text, _, _, _ ->
            vm.SendIntent(
                Screen1ViewModel.Intent.UpdatePasswordState(
                    password = text.toString()
                )
            )
        }

        // State Observer
        // Normally we will submit the viewModel's state to the edit text, so the source of truth is still the viewmodel.
        // But for the case of EditText, since it is a StateFull widget - doing that will cause overlapping state for the displaying text.
        // Also, resetting the text on the EditText will re emit onTextChanged event, which will cause infinite loop.
        // ex:
        // collect(
        //     state = vm.Password,
        //     collector = { password ->
        //         binding.form.edPassword.setText(password)
        //     }
        // )

        // Password Validation
        // With Literal State, we don't need to process it, just display it right away.
        collect(
            state = vm.PasswordError,
            collector = { passwordValidation ->
                binding.form.tlPassword.error = passwordValidation
            }
        )
    }

    private fun setupSubmitButton() {
        // set default state
        // ... no default state

        // event handler
        binding.btnSubmit.setOnClickListener {
            vm.SendIntent(
                Screen1ViewModel.Intent.SubmitData()
            )
        }

        // state observing
        // with literal state, we don't need to process it, just display it right away.
        collect(
            state = vm.SubmitButtonState,
            collector = { submitButtonState ->
                binding.btnSubmit
                    .apply {
                        isEnabled = submitButtonState.enable
                        text = if (submitButtonState.loading) "Loading..." else "Submit"
                        isClickable = submitButtonState.enable && !submitButtonState.loading
                    }

            }
        )
    }

    private fun setupErrorToast() {
        // set default state
        // ... no default state

        // state observing
        // with literal state, we don't need to process it, just display it right away.
        collect(
            state = vm.ErrorToast,
            collector = { errorToast ->
                Toast.makeText(
                    requireContext(),
                    errorToast,
                    Toast.LENGTH_SHORT
                ).show()
            }
        )
    }
}