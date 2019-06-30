package cafe.adriel.hal

import cafe.adriel.hal.HAL.Action
import cafe.adriel.hal.HAL.State
import cafe.adriel.hal.HAL.StateMachine

val <S : State> StateMachine<out Action, S>.currentState: S
    get() = hal.currentState

fun <S : State> StateMachine<out Action, S>.observeState(callback: (S) -> Unit) =
    hal.observe(CallbackStateObserver(callback))

fun <S : State> StateMachine<out Action, S>.observeState(observer: HAL.StateObserver<S>) =
    hal.observe(observer)

operator fun <A : Action> StateMachine<A, out State>.plus(action: A) =
    hal.emit(action)
