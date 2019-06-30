package cafe.adriel.hal

import cafe.adriel.hal.HAL.Action
import cafe.adriel.hal.HAL.State
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty

class HAL<A : Action, S : State> (
    private val scope: CoroutineScope,
    private val initialState: S,
    bufferCapacity: Int = Channel.UNLIMITED,
    private val reducerDispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val reducer: suspend (action: A, transitionTo: (S) -> Unit) -> Unit
) {

    private val stateChannel by lazy { Channel<S>(bufferCapacity) }

    private lateinit var stateObserver: StateObserver<S>

    lateinit var currentState: S
        private set

    fun observe(observer: StateObserver<S>) {
        stateObserver = observer
        stateChannel.offer(initialState)

        scope.launch {
            stateChannel.consumeEach { state ->
                currentState = state.also(stateObserver::transitionTo)
            }
        }
    }

    fun emit(action: A) {
        if (::stateObserver.isInitialized.not()) {
            throw UninitializedPropertyAccessException("The state observer must be initialized before invoking emit()")
        }

        scope.launch(reducerDispatcher) {
            reducer(action) { stateChannel.offer(it) }
        }
    }

    operator fun getValue(thisRef: StateMachine<A, S>, property: KProperty<*>) = this

    interface Action

    interface State

    interface StateObserver<S : State> {

        val callback: (S) -> Unit

        fun transitionTo(newState: S)
    }

    interface StateMachine<A : Action, S : State> {

        val hal: HAL<A, S>
    }
}
