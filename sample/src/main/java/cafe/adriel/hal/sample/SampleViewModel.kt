package cafe.adriel.hal.sample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.hal.HAL
import kotlinx.coroutines.delay

class SampleViewModel : ViewModel(), HAL.StateMachine<SampleAction, SampleState> {

    override val hal by HAL(viewModelScope, ::reducer)

    private suspend fun reducer(action: SampleAction): SampleState =
        when (action) {
            is SampleAction.OnButtonClicked -> {
                val message = loadMessage()
                SampleState.MessageLoaded(message)
            }
        }

    private suspend fun loadMessage(): String {
        delay(ACTION_DELAY) // Simulating an IO operation
        return "Hello HAL!"
    }

    companion object {
        private const val ACTION_DELAY = 500L
    }
}
