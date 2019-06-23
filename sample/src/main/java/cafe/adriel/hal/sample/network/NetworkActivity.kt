package cafe.adriel.hal.sample.network

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import cafe.adriel.hal.livedata.observeState
import cafe.adriel.hal.plus
import cafe.adriel.hal.sample.R
import kotlinx.android.synthetic.main.activity_network.*

class NetworkActivity : AppCompatActivity() {

    private val viewModel by viewModels<NetworkViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network)

        vLoadPosts.setOnClickListener {
            viewModel + NetworkAction.LoadPosts
        }

        viewModel.observeState(this) { state ->
            when (state) {
                is NetworkState.Init -> {
                    setLoading(false)
                    clearPosts()
                }
                is NetworkState.PostsLoaded -> {
                    setLoading(false)
                    showPosts(state.posts)
                }
                is NetworkState.Loading -> setLoading(true)
                is NetworkState.Error -> {
                    setLoading(false)
                    clearPosts()
                    showError(state.message)
                }
            }
        }
    }

    private fun showPosts(posts: List<String>) {
        vPosts.text = posts
            .mapIndexed { i, post -> "${i + 1}. $post" }
            .joinToString("\n\n")
    }

    private fun clearPosts() {
        vPosts.text = ""
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun setLoading(loading: Boolean) {
        if (loading) {
            vPosts.visibility = View.GONE
            vLoading.visibility = View.VISIBLE
        } else {
            vPosts.visibility = View.VISIBLE
            vLoading.visibility = View.GONE
        }
    }
}
