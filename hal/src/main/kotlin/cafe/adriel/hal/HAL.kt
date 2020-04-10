package cafe.adriel.hal

import cafe.adriel.hal.HAL.Action
import cafe.adriel.hal.HAL.Buffer.UNLIMITED
import cafe.adriel.hal.HAL.State
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HAL<A : Action, S : State> (
    private val initialState: S,
    private val scope: CoroutineScope,
    private val dispatcher: CoroutineContext = Dispatchers.Default,
    buffer: Buffer = UNLIMITED,
    private val reducer: suspend StateMachineContext<S>.(action: A) -> Unit
) {

    interface Action

    interface State

    interface StateMachine<A : Action, S : State> {

        val stateMachine: HAL<A, S>
    }

    enum class Buffer(internal val value: Int) {
        UNLIMITED(Channel.UNLIMITED),
        RENDEZVOUS(Channel.RENDEZVOUS),
        CONFLATED(Channel.CONFLATED)
    }

    private val stateChannel by lazy {
        Channel<S>(buffer.value)
    }

    private val stateFlow by lazy {
        stateChannel.receiveAsFlow()
            .onEach { state -> currentState = state }
            .onStart { emit(initialState) }
            .flowOn(dispatcher)
    }

    var currentState = initialState
        private set

    fun handleState(handlerScope: CoroutineScope? = null, operation: suspend (value: S) -> Unit) {
        stateFlow
            .onEach(operation)
            .launchIn(handlerScope ?: scope)
    }

    fun emit(action: A) {
        scope.launch(dispatcher) {
            StateMachineContext(scope, dispatcher, stateChannel)
                .reducer(action)
        }
    }

    operator fun getValue(thisRef: StateMachine<A, S>, property: KProperty<*>): HAL<A, S> = this
}
