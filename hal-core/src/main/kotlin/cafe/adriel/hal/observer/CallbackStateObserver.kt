package cafe.adriel.hal.observer

import cafe.adriel.hal.HAL.State
import cafe.adriel.hal.HAL.StateObserver

class CallbackStateObserver<S : State>(
    override val observer: (S) -> Unit
) : StateObserver<S> {

    override suspend fun update(newState: S) = observer(newState)

    override suspend fun start() { /* Do nothing */ }

    override fun stop() { /* Do nothing */ }
}
