package cafe.adriel.hal.sample.network

import cafe.adriel.hal.HAL

sealed class NetworkAction : HAL.Action {
    object LoadPosts : NetworkAction()
}

sealed class NetworkState : HAL.State {
    object Init : NetworkState()
    object Loading : NetworkState()
    data class PostsLoaded(val posts: List<String>) : NetworkState()
    data class Error(val message: String) : NetworkState()
}
