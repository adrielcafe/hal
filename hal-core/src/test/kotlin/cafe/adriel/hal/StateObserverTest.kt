package cafe.adriel.hal

import cafe.adriel.hal.observer.FlowStateObserver
import cafe.adriel.hal.util.CustomStateObserver
import cafe.adriel.hal.util.TestCoroutineScopeRule
import cafe.adriel.hal.util.TurnstileState
import io.mockk.coVerify
import io.mockk.spyk
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class StateObserverTest {

    @get:Rule
    val testScopeRule = TestCoroutineScopeRule()

    private lateinit var stateChannel: BroadcastChannel<TurnstileState>
    private lateinit var flowObserver: FlowStateObserver<TurnstileState>
    private lateinit var customObserver: CustomStateObserver<TurnstileState>

    private val flowListener = spyk(::flowOnStateChanged)
    private val customListener = spyk(::customOnStateChanged)

    @Before
    fun setup() {
        stateChannel = ConflatedBroadcastChannel()

        flowObserver = FlowStateObserver(
            testScopeRule,
            testScopeRule.dispatcher,
            flowListener
        ).apply {
            observe(stateChannel.openSubscription())
        }

        customObserver = CustomStateObserver(
            testScopeRule,
            customListener
        ).apply {
            observe(stateChannel.openSubscription())
        }
    }

    @Test
    fun `when channel emits a state then flow observer calls listener`() {
        stateChannel.offer(TurnstileState.Locked)

        coVerify { flowListener(TurnstileState.Locked) }
    }

    @Test
    fun `when channel emits a state then custom observer calls listener`() {
        stateChannel.offer(TurnstileState.Locked)

        coVerify { customListener(TurnstileState.Locked) }
    }

    private suspend fun flowOnStateChanged(state: TurnstileState) {
        // Do nothing
    }

    private fun customOnStateChanged(state: TurnstileState) {
        // Do nothing
    }
}
