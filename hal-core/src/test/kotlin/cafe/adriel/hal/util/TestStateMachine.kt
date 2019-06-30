package cafe.adriel.hal.util

import cafe.adriel.hal.HAL
import io.mockk.spyk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

class TestStateMachine(
    scope: CoroutineScope,
    dispatcher: CoroutineDispatcher
) : HAL.StateMachine<TurnstileAction, TurnstileState> {

    override val hal by spyk(
        HAL(scope, TurnstileState.Locked, reducerDispatcher = dispatcher, reducer = ::reducer)
    )

    private suspend fun reducer(action: TurnstileAction, transitionTo: (TurnstileState) -> Unit) =
        when (action) {
            is TurnstileAction.InsertCoin -> transitionTo(TurnstileState.Unlocked)
            is TurnstileAction.Push -> transitionTo(TurnstileState.Locked)
        }
}

sealed class TurnstileAction : HAL.Action {
    object InsertCoin : TurnstileAction()
    object Push : TurnstileAction()
}

sealed class TurnstileState : HAL.State {
    object Locked : TurnstileState()
    object Unlocked : TurnstileState()
}
