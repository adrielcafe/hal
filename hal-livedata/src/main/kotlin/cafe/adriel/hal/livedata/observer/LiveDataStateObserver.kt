package cafe.adriel.hal.livedata.observer

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.liveData
import androidx.lifecycle.observe
import cafe.adriel.hal.HAL
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach

class LiveDataStateObserver<S : HAL.State>(
    private val owner: LifecycleOwner,
    private val onStateChanged: (S) -> Unit
) : HAL.StateObserver<S> {

    override fun observe(receiver: ReceiveChannel<S>) {
        liveData {
            receiver.consumeEach { emit(it) }
        }.observe(owner, onStateChanged)
    }
}
