package cafe.adriel.hal.observer

import cafe.adriel.hal.HAL.State
import cafe.adriel.hal.HAL.StateObserver

class CallbackStateObserver<S : State>(
    override val observer: (S) -> Unit
) : StateObserver<S> {

    override fun transitionTo(newState: S) = observer(newState)
}
