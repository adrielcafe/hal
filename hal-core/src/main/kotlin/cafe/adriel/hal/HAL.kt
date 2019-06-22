package cafe.adriel.hal

import cafe.adriel.hal.HAL.Action
import cafe.adriel.hal.HAL.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.reflect.KProperty

class HAL<A : Action, S : State> (
    private val scope: CoroutineScope,
    private val reducer: suspend (A) -> S
) {

    private lateinit var observer: StateObserver<S>

    fun observe(newObserver: StateObserver<S>) {
        scope.launch {
            observer = newObserver
            observer.start()
        }.invokeOnCompletion {
            observer.stop()
        }
    }

    fun emit(action: A) {
        scope.launch {
            val newState = withContext(Dispatchers.Default) {
                reducer(action)
            }
            observer.update(newState)
        }
    }

    operator fun getValue(thisRef: StateMachine<A, S>, property: KProperty<*>): HAL<A, S> = this

    interface Action

    interface State

    interface StateObserver<S : State> {

        val observer: (S) -> Unit

        suspend fun update(newState: S)

        suspend fun start()

        fun stop()
    }

    interface StateMachine<A : Action, S : State> {

        val hal: HAL<A, S>
    }
}
