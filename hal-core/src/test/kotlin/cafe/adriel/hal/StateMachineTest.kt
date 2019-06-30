package cafe.adriel.hal

import cafe.adriel.hal.util.TestStateMachine
import cafe.adriel.hal.util.TurnstileAction
import cafe.adriel.hal.util.TurnstileState
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Test
import strikt.api.expectThat
import strikt.api.expectThrows
import strikt.assertions.isEqualTo

class StateMachineTest {

    private val testScope = TestCoroutineScope()
    private val testDispatcher = TestCoroutineDispatcher()

    private val observer = CallbackStateObserver<TurnstileState> { }
    private val stateMachine = TestStateMachine(testScope, testDispatcher)

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
}
