package cafe.adriel.hal.observer

import cafe.adriel.hal.HAL.State
import cafe.adriel.hal.HAL.StateObserver
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf

@FlowPreview
class FlowStateObserver<S : State>(
    override val observer: (S) -> Unit
) : StateObserver<S> {

    override suspend fun update(newState: S) = flowOf(newState).collect { observer(it) }

    override suspend fun start() { /* Do nothing */ }

    override fun stop() { /* Do nothing */ }
}
