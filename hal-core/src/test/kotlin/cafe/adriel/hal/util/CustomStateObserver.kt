package cafe.adriel.hal.util

import cafe.adriel.hal.HAL
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CustomStateObserver<S : HAL.State>(
    private val scope: CoroutineScope,
    private val onStateChanged: (S) -> Unit
) : HAL.StateObserver<S> {

    override fun observe(stateFlow: Flow<S>) {
        scope.launch {
            stateFlow.collect { onStateChanged(it) }
        }
    }
}
