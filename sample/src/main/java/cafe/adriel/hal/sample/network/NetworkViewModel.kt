package cafe.adriel.hal.sample.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.hal.HAL
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitStringResult
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import org.json.JSONArray

class NetworkViewModel : ViewModel(), HAL.StateMachine<NetworkAction, NetworkState> {

    override val hal by HAL(viewModelScope, NetworkState.Init, ::reducer)

    private suspend fun reducer(action: NetworkAction, transitionTo: (NetworkState) -> Unit) =
        when (action) {
            is NetworkAction.LoadPosts -> {
                transitionTo(NetworkState.Loading)

                // Extending the loading state
                delay(REQUEST_DELAY)

                Fuel.get(REQUEST_URL)
                    .awaitStringResult()
                    .fold(
                        success = { json ->
                            val posts = getPostsFromJson(json)
                            transitionTo(NetworkState.PostsLoaded(posts))
                        },
                        failure = { error ->
                            transitionTo(NetworkState.Error(error.localizedMessage))
                        }
                    )
            }
        }

    private suspend fun getPostsFromJson(json: String): List<String> = coroutineScope {
        val postsJsonArray = JSONArray(json)
        val posts = mutableListOf<String>()

        (0 until postsJsonArray.length()).forEach { i ->
            val postTitle = postsJsonArray
                .getJSONObject(i)
                .getString("title")
                .capitalize()
            posts.add(postTitle)
        }

        posts
    }

    companion object {
        private const val REQUEST_URL = "https://jsonplaceholder.typicode.com/posts"
        private const val REQUEST_DELAY = 1000L
    }
}
