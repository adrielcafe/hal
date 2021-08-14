package cafe.adriel.hal

import cafe.adriel.hal.observer.FlowStateObserver
import cafe.adriel.hal.util.CustomStateObserver
import cafe.adriel.hal.util.TestCoroutineScopeRule
import cafe.adriel.hal.util.TurnstileState
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class StateObserverTest {

    @get:Rule
    val testScopeRule = TestCoroutineScopeRule()

    private lateinit var stateFlow: MutableStateFlow<TurnstileState>
    private lateinit var flowObserver: FlowStateObserver<TurnstileState>
    private lateinit var customObserver: CustomStateObserver<TurnstileState>

    private val flowListener = mockk<suspend (TurnstileState) -> Unit>(relaxed = true)
    private val customListener = mockk<(TurnstileState) -> Unit>(relaxed = true)

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
    fun `when flow emits a state then flow observer calls listener`() {
        coVerify { flowListener(TurnstileState.Locked) }
    }

    @Test
    fun `when flow emits a state then custom observer calls listener`() {
        coVerify { customListener(TurnstileState.Locked) }
    }
}
