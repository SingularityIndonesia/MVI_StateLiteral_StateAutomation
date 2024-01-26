package com.singularityindonesia.android.mviliteralstatestateautomation.screens.search1

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.singularityindonesia.android.mviliteralstatestateautomation.util.ButtonState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * Created by : Stefanus Ayudha Junior
 * Mailto : stefanus.ayudha@gmail.com
 * Created at : Friday 1/26/24 : 8:58 AM
 **/
class Screen1ViewModel : ViewModel() {

    sealed interface Intent {
        data class UpdateEmailState(
            val email: String
        ) : Intent

        data class UpdatePasswordState(
            val password: String
        ) : Intent

        data class SubmitData(
            val signature: String = System.currentTimeMillis().toString()
        ) : Intent
    }

    private val intent = MutableSharedFlow<Intent>(1)
    fun SendIntent(intent: Intent) {
        viewModelScope.launch {
            this@Screen1ViewModel.intent.emit(intent)
        }
    }

    val Email = intent
        .filterIsInstance<Intent.UpdateEmailState>()
        .flowOn(Dispatchers.IO)
        .scan("") { _, intent ->
            intent.email
        }

    val EmailValidation = Email
        .map { email ->
            if (email.isEmpty()) {
                return@map "Email must not be empty"
            }

            if (!email.matches(Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"))) {
                return@map "Email must be valid"
            }

            return@map ""
        }
        .flowOn(Dispatchers.IO)

    val EmailError = combine(
        Email,
        EmailValidation
    ) { email, emailValidation ->
        if (email.isBlank()) {
            return@combine ""
        }

        return@combine emailValidation
    }.flowOn(Dispatchers.IO)

    val Password = intent
        .filterIsInstance<Intent.UpdatePasswordState>()
        .flowOn(Dispatchers.IO)
        .scan("") { _, intent ->
            intent.password
        }


    val PasswordValidation = Password
        .map { password ->
            if (password.isEmpty()) {
                return@map "Password must not be empty"
            }

            if (password.length < 8) {
                return@map "Password must be at least 8 characters"
            }

            return@map ""
        }
        .flowOn(Dispatchers.IO)

    val PasswordError = combine(
        Password,
        PasswordValidation
    ) { password, passwordValidation ->
        if (password.isBlank()) {
            return@combine ""
        }

        return@combine passwordValidation
    }.flowOn(Dispatchers.IO)

    @ExperimentalCoroutinesApi
    val SubmitDataState =
        combine(
            intent.filterIsInstance<Intent.SubmitData>()
                .scan("Idle") { _, intent ->
                    intent.signature
                },
            Email,
            Password,
            EmailValidation,
            PasswordValidation
        ) { signature, email, password, emailIsValid, passwordIsValid ->
            val dataIsValid = emailIsValid.isBlank() && passwordIsValid.isBlank()
            email to password to dataIsValid to signature
        }
            .flowOn(Dispatchers.IO)
            // skip Idle state
            .filter { data ->
                data.second != "Idle"
            }
            // skip if signature is not changed
            .distinctUntilChanged { oldData, newData ->
                oldData.second == newData.second
            }
            // this operation will cancel automatically when new state comes
            .cancellable()
            .flatMapLatest { data ->
                flow {
                    if (!data.first.second) {
                        emit("Error: data invalid")
                        return@flow
                    }

                    emit("Loading")

                    try {
                        // emulate fake data operation delay
                        delay(3000)
                        emit("Error: Dummy operation timeout.")
                    } finally {
                        // wash the disc after using it
                    }

                }
            }
            // Adding Default state is a MUST!
            // Without default state, another state that using combine() will not emit anything until this state emit something.
            .scan("Idle") { prev, new ->
                new
            }

    val ErrorToast = SubmitDataState
        .filter { it.contains("Error") }
        .flowOn(Dispatchers.IO)

    val SubmitButtonState = combine(
        EmailValidation,
        PasswordValidation,
        SubmitDataState
    ) { emailIsValid, passwordIsValid, submitDataState ->
        val dataIsValid = emailIsValid.isBlank() && passwordIsValid.isBlank()
        Pair(dataIsValid, submitDataState)
    }.scan(
        ButtonState(enable = false) // default button state
    ) { prev, it ->
        val (dataIsValid, submitDataState) = it
        prev.copy(
            enable = dataIsValid,
            loading = submitDataState.contains("Loading")
        )
    }.flowOn(Dispatchers.IO)
}