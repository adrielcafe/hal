package cafe.adriel.hal

import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow

@DslMarker
private annotation class HALDsl

@HALDsl
class HALContext<S : HAL.State>(
    val halScope: CoroutineScope,
    val halDispatcher: CoroutineContext,
    private val stateFlow: MutableStateFlow<S>
) {

    fun transitionTo(state: S) {
        stateFlow.value = state
    }

    operator fun S.unaryPlus() = transitionTo(this)
}
