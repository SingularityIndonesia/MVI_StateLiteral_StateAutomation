Author: [Stefanus Ayudha](https://github.com/stefanusayudha) <br>
©January2024 stefanus.ayudha@gmail.com

# MVI + Literal State + State Automation PATTERN
This is sample android project for a Pattern Guideline.

This project promote MVI + Literal State + State Automation Pattern designed by me.
This pattern targetting simplicity and less side effect. The idea of this pattern is:

1. Use intent to comunicate with viewmodel instead of triggering direct action via viewModel's APIs.
2. ViewModel as abstract presentation. It means that view model is the ultimate source of truth of what we see in the presentation.
3. State Automation: Every state in the viewmodel must thinking on it self. It cannot changed by using side effect. The state collecting information by them self and decide whether they should change or not - by it self.
4. Literal State: It means that state in viewmodel must not requires extra process to be represented to the screen.

## ALSO:
6. Fine graining setup. every component on the screen must be set individualy and separately. example:

INSTEAD OF DOING:
```kotlin

fun initData {
  .. side effect to button
  .. side effect to the form
  .. etc
}

fun initObserver {
  .. apply to button
  .. apply to the form
  .. etc
}

fun initUI {
  .. apply to button
  .. apply to the form
  .. etc
}

fun initAction {
  .. observe on the form -> side effect to the button
  .. observe on the button -> side effect to ....
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
