package cafe.adriel.hal.util

import cafe.adriel.hal.HAL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

class CustomStateObserver<S : HAL.State>(
    private val scope: CoroutineScope,
    private val onStateChanged: (S) -> Unit
) : HAL.StateObserver<S> {

    override fun observe(receiver: ReceiveChannel<S>) {
        scope.launch {
            receiver.consumeEach(onStateChanged)
        }
    }
}
