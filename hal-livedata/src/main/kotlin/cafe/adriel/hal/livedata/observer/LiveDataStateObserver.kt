package cafe.adriel.hal.livedata.observer

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import cafe.adriel.hal.HAL.State
import cafe.adriel.hal.HAL.StateObserver

class LiveDataStateObserver<S : State>(
    lifecycleOwner: LifecycleOwner,
    override val observer: (S) -> Unit
) : StateObserver<S> {

    private val liveData by lazy { MutableLiveData<S>() }

    init {
        liveData.observe(lifecycleOwner, Observer(observer))
    }

    override fun transitionTo(newState: S) {
        liveData.value = newState
    }
}