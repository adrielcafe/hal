package cafe.adriel.hal.sample.network

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import cafe.adriel.hal.observeState
import cafe.adriel.hal.plusAssign
import cafe.adriel.hal.sample.R
import cafe.adriel.hal.sample.network.model.Post
import kotlinx.android.synthetic.main.activity_network.*

class NetworkActivity : AppCompatActivity(R.layout.activity_network) {

    private val viewModel by viewModels<NetworkViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vLoadPosts.setOnClickListener {
            viewModel += NetworkAction.LoadPosts
        }

        viewModel.observeState(lifecycleScope) { state ->
            showPosts(state.posts)
            setLoading(state.loading)
            state.error?.let(::showError)
        }
    }

    private fun showPosts(posts: List<Post>) {
        vPosts.text = posts
            .mapIndexed { i, post -> "${i + 1}. ${post.title}" }
            .joinToString("\n\n")
    }

    private fun setLoading(loading: Boolean) {
        vPosts.isVisible = loading.not()
        vLoading.isVisible = loading
    }

    private fun showError(error: Throwable) {
        Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
    }
}
