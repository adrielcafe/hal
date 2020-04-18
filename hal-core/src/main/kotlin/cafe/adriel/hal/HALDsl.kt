package cafe.adriel.hal

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.SendChannel

@DslMarker
private annotation class HALDsl

@HALDsl
class HALContext<S : HAL.State>(
    val halScope: CoroutineScope,
    val halDispatcher: CoroutineContext,
    private val stateChannel: SendChannel<S>
) {

    fun transitionTo(state: S) {
        stateChannel.offer(state)
    }

    operator fun S.unaryPlus() = transitionTo(this)
}