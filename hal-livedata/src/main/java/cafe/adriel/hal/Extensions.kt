package cafe.adriel.hal

import androidx.lifecycle.LifecycleOwner
import cafe.adriel.hal.HAL.Action
import cafe.adriel.hal.HAL.State
import cafe.adriel.hal.HAL.StateMachine
import cafe.adriel.hal.observer.LiveDataStateObserver

fun <S : State> StateMachine<out Action, S>.observeState(
    lifecycleOwner: LifecycleOwner,
    callback: (S) -> Unit
) =
    hal.observe(LiveDataStateObserver(lifecycleOwner, callback))
