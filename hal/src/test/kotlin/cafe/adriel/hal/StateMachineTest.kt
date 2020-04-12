package cafe.adriel.hal

import cafe.adriel.hal.util.FakeStateMachine
import cafe.adriel.hal.util.TurnstileAction
import cafe.adriel.hal.util.TurnstileState
import io.mockk.coVerify
import io.mockk.spyk
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class StateMachineTest {

    private val reducer = spyk(::suspendReducer)

    @Test
    fun `when start HAL then set the initial state`() = runBlockingTest {
        val stateMachine = FakeStateMachine(this, reducer)

        expectThat(stateMachine.currentState) isEqualTo TurnstileState.Locked
    }

    @Test
    fun `when emit one single action then transition to the next state`() = runBlockingTest {
        val stateMachine = FakeStateMachine(this, reducer)

        stateMachine += TurnstileAction.InsertCoin

        stateMachine.collectState().launchIn(this)

        coVerify { reducer(TurnstileAction.InsertCoin, any()) }
        expectThat(stateMachine.currentState) isEqualTo TurnstileState.Unlocked
    }

    private suspend fun suspendReducer(action: TurnstileAction, transitionTo: (TurnstileState) -> Unit) =
        when (action) {
            is TurnstileAction.InsertCoin -> transitionTo(TurnstileState.Unlocked)
            is TurnstileAction.Push -> transitionTo(TurnstileState.Locked)
        }
}
