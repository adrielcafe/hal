package cafe.adriel.hal

import cafe.adriel.hal.util.FakeStateMachine
import cafe.adriel.hal.util.TestCoroutineScopeRule
import cafe.adriel.hal.util.TurnstileAction
import cafe.adriel.hal.util.TurnstileState
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.spyk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class StateMachineTest {

    @get:Rule
    val testScopeRule = TestCoroutineScopeRule()

    private lateinit var stateMachine: FakeStateMachine

    private val reducer = spyk(::stateReducer)

    @Before
    fun setup() {
        stateMachine = FakeStateMachine(testScopeRule, testScopeRule.dispatcher) { action, _ ->
            reducer(action, ::transitionTo)
        }
    }

    @Test
    fun `when start HAL then set the initial state`() {
        expectThat(stateMachine.currentState) isEqualTo TurnstileState.Locked
    }

    @Test
    fun `when emit one single action then transition to expected state`() {
        stateMachine += TurnstileAction.InsertCoin

        stateMachine.observeState(testScopeRule, testScopeRule.dispatcher) {}

        coVerify { reducer(TurnstileAction.InsertCoin, any()) }

        expectThat(stateMachine.currentState) isEqualTo TurnstileState.Unlocked
    }

    @Test
    fun `when emit multiples actions then transition to expected states`() {
        stateMachine += TurnstileAction.InsertCoin
        stateMachine += TurnstileAction.Push

        stateMachine.observeState(testScopeRule, testScopeRule.dispatcher) {}

        coVerifySequence {
            reducer(TurnstileAction.InsertCoin, any())
            reducer(TurnstileAction.Push, any())
        }

        expectThat(stateMachine.currentState) isEqualTo TurnstileState.Locked
    }

    private fun stateReducer(action: TurnstileAction, transitionTo: (TurnstileState) -> Unit) =
        when (action) {
            is TurnstileAction.InsertCoin -> transitionTo(TurnstileState.Unlocked)
            is TurnstileAction.Push -> transitionTo(TurnstileState.Locked)
        }
}
