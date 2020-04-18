package cafe.adriel.hal.observer

import cafe.adriel.hal.util.TestCoroutineScopeRule
import cafe.adriel.hal.util.TurnstileState
import io.mockk.coVerify
import io.mockk.spyk
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FlowStateObserverTest {

    @get:Rule
    val testScopeRule = TestCoroutineScopeRule()

    private lateinit var stateChannel: BroadcastChannel<TurnstileState>
    private lateinit var stateObserver: FlowStateObserver<TurnstileState>

    private val listener = spyk(::onStateChanged)

    @Before
    fun setup() {
        stateChannel = ConflatedBroadcastChannel()
        stateObserver = FlowStateObserver(testScopeRule, testScopeRule.dispatcher, listener).apply {
            observe(stateChannel.openSubscription())
        }
    }

    @Test
    fun `when channel emits a state then observer calls listener`() {
        stateChannel.offer(TurnstileState.Locked)

        coVerify { listener(TurnstileState.Locked) }
    }

    private suspend fun onStateChanged(state: TurnstileState) {
        // Do nothing
    }
}
