package cafe.adriel.hal

import cafe.adriel.hal.HAL.Action
import cafe.adriel.hal.HAL.State
import cafe.adriel.hal.HAL.StateMachine
import cafe.adriel.hal.HAL.StateObserver
import cafe.adriel.hal.observer.FlowStateObserver
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

val <S : State> StateMachine<out Action, S>.currentState: S
    get() = stateMachine.currentState

fun <S : State> StateMachine<out Action, S>.observeState(stateObserver: StateObserver<S>) =
    stateMachine observeState stateObserver

fun <S : State> StateMachine<out Action, S>.observeState(
    scope: CoroutineScope,
    dispatcher: CoroutineContext = Dispatchers.Main,
    onStateChanged: suspend (state: S) -> Unit
) =
    stateMachine observeState FlowStateObserver(scope, dispatcher, onStateChanged)

infix fun <A : Action> StateMachine<A, out State>.emit(action: A) =
    stateMachine emit action

operator fun <A : Action> StateMachine<A, out State>.plusAssign(action: A) =
    emit(action)
