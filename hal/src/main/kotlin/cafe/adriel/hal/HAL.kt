package cafe.adriel.hal

import cafe.adriel.hal.HAL.Action
import cafe.adriel.hal.HAL.State
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HAL<A : Action, S : State> (
    private val initialState: S,
    private val scope: CoroutineScope,
    private val dispatcher: CoroutineContext = Dispatchers.Default,
    private val reducer: suspend HALContext<S>.(action: A, state: S) -> Unit
) {

    interface Action

    interface State

    interface StateMachine<A : Action, S : State> {
        val stateMachine: HAL<A, S>
    }

    private val stateMachineContext by lazy {
        HALContext(scope, dispatcher, stateChannel)
    }

    private val stateChannel by lazy {
        ConflatedBroadcastChannel(initialState)
    }

    val currentState: S
        get() = stateChannel.value

    fun collectState(
        handlerScope: CoroutineScope?,
        handlerDispatcher: CoroutineContext?,
        operation: suspend (state: S) -> Unit
    ) {
        stateChannel
            .asFlow()
            .onEach(operation)
            .flowOn(handlerDispatcher ?: Dispatchers.Main)
            .launchIn(handlerScope ?: scope)
    }

    fun emit(action: A) {
        scope.launch(dispatcher) {
            stateMachineContext.reducer(action, currentState)
        }
    }

    operator fun getValue(thisRef: StateMachine<A, S>, property: KProperty<*>): HAL<A, S> = this
}
