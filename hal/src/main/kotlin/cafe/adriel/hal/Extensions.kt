package cafe.adriel.hal

import cafe.adriel.hal.HAL.Action
import cafe.adriel.hal.HAL.State
import cafe.adriel.hal.HAL.StateMachine
import kotlinx.coroutines.flow.Flow

val <S : State> StateMachine<out Action, S>.currentState: S
    get() = stateMachine.currentState

fun <S : State> StateMachine<out Action, S>.observeState(): Flow<S> =
    stateMachine.observe()

operator fun <A : Action> StateMachine<A, out State>.plusAssign(action: A) =
    stateMachine.emit(action)
