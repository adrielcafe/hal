package cafe.adriel.hal.sample.network

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import cafe.adriel.hal.observeState
import cafe.adriel.hal.plusAssign
import cafe.adriel.hal.sample.R
import kotlinx.android.synthetic.main.activity_network.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class NetworkActivity : AppCompatActivity() {

    private val viewModel by viewModels<NetworkViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_network)

        vLoadPosts.setOnClickListener {
            viewModel += NetworkAction.LoadPosts
        }

        lifecycleScope.launch {
            viewModel.observeState().collect { state ->
                resetState()

                when (state) {
                    is NetworkState.PostsLoaded -> showPosts(state.posts)
                    is NetworkState.Loading -> setLoading(true)
                    is NetworkState.Error -> showError(state.message)
                }
            }
        }
    }

    private fun resetState() {
        setLoading(false)
        clearPosts()
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
