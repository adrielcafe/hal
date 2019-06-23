package cafe.adriel.hal

import cafe.adriel.hal.HAL.Action
import cafe.adriel.hal.HAL.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlin.reflect.KProperty

class HAL<A : Action, S : State> (
    private val scope: CoroutineScope,
    private val initialState: S,
    private val reducer: suspend (action: A, transitionTo: suspend (S) -> Unit) -> Unit
) {

    // TODO Replace by BroadcastChannel and add support to multiple observers
    private val stateChannel by lazy { Channel<S>(Channel.UNLIMITED) }

    private lateinit var stateObserver: StateObserver<S>

    lateinit var currentState: S

    fun observe(observer: StateObserver<S>) {
        scope.launch {
            stateObserver = observer.apply {
                start()
            }
            stateChannel.apply {
                send(initialState)
                consumeEach { newState ->
                    currentState = newState
                    stateObserver.transitionTo(currentState)
                }
            }
        }.invokeOnCompletion {
            stateObserver.stop()
        }
    }

    fun emit(action: A) {
        scope.launch(Dispatchers.Default) {
            reducer(action, stateChannel::send)
        }
    }

    operator fun getValue(thisRef: StateMachine<A, S>, property: KProperty<*>) = this

    interface Action

    interface State

    interface StateObserver<S : State> {

        val observer: (S) -> Unit

        suspend fun transitionTo(newState: S)

        suspend fun start()

        fun stop()
    }

    interface StateMachine<A : Action, S : State> {

        val hal: HAL<A, S>
    }
}
