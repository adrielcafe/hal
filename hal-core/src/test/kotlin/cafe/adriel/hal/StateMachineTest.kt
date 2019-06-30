package cafe.adriel.hal

import cafe.adriel.hal.util.TestStateMachine
import cafe.adriel.hal.util.TurnstileAction
import cafe.adriel.hal.util.TurnstileState
import io.mockk.coVerify
import io.mockk.spyk
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo

class StateMachineTest {

    private val testScope = TestCoroutineScope()

    private val observer = CallbackStateObserver<TurnstileState> { }
    private val reducer = spyk(::suspendReducer)
    private val stateMachine = TestStateMachine(reducer)

    @After
    fun tearDown() {
        testScope.cleanupTestCoroutines()
    }

    @Test
    fun `when set the observer with a StateObserver instance then set the initial state`() = runBlockingTest {
        stateMachine.observeState(observer)

        expectThat(stateMachine.currentState).isEqualTo(TurnstileState.Locked)
    }

    @Test
    fun `when set the observer with a lambda then set the initial state`() = runBlockingTest {
        stateMachine.observeState { }

        expectThat(stateMachine.currentState).isEqualTo(TurnstileState.Locked)
    }

    @Test
    fun `when emit an action then transition to the next state`() = runBlockingTest {
        stateMachine.observeState(observer)

        stateMachine + TurnstileAction.InsertCoin

        coVerify { reducer(TurnstileAction.InsertCoin, any()) }

        expectThat(stateMachine.currentState).isEqualTo(TurnstileState.Unlocked)
    }

    @Test
    fun `when emit an action without set the observer then throw exception`() = runBlockingTest {
        expectThrows<UninitializedPropertyAccessException> {
            stateMachine + TurnstileAction.InsertCoin
        }
    }

    @Test
    fun `when get the current state without set the observer then throw exception`() = runBlockingTest {
        expectThrows<UninitializedPropertyAccessException> {
            expectThat(stateMachine.currentState).isEqualTo(TurnstileState.Unlocked)
        }
    }

    private suspend fun suspendReducer(action: TurnstileAction, transitionTo: (TurnstileState) -> Unit) =
        when (action) {
            is TurnstileAction.InsertCoin -> transitionTo(TurnstileState.Unlocked)
            is TurnstileAction.Push -> transitionTo(TurnstileState.Locked)
        }
}
