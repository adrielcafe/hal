package cafe.adriel.hal

import cafe.adriel.hal.HAL.Action
import cafe.adriel.hal.HAL.State
import cafe.adriel.hal.HAL.StateMachine
import kotlinx.coroutines.CoroutineScope

val <S : State> StateMachine<out Action, S>.currentState: S
    get() = stateMachine.currentState

fun <S : State> StateMachine<out Action, S>.handleState(
    scope: CoroutineScope? = null,
    operation: suspend (value: S) -> Unit
) =
    stateMachine.handleState(scope, operation)

operator fun <A : Action> StateMachine<A, out State>.plusAssign(action: A) =
    stateMachine.emit(action)
