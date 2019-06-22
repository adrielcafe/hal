package cafe.adriel.hal.observer

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import cafe.adriel.hal.HAL.State
import cafe.adriel.hal.HAL.StateObserver

class LiveDataStateObserver<S : State>(
    private val lifecycleOwner: LifecycleOwner,
    override val observer: (S) -> Unit
) : StateObserver<S> {

    private val liveData by lazy { MutableLiveData<S>() }

    override suspend fun update(newState: S) {
        liveData.value = newState
    }

    override suspend fun start() = liveData.observe(lifecycleOwner, Observer(observer))

    override fun stop() { /* Managed by lifecycle owner */ }
}
