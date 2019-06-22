package cafe.adriel.hal.observer

import cafe.adriel.hal.HAL.State
import cafe.adriel.hal.HAL.StateObserver
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach

class ChannelStateObserver<S : State>(
    private val bufferCapacity: Int = Channel.RENDEZVOUS,
    override val observer: (S) -> Unit
) : StateObserver<S> {

    private val channel by lazy { Channel<S>(bufferCapacity) }

    override suspend fun update(newState: S) = channel.send(newState)

    // TODO Will become experimental in 1.3.0
    @ObsoleteCoroutinesApi
    override suspend fun start() = channel.consumeEach { observer(it) }

    override fun stop() = channel.cancel()
}
