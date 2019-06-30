package cafe.adriel.hal

import cafe.adriel.hal.HAL.State
import cafe.adriel.hal.HAL.StateObserver

class CallbackStateObserver<S : State>(
    override val callback: (S) -> Unit
) : StateObserver<S> {

    override fun transitionTo(newState: S) = callback(newState)
}
