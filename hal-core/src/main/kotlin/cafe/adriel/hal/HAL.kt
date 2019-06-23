package cafe.adriel.hal

import cafe.adriel.hal.HAL.Action
import cafe.adriel.hal.HAL.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty

class HAL<A : Action, S : State> (
    private val scope: CoroutineScope,
    private val initialState: S,
    private val reducer: suspend (action: A, transitionTo: (S) -> Unit) -> Unit
) {

    private val stateChannel by lazy { Channel<S>(Channel.UNLIMITED) }

    private lateinit var stateObserver: StateObserver<S>

    var currentState = initialState

    init {
        scope.launch {
            stateChannel.consumeEach { state ->
                currentState = state
                stateObserver.transitionTo(currentState)
            }
        }
    }

    fun observe(observer: StateObserver<S>) {
        scope.launch(Dispatchers.Default, CoroutineStart.ATOMIC) {
            stateObserver = observer
            stateChannel.send(initialState)
        }
    }

    fun emit(action: A) {
        scope.launch(Dispatchers.Default, CoroutineStart.ATOMIC) {
            reducer(action, stateChannel::offer)
        }
    }

    operator fun getValue(thisRef: StateMachine<A, S>, property: KProperty<*>) = this

    interface Action

    interface State

    interface StateObserver<S : State> {

        val observer: (S) -> Unit

        fun transitionTo(newState: S)
    }

    interface StateMachine<A : Action, S : State> {

        val hal: HAL<A, S>
    }
}
