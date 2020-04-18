package cafe.adriel.hal

import cafe.adriel.hal.HAL.Action
import cafe.adriel.hal.HAL.State
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.ReceiveChannel
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

    interface StateObserver<S : State> {

        fun observe(receiver: ReceiveChannel<S>)
    }

    private val context by lazy {
        HALContext(scope, dispatcher, stateChannel)
    }

    private val stateChannel by lazy {
        ConflatedBroadcastChannel(initialState)
    }

    val currentState: S
        get() = stateChannel.value

    infix fun observeState(observer: StateObserver<S>) =
        observer.observe(stateChannel.openSubscription())

    infix fun emit(action: A) {
        scope.launch(dispatcher) {
            context.reducer(action, currentState)
        }
    }

    operator fun getValue(thisRef: StateMachine<A, S>, property: KProperty<*>): HAL<A, S> = this
}
