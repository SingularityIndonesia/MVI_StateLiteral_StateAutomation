Author: [Stefanus Ayudha](https://github.com/stefanusayudha) <br>
©January2024 stefanus.ayudha@gmail.com

# MVI + Literal State + State Automation PATTERN
This is sample android project for a Pattern Guideline.

This project promote MVI + Literal State + State Automation Pattern designed by me.
This pattern targetting simplicity and less side effect. The idea of this pattern is:

1. Use intent to comunicate with viewmodel instead of triggering direct action via viewModel's APIs. The purpose is to reduce interface coupling between modules with direct dependency like the ViewModel.
2. ViewModel as abstract presentation. It means that view model is the ultimate source of truth of what we see in the presentation. 
3. State Automation: Every state in the viewmodel must thinking on it self. It cannot changed by using side effect. The state collecting information by them self and decide whether they should change or not - by it self.
   ```kotlin
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
   ```
4. Literal State: It means that state in viewmodel must not requires extra process to be represented to the screen.
   A literal state is a representation of what you literally see on the screen.
   ```kotlin
   val ErrorToast = SubmitDataState
      .filter { it.contains("Error") }
      .flowOn(Dispatchers.IO)
   ```

## ALSO:
5. Fine graining setup. every component on the screen must be set individualy and separately. example:

    INSTEAD OF DOING:
    ```kotlin
    
    fun initData {
      .. side effect to button     // side effect to the button in multiple different places
      .. side effect to the form   // side effect to the form in multiple different places
      .. etc
    }
    
    fun initObserver {
      .. apply to button           // side effect to the button in multiple different places
      .. apply to the form         // side effect to the form in multiple different places
      .. etc
    }
    
    fun initUI {
      .. apply to button           // side effect to the button in multiple different places
      .. apply to the form         // side effect to the form in multiple different places
      .. etc
    }
    
    fun initAction {
      .. observe on the form -> side effect to the button   // side effect to the button in multiple different places
      .. observe on the button -> side effect to ....       // side effect to the form in multiple different places
    }
    ```
    
    WE DO:
    ```kotlin
    fun setupSubmitButton() {
      .. observe button -> send event / intent
      .. observe viewmodel state -> apply to button
    }
    
    fun setupInput1() {
      .. observe on input -> save state to viewmodel
      .. observe viewmodel state -> apply to input
    }
    
    .. etc
    ```
