package cafe.adriel.hal

import cafe.adriel.hal.HAL.Action
import cafe.adriel.hal.HAL.State
import cafe.adriel.hal.HAL.StateMachine
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope

val <S : State> StateMachine<out Action, S>.currentState: S
    get() = stateMachine.currentState

fun <S : State> StateMachine<out Action, S>.collectState(
    scope: CoroutineScope? = null,
    dispatcher: CoroutineContext? = null,
    operation: suspend (state: S) -> Unit
) =
    stateMachine.collectState(scope, dispatcher, operation)

fun <A : Action> StateMachine<A, out State>.emit(action: A) =
    stateMachine.emit(action)

operator fun <A : Action> StateMachine<A, out State>.plusAssign(action: A) =
    emit(action)
