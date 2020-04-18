package cafe.adriel.hal.sample.network

import cafe.adriel.hal.HAL
import cafe.adriel.hal.sample.network.model.Post

sealed class NetworkAction : HAL.Action {
    object LoadPosts : NetworkAction()
}

data class NetworkState(
    val posts: List<Post> = emptyList(),
    val loading: Boolean = false,
    val error: Throwable? = null
) : HAL.State
