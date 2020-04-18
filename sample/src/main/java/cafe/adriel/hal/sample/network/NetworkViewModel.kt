package cafe.adriel.hal.sample.network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.hal.HAL
import cafe.adriel.hal.sample.network.model.Post
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import kotlinx.coroutines.delay

class NetworkViewModel : ViewModel(), HAL.StateMachine<NetworkAction, NetworkState> {

    private val httpClient = HttpClient(Android) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    override val stateMachine by HAL(NetworkState(), viewModelScope) { action, state ->
        when (action) {
            is NetworkAction.LoadPosts -> {
                +state.copy(loading = true)

                // Extending the loading state
                delay(REQUEST_DELAY)

                try {
                    val posts = httpClient.get<List<Post>>(REQUEST_URL)
                    +state.copy(posts = posts)
                } catch (e: Throwable) {
                    +state.copy(error = e)
                }
            }
        }
    }

    companion object {
        private const val REQUEST_URL = "https://jsonplaceholder.typicode.com/posts"
        private const val REQUEST_DELAY = 1_000L
    }
}
