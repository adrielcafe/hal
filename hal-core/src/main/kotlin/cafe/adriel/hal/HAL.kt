package cafe.adriel.hal

import cafe.adriel.hal.HAL.Action
import cafe.adriel.hal.HAL.State
import kotlin.coroutines.CoroutineContext
import kotlin.reflect.KProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

        fun observe(stateFlow: Flow<S>)
    }

    private val context by lazy {
        HALContext(scope, dispatcher, _state)
    }

    private val _state by lazy {
        MutableStateFlow(initialState)
    }

    val state by lazy {
        _state.asStateFlow()
    }

    infix fun observeState(observer: StateObserver<S>) =
        observer.observe(_state)

    infix fun emit(action: A) {
        scope.launch(dispatcher) {
            context.reducer(action, state.value)
        }
    }

    operator fun getValue(thisRef: StateMachine<A, S>, property: KProperty<*>): HAL<A, S> = this
}
