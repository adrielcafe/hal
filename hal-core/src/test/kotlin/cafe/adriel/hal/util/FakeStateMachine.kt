package cafe.adriel.hal.util

import cafe.adriel.hal.HAL
import cafe.adriel.hal.HALContext
import io.mockk.spyk
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope

class FakeStateMachine(
    scope: TestCoroutineScope,
    dispatcher: TestCoroutineDispatcher,
    reducer: suspend HALContext<TurnstileState>.(TurnstileAction, TurnstileState) -> Unit
) : HAL.StateMachine<TurnstileAction, TurnstileState> {

    override val stateMachine by spyk(
        HAL<TurnstileAction, TurnstileState>(
            initialState = TurnstileState.Locked,
            scope = scope,
            dispatcher = dispatcher,
            reducer = reducer
        )
    )
}

sealed class TurnstileAction : HAL.Action {
    object InsertCoin : TurnstileAction()
    object Push : TurnstileAction()
}

sealed class TurnstileState : HAL.State {
    object Locked : TurnstileState()
    object Unlocked : TurnstileState()
}
