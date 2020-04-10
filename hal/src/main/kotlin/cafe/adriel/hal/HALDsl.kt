package cafe.adriel.hal

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.SendChannel

@DslMarker
private annotation class StateMachineDsl

@StateMachineDsl
class StateMachineContext<S : HAL.State>(
    val scope: CoroutineScope,
    val dispatcher: CoroutineContext,
    private val stateChannel: SendChannel<S>
) {

    fun transitionTo(state: S) {
        stateChannel.offer(state)
    }

    operator fun S.unaryPlus() = transitionTo(this)
}
