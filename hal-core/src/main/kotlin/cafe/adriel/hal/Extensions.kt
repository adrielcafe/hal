package cafe.adriel.hal

import cafe.adriel.hal.HAL.Action
import cafe.adriel.hal.HAL.State
import cafe.adriel.hal.HAL.StateMachine
import cafe.adriel.hal.HAL.StateObserver

fun <S : State> StateMachine<out Action, S>.observe(observer: StateObserver<S>) =
    hal.observe(observer)

operator fun <A : Action> StateMachine<A, out State>.plus(action: A) =
    hal.emit(action)
