package cafe.adriel.hal.livedata.observer

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.liveData
import androidx.lifecycle.observe
import cafe.adriel.hal.HAL
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect

class LiveDataStateObserver<S : HAL.State>(
    private val owner: LifecycleOwner,
    private val onStateChanged: (S) -> Unit
) : HAL.StateObserver<S> {

    override fun observe(stateFlow: Flow<S>) {
        liveData<S> {
            stateFlow.collect(::emit)
        }.observe(owner, onStateChanged)
    }
}
