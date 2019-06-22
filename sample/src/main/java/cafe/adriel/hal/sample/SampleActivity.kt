package cafe.adriel.hal.sample

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import cafe.adriel.hal.observe
import cafe.adriel.hal.observer.LiveDataStateObserver
import cafe.adriel.hal.plus
import kotlinx.android.synthetic.main.activity_sample.*

class SampleActivity : AppCompatActivity() {

    private val viewModel by viewModels<SampleViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        vClickMe.setOnClickListener {
            viewModel + SampleAction.OnButtonClicked
        }

        viewModel.observe(LiveDataStateObserver(this) { state ->
            when (state) {
                is SampleState.MessageLoaded -> showMessage(state.message)
            }
        })
    }

    private fun showMessage(message: String) =
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}
