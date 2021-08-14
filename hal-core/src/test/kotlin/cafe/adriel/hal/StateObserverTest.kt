package cafe.adriel.hal

import cafe.adriel.hal.observer.FlowStateObserver
import cafe.adriel.hal.util.CustomStateObserver
import cafe.adriel.hal.util.TestCoroutineScopeRule
import cafe.adriel.hal.util.TurnstileState
import io.mockk.coVerify
import io.mockk.spyk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

class StateObserverTest {

    @get:Rule
    val testScopeRule = TestCoroutineScopeRule()

    private lateinit var stateFlow: MutableStateFlow<TurnstileState>
    private lateinit var flowObserver: FlowStateObserver<TurnstileState>
    private lateinit var customObserver: CustomStateObserver<TurnstileState>

    private val flowListener = spyk(::flowOnStateChanged)
    private val customListener = spyk(::customOnStateChanged)

    @Before
    fun setup() {
        stateFlow = MutableStateFlow(TurnstileState.Locked)

        flowObserver = FlowStateObserver(
            testScopeRule,
            testScopeRule.dispatcher,
            flowListener
        ).apply {
            observe(stateFlow)
        }

        customObserver = CustomStateObserver(
            testScopeRule,
            customListener
        ).apply {
            observe(stateFlow)
        }
    }

    @Test
    @Ignore("Broke after update Coroutines, needs investigation")
    fun `when flow emits a state then flow observer calls listener`() {
        coVerify { flowListener(TurnstileState.Locked) }
    }

    @Test
    fun `when flow emits a state then custom observer calls listener`() {
        coVerify { customListener(TurnstileState.Locked) }
    }

    private suspend fun flowOnStateChanged(state: TurnstileState) {
        // Do nothing
    }

    private fun customOnStateChanged(state: TurnstileState) {
        // Do nothing
    }
}
