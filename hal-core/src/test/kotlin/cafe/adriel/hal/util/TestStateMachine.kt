package cafe.adriel.hal.util

import cafe.adriel.hal.HAL
import io.mockk.spyk
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope

class TestStateMachine(
    reducer: suspend (TurnstileAction, (TurnstileState) -> Unit) -> Unit
) : HAL.StateMachine<TurnstileAction, TurnstileState> {

    override val hal by spyk(HAL<TurnstileAction, TurnstileState>(
        scope = TestCoroutineScope(),
        initialState = TurnstileState.Locked,
        reducerDispatcher = TestCoroutineDispatcher(),
        reducer = reducer)
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
