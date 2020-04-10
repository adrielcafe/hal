package cafe.adriel.hal.sample.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.hal.HAL
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.coroutines.awaitStringResult
import kotlinx.coroutines.delay
import org.json.JSONArray

class NetworkViewModel : ViewModel(), HAL.StateMachine<NetworkAction, NetworkState> {

    override val stateMachine by HAL(NetworkState.Init, viewModelScope) { action ->
        when (action) {
            is NetworkAction.LoadPosts -> {
                +NetworkState.Loading
dispatcher
                // Extending the loading state
                delay(REQUEST_DELAY)

                Fuel.get(REQUEST_URL)
                    .awaitStringResult()
                    .fold(
                        success = { json ->
                            val posts = getPostsFromJson(json)
                            +NetworkState.PostsLoaded(posts)
                        },
                        failure = { error ->
                            +NetworkState.Error(error.localizedMessage)
                        }
                    )
            }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun getPostsFromJson(json: String): List<String> {
        val postsJsonArray = JSONArray(json)

        return buildList {
            (0 until postsJsonArray.length()).forEach { i ->
                val postTitle = postsJsonArray
                    .getJSONObject(i)
                    .getString("title")
                    .capitalize()
                add(postTitle)
            }
        }
    }

    companion object {
        private const val REQUEST_URL = "https://jsonplaceholder.typicode.com/posts"
        private const val REQUEST_DELAY = 1000L
    }
}
