package cafe.adriel.hal

import cafe.adriel.hal.util.TurnstileState
import io.mockk.spyk
import io.mockk.verify
import org.junit.Test

class StateObserverTest {

    private val callback = spyk({ _: TurnstileState -> })
    private val observer = spyk(CallbackStateObserver(callback))

    @Test
    fun `when transition to the next state then invoke the callback function`() {
        observer.transitionTo(TurnstileState.Unlocked)

        verify { callback(TurnstileState.Unlocked) }
    }
}
