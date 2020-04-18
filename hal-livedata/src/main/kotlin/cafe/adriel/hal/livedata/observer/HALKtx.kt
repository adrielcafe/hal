package cafe.adriel.hal.livedata.observer

import androidx.lifecycle.LifecycleOwner
import cafe.adriel.hal.HAL.Action
import cafe.adriel.hal.HAL.State
import cafe.adriel.hal.HAL.StateMachine

fun <S : State> StateMachine<out Action, S>.observeState(
    owner: LifecycleOwner,
    onStateChanged: (state: S) -> Unit
) =
    stateMachine observeState LiveDataStateObserver(owner, onStateChanged)
